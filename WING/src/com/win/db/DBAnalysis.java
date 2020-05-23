package com.win.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBAnalysis extends DB {
	private static PreparedStatement priceATRStmnt = null;
	private static PreparedStatement btcQueryStmnt = null;
	private static PreparedStatement bycQueryStmnt = null;
	private static PreparedStatement bpcQueryStmnt = null;
	private static PreparedStatement bbsQueryStmnt = null;
	private static PreparedStatement bbsUpdateStmnt = null;
	private static PreparedStatement btcSumStmnt = null;
	private static PreparedStatement btsUpdateStmnt = null;
	public static short records = 4;

	public static void cleanDB() {
		try {
			if (priceATRStmnt != null) {
				priceATRStmnt.close();
				priceATRStmnt = null;
			}
			if (btcQueryStmnt != null) {
				btcQueryStmnt.close();
				btcQueryStmnt = null;
			}
			if (bycQueryStmnt != null) {
				bycQueryStmnt.close();
				bycQueryStmnt = null;
			}
			if (bycQueryStmnt != null) {
				bycQueryStmnt.close();
				bycQueryStmnt = null;
			}

			if (btcSumStmnt != null) {
				btcSumStmnt.close();
				btcSumStmnt = null;
			}
			if (btsUpdateStmnt != null) {
				btsUpdateStmnt.close();
				btsUpdateStmnt = null;
			}

			if (bbsQueryStmnt != null) {
				bbsQueryStmnt.close();
				bbsQueryStmnt = null;
			}
			if (bbsUpdateStmnt != null) {
				bbsUpdateStmnt.close();
				bbsUpdateStmnt = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PreparedStatement getPriceATRQueryStmnt() {
		// getConnection();

		if (priceATRStmnt == null) {
			try {
				String query = "SELECT CLOSE, ATR FROM BBROCK WHERE STOCKID =? AND  DATEID = ? ";

				priceATRStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return priceATRStmnt;

	}

	public static PreparedStatement getBTCSumStmnt() {
		// getConnection();

		if (btcSumStmnt == null) {
			try {
				String query = "SELECT COUNT(*) FROM BBROCK WHERE STOCKID =? AND DATEID >=? AND DATEID <= ?";
				btcSumStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return btcSumStmnt;

	}

	public static PreparedStatement getBTSUpdateStmnt() {
		// getConnection();

		if (btsUpdateStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BTS = ?  WHERE STOCKID =?  AND  DATEID =?";
				btsUpdateStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return btsUpdateStmnt;

	}

	public static PreparedStatement getBtcQueryStmnt() {

		if (btcQueryStmnt == null) {
			try {
				String query = "SELECT DATEID, BTC FROM BBROCK WHERE STOCKID =? AND BTC > 0 ORDER BY DATEID DESC LIMIT " + records;

				btcQueryStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return btcQueryStmnt;
	}

	public static PreparedStatement getBpcQueryStmnt(){

		if (bpcQueryStmnt == null) {
			try {
				String query = "SELECT DATEID, BPC FROM BBROCK WHERE STOCKID =?  AND BPC > 0 ORDER BY DATEID DESC LIMIT " + records;

				bpcQueryStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bpcQueryStmnt;
	}
	
	public static PreparedStatement getBycQueryStmnt() {

		if (bycQueryStmnt == null) {
			try {
				String query = "SELECT DATEID, BYC FROM BBROCK WHERE STOCKID =? AND BYC > 0 ORDER BY DATEID DESC LIMIT " + records;

				bycQueryStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bycQueryStmnt;
	}

	public static PreparedStatement getBbsQueryStmnt() {
		// getConnection();

		if (bbsQueryStmnt == null) {
			try {
				String query = "SELECT DATEID, BBS FROM BBROCK WHERE STOCKID =? AND BBS>0 ORDER BY DATEID DESC LIMIT "
						+ records;

				bbsQueryStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bbsQueryStmnt;

	}

	public static PreparedStatement getBbsUpdateStmnt() {
		// getConnection();

		if (bbsUpdateStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BBS = ? WHERE STOCKID =? AND DATEID =? ";

				bbsUpdateStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bbsUpdateStmnt;

	}

}
