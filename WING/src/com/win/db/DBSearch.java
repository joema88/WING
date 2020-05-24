package com.win.db;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class DBSearch extends DB {

	private static PreparedStatement typSearchStmnt = null;
	private static Statement stmnt = null;

	public static void cleanDB() {
		try {

			if (typSearchStmnt != null) {
				typSearchStmnt.close();
				typSearchStmnt = null;
			}
			if (stmnt != null) {
				stmnt.close();
				stmnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Statement getSearchStmnt() {

		try {
			if (stmnt == null) {
				stmnt = DB.getConnection().createStatement();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

		return stmnt;

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

	public static Hashtable findQualifiedStocks(int[] data) {
		Hashtable result = new Hashtable();

		getSearchStmnt();
		try {
			String query = "SELECT  b.SYMBOL, a.STOCKID FROM BBROCK a, SYMBOLS b WHERE a.STOCKID =b.STOCKID  ";
			if (data[0] == 0 || data[0] == 1) {
				query = query + " AND TEAL = " + data[0];
			}
			if (data[1] == 0 || data[1] == 1) {
				query = query + " AND YELLOW = " + data[1];
			}
			if (data[2] == 0 || data[2] == 1) {
				query = query + " AND PINK = " + data[2];
			}
			query = query + " AND DATEID = " + data[3];

			ResultSet rs = stmnt.executeQuery(query);
			
			while(rs.next()) {
				String symbol = rs.getString(1);
				String stockID = ""+rs.getInt(2);
				result.put(symbol, stockID);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return result;

	}

}
