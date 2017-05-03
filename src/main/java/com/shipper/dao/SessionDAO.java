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
import com.shipper.model.SessionInfo;

public class SessionDAO {
	
	public static List<SessionInfo> getSessionByKey(String sessionKey) {
		String sql = "SELECT * from userSession where sessionKey = '" + sessionKey  + "'";
		return getUserSession(sql);
	}

	public static List<SessionInfo> getSessionByUser(String userName, int role) {
		String sql = "SELECT * from userSession where roleUserName = '" + (role + userName)  + "'";
		return getUserSession(sql);
	}
	
	public static SessionInfo resultToSessionInfo(ResultSet rs) {
		SessionInfo session = new SessionInfo();
		try {
			String roleUserName = rs.getString("roleUserName");
			int role = Integer.parseInt(roleUserName.substring(0,1));
			String userName = roleUserName.substring(1, roleUserName.length());
			session.setRole(role);
			session.setUserName(userName);
			session.setSessionKey(rs.getString("sessionKey"));
			session.setCreated(rs.getTimestamp("updated"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return session;
	}
	
	public static List<SessionInfo> getUserSession(String sql) {
		List<SessionInfo> result = new ArrayList<SessionInfo>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	SessionInfo session = resultToSessionInfo(rs);
                result.add(session);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	

	

	public static boolean updateUserSession(String userName, int role, String sessionKey) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "update userSession SET "
						+ " sessionKey = ?, "
						+ " updated = ?"
						+ " where roleUserName = ?;";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, sessionKey);
		    preparedStmt.setTimestamp(2, timestamp);
		    preparedStmt.setString (3, role+userName);

		    
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
	
	

	public static boolean createUserSession(String sessionKey, String userName, int role) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    
		    String query = "INSERT INTO "
						+ " userSession(roleUserName, sessionKey, updated)"
						+ " VALUES (?, ?, ?);";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setString (1, role + userName);
		    preparedStmt.setString (2, sessionKey);
		    preparedStmt.setTimestamp(3, timestamp);

		    
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
