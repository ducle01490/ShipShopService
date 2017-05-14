package com.shipper.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shipper.logic.Constant;
import com.shipper.logic.geo.GeoLogic;
import com.shipper.model.Shop;
import com.shipper.model.User;

public class ShopDAO {

	

	public static void main(String[] args) {
	}
	
	/*
 shop_id     | int(11)       | NO   | PRI | NULL                | auto_increment              |
| userName    | varchar(200)  | NO   | MUL | NULL                |                             |
| password    | varchar(60)   | NO   |     | NULL                |                             |
| phoneNumber | varchar(20)   | NO   |     | NULL                |                             |
| status      | int(11)       | NO   |     | NULL                |                             |
| shopName    | varchar(200)  | NO   |     | NULL                |                             |
| address     | varchar(1000) | NO   |     | NULL                |                             |
| bankInfo    | varchar(5000) | NO   |     | NULL                |                             |
| facebook    | varchar(1000) | YES  |     | NULL                |                             |
| zalo        | varchar(1000) | YES  |     | NULL                |                             
	 */
	
	public static List<Shop> getShopByUserName(String userName) {
		String sql = "SELECT * from shop where userName = '" + userName  + "'";
		return getShop(sql);
	}
	
	public static Shop resultToShop(ResultSet rs) {
		Shop shop = new Shop();
		try {
			shop.setShopId(rs.getInt("shop_id"));
			shop.setUserName(rs.getString("userName"));
			shop.setPassword(rs.getString("password"));
			shop.setPhoneNumber(rs.getString("phoneNumber"));
			shop.setStatus(rs.getInt("status"));
			shop.setShopName(rs.getString("shopName"));
			shop.setAddress(rs.getString("address"));
			shop.setBankInfo(rs.getString("bankInfo"));
			shop.setFacebook(rs.getString("facebook"));
			shop.setZalo(rs.getString("zalo"));
			
			
			String city = rs.getString("city");
			if(city == null) {
				city = GeoLogic.hanoi;
			}
			String province = rs.getString("province");
			if(province == null) {
				province = GeoLogic.hanoi_provinces[0];
			}
			Integer id = rs.getInt("cityGeoId");
			if(id == null) {
				id = 1;
			}
			
			shop.setCity(city);
			shop.setProvince(province);
			shop.setCityGeoId(id);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return shop;
	}
	
	public static List<Shop> getShop(String sql) {
		List<Shop> result = new ArrayList<Shop>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                Shop shop = resultToShop(rs);
                result.add(shop);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
    }
	
	public static boolean updateShopProfile(String userName, String shopName, String address, String bankInfo, String facebook, String zalo,
			String city, String province, int cityGeoId) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			String sql = "UPDATE "
					+ " shop SET"
					
					
					+ " shopName = '" + shopName + "', "
					+ " address = '" + address + "', "
					+ " bankInfo = '" + bankInfo + "', "
					+ " facebook = '" + facebook + "', "
					+ " zalo = '" + zalo + "', "
					
					+ " city = '" + city + "', "
					+ " province = '" + province + "', "
					+ " cityGeoId = '" + cityGeoId + "' "
					
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
	
	public static void updateShopAccount(Shop shop) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			

			String sql = "UPDATE "
					+ " shop SET"
					
					+ " userName = '" + shop.getUserName() + "', "
					+ " password = '" + shop.getPassword() + "', "
					+ " phoneNumber = '" + shop.getPhoneNumber() + "', "
					+ " status = " + shop.getStatus() + ", "
					
					+ " shopName = '" + shop.getShopName() + "', "
					+ " address = '" + shop.getAddress() + "', "
					+ " bankInfo = '" + shop.getBankInfo() + "', "
					+ " facebook = '" + shop.getFacebook() + "', "
					+ " zalo = '" + shop.getZalo() + "', "
					
					+ " city = '" + shop.getCity() + "', "
					+ " province = '" + shop.getProvince() + "', "
					+ " cityGeoId = '" + shop.getCityGeoId() + "' "
					
					+ " where userName = '" + shop.getUserName() + "'";
			
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
	
	
	public static boolean updateShopStatus(String userName, int status) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shop SET"
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
	
	public static boolean updateShopPhone(String userName, String phoneNumber) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shop SET"
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
	
	public static boolean updateShopPassword(String userName, String password) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shop SET"
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
	
	public static boolean createShopAccount(Shop shop) {
		
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "INSERT INTO "
					+ " shop(userName, password, phoneNumber, status, shopName, address, bankInfo, facebook, zalo)"
					+ " VALUES ("
					+ " '" + shop.getUserName() + "', "
					+ " '" + shop.getPassword() + "', "
					+ " '" + shop.getPhoneNumber() + "', "
					+ " " + shop.getStatus() + ", "
					+ " '" + shop.getShopName() + "', "
					+ " '" + shop.getAddress() + "', "
					+ " '" + shop.getBankInfo() + "', "
					+ " '" + shop.getFacebook() + "', "
					+ " '" + shop.getZalo() + "' "
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
