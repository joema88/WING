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
		if (stockID == 1688) {
			int a = 0;
			a = a * 3;
		}
		// check previous BBS to set stage
		int[][] previousBBS = getPreviousRecords(DBAnalysis.getBbsQueryStmnt(), stockID);

		// check BYC, BTC, BPC
		//if (previousBBS[0][0] == 0) // new case, no BBS set yet ever
		{
			int[][] previousBTC = getPreviousRecords(DBAnalysis.getBtcQueryStmnt(), stockID);
			int[][] previousBYC = getPreviousRecords(DBAnalysis.getBycQueryStmnt(), stockID);
			int[][] previousBPC = getPreviousRecords(DBAnalysis.getBpcQueryStmnt(), stockID);
			// case 1 all TEAL
			if (previousBTC[0][0] > 0 && previousBYC[0][0] == 0 && previousBPC[0][0] == 0) {
				allTealAnalysis(stockID, previousBTC, previousBYC, previousBPC,0);
			}

			// case 2 all yellow, pink
			if (previousBTC[0][0] == 0 && previousBYC[0][0] > 0 && previousBPC[0][0] == 0) {
				allYellowAnalysis(stockID, previousBTC, previousBYC, previousBPC, 0);
			}

			// case 3 mixed teal/yellow|pink
			if (previousBTC[0][0] > 0 && (previousBYC[0][0] > 0||previousBPC[0][0] > 0)) {
				overlapAnalysis(stockID, previousBTC, previousBYC, previousBPC);
			}
			// case 4 no signal

		}

		// update PTVAL, PEAK | TROUGH VAL

		// UPDATE PCTP Change%

	}

	public static void overlapAnalysis(int stockID, int[][] previousBTC, int[][] previousBYC, int[][] previousBPC) {

		int btcEnd = 0;
		int btcStart = 0;
		int btcMax = 0;
		int bycEnd = 0;
		int bycStart = 0;
		int bycMax = 0;
		int bpcEnd = 0;
		int bpcStart = 0;
		int bpcMax = 0;

		for (int k = 0; k < records; k++) {
			if (previousBYC[k][0] > 0) {
				if (k == 0) {
					bycEnd = previousBYC[k][0];
					bycMax = previousBYC[k][1];
					bycStart = previousBYC[k][0];
				} else {
					bycStart = previousBYC[k][0];
					if (previousBYC[k][1] > bycMax) {
						bycMax = previousBYC[k][1];
					}
				}
			}
		}

		for (int k = 0; k < records; k++) {
			if (previousBTC[k][0] > 0) {
				if (k == 0) {
					btcEnd = previousBTC[k][0];
					btcMax = previousBTC[k][1];
					btcStart = previousBTC[k][0];
				} else {
					btcStart = previousBTC[k][0];
					if (previousBTC[k][1] > btcMax) {
						btcMax = previousBTC[k][1];
					}
				}
			}
		}

		for (int k = 0; k < records; k++) {
			if (previousBPC[k][0] > 0) {
				if (k == 0) {
					bpcEnd = previousBPC[k][0];
					bpcMax = previousBPC[k][1];
					bpcStart = previousBPC[k][0];
				} else {
					bpcStart = previousBPC[k][0];
					if (previousBTC[k][1] > bpcMax) {
						bpcMax = previousBPC[k][1];
					}
				}
			}
		}

		int pyStart = 0;
		if(bpcStart>0 && bycStart==0) {
			pyStart = bpcStart;
		}else if(bycStart>0 && bpcStart==0) {
			pyStart = bycStart;
		}else if(bpcStart> bycStart) {
			pyStart = bpcStart;
		}else {
			pyStart = bycStart;
		}
		// yellow/pink wraps around teal case
		if ((bpcMax >= btcMax || bycMax>= btcMax)&&(bpcEnd >= btcEnd || bycEnd >= btcEnd)&&(pyStart <= btcStart)) {
			for (int k = 0; k < records; k++) {
				previousBTC[k][0] = 0;
				previousBTC[k][1] = 0;
				previousBPC[k][0] = 0;
				previousBPC[k][1] = 0;
				previousBYC[k][0] = 0;
				previousBYC[k][1] = 0;

			}

			int endPY = 0;
			int maxPY = 0;
			int beginPY = 0;
			int minPY = 0;

			if (bpcEnd >= bycEnd) {
				endPY = bpcEnd;
			} else {
				endPY = bycEnd;
			}

			if (bpcStart <= bycStart) {
				beginPY = bpcStart;
			} else {
				beginPY = bycStart;
			}

			maxPY = bycMax + bpcMax;

			minPY = 1;

			previousBYC[0][0] = endPY;
			previousBYC[0][1] = maxPY;
			previousBYC[1][0] = beginPY;
			previousBYC[1][1] = minPY;

			allYellowAnalysis(stockID, previousBTC, previousBYC, previousBPC, 1000);

		}

		// teal wraps around yellow/pink case //119, 4285
		if ((bpcMax <= btcMax && bycMax<= btcMax) && (btcEnd >= bpcEnd && btcEnd >= bycEnd) && (btcStart <= pyStart)) {
			for (int k = 0; k < records; k++) {
				previousBTC[k][0] = 0;
				previousBTC[k][1] = 0;
				previousBPC[k][0] = 0;
				previousBPC[k][1] = 0;
				previousBYC[k][0] = 0;
				previousBYC[k][1] = 0;

			}

			previousBTC[0][0] = btcEnd;
			previousBTC[0][1] = btcMax;
			previousBTC[1][0] = btcStart;
			previousBTC[1][1] = 1;

			allTealAnalysis(stockID, previousBTC, previousBYC, previousBPC, 1000);

		}
		
		//yellow/pink --> teal case
		//1688
		
		//teal --> yellow/pink case

	}

	
	public static void allYellowAnalysis(int stockID, int[][] previousBTC, int[][] previousBYC, int[][] previousBPC,
			int bullScore) {
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
			if (previousBYC[k][0] > 0) {
				if (k == 0) {
					cdate = previousBYC[k][0];
					cbtc = previousBYC[k][1];
					float[][] priceATR = getPriceATR(DBAnalysis.getPriceATRQueryStmnt(), stockID, cdate);
					cprice = priceATR[0][0];
					catr = priceATR[0][1];
					edate = cdate;
					ebtc = cbtc;
					eprice = cprice;
					eatr = catr;
					c1 = true;
				} else if (k == 1) {
					pdate = previousBYC[k][0];
					pbtc = previousBYC[k][1];
					float[][] priceATR = getPriceATR(DBAnalysis.getPriceATRQueryStmnt(), stockID, pdate);
					pprice = priceATR[0][0];
					patr = priceATR[0][1];
					c2 = true;
				} else {
					cdate = pdate;
					cbtc = pbtc;
					cprice = pprice;
					catr = patr;
					pdate = previousBYC[k][0];
					pbtc = previousBYC[k][1];
					float[][] priceATR = getPriceATR(DBAnalysis.getPriceATRQueryStmnt(), stockID, pdate);
					pprice = priceATR[0][0];
					patr = priceATR[0][1];

				}

				if (c1 && c2) { // let's compare!
					if (cprice > 0.01f && pprice > 0.01f) {
						if (patr > 0.01f && cprice <= (pprice - 3 * patr)) { // very bull case 3xxxx
							bullscore = -30000;
						} else if (patr > 0.01f && cprice <= (pprice - 2 * patr)) { // very bull case 3xxxx
							bullscore = -20000;
						} else if (patr > 0.01f) { // very bull case 3xxxx
							bullscore = -10000;
						}
					}

				}
			}
		}

		// one update at the end
		if (eprice > 0.01f && pprice > 0.01f) {
			System.out.println("Compare stockID " + stockID + " edate " + edate + " eprice " + eprice + " pdate" + pdate
					+ " pprice " + pprice + " patr " + patr);

			int days = edate - pdate + 1;
			// System.out.println("30000 + days " + ( bullscore + days));
			updateRecord(DBAnalysis.getBbsUpdateStmnt(), stockID, edate, bullscore - days);
			updateYTPSummary(stockID, pdate, edate);
		}

	}

	public static void allTealAnalysis(int stockID, int[][] previousBTC, int[][] previousBYC, int[][] previousBPC,
			int bullscore) {
		int edate = 0;
		int ebtc = 0;
		float eprice = 0.0f;
		float eatr = 0.0f;
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
						if (patr > 0.01f && cprice >= (pprice + 3 * patr)) { // very bull case 3xxxx
							if (bullscore == 0) {
								bullscore = 30000;
							} else {
								bullscore = 3 * bullscore;
							}
						} else if (patr > 0.01f && cprice >= (pprice + 2 * patr)) { // very bull case 3xxxx
							if (bullscore == 0) {
								bullscore = 20000;
							} else {
								bullscore = 2 * bullscore;
							}
						} else if (patr > 0.01f) { // very bull case 3xxxx
							if (bullscore == 0) {
								bullscore = 10000;
							} else {
								bullscore = 1 * bullscore;
							}
						}
					}

				}
			}
		}

		// one update at the end
		if (eprice > 0.01f && pprice > 0.01f) {
			System.out.println("Compare stockID " + stockID + " edate " + edate + " eprice " + eprice + " pdate" + pdate
					+ " pprice " + pprice + " patr " + patr);

			int days = edate - pdate + 1;
			// System.out.println("30000 + days " + ( bullscore + days));
			updateRecord(DBAnalysis.getBbsUpdateStmnt(), stockID, edate, bullscore + days);
			updateYTPSummary(stockID, pdate, edate);
		}

	}

	public static void updateYTPSummary(int stockID, int dateStart, int dateEnd) {
		try {
			PreparedStatement stmnt1 = DBAnalysis.getTealSumStmnt();
			stmnt1.setInt(1, stockID);
			stmnt1.setInt(2, dateStart);
			stmnt1.setInt(3, dateEnd);
			ResultSet rs1 = stmnt1.executeQuery();
			int tsc = 0;
			if (rs1.next())
				tsc = rs1.getInt(1);

			PreparedStatement stmnt2 = DBAnalysis.getPinkSumStmnt();
			stmnt2.setInt(1, stockID);
			stmnt2.setInt(2, dateStart);
			stmnt2.setInt(3, dateEnd);
			ResultSet rs2 = stmnt2.executeQuery();
			int tpc = 0;
			if (rs2.next())
				tpc = rs2.getInt(1);

			PreparedStatement stmnt3 = DBAnalysis.getYellowSumStmnt();
			stmnt3.setInt(1, stockID);
			stmnt3.setInt(2, dateStart);
			stmnt3.setInt(3, dateEnd);
			ResultSet rs3 = stmnt3.executeQuery();
			int tyc = 0;
			if (rs3.next())
				tyc = rs3.getInt(1);

			PreparedStatement stmnt4 = DBAnalysis.getTYCSUpdateStmnt();
			stmnt4.setInt(1, tsc);
			stmnt4.setInt(2, tyc);
			stmnt4.setInt(3, tpc);
			stmnt4.setInt(4, stockID);
			stmnt4.setInt(5, dateEnd);
			stmnt4.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

}
