package com.virusanalyzer.library;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.text.DateFormatter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.virusanalyzer.file.MD5Checksum;

@SuppressWarnings("deprecation")
public class Utils implements Runnable {

	private static Stack<File> stack;

	private static int indexSample = 0;

	private HttpClient httpclient;

	public Utils() {
		stack = new Stack<File>();
	}

	public void sendFile() {
		while (true) {
			try {
				File file = getFileStack();
				String md5 = (new MD5Checksum(file.getPath())).getCheckSum();
				httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(Constants.URL_SEND);

				FileBody bin = new FileBody(file);
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("resource", new StringBody(md5));
				entity.addPart("comment", new StringBody(""));
				entity.addPart("apikey", new StringBody(Constants.API_KEY));
				entity.addPart("file", bin);

				post.setEntity(entity);
				System.out.println("Sending request for " + file.getName());
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String json = httpclient.execute(post, responseHandler);
				if (json.contains("Scan request successfully queued, come back later for the report")) {
					System.out.println("Resquest finished for "
							+ file.getName());
					getResult(json, md5, file.getPath());
					break;
				} else {
					System.out
							.println("There is an error. Please wait 30 second to send again the file.");
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getResult(String json, String md5, String pathFile) {
		Map<String, Object> jsonMap;
		System.out.println("Getting the result for " + pathFile);
		while (true) {
			try {
				httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(Constants.URL_REPORT);
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("resource", new StringBody(md5));
				entity.addPart("apikey", new StringBody(Constants.API_KEY));

				post.setEntity(entity);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				json = httpclient.execute(post, responseHandler);
				Gson gson = new GsonBuilder().create();
				jsonMap = gson.fromJson(json, Map.class);

				if (jsonMap == null
						|| !jsonMap
								.containsValue("Scan finished, information embedded")) {
					try {
						System.out.println("Wait for 30 seconds.");
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					printJson(jsonMap, pathFile);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void fillStack(String pathFile) {
		File path = new File(pathFile);
		if (path.exists()) {
			File[] files = path.listFiles();
			for (File fl : files) {
				if (fl.isDirectory()) {
					fillStack(fl.getPath());
				} else {
					stack.push(fl);
				}
			}
		}
	}

	public void printJson(Map<String, Object> jsonMap, String pathFile) {
		StringBuilder sb = new StringBuilder();
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
				new DateTypeAdapter()).create();
		sb.append("File name: " + pathFile + "\n");
		sb.append("MD5: " + jsonMap.get("md5") + "\n");
		sb.append("Total antivirus: " + jsonMap.get("total") + "\n");
		sb.append("Total positives: " + jsonMap.get("positives")
				+ "\nResult:\n");
		Map<String, Object> result = gson.fromJson(jsonMap.get("scans")
				.toString(), Map.class);
		for (String str : result.keySet()) {
			sb.append("  " + str + ": ");
			Gson dateGson = new GsonBuilder().setDateFormat(
					DateFormat.DATE_FIELD).create();
			Map<String, Object> tmp = dateGson.fromJson(result.get(str)
					.toString(), Map.class);
			int count = 0;
			int totalElements = tmp.keySet().size();
			for (String res : tmp.keySet()) {
				count++;
				if (count < totalElements) {
					if (tmp.get(res) == null) {
						sb.append(res + ": " + "null, ");
					} else {
						if (res.contains("update")) {
							sb.append(res
									+ ": "
									+ gson.toJson(new Date((long) Double
											.parseDouble(tmp.get(res)
													.toString()))));
						} else {
							sb.append(res + ": " + tmp.get(res).toString()
									+ ", ");
						}

					}
				} else {
					if (tmp.get(res).toString() == null) {
						sb.append(res + ": " + "null");
					} else {
						if (res.contains("update")) {
							String date = tmp.get(res).toString();
							sb.append(res + ": " + date.charAt(5)
									+ date.charAt(6) + "/" + date.charAt(7)
									+ date.charAt(8) + "/" + date.charAt(0)
									+ date.charAt(2) + date.charAt(3)
									+ date.charAt(4));
						} else {
							sb.append(res + ": " + tmp.get(res).toString());
						}

					}
				}
			}
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}

	public String toString() {
		String str = "stack size -> " + stack.size() + "\nFiles:";
		for (int i = 0; i < stack.size(); i++) {
			str += "\tFile " + (i + 1) + " -> " + stack.get(i).getPath() + "\n";
		}
		return str;
	}

	private synchronized File getFileStack() {
		if (!stack.isEmpty()) {
			return stack.pop();
		}
		return null;
	}

	@Override
	public void run() {
		while (!stack.isEmpty()) {
			if (indexSample < Constants.SAMPLES_COUNT) {
				indexSample++;
				sendFile();
			} else {
				try {
					System.out.println("Please wait 1 minute.");
					Thread.sleep(60000);
					if (indexSample != 0) {
						indexSample = 0;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
