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
import com.shipper.model.CityGeo;

public class ShipperProvinceDAO {

	public static List<CityGeo> getCityGeoByUser(String shipperUserName) {
		String sql = "SELECT * from shipProvince where shipperUserName = '" + shipperUserName  + "'";
		return getCityGeo(sql);
	}
	
	public static CityGeo resultToCityGeo(ResultSet rs) {
		CityGeo city = new CityGeo();
		try {
			
			
			city.setId(rs.getInt("geoId"));
			city.setCity(rs.getString("city"));
			city.setProvince(rs.getString("province"));
			city.setDetail(rs.getString("detail"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return city;
	}
	
	public static List<CityGeo> getCityGeo(String sql) {
		List<CityGeo> result = new ArrayList<CityGeo>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	CityGeo geo = resultToCityGeo(rs);
                result.add(geo);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	

	

	public static boolean updateCityGeo(String shipperUserName, int geoId, String city, String province, String detail) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "update shipProvince SET "
		    		+ " geoId = ?,"
		    		+ " city = ?, "
					+ " province = ?, "
					+ " detail = ?, "
					+ " updated = ?"
					+ " where shipperUserName = ?;";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setInt (1, geoId);
		    preparedStmt.setString (2, city);
		    preparedStmt.setString (3, province);
		    preparedStmt.setString (4, detail);
		    preparedStmt.setTimestamp(5, timestamp);
		    preparedStmt.setString (6, shipperUserName);

		    
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
	
	

	public static boolean createCityGeo(String shipperUserName, int geoId, String city, String province, String detail) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "INSERT INTO "
						+ " shipProvince(shipperUserName,  geoId, city, province, detail, updated)"
						+ " VALUES (?,  ?, ?, ?, ? , ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, shipperUserName);
		    preparedStmt.setInt (2, geoId);
		    preparedStmt.setString (3, city);
		    preparedStmt.setString (4, province);
		    preparedStmt.setString (5, detail);
		    preparedStmt.setTimestamp(6, timestamp);

		    
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
