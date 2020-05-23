package com.win.score;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.win.db.*;

/*
 * More dynamic rules/logic are defined here as they are still to be refined with more frequent
 * changes likely. With DBAnalysis DB Class defined.
 */
public class DailyAnalysis {

	public static void main(String[] args) {
		analyzeStockTrend("2020-05-22");
	}

	public static short records = 4;

	public static void analyzeStockTrend(String dateStr) {
		try {
			// String dateStr = "2020-05-20";
			int dateID = 4;
			dateID = DB.getDateID(dateStr);

			Statement stmnt = DBScore.getBytpStatement();
			String query = "SELECT STOCKID FROM BBROCK WHERE DATEID = " + dateID + " ORDER BY STOCKID ASC";
			ResultSet rs = stmnt.executeQuery(query);

			while (rs.next()) {
				int stockID = rs.getInt(1);
				// System.out.println("Analyze stockID "+stockID);

				checkTrend(stockID, dateID);
			}

			stmnt.close();
			stmnt = null;
		} catch (Exception ex) {

		}

	}

	public static void updateRecord(PreparedStatement stmnt, int stockID, int dateID, int bbs) {
		try {
			stmnt.setInt(1, bbs);
			stmnt.setInt(2, stockID);
			stmnt.setInt(3, dateID);
			stmnt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

	public static float[][] getPriceATR(PreparedStatement stmnt, int stockID, int dateID) {
		float[][] result = new float[1][2];
		try {
			stmnt.setInt(1, stockID);
			stmnt.setInt(2, dateID);
			ResultSet rs = stmnt.executeQuery();
			int loop = 0;
			while (rs.next()) {
				// get close price value
				result[loop][0] = rs.getFloat(1);
				// get ATR value
				result[loop][1] = rs.getFloat(2);
				loop++;
			}

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return result;
	}

	public static int[][] getPreviousRecords(PreparedStatement stmnt, int stockID) {
		int[][] result = new int[records][2];
		// result[0][0] = -5;
		try {
			// PreparedStatement stmnt = DBAnalysis.getBbsQueryStmnt();
			stmnt.setInt(1, stockID);
			ResultSet rs = stmnt.executeQuery();
			int loop = 0;
			while (rs.next()) {
				// get dateID value
				result[loop][0] = rs.getInt(1);
				// get BBS or BTC or BYC or BPC value
				result[loop][1] = rs.getInt(2);
				loop++;
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return result;
	}

	public static void checkTrend(int stockID, int dateID) {
		if(stockID==6551) {
			int a =0;
			a= a*3;
		}
			// check previous BBS to set stage
		int[][] previousBBS = getPreviousRecords(DBAnalysis.getBbsQueryStmnt(), stockID);

		// check BYC, BTC, BPC
		if (previousBBS[0][0] == 0) // new case, no BBS set yet ever
		{
			int[][] previousBTC = getPreviousRecords(DBAnalysis.getBtcQueryStmnt(), stockID);
			int[][] previousBYC = getPreviousRecords(DBAnalysis.getBycQueryStmnt(), stockID);
			int[][] previousBPC = getPreviousRecords(DBAnalysis.getBpcQueryStmnt(), stockID);
			// case 1 all TEAL
			if (previousBTC[0][0] > 0 && previousBYC[0][0] == 0 && previousBPC[0][0] == 0) {

				int edate = 0;
				int ebtc = 0;
				float eprice = 0.0f;
				float eatr = 0.0f;
				int bullscore = 0;
				int pdate = 0;
				int pbtc = 0;
				float pprice = 0.0f;
				float patr = 0.0f;
				int cdate = 0;
				int cbtc = 0;
				float cprice = 0.0f;
				float catr = 0.0f;
				boolean c1 = false;
				boolean c2 = false;

				for (int k = 0; k < records; k++) {
					if (previousBTC[k][0] > 0) {
						if (k == 0) {
							cdate = previousBTC[k][0];
							cbtc = previousBTC[k][1];
							float[][] priceATR = getPriceATR(DBAnalysis.getPriceATRQueryStmnt(), stockID, cdate);
							cprice = priceATR[0][0];
							catr = priceATR[0][1];
							edate = cdate;
							ebtc = cbtc;
							eprice = cprice;
							eatr = catr;
							c1 = true;
						} else if (k == 1) {
							pdate = previousBTC[k][0];
							pbtc = previousBTC[k][1];
							float[][] priceATR = getPriceATR(DBAnalysis.getPriceATRQueryStmnt(), stockID, pdate);
							pprice = priceATR[0][0];
							patr = priceATR[0][1];
							c2 = true;
						} else {
							cdate = pdate;
							cbtc = pbtc;
							cprice = pprice;
							catr = patr;
							pdate = previousBTC[k][0];
							pbtc = previousBTC[k][1];
							float[][] priceATR = getPriceATR(DBAnalysis.getPriceATRQueryStmnt(), stockID, pdate);
							pprice = priceATR[0][0];
							patr = priceATR[0][1];

						}

						if (c1 && c2) { // let's compare!
							if (cprice > 0.01f && pprice > 0.01f) {
								if (patr>0.01f && cprice >= (pprice +3 * patr)) { // very bull case 3xxxx
									bullscore = 30000;
								}else if (patr>0.01f && cprice >= (pprice +2 * patr)) { // very bull case 3xxxx
									bullscore = 20000;
								}else if (patr>0.01f && cprice >= (pprice +1 * patr)) { // very bull case 3xxxx
									bullscore = 10000;
								}
							}

						}
					}
				}

				// one update at the end
				if (eprice > 0.01f && pprice > 0.01f ) {
					System.out.println("Compare stockID " + stockID + " edate " + edate + " eprice " + eprice + " pdate"
							+ pdate + " pprice " + pprice + " patr " + patr);

					int days = edate - pdate + 1;
					//System.out.println("30000 + days " + ( bullscore + days));
					updateRecord(DBAnalysis.getBbsUpdateStmnt(), stockID, edate,  bullscore + days);
				}

			}

			// case 2 all yellow, pink

			// case 3 mixed teal/yellow

			// case 4 no signal

			// case 5 yellow, pink (bear)--> Teal

			// case 6 yellow, pink (mild bull)--> Teal

			// case 7 yellow, pink (random)--> Teal

			// case 8 teal --> yellow, pink

		}

		// update YTP Summary

		// update BBS summary

		// update PTVAL, PEAK | TROUGH VAL

		// UPDATE PCTP Change%

	}

	public static void updateYTPSummary(int stockID, int dateID, int bys, int bts, int bps) {
		try {
			PreparedStatement stmnt = null;// DBAnalysis.getbytpSummaryPreparedStatement();
			stmnt.setInt(1, bys);
			stmnt.setInt(2, bts);
			stmnt.setInt(3, bps);
			stmnt.setInt(4, stockID);
			stmnt.setInt(5, dateID);
			stmnt.executeUpdate();
			if (bys >= 1 || bts >= 1 || bps >= 1) {
				System.out.println(stockID + ":" + dateID + ":" + bys + ":" + bts + ":" + bps);
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

}
