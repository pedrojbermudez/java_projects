package com.virusanalyzer.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class WriteFile {
	private File outputFile;

	public WriteFile() {
		this(System.getProperty("user.home")
				+ System.getProperty("file.separator")
				+ (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10 ? "0"
						+ (Calendar.getInstance().get(Calendar.MONTH) + 1)
						: Integer.toString(Calendar.getInstance().get(
								Calendar.MONTH) + 1))
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
				+ Calendar.getInstance().get(Calendar.YEAR)
				+ "_analysis_result.txt");
	}

	public WriteFile(String file) {
		outputFile = new File(file);
	}

	public void setOutputFile(String file) {
		outputFile = new File(file);
	}

	public void deleteContent() {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(outputFile);
			pw = new PrintWriter(fw);
			pw.println("");
			pw.close();
		} catch (IOException e) {
			System.err.println("Error. The file can't be written.");
		}
	}

	public synchronized void write(String text) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(outputFile, true);
			pw = new PrintWriter(fw);
			pw.println(text);
			pw.close();
		} catch (IOException e) {
			System.err.println("Error. The file can't be written.");
		}
	}
}
