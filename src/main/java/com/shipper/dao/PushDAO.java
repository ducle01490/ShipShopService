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
import com.shipper.model.PushInfo;

public class PushDAO {



	public static List<PushInfo> getPushInfoByUser(String userName, int role) {
		String sql = "SELECT * from userPush where roleUserName = '" + (role + userName)  + "'";
		return getPushInfo(sql);
	}
	
	public static PushInfo resultToPushInfo(ResultSet rs) {
		PushInfo session = new PushInfo();
		try {
			String roleUserName = rs.getString("roleUserName");
			int role = Integer.parseInt(roleUserName.substring(0,1));
			String userName = roleUserName.substring(1, roleUserName.length());
			session.setRole(role);
			session.setUserName(userName);
			session.setDeviceToken(rs.getString("deviceToken"));
			session.setDeviceOs(rs.getInt("deviceOs"));
			session.setUpdate(rs.getTimestamp("updated"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return session;
	}
	
	public static List<PushInfo> getPushInfo(String sql) {
		List<PushInfo> result = new ArrayList<PushInfo>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	PushInfo session = resultToPushInfo(rs);
                result.add(session);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	

	

	public static boolean updatePushInfo(String userName, int role, String deviceToken, int deviceOs) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "update userPush SET "
						+ " deviceToken = ?, "
						+ " deviceOs = ?, "
						+ " updated = ?"
						+ " where roleUserName = ?;";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, deviceToken);
		    preparedStmt.setInt (2, deviceOs);
		    preparedStmt.setTimestamp(3, timestamp);
		    preparedStmt.setString (4, role+userName);

		    
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
	
	

	public static boolean createPushInfo(String deviceToken, int deviceOs, String userName, int role) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "INSERT INTO "
						+ " userPush(roleUserName, deviceToken, deviceOs, updated)"
						+ " VALUES (?, ?, ?, ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, role + userName);
		    preparedStmt.setString (2, deviceToken);
		    preparedStmt.setInt (3, deviceOs);
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
