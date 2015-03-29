package com.virusanalyzer.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;
import java.util.Scanner;

import com.virusanalyzer.file.WriteFile;
import com.virusanalyzer.library.Constants;
import com.virusanalyzer.library.FileAnalyzer;

public class Main {
	public static void main(String[] args) {
		String filesToAnalyze = null, outputFile;
		Scanner sc = new Scanner(System.in);
		String askUser = null;
		WriteFile wf = null;
		boolean nextStep = false;

		// Ask which file or directory wants to analyze.
		while (!nextStep
				|| (filesToAnalyze != null && !(new File(filesToAnalyze)
						.exists()))) {
			System.out.println("Write the file or directory"
					+ " you wish to analyze.");
			filesToAnalyze = sc.next();
			if ((new File(filesToAnalyze)).isDirectory() || (new File(filesToAnalyze)).isFile()
					&& (new File(filesToAnalyze)).length() / 1048576 < 128) {
				do {
					System.out.println("Do you really want to analyze "
							+ filesToAnalyze + " ? Yes/no");
					askUser = sc.next().toLowerCase();
				} while (!askUser.equals("yes") && !askUser.equals("no")
						&& !askUser.equals("n") && !askUser.equals("y"));
				nextStep = askUser.equals("yes") || askUser.equals("y");
			} else if(!(new File(filesToAnalyze)).exists()){
				System.out.println("The file " + filesToAnalyze + " doesn't exist.");
			} else {
				System.out.println("The file " + filesToAnalyze + " is bigger than 128MB.");
			}

		}
		nextStep = false;

		// Ask location for the output file.
		do {
			System.out.println("Do you wish to change the default "
					+ "name and location for the output file? (Yes/no) ");
			System.out
					.println("The default path is"
							+ System.getProperty("user.home")
							+ System.getProperty("file.separator")
							+ (Calendar.getInstance().get(Calendar.MONTH) + 1 < 10 ? "0"
									+ (Calendar.getInstance().get(
											Calendar.MONTH) + 1)
									: Integer.toString(Calendar.getInstance()
											.get(Calendar.MONTH) + 1))
							+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
							+ Calendar.getInstance().get(Calendar.YEAR)
							+ "_analysis_result.txt");
			askUser = sc.next().toLowerCase();
			if (askUser.equals("yes") || askUser.equals("y")) {
				nextStep = true;
				do {
					System.out
							.println("Please write the path for the wished file.");
					outputFile = sc.next();
					if (!(new File(outputFile)).isFile()) {
						System.err
								.println("Please write the path of a file.\n");
					}
				} while (!(new File(outputFile)).isFile());
				wf = new WriteFile(outputFile);
			} else if (askUser.equals("no") || askUser.equals("n")) {
				nextStep = true;
				wf = new WriteFile();
			}
		} while (!nextStep);

		sc.close();

		// Call to FileAnalyzer constructor and fill the stack.
		FileAnalyzer util = new FileAnalyzer(wf);
		System.out
				.println("Filling the pool. Please wait a moment\nAnd remember all files which are 128MB or bigger can't be uploaded.");
		util.fillStack(filesToAnalyze);
		System.out.println("Pool filled.");

		Thread[] threads = new Thread[Constants.TOTAL_THREADS];

		// Start the thread and let sleep one second just for a issue when you
		// have two or more simultaneous connection.
		for (int i = 0; i < Constants.TOTAL_THREADS; i++) {
			threads[i] = new Thread(util, "Thread -> " + i);
			try {
				threads[i].start();
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.getCause();
			}
		}
		System.out.println("Finished.\n");
	}
}
