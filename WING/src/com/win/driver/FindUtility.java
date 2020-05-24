package com.win.driver;

import com.win.db.*;
import java.sql.*;
import java.util.*;


public class FindUtility {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] day1Condition = new int[4];
		int[] day2Condition = new int[4];
		int[] day3Condition = new int[4];
		int[] day4Condition = new int[4];
		Hashtable result1 = new Hashtable();
		Hashtable result2 = new Hashtable();
		Hashtable result3 = new Hashtable();
		Hashtable result4 = new Hashtable();
		
		try {
			PreparedStatement stmnt = DBSearch.getTypSearchStmnt();
			//day1 search condition
			//teal
			day1Condition[0]=0;
			//yellow
			day1Condition[1]=1;
			//pink
			day1Condition[2]=0;
			//dateID
			day1Condition[3]=1;
			stmnt.setInt(1, day1Condition[0]);
			stmnt.setInt(2, day1Condition[1]);
			stmnt.setInt(3, day1Condition[2]);
			stmnt.setInt(4, day1Condition[3]);
			ResultSet rs1 = stmnt.executeQuery();
			
			while(rs1.next()) {
				result1.put(rs1.getString(1),""+rs1.getInt(2));
			}
			
			//day2 search condition
			//teal
			day2Condition[0]=0;
			//yellow
			day2Condition[1]=0;
			//pink
			day2Condition[2]=0;
			//dateID
			day2Condition[3]=2;
			stmnt.setInt(1, day2Condition[0]);
			stmnt.setInt(2, day2Condition[1]);
			stmnt.setInt(3, day2Condition[2]);
			stmnt.setInt(4, day2Condition[3]);
			ResultSet rs2 = stmnt.executeQuery();
			
			while(rs2.next()) {
				result2.put(rs2.getString(1),""+rs2.getInt(2));
			}

			
			//day3 search condition
			//teal
			day3Condition[0]=0;
			//yellow
			day3Condition[1]=0;
			//pink
			day3Condition[2]=1;
			//dateID
			day3Condition[3]=3;
			stmnt.setInt(1, day3Condition[0]);
			stmnt.setInt(2, day3Condition[1]);
			stmnt.setInt(3, day3Condition[2]);
			stmnt.setInt(4, day3Condition[3]);
			ResultSet rs3 = stmnt.executeQuery();
			
			while(rs3.next()) {
				result3.put(rs3.getString(1),""+rs3.getInt(2));
			}
			
			//day4 search condition
			//teal
			day4Condition[0]=0;
			//yellow
			day4Condition[1]=0;
			//pink
			day4Condition[2]=0;
			//dateID
			day4Condition[3]=4;
			stmnt.setInt(1, day4Condition[0]);
			stmnt.setInt(2, day4Condition[1]);
			stmnt.setInt(3, day4Condition[2]);
			stmnt.setInt(4, day4Condition[3]);
			ResultSet rs4 = stmnt.executeQuery();
			
			while(rs4.next()) {
				result4.put(rs4.getString(1),""+rs4.getInt(2));
			}
			
			Enumeration en = result1.keys();
			
			while(en.hasMoreElements()) {
				String symbol = en.nextElement().toString();
				String stockID = result1.get(symbol).toString();
				
				if(result2.containsKey(symbol)&&result3.containsKey(symbol)&&result4.containsKey(symbol)) {
					System.out.println(symbol+" : "+stockID);
				}
			}
			
		}catch(Exception ex) {
			ex.printStackTrace(System.out);
		}

	}

}
