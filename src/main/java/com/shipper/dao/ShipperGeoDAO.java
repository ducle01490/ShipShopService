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
import com.shipper.model.ShipperGeo;

public class ShipperGeoDAO {
	

	public static List<ShipperGeo> getShipperGeoByUser(String shipperUserName) {
		String sql = "SELECT * from shipperGeo where shipperUserName = '" + shipperUserName  + "'";
		return getShipperGeo(sql);
	}
	
	public static ShipperGeo resultToShipperGeo(ResultSet rs) {
		ShipperGeo geo = new ShipperGeo();
		try {
			
			
			geo.setShipperUserName(rs.getString("shipperUserName"));
			geo.setShipperUserName(rs.getString("latitude"));
			geo.setShipperUserName(rs.getString("longitude"));
			geo.setShipperUserName(rs.getString("address"));
			geo.setUpdated(rs.getTimestamp("updated"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return geo;
	}
	
	public static List<ShipperGeo> getShipperGeo(String sql) {
		List<ShipperGeo> result = new ArrayList<ShipperGeo>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	ShipperGeo session = resultToShipperGeo(rs);
                result.add(session);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	

	

	public static boolean updateShipperGeo(String shipperUserName, String longitude, String latitude, String address) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "update shipperGeo SET "
						+ " longitude = ?, "
						+ " latitude = ?, "
						+ " address = ?, "
						+ " updated = ? "
						+ " where shipperUserName = ?;";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, longitude);
		    preparedStmt.setString (2, latitude);
		    preparedStmt.setString (3, address);
		    preparedStmt.setTimestamp(4, timestamp);
		    preparedStmt.setString (5, shipperUserName);

		    
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
	
	

	public static boolean createShipperGeo(String shipperUserName, String longitude, String latitude, String address) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "INSERT INTO "
						+ " userSession(shipperUserName, latitude, longitude, address, updated)"
						+ " VALUES (?, ?, ?, ? , ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, shipperUserName);
		    preparedStmt.setString (2, latitude);
		    preparedStmt.setString (3, longitude);
		    preparedStmt.setString (4, address);
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
