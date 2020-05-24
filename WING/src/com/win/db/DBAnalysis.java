package com.win.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBAnalysis extends DB {
	private static PreparedStatement priceATRStmnt = null;
	private static PreparedStatement btcQueryStmnt = null;
	private static PreparedStatement bycQueryStmnt = null;
	private static PreparedStatement bpcQueryStmnt = null;
	private static PreparedStatement bbsQueryStmnt = null;
	private static PreparedStatement bbsPricePTUpdateStmnt = null;
	private static PreparedStatement tealSumStmnt = null;
	private static PreparedStatement yellowSumStmnt = null;
	private static PreparedStatement pinkSumStmnt = null;
	private static PreparedStatement tycsUpdateStmnt = null;
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

			if (tealSumStmnt != null) {
				tealSumStmnt.close();
				tealSumStmnt = null;
			} 
			if (yellowSumStmnt != null) {
				yellowSumStmnt.close();
				yellowSumStmnt = null;
			} 
			if (pinkSumStmnt != null) {
				pinkSumStmnt.close();
				pinkSumStmnt = null;
			} 
			if (tycsUpdateStmnt != null) {
				tycsUpdateStmnt.close();
				tycsUpdateStmnt = null;
			}

			if (bbsQueryStmnt != null) {
				bbsQueryStmnt.close();
				bbsQueryStmnt = null;
			}
			if (bbsPricePTUpdateStmnt != null) {
				bbsPricePTUpdateStmnt.close();
				bbsPricePTUpdateStmnt = null;
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

	public static PreparedStatement getTealSumStmnt() {
		// getConnection();

		if (tealSumStmnt == null) {
			try {
				String query = "SELECT COUNT(*) FROM BBROCK WHERE TEAL=1 AND STOCKID =? AND DATEID >=? AND DATEID <= ?";
				tealSumStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return tealSumStmnt;

	}

	public static PreparedStatement getYellowSumStmnt() {
		
		if (yellowSumStmnt == null) {
			try {
				String query = "SELECT COUNT(*) FROM BBROCK WHERE YELLOW=1 AND STOCKID =? AND DATEID >=? AND DATEID <= ?";
				yellowSumStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return yellowSumStmnt;

	}
	
	public static PreparedStatement getPinkSumStmnt() {
		
		if (pinkSumStmnt == null) {
			try {
				String query = "SELECT COUNT(*) FROM BBROCK WHERE PINK=1 AND STOCKID =? AND DATEID >=? AND DATEID <= ?";
				pinkSumStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return pinkSumStmnt;

	}
	
	
	public static PreparedStatement getTYCSUpdateStmnt() {
		// getConnection();

		if (tycsUpdateStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BTS = ?, BYS = ?, BPS = ?  WHERE STOCKID =?  AND  DATEID =?";
				tycsUpdateStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return tycsUpdateStmnt;

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

	public static PreparedStatement getBbsPricePTUpdateStmnt() {
		// getConnection();

		if (bbsPricePTUpdateStmnt == null) {
			try {
				String query = "UPDATE BBROCK SET BBS = ?, PTVAL=? WHERE STOCKID =? AND DATEID =? ";

				bbsPricePTUpdateStmnt = DB.getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return bbsPricePTUpdateStmnt;

	}

}
