package com.shipper.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.shipper.logic.Constant;

public class ConfigDAO {


	public static Map<String, String> getConfig() {
		String sql = "SELECT * from shipConfig";
		return getConfig(sql);
	}
	
	public static Map<String, String> resultToConfig(ResultSet rs) {
		Map<String, String> r = new HashMap<String, String>();
		try {
			while ( rs.next() ) {
				r.put(rs.getString(1), rs.getString(2));	
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return r;
	}
	
	public static Map<String, String> getConfig(String sql) {
		Map<String, String> r = new HashMap<String, String>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            
            rs = stmt.executeQuery(sql);
            r = resultToConfig(rs);
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return r;
    }
	

	public static boolean createUpdateConfig(String key, String value) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "INSERT INTO shipConfig (shipKey, shipValue) VALUES (?,?) ON DUPLICATE key UPDATE shipValue = ?;";
		    
		    PreparedStatement preparedStmt = conn.prepareStatement(sql);
		    preparedStmt.setString (1, key);
		    preparedStmt.setString (2, value);
		    preparedStmt.setString (3, value);

		    
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
	

	public static boolean updateConfig(String key, String value) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

		    String query = "update shipConfig SET "
						+ " shipKey = ?, "
						+ " shipValue = ?;";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, key);
		    preparedStmt.setString (2, value);

		    
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
	
	

	public static boolean createConfig(String key, String value) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

		    String query = "INSERT INTO "
						+ " shipConfig(shipKey, shipValue)"
						+ " VALUES (?, ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, key);
		    preparedStmt.setString (2, value);

		    
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
