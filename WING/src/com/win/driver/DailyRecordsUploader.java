package com.win.driver;
import com.win.file.CSVReader;
import com.win.db.*;

public class DailyRecordsUploader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readLoadRecords("2020-05-19-watchlistUP.csv");

	}
	
	public static void readLoadRecords(String fileName) {
		CSVReader.uploadCSVtoDB(fileName);
	}

}
