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

	public static void checkTrend(int stockID, int dateID) {
		// first check BYC, BTC, BPC
		{
			// case 1 all TEAL
			
			//case 2 all yellow, pink
			
			//case 3 mixed teal/yellow
			
			//case 4 no signal
			
			//case 5 yellow, pink (bear)--> Teal
			
			//case 6 yellow, pink (mild bull)--> Teal
			
			//case 7 yellow, pink (random)--> Teal

			//case 8 teal --> yellow, pink


		}

		// update YTP Summary

		// update BBS summary

		// update PTVAL, PEAK | TROUGH VAL

		// UPDATE PCTP Change%

	}
	
	public static void updateYTPSummary(int stockID, int dateID, int bys, int bts, int bps) {
		try {
			PreparedStatement stmnt = DBAnalysis.getbytpSummaryPreparedStatement();
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
