package com.shipper.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shipper.logic.Constant;
import com.shipper.model.Shipper;
import com.shipper.model.User;

public class ShipperDAO {

//	// JDBC driver name and database URL
//	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//	static final String DB_URL = "jdbc:mysql://localhost/shipshop";
//
//	// Database credentials
//	static final String USER = "root";
//	static final String PASS = "orek@db16";

	public static void main(String[] args) {
	}
	

	
	public static List<Shipper> getShipperByUserName(String userName) {
		String sql = "SELECT * from shipper where userName = '" + userName  + "'";
		return getShipper(sql);
	}
	
	public static Shipper resultToShipper(ResultSet rs) {
		Shipper shipper = new Shipper();
		try {
			shipper.setShipperId(rs.getInt("shipper_id"));
			shipper.setUserName(rs.getString("userName"));
			shipper.setPassword(rs.getString("password"));
			shipper.setPhoneNumber(rs.getString("phoneNumber"));
			shipper.setStatus(rs.getInt("status"));
			shipper.setShipperName(rs.getString("shipperName"));
			shipper.setAddress(rs.getString("address"));
			shipper.setBirthDay(rs.getString("birthday"));
			shipper.setIdNumber(rs.getString("idNumber"));
			shipper.setMotorNumber(rs.getString("motorNumber"));
			
			shipper.setPreLoginDid(rs.getString("previousLoginDeviceId"));
			shipper.setPreLoginGps(rs.getString("preivousLoginGPS"));
			shipper.setUpdateGpsTime(rs.getDate("updateGPSTime").getTime());
			shipper.setPreLoginTime(rs.getDate("previousLoginTime").getTime());
			shipper.setCurrentGps(rs.getString("currentGPS"));
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shipper;
	}
	
	public static List<Shipper> getShipper(String sql) {
		List<Shipper> result = new ArrayList<Shipper>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	Shipper shipper = resultToShipper(rs);
                result.add(shipper);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	
	/*
+-----------------------+---------------+------+-----+---------------------+-----------------------------+
| Field                 | Type          | Null | Key | Default             | Extra                       |
+-----------------------+---------------+------+-----+---------------------+-----------------------------+
| shipper_id            | int(11)       | NO   | PRI | NULL                | auto_increment              |
| userName              | varchar(200)  | NO   | MUL | NULL                |                             |
| password              | varchar(60)   | NO   |     | NULL                |                             |
| phoneNumber           | varchar(20)   | NO   |     | NULL                |                             |
| status                | int(11)       | NO   |     | NULL                |                             |
| shipperName           | varchar(200)  | NO   |     | NULL                |                             |
| motorNumber           | varchar(20)   | NO   |     | NULL                |                             |
| birthday              | varchar(60)   | NO   |     | NULL                |                             |
| address               | varchar(1000) | NO   |     | NULL                |                             |
| idNumber              | varchar(20)   | NO   |     | NULL                |                             |
| previousLoginDeviceId | varchar(200)  | YES  |     | NULL                |                             |
| previousLoginGPS      | varchar(1000) | YES  |     | NULL                |                             |
| currentGPS            | varchar(1000) | YES  |     | NULL                |                             |
| previousLogintime     | datetime      | YES  |     | NULL                |                             |
| uupdateGPSTime        | datetime      | YES  |     | NULL                |                             |
              
	 */	
	
	public static boolean updateShipperPassword(String userName, String password) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shipper SET"
					+ " password = '" + password + "' "
					+ " where userName = '" + userName + "'";
			stmt.executeUpdate(sql);
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
	
	
	public static boolean updateShipperStatus(String userName, int status) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shipper SET"
					+ " status = " + status + " "
					+ " where userName = '" + userName + "'";
			stmt.executeUpdate(sql);
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
	
	public static boolean updateShipperPhone(String userName, String phoneNumber) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shipper SET"
					+ " status = " + User.s_nonVerified + ", "
					+ " phoneNumber = '" + phoneNumber + "' "
					+ " where userName = '" + userName + "'";
			stmt.executeUpdate(sql);
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
	
	public static boolean updateShipperProfile(String userName, String shipperName, String motorNumber, String birthDay, String address, String idNumber) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			String sql = "UPDATE "
					+ " shipper SET"
					
					
					+ " shipperName = '" + shipperName + "', "
					+ " motorNumber = '" + motorNumber + "', "
					+ " birthday = '" + birthDay + "', "
					+ " address = '" + address + "', "
					+ " idNumber = '" + idNumber + "' "
					+ " where userName = '" + userName + "'";
			
		    stmt.executeUpdate(sql);
			
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
	
	public static void updateShipperAccount(Shipper shipper) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			String sql = "UPDATE "
					+ " shipper SET"
					
					+ " userName = '" + shipper.getUserName() + "', "
					+ " password = '" + shipper.getPassword() + "', "
					+ " phoneNumber = '" + shipper.getPhoneNumber() + "', "
					+ " status = " + shipper.getStatus() + ", "
					
					+ " shipperName = '" + shipper.getShipperName() + "', "
					+ " motorNumber = '" + shipper.getMotorNumber() + "', "
					+ " birthday = '" + shipper.getBirthDay() + "', "
					+ " address = '" + shipper.getAddress() + "', "
					+ " idNumber = '" + shipper.getIdNumber() + "' "
					+ " where userName = '" + shipper.getUserName() + "'";
			
		    stmt.executeUpdate(sql);
			

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
		
		
	}
	
	
	public static boolean createShipperAccount(Shipper shipper) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "INSERT INTO "
					+ " shipper(userName, password, phoneNumber, status, shipperName, motorNumber, birthday, address, idNumber)"
					+ " VALUES ("
					+ " '" + shipper.getUserName() + "', "
					+ " '" + shipper.getPassword() + "', "
					+ " '" + shipper.getPhoneNumber() + "', "
					+ " " + shipper.getStatus() + ", "
					
					+ " '" + shipper.getShipperName() + "', "
					+ " '" + shipper.getMotorNumber() + "', "
					+ " '" + shipper.getBirthDay() + "', "
					+ " '" + shipper.getAddress() + "', "
					+ " '" + shipper.getIdNumber() + "' "
					+ ");";
			stmt.executeUpdate(sql);
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
