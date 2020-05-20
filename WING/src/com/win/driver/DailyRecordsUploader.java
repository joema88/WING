package com.win.driver;

import com.win.file.CSVReader;
import com.win.db.*;
import java.io.File;
import java.io.FilenameFilter;

public class DailyRecordsUploader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "/home/joma/share/test/";
		boolean currentDateProcessOnly = true;
		String cDate = "2020-05-19";
		readLoadRecords(path, currentDateProcessOnly, cDate);
		DB.closeConnection();

	}

	public static void readLoadRecords(String path, boolean cdOnly, String cDate) {
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
			if (cdOnly && nextFile.indexOf(cDate) >= 0) {
				System.out.println("Processing 1..."+nextFile);
				CSVReader.uploadCSVtoDB(path, nextFile);
			}else if(!cdOnly) {
				System.out.println("Processing 2..."+nextFile);
				CSVReader.uploadCSVtoDB(path, nextFile);
			}
		}

	}

}
