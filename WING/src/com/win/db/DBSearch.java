package com.win.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBSearch extends DB {
	
	private static PreparedStatement typSearchStmnt = null;
	
	public static void cleanDB() {
		try {
			
			if (typSearchStmnt != null) {
				typSearchStmnt.close();
				typSearchStmnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PreparedStatement getTypSearchStmnt() {
		// getConnection();

		if (typSearchStmnt == null) {
			try {
				String query = "SELECT  b.SYMBOL, a.STOCKID FROM BBROCK a, SYMBOLS b WHERE a.STOCKID =b.STOCKID AND TEAL = ? AND YELLOW = ? AND PINK = ? AND DATEID = ?";
				
				typSearchStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return typSearchStmnt;

	}

}
