package com.virusanalyzer.main;

import com.virusanalyzer.library.Constants;
import com.virusanalyzer.library.Utils;

public class Main {

	public static void main(String[] args) {
		Utils util = new Utils();
		util.fillStack("/home/pedro/test/");

		Thread[] threads;
		threads = new Thread[Constants.TOTAL_THREADS];

		// Start the thread and let sleep one second just for a issue when you
		// have two or more simultaneous connection
		for (int i = 0; i < Constants.TOTAL_THREADS; i++) {
			threads[i] = new Thread(util, "Thread -> " + i);
			try {
				threads[i].start();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
