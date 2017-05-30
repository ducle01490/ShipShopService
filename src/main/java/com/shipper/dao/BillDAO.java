package com.shipper.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.shipper.logic.Constant;
import com.shipper.model.ShipBill;

public class BillDAO {
	

	
	public static List<ShipBill> getShipBillById(String shopUserName, 
			String startTime, String endTime, int offset, int limit) {
		String sql = "SELECT * from shipBill "
				+ " where shopUserName = '" + shopUserName + "' "
				+ " and billDate <= '" + endTime  + "' "
				+ " and billDate >= '" + startTime + "' "
				+ " order by id desc "
				+ " limit " + offset + " , " + limit
				+ ";";
		return getShipBill(sql);
	}
	
	public static ShipBill resultToOrderInfo(ResultSet rs) {
		ShipBill order = new ShipBill();
		try {
			order.setShopUserName(rs.getString("shopUserName"));
			order.setBillDate(rs.getDate("billDate"));
			order.setOrderFromDate(rs.getDate("orderFromDate"));
			order.setOrderToDate(rs.getDate("orderToDate"));
			order.setMoney(rs.getLong("money"));
			order.setBillMethod(rs.getString("billMethod"));
			order.setUpdatedBy(rs.getString("updatedBy"));
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		
		return order;
	}
	
	public static List<ShipBill> getShipBill(String sql) {
		List<ShipBill> result = new ArrayList<ShipBill>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	ShipBill order = resultToOrderInfo(rs);
            	if(order!=null)
            		result.add(order);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	
	public static boolean createBill(String shopUserName, String billDate, 
			String orderFromDate, String orderToDate, long money, String billMethod, 
			String updatedBy) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    // the mysql insert statement
		    String query = "INSERT INTO "
						+ " shipBill(shopUserName, billDate, orderFromDate, orderToDate, money, billMethod, updatedBy, created)"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, shopUserName);
		    preparedStmt.setString (2, billDate);
		    preparedStmt.setString (3, orderFromDate);
		    preparedStmt.setString (4, orderToDate);
		    preparedStmt.setLong (5, money);
		    preparedStmt.setString (6, billMethod);
		    preparedStmt.setString (7, updatedBy);
		    preparedStmt.setTimestamp(8, timestamp);
		    
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
