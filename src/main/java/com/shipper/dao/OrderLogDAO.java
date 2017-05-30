package com.shipper.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.shipper.logic.Constant;
import com.shipper.model.OrderInfo;

public class OrderLogDAO {
	
	
	public static boolean logOrderStatus(int orderId, int status, int role) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();


			
			// create a sql date object so we can use it in our INSERT statement
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    // the mysql insert statement
		    String query = "INSERT INTO "
						+ " orderLog(orderId, status, role, created)"
						+ " VALUES (?, ?, ?, ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setInt (1, orderId);
		    preparedStmt.setInt (2, status);
		    preparedStmt.setInt (3, role);
		    preparedStmt.setTimestamp(4, timestamp);

		    preparedStmt.execute();
			
			return true;
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
		return false;
	}
	
	
	
	public static boolean logOrderUpdate(int orderId, String feature, String logValue, int role) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();


			
			// create a sql date object so we can use it in our INSERT statement
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    // the mysql insert statement
		    String query = "INSERT INTO "
						+ " orderUpdateLog(orderId, feature, logValue, role, created)"
						+ " VALUES (?, ?, ?, ?, ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setInt (1, orderId);
		    preparedStmt.setString (2, feature);
		    preparedStmt.setString (3, logValue);
		    preparedStmt.setInt (4, role);
		    preparedStmt.setTimestamp(5, timestamp);

		    preparedStmt.execute();
			
			return true;
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		
		return false;
	}
	

}
