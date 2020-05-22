package com.win.score;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.win.db.*;

/*
 * More static rules are defined here with matching DBScore DB Class
 */
public class DailyIncrease {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dateStr = "2020-05-20";

		processStocksYTP(dateStr);

	}

	public static void processStocksYTP(String dateStr) {
		try {
			// String dateStr = "2020-05-20";
			int dateID = 0;
			dateID = DB.getDateID(dateStr);

			Statement stmnt = DBScore.getBytpStatement();
			String query = "SELECT STOCKID FROM BBROCK WHERE DATEID = " + dateID + " ORDER BY STOCKID ASC";
			ResultSet rs = stmnt.executeQuery(query);

			while (rs.next()) {
				int stockID = rs.getInt(1);
				updateStockYTP(stockID, dateID);
			}

			stmnt.close();
			stmnt = null;
		} catch (Exception ex) {

		}

	}

	public static void updateStockYTP(int stockID, int dateID) {
		try {
			PreparedStatement stmnt = DBScore.getBytpQueryStmnt();
			stmnt.setInt(1, stockID);

			ResultSet rs = stmnt.executeQuery();
			int yellow1 = 0;
			int teal1 = 0;
			int pink1 = 0;
			int byc1 = 0;
			int btc1 = 0;
			int bpc1 = 0;
			int bys1 = 0;
			int bts1 = 0;
			int bps1 = 0;

			int yellow2 = 0;
			int teal2 = 0;
			int pink2 = 0;
			int byc2 = 0;
			int btc2 = 0;
			int bpc2 = 0;
			int bys2 = 0;
			int bts2 = 0;
			int bps2 = 0;
			int loopCount = 0;

			while (rs.next()) {
				loopCount++;

				if (loopCount == 1) {
					yellow1 = rs.getInt(1);
					teal1 = rs.getInt(2);
					pink1 = rs.getInt(3);
					byc1 = rs.getInt(4);
					btc1 = rs.getInt(5);
					bpc1 = rs.getInt(6);
					bys1 = rs.getInt(7);
					bts1 = rs.getInt(8);
					bps1 = rs.getInt(9);

				} else if (loopCount == 2) {
					yellow2 = rs.getInt(1);
					teal2 = rs.getInt(2);
					pink2 = rs.getInt(3);
					byc2 = rs.getInt(4);
					btc2 = rs.getInt(5);
					bpc2 = rs.getInt(6);
					bys2 = rs.getInt(7);
					bts2 = rs.getInt(8);
					bps2 = rs.getInt(9);
					int newyc = 0;
					int oldyc = 0;
					int newtc = 0;
					int oldtc = 0;
					int newpc = 0;
					int oldpc = 0;

					if (yellow1 == 1) {
						newyc = byc2 + yellow1;
						if (byc2 <= 1) {
							oldyc = byc2;
						} else {
							oldyc = 0;
						}
					} else {
						newyc = 0;
						oldyc = byc2;

					}

					if (teal1 == 1) {
						newtc = btc2 + teal1;
						if (btc2 <= 1) {
							oldtc = btc2;
						} else {
							oldtc = 0;
						}
					} else {
						newtc = 0;
						oldtc = btc2;

					}

					if (pink1 == 1) {
						newpc = bpc2 + pink1;
						if (bpc2 <= 1) {
							oldpc = bpc2;
						} else {
							oldpc = 0;
						}
					} else {
						newpc = 0;
						oldpc = bpc2;

					}

					// update previous day YTP count
					updateYTPCount(stockID, dateID - 1, oldyc, oldtc, oldpc);
					// update current day YTP count
					updateYTPCount(stockID, dateID, newyc, newtc, newpc);
					// update current day T9 count
					processBT9(stockID, dateID);
				}

				if (loopCount >= 2)
					break;
			}

			// we only have first record date
			if (loopCount == 1) {
				updateYTPCount(stockID, dateID, yellow1, teal1, pink1);
				// updateYTPSummary(stockID, dateID, yellow1, teal1, pink1);
			}

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

	public static void processBT9(int stockID, int dateID) {
		try {
			PreparedStatement stmnt1 = DBScore.getBT9FindPreparedStatement();
			stmnt1.setInt(1, stockID);
			ResultSet rs1 = stmnt1.executeQuery();
			int btCount = 0;
			while (rs1.next()) { //loop through past 9 records
				btCount = btCount + rs1.getInt(1);
			}

			if (btCount == 9) { //only need to update if is 9
				PreparedStatement stmnt2 = DBScore.getBT9QueryPreparedStatement();
				stmnt2.setInt(1, stockID);
				stmnt2.setInt(2, dateID);
				ResultSet rs2 = stmnt1.executeQuery();
				int bt9 = 0;
			
				if (rs2.next()&&rs2.next()) { //need previous day record
					bt9 = rs2.getInt(1);
					updateBT9(stockID, dateID, bt9 + 1);
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

	public static void updateBT9(int stockID, int dateID, int bt9) {
		try {
			PreparedStatement stmnt = DBScore.getBT9UpdatePreparedStatement();
			stmnt.setInt(1, bt9);
			stmnt.setInt(2, stockID);
			stmnt.setInt(3, dateID);
			stmnt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

	public static void updateYTPCount(int stockID, int dateID, int byc, int btc, int bpc) {
		try {
			PreparedStatement stmnt = DBScore.getbytpCountUpdatePreparedStatement();
			stmnt.setInt(1, byc);
			stmnt.setInt(2, btc);
			stmnt.setInt(3, bpc);
			stmnt.setInt(4, stockID);
			stmnt.setInt(5, dateID);
			stmnt.executeUpdate();
			if (byc >= 1 || btc >= 1 || bpc >= 1) {
				// System.out.println(stockID + ":" + dateID + ":" + byc + ":" + btc + ":" +
				// bpc);
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

	


}
