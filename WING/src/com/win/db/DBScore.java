package com.win.db;

import java.sql.*;
import java.util.Calendar;

public class DBScore extends DB {

	private static PreparedStatement bytpCountStmnt = null;
	private static PreparedStatement bytpSummaryStmnt = null;
	private static Statement bytpStmnt = null;
	
	public static void cleanDB() {
		try {
			if (bytpCountStmnt != null) {
				bytpCountStmnt.close();
			}
			if (bytpSummaryStmnt != null) {
				bytpSummaryStmnt.close();
			}
			if (bytpStmnt != null) {
				bytpStmnt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static Statement getBytpStatement() {
		
		try {
			if (bytpStmnt == null) {
				bytpStmnt = DB.getConnection().createStatement();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bytpStmnt;
	}


	public static PreparedStatement getbytpCountPreparedStatement() {
		// getConnection();

		if (bytpCountStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BYC = ?, BTC = ?, BPC = ? WHERE STOCKID=? AND DATEID = ?";

				bytpCountStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bytpCountStmnt;

	}

	public static PreparedStatement getbytpSummaryPreparedStatement() {
		// getConnection();

		if (bytpSummaryStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BYS = ?, BTS = ?, BPS = ? WHERE STOCKID=? AND DATEID = ?";

				bytpSummaryStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bytpSummaryStmnt;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

	

}
