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

public class GeoDAO {



	public static List<CityGeo> getCityGeoList() {
		String sql = "SELECT * from cityGeo order by id";
		return getCityGeo(sql);
	}
	
	public static CityGeo resultToCityGeo(ResultSet rs) {
		CityGeo city = new CityGeo();
		try {
			
			city.setId(rs.getInt("id"));
			city.setCity(rs.getString("city"));
			city.setProvince(rs.getString("province"));
			city.setDetail(rs.getString("detail"));
			city.setUpdated(rs.getTimestamp("updated"));
			city.setShipPrice(rs.getLong("shipPrice"));
			city.setFastShipPrice(rs.getLong("fastShipPrice"));
			
			
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
            	CityGeo city = resultToCityGeo(rs);
                result.add(city);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	

	

	public static boolean updateCityGeo(int id, String city, String province, String detail, long shipPrice, long fastShipPrice) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "update cityGeo SET "
						+ " city = ?, "
						+ " province = ?, "
						+ " detail = ?, "
						+ " updated = ?, "
						+ " shipPrice = ?, "
						+ " fastShipPrice = ?, "
						+ " where id = ?;";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, city);
		    preparedStmt.setString (2, province);
		    preparedStmt.setString (3, detail);
		    preparedStmt.setTimestamp(4, timestamp);
		    preparedStmt.setLong (6, shipPrice);
		    preparedStmt.setLong (7, fastShipPrice);
		    preparedStmt.setInt (8, id);

		    
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
	
	

	public static boolean createCityGeo(String city, String province, String detail) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "INSERT INTO "
						+ " cityGeo(city, province, detail, updated)"
						+ " VALUES (?, ?, ?, ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, city);
		    preparedStmt.setString (2, province);
		    preparedStmt.setString (3, detail);
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
}
