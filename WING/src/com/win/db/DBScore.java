package com.win.db;

import java.sql.*;
import java.util.Calendar;

public class DBScore extends DB {
    
	private static PreparedStatement bytpQueryStmnt = null;
	private static PreparedStatement bytpCountUpdateStmnt = null;
	private static PreparedStatement BT9UpdateStmnt = null;
	private static PreparedStatement BT9QueryStmnt = null;
	private static PreparedStatement BT9FindStmnt = null;
	private static Statement bytpStmnt = null;
	
	public static void cleanDB() {
		try {
			
			if (bytpQueryStmnt != null) {
				bytpQueryStmnt.close();
				bytpQueryStmnt = null;
			}
			if (bytpCountUpdateStmnt != null) {
				bytpCountUpdateStmnt.close();
				bytpCountUpdateStmnt = null;
			}
		
			if (bytpStmnt != null) {
				bytpStmnt.close();
				bytpStmnt = null;
			}
			if (BT9UpdateStmnt != null) {
				BT9UpdateStmnt.close();
				BT9UpdateStmnt = null;
			}
			if (BT9QueryStmnt != null) {
				BT9QueryStmnt.close();
				BT9QueryStmnt = null;
			}
			if (BT9FindStmnt != null) {
				BT9FindStmnt.close();
				BT9FindStmnt = null;
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
	




	
	public static PreparedStatement getBytpQueryStmnt() {
		// getConnection();

		if (bytpQueryStmnt == null) {
			try {
				String query = "SELECT YELLOW, TEAL, PINK, BYC, BTC, BPC, BYS, BTS, BPS FROM BBROCK WHERE STOCKID =? ORDER BY DATEID DESC LIMIT 3";
				
				bytpQueryStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bytpQueryStmnt;

	}

	public static PreparedStatement getBT9QueryPreparedStatement() {
		// getConnection();

		if (BT9QueryStmnt  == null) {
			try {
				String query = "SELECT BT9 FROM BBROCK WHERE STOCKID=? ORDER BY DATEID DESC LIMIT 2";

				BT9QueryStmnt  = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return BT9QueryStmnt ;

	}
	
	public static PreparedStatement getBT9FindPreparedStatement() {
		// getConnection();

		if (BT9FindStmnt  == null) {
			try {
				String query = "SELECT TEAL FROM BBROCK WHERE STOCKID=? ORDER BY DATEID DESC LIMIT 9";

				BT9FindStmnt  = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return BT9FindStmnt ;

	}
	
	public static PreparedStatement getBT9UpdatePreparedStatement() {
		// getConnection();

		if (BT9UpdateStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BT9 = ? WHERE STOCKID=? AND DATEID = ?";

				BT9UpdateStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return BT9UpdateStmnt;

	}

	public static PreparedStatement getbytpCountUpdatePreparedStatement() {
		// getConnection();

		if (bytpCountUpdateStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BYC = ?, BTC = ?, BPC = ? WHERE STOCKID=? AND DATEID = ?";

				bytpCountUpdateStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bytpCountUpdateStmnt;

	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

	

}
