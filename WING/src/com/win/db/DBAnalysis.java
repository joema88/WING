package com.win.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBAnalysis extends DB {
	private static PreparedStatement bbsQueryStmnt = null;

	private static PreparedStatement bytpSummaryStmnt = null;
	
	public static void cleanDB() {
		try {
			if (bbsQueryStmnt != null) {
				bbsQueryStmnt.close();
				bbsQueryStmnt = null;
			}
			if (bytpSummaryStmnt != null) {
				bytpSummaryStmnt.close();
				bytpSummaryStmnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PreparedStatement getBbsQueryStmnt() {
		// getConnection();

		if (bbsQueryStmnt == null) {
			try {
				String query = "SELECT DATEID, BBS FROM BBROCK WHERE STOCKID =? AND BBS>0 ORDER BY DATEID DESC LIMIT 3";

				bbsQueryStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bbsQueryStmnt;

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

}
