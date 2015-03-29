package com.virusanalyzer.library;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.MalformedJsonException;
import com.virusanalyzer.file.MD5Checksum;
import com.virusanalyzer.file.WriteFile;

@SuppressWarnings("deprecation")
public class FileAnalyzer implements Runnable {

	private static Stack<File> stack;
	private static int indexSample = 0;
	private StringBuilder result;
	private HttpClient httpclient;
	private static WriteFile wf;

	public FileAnalyzer(WriteFile writeFile) {
		stack = new Stack<File>();
		result = new StringBuilder();
		wf = writeFile;
	}

	public void sendFile() {
		while (true) {
			try {
				File file = getFileStack();
				String md5 = (new MD5Checksum(file.getPath())).getCheckSum();
				PoolingClientConnectionManager conMan = new PoolingClientConnectionManager();
				conMan.setMaxTotal(200);
				conMan.setDefaultMaxPerRoute(200);
				httpclient = new DefaultHttpClient(conMan);
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
					System.out.println("The file " + file.getName()
							+ " was sent");
					getReport(md5, file.getPath(), json);
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
				System.err.println("Malformed URL error: " + e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error: " + e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				System.err
						.println("No such algorithm error: " + e.getMessage());
			}
		}
	}

	public void getReport(String md5, String pathFile, String jsonSend) {
		Map<String, Object> jsonMap;
		Gson gson = new GsonBuilder().create();
		Map<String, String> jsonS = gson.fromJson(jsonSend, Map.class);
		System.out.println("Waiting for the result for " + pathFile + " ("
				+ md5 + ")");
		while (true) {
			try {
				PoolingClientConnectionManager conMan = new PoolingClientConnectionManager();
				conMan.setMaxTotal(200);
				conMan.setDefaultMaxPerRoute(200);
				httpclient = new DefaultHttpClient(conMan);
				HttpPost post = new HttpPost(Constants.URL_REPORT);
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("resource",
						new StringBody(jsonS.get("resource")));
				entity.addPart("apikey", new StringBody(Constants.API_KEY));
				post.setEntity(entity);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String json = httpclient.execute(post, responseHandler);
				jsonMap = gson.fromJson(json, Map.class);
				if (jsonMap == null
						|| !jsonMap
								.containsValue("Scan finished, information embedded")) {
					try {
						System.out.println("Wait for 30 seconds. The result is still not.");
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						continue;
					}
				} else {
					break;
				}
			} catch (IOException e) {
				System.err.println("IO error: " + e.getMessage());
				continue;
			}
		}
		formatResult(jsonMap, pathFile);
	}

	public void fillStack(String pathFile) {
		File path = new File(pathFile);
		if (path.exists() && path.isDirectory()) {
			File[] files = path.listFiles();
			for (File fl : files) {
				if (fl.isDirectory()) {
					fillStack(fl.getPath());
				} else {
					if (fl.length() / 1048576 < 128) {
						stack.push(fl);
					}
				}
			}
		} else {
			if (path.length() / 1048576 < 128) {
				stack.push(path);
			}
		}
	}

	public void formatResult(Map<String, Object> jsonMap, String pathFile) {
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
				new DateTypeAdapter()).create();
		DateFormat dateFormat = new SimpleDateFormat("/MM/dd/yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		result.append("Date: " + dateFormat.format(cal.getTime())+"\n");
		result.append("File name: " + pathFile + "\n");
		result.append("MD5: " + jsonMap.get("md5") + "\n");
		result.append("Total antivirus: "
				+ (int) Double.parseDouble(jsonMap.get("total").toString())
				+ "\n");
		result.append("Total positives: " + jsonMap.get("positives")
				+ "\nResult:\n");
		try {
			Map<String, Object> resTmp = gson.fromJson(jsonMap.get("scans")
					.toString(), Map.class);
			for (String str : resTmp.keySet()) {
				result.append("  " + str + " -> ");
				Gson dateGson = new GsonBuilder().setDateFormat(
						DateFormat.DATE_FIELD).create();
				Map<String, Object> tmp = dateGson.fromJson(resTmp.get(str)
						.toString(), Map.class);
				int count = 0;
				int totalElements = tmp.keySet().size();
				for (String res : tmp.keySet()) {
					count++;
					if (count < totalElements) {
						if (tmp.get(res) == null) {
							result.append(res + ": " + "null, ");
						} else {
							if (res.contains("update")) {
								result.append(res
										+ ": "
										+ gson.toJson(new Date((long) Double
												.parseDouble(tmp.get(res)
														.toString()))));
							} else {
								result.append(res + ": "
										+ tmp.get(res).toString() + ", ");
							}

						}
					} else {
						if (tmp.get(res).toString() == null) {
							result.append(res + ": " + "null");
						} else {
							if (res.contains("update")) {
								String date = tmp.get(res).toString();
								result.append(res + ": " + date.charAt(5)
										+ date.charAt(6) + "/" + date.charAt(7)
										+ date.charAt(8) + "/" + date.charAt(0)
										+ date.charAt(2) + date.charAt(3)
										+ date.charAt(4));
							} else {
								result.append(res + ": "
										+ tmp.get(res).toString());
							}

						}
					}
				}
				result.append("\n");
			}
			result.append("-----------------------------------------\n\n");
			System.out.println("The file " + pathFile + " has the result.");
		} catch (JsonSyntaxException e) {
			System.err.println("File -> " + pathFile + "; json error: "
					+ e.getMessage() + "; jsonMap -> " + jsonMap);
		}
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
				wf.write(result.toString());
			} else {
				try {
					System.out.println("The limit (4 sample per minutes) was overtaken. Please wait 60 seconds.");
					Thread.sleep(60000);
					indexSample = 0;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
