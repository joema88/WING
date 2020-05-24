package com.win.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.Calendar;

public class DB {

	/*
	 *   //check DB size
	 *   SELECT
         table_schema AS 'DB Name',
         ROUND(SUM(data_length + index_length) / 1024 / 1024, 1) AS 'DB Size in MB'
         FROM
         information_schema.tables
         GROUP BY
         table_schema;
	 */
	private static Connection dbcon = null;
	private static PreparedStatement symbolStmnt = null;
	private static PreparedStatement dateStmnt = null;
	private static PreparedStatement rockStmnt = null;
	private static Statement stmnt = null;

	public static void closeConnection() {
		try {
			if (stmnt != null) {
				stmnt.close();
				stmnt = null;
			}
			if (symbolStmnt != null) {
				symbolStmnt.close();
				symbolStmnt = null;
			}
			if (dateStmnt != null) {
				dateStmnt.close();
				dateStmnt = null;
			}
			if (rockStmnt != null) {
				rockStmnt.close();
				rockStmnt = null;
			}
			
			if(dbcon!=null) {
				dbcon.close();
				dbcon = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {

		try {
			if (dbcon == null) {
				dbcon = DriverManager.getConnection(
						"jdbc:mysql://127.0.0.1:3306/STOCKS?allowPublicKeyRetrieval=true&useSSL=false", "root",
						"Goldfish@3224");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dbcon;
	}

	public static Statement getStatement() {
		getConnection();
		try {
			if (stmnt == null) {
				stmnt = dbcon.createStatement();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stmnt;
	}

	public static int getNextDateID() {
		int nextID = 1;
		getStatement();
		try {
			String query = " SELECT COUNT(*) FROM DATES";

			ResultSet rs = stmnt.executeQuery(query);

			if (rs.next()) {
				nextID = rs.getInt(1) + 1;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return nextID;
	}

	public static int getDateID(String date) {
		int dateID = 0;
		getStatement();
		try {
			String query = " SELECT DATEID FROM DATES WHERE date(CDATE)='"+ date+"'";

			ResultSet rs = stmnt.executeQuery(query);

			if (rs.next()) {
				dateID = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return dateID;
	}
	
	public static int getSymbolID(String symbol) {
		int stockID = 0;
		getStatement();
		try {
			String query = " SELECT STOCKID FROM SYMBOLS WHERE SYMBOL='"+symbol+"'";

			ResultSet rs = stmnt.executeQuery(query);

			if (rs.next()) {
				stockID = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return stockID;
	}
	
	public static int getNextSymbolID() {
		int nextID = 1;
		getStatement();
		try {
			String query = " SELECT COUNT(*) FROM SYMBOLS";

			ResultSet rs = stmnt.executeQuery(query);

			if (rs.next()) {
				nextID = rs.getInt(1) + 1;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return nextID;
	}

	// SELECT our_date FROM our_table WHERE idate >= '1997-05-05';
	public static boolean checkDateExist(String date) {
		boolean exist = true;
		getStatement();
		try {
			String query = " SELECT COUNT(*) FROM DATES WHERE date(CDATE)='"+ date+"'";

			ResultSet rs = stmnt.executeQuery(query);

			if (rs.next()&&rs.getInt(1) == 0) {
				exist = false;
			}
			;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return exist;
	}

	public static boolean checkSymbolExist(String symbol) {
		boolean exist = true;
		getStatement();
		try {
			String query = " SELECT COUNT(*) FROM SYMBOLS WHERE SYMBOL='" + symbol + "'";

			ResultSet rs = stmnt.executeQuery(query);

			if (rs.next()&&rs.getInt(1) == 0) {
				exist = false;
			}
			;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return exist;
	}

	
	public static boolean checkBBRecordExist(int stockID, int dateID) {
		boolean exist = true;
		getStatement();
		try {
			String query = " SELECT COUNT(*) FROM BBROCK WHERE STOCKID = " + stockID + " and DATEID = "+dateID;

			ResultSet rs = stmnt.executeQuery(query);

			if (rs.next()&&rs.getInt(1) == 0) {
				exist = false;
			}
			;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}

		return exist;
	}
	
	public static PreparedStatement getDateInsertStatement() {
		getConnection();

		if (dateStmnt == null) {
			try {
				String query = " insert into DATES (DATEID, CDATE) values (?, ?)";

				dateStmnt = dbcon.prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return dateStmnt;
	}

	public static PreparedStatement getSymbolInsertStatement() {
		getConnection();

		if (symbolStmnt == null) {
			try {
				String query = " insert into SYMBOLS (STOCKID, SYMBOL) values (?, ?)";

				symbolStmnt = dbcon.prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return symbolStmnt;
	}

	public static PreparedStatement getRockInsertStatement() {
		getConnection();

		if (rockStmnt == null) {
			try {
				String query = " insert into BBROCK(STOCKID,DATEID,PERCENT,CLOSE,NETCHANGE,ATR,OPEN,HIGH,LOW,LOW52,HIGH52,MARKCAP,VOLUME,YELLOW,TEAL,PINK)"
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				rockStmnt = dbcon.prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace(System.out);
			}
		}

		return rockStmnt;
	}

	public static void main(String[] args) {

		// https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html#package.description
		// auto java.sql.Driver discovery -- no longer need to load a java.sql.Driver
		// class via Class.forName

		// register JDBC driver, optional since java 1.6
		/*
		 * try { Class.forName("com.mysql.jdbc.Driver"); } catch (ClassNotFoundException
		 * e) { e.printStackTrace(); }
		 */

		// $$$$$$$$ SQL
		/*
		 * 1. CREATE TABLE SYMBOLS(STOCKID SMALLINT, SYMBOL VARCHAR(10), PRIMARY KEY
		 * (STOCKID)); 
		 * 2. CREATE TABLE DATES(DATEID SMALLINT, CDATE DATE, PRIMARY KEY
		 * (DATEID)); 
		 * 3. CREATE TABLE BBROCK(STOCKID SMALLINT, DATEID SMALLINT, PERCENT FLOAT, CLOSE FLOAT,NETCHANGE FLOAT, ATR FLOAT, OPEN FLOAT,HIGH FLOAT, LOW FLOAT,LOW52 FLOAT,HIGH52 FLOAT, MARKCAP FLOAT,VOLUME INT, YELLOW TINYINT DEFAULT 0,TEAL TINYINT DEFAULT 0, PINK TINYINT DEFAULT 0, BYC SMALLINT DEFAULT 0, BTC SMALLINT DEFAULT 0, BPC SMALLINT DEFAULT 0, BYS SMALLINT DEFAULT 0,BTS SMALLINT DEFAULT 0, BPS SMALLINT DEFAULT 0,BBS SMALLINT DEFAULT 0,BT9 SMALLINT DEFAULT 0, PTVAL FLOAT DEFAULT 0.0, PTCP FLOAT DEFAULT 0.0, MERGE TINYINT DEFAULT 0, PRIMARY KEY (STOCKID, DATEID), FOREIGN KEY (STOCKID) REFERENCES SYMBOLS(STOCKID) ON DELETE CASCADE, FOREIGN KEY (DATEID) REFERENCES DATES(DATEID));
		 * */
		//select a.DATEID,CDATE, a.STOCKID, CLOSE, ATR,TEAL, YELLOW, PINK, BTC,BYC,BPC,BTS,BYS,BPS,BBS,PTVAL, PTCP,BT9, MERGE, MARKCAP, VOLUME FROM BBROCK a, SYMBOLS b, DATES c  WHERE a.STOCKID = b.STOCKID and a.DATEID=c.DATEID and b.SYMBOL='LYG';
		
		//$$$$$$$$$ SQL

		// auto close connection
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/STOCKS?allowPublicKeyRetrieval=true&useSSL=false", "root",
					"Goldfish@3224");

			if (conn != null) {
				System.out.println("Connected to the database!");

				/*
				 * create table users ( id int unsigned auto_increment not null, first_name
				 * varchar(32) not null, last_name varchar(32) not null, date_created timestamp
				 * default now(), is_admin boolean, num_points int, primary key (id) );
				 */

				// create a sql date object so we can use it in our INSERT statement
				Calendar calendar = Calendar.getInstance();
				java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

				/*
				 * // the mysql insert statement String query =
				 * " insert into users (first_name, last_name, date_created, is_admin, num_points)"
				 * + " values (?, ?, ?, ?, ?)";
				 * 
				 * // create the mysql insert preparedstatement PreparedStatement preparedStmt =
				 * conn.prepareStatement(query); preparedStmt.setString (1, "Barney");
				 * preparedStmt.setString (2, "Rubble"); preparedStmt.setDate (3, startDate);
				 * preparedStmt.setBoolean(4, false); preparedStmt.setInt (5, 5000);
				 * 
				 * // execute the preparedstatement preparedStmt.execute();
				 * 
				 * conn.close();
				 */
				String query = " insert into HISTORY (STOCKID, DATEID, OPEN, YELLOW)" + " values (?, ?, ?, ?)";

				// create the mysql insert preparedstatement
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setInt(1, 3);
				preparedStmt.setInt(2, 1);
				preparedStmt.setFloat(3, 9.80f);
				preparedStmt.setInt(4, 3);

				// execute the preparedstatement
				preparedStmt.execute();

				conn.close();
			} else {
				System.out.println("Failed to make connection!");
			}

		} catch (SQLException e) {
			e.printStackTrace(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
