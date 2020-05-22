package com.win.driver;

import com.win.file.CSVReader;
import com.win.db.*;
import java.io.File;
import java.io.FilenameFilter;
import com.win.score.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class DailyRecordsUploader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "/home/joma/share/test/";
		boolean currentDateProcessOnly = false;
		String cDate = "2020-05-19";
		int dateCountToBeProcessed = 3;
		int loopCount = 0;
		long t1 = System.currentTimeMillis();

		do {
			readLoadRecords(path, cDate);
			DailyIncrease.processStocksYTP(cDate);
			DBScore.cleanDB();
			DB.closeConnection();
			try {
				Thread.sleep(5000);
			} catch (Exception ex) {

			}
			cDate = getNextDateString(cDate);
			loopCount++;
		} while (!currentDateProcessOnly && loopCount <= dateCountToBeProcessed);
		
		long t2 = System.currentTimeMillis();
		
		System.out.println("Total time cost is minutes: "+(t2-t1)/1000.0f/60.0f);

	}

	public static String getNextDateString(String preDay) {
		String nextDate = "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(preDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			cal.add(Calendar.DATE, 1);

			while ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					|| (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) { // or
				// sunday
				cal.add(Calendar.DATE, 1);

				System.out.println("WEEKEND");
			}

			nextDate = sdf.format(cal.getTime());
			System.out.println("next date is " + nextDate);

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return nextDate;
	}

	public static void readLoadRecords(String path, String cDate) {
		File folder = new File(path);

		// Implementing FilenameFilter to retrieve only txt files

		FilenameFilter txtFileFilter = new FilenameFilter() {
			// @Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".csv")) {
					return true;

				} else {
					return false;
				}
			}
		};

		// Passing txtFileFilter to listFiles() method to retrieve only txt files

		File[] files = folder.listFiles(txtFileFilter);

		for (int k = 0; k < files.length; k++) {
			String nextFile = files[k].getName();
			try {
				if (nextFile.indexOf(cDate) >= 0) {
					System.out.println("Processing 1..." + nextFile);
					CSVReader.uploadCSVtoDB(path, nextFile);
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.out);
			}
		}

	}

}
