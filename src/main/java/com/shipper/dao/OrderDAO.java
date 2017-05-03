package com.shipper.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.shipper.logic.Constant;
import com.shipper.logic.Utils;
import com.shipper.model.OrderInfo;
import com.shipper.model.ShipperAggregate;
import com.shipper.model.User;

public class OrderDAO {


	
	
	public static int getCurrentId() {
		List<OrderInfo> o = getOrderLastest();
		if(o.size() == 0) {
			return 0;
		} else {
			int v = o.get(0).getOrderId();
			return v;
		}
	}
	
	
	public static List<OrderInfo> getOrderLastest() {
		String sql = "SELECT * from shipOrder order by orderId desc limit 0, 1;";
		return getOrderInfo(sql);
	}
	
	public static List<OrderInfo> getOrderInfoById(int orderId) {
		String sql = "SELECT * from shipOrder where orderId = " + orderId  + ";";
		return getOrderInfo(sql);
	}
	
	public static OrderInfo resultToOrderInfo(ResultSet rs) {
		OrderInfo order = new OrderInfo();
		try {
			order.setOrderId(rs.getInt("orderId"));
			order.setOrderTitle(rs.getString("orderTitle"));
			order.setShopId(rs.getInt("shopId"));
			order.setShopUserName(rs.getString("shopUserName"));
			order.setShopName(rs.getString("shopName"));
			
			order.setReceiveAddress(rs.getString("receiveAddress"));
			order.setCustomerAddress(rs.getString("customerAddress"));
			order.setCustomerName(rs.getString("customerName"));
			order.setCustomerPhone(rs.getString("customerPhone"));
			
			order.setDeliveryType(rs.getInt("deliveryType"));
			order.setDeliveryPrice(rs.getLong("deliveryPrice"));
			order.setProductPrice(rs.getLong("productPrice"));
			order.setNoteTime(rs.getString("noteTime"));
			order.setNoteProduct(rs.getString("noteProduct"));
			order.setOrderStatus(rs.getInt("orderStatus"));
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		
		return order;
	}
	
	public static List<OrderInfo> getOrderInfo(String sql) {
		List<OrderInfo> result = new ArrayList<OrderInfo>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	OrderInfo order = resultToOrderInfo(rs);
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
	
	public static boolean createOrder(int orderId, String orderTitle,
			int shopId, String shopUserName, String shopName,
			String receiveAddress,
			String customerAddress, String customerName,
			String customerPhone,
			int deliveryType, 
			long deliveryPrice, long productPrice,
			String noteTime, String noteProduct) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

//			String sql = "INSERT INTO "
//					+ " shipOrder(orderId, orderTitle, shopId, shopUserName, shopName,"
//					+ " receiveAddress, customerAddress, customerName, customerPhone,"
//					+ " deliveryType, deliveryPrice, productPrice, noteTime, noteProduct,"
//					+ " orderStatus, created"
//					+ ")"
//					+ " VALUES ("
//					+ " " + orderId + ", "
//					+ " '" + orderTitle + "', "
//					+ " " + shopId + ", "
//					+ " '" + shopUserName + "', "
//					+ " '" + shopName + "', "
//					
//					+ " '" + receiveAddress + "', "
//					+ " '" + customerAddress + "', "
//					+ " '" + customerName + "', "
//					+ " '" + customerPhone + "', "
//					+ " " + deliveryType + ", "
//					+ " " + deliveryPrice + ", "
//					+ " " + productPrice + ", "
//					+ " '" + noteTime + "', "
//					+ " '" + noteProduct + "', "
//					
//					+ " " + OrderInfo.order_wait + " "
//					
//					+ ");";
			
			
			// create a sql date object so we can use it in our INSERT statement
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    // the mysql insert statement
		    String query = "INSERT INTO "
						+ " shipOrder(orderId, orderTitle, shopId, shopUserName, shopName,"
						+ " receiveAddress, customerAddress, customerName, customerPhone,"
						+ " deliveryType, deliveryPrice, productPrice, noteTime, noteProduct,"
						+ " orderStatus, created" + ")"
						+ " VALUES ("+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "
						+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "
						+ " ?, "+ " ? "+ ");";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setInt (1, orderId);
		    preparedStmt.setString (2, orderTitle);
		    preparedStmt.setInt (3, shopId);
		    preparedStmt.setString (4, shopUserName);
		    preparedStmt.setString (5, shopName);
		    preparedStmt.setString (6, receiveAddress);
		    preparedStmt.setString (7, customerAddress);
		    preparedStmt.setString (8, customerName);
		    preparedStmt.setString (9, customerPhone);
		    preparedStmt.setInt (10, deliveryType);
		    preparedStmt.setLong(11, deliveryPrice);
		    preparedStmt.setLong(12, productPrice);
		    preparedStmt.setString (13, noteTime);
		    preparedStmt.setString (14, noteProduct);
		    preparedStmt.setInt (15, OrderInfo.order_wait);
		    preparedStmt.setTimestamp(16, timestamp);

		    // execute the preparedstatement
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
	
	
	public static boolean confirmOrderStatus(int orderId, int status, int role) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			if(role == User.role_shipper) {
				String sql = "UPDATE "
						+ " shipOrder SET"
						+ " statusShipperConfirmed = " + status + " "
						+ " where orderId = " + orderId + "";
				stmt.executeUpdate(sql);
			} else if(role == User.role_shop) {
				String sql = "UPDATE "
						+ " shipOrder SET"
						+ " statusShopConfirmed = " + status + " "
						+ " where orderId = " + orderId + "";
				stmt.executeUpdate(sql);
			}
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
	
	
	public static boolean updateOrderStatus(int orderId, int status, int role) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			if(role == User.role_shipper) {
				String sql = "UPDATE "
						+ " shipOrder SET"
						+ " orderStatus = " + status + ", "
						+ " statusShipperConfirmed = " + status + " "
						+ " where orderId = " + orderId + "";
				stmt.executeUpdate(sql);
			} else if(role == User.role_shop) {
				String sql = "UPDATE "
						+ " shipOrder SET"
						+ " orderStatus = " + status + ", "
						+ " statusShopConfirmed = " + status + " "
						+ " where orderId = " + orderId + "";
				stmt.executeUpdate(sql);
			}
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
	
	public static boolean updateOrderStatus(int orderId, int status) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shipOrder SET"
					+ " orderStatus = " + status + " "
					+ " where orderId = " + orderId + "";
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
	
	
	public static boolean bidOrder(int orderId, int shipperId, String  shipperUserName, String shipperName) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shipOrder SET"
					+ " orderStatus = " + OrderInfo.order_bidded + ", "
					+ " shipperId = " + shipperId + ", "
					+ " shipperUserName = '" + shipperUserName + "', "
					+ " shipperName = '" + shipperName + "', "
					+ " statusShipperConfirmed = " + OrderInfo.order_bidded + " "
					+ " where orderId = " + orderId + "";
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
	
	

	
	public static boolean cancelOrder(int orderId) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();

			String sql = "UPDATE "
					+ " shipOrder SET"
					+ " orderStatus = " + OrderInfo.order_cancel + ""
					+ " where orderId = " + orderId + "";
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
	
	
	
	public static List<OrderInfo> getOrderFullById(int orderId) {
		String sql = "SELECT * from shipOrder where orderId = " + orderId  + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getOrderFull(int offset, int numb) {
		String sql = "SELECT * from shipOrder order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShopOrderFull(String shopUserName, int offset, int numb) {
		
		String sql = "SELECT * from shipOrder where shopUserName = '" + shopUserName + "' "
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getOrderFullByStatus(int orderStatus, int offset, int numb) {
		String sql = "SELECT * from shipOrder where orderStatus = " + orderStatus
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getOrderFullByStatus(String shopUserName, int orderStatus, int offset, int numb) {
		String sql = "SELECT * from shipOrder where shopUserName = '" + shopUserName + "' "
				+ " AND orderStatus = " + orderStatus 
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShopOrderFullByUpdatedtime(String shopUserName, int orderStatus, String startTime, String endTime) {
		String sql = "SELECT * from shipOrder where shopUserName = '" + shopUserName + "' "
				+ " AND orderStatus = " + orderStatus
				+ " AND updated >= '" + startTime + "' AND updated <=  '" + endTime + "' "
				+ "order by orderId desc";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShopOrderFullByUpdatedtime(String shopUserName, String startTime, String endTime) {
		String sql = "SELECT * from shipOrder where shopUserName = '" + shopUserName + "' "
				+ " AND updated >= '" + startTime + "' AND updated <=  '" + endTime + "' "
				+ "order by orderId desc";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getOrderFullByUpdatedtime(int orderStatus, String startTime, String endTime) {
		String sql = "SELECT * from shipOrder where orderStatus = " + orderStatus + " "
				+ " AND updated >= '" + startTime + "' AND updated <=  '" + endTime + "' "
				+ "order by orderId desc";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getOrderFullByUpdatedtime(String startTime, String endTime) {
		String sql = "SELECT * from shipOrder where updated >= '" + startTime + "' AND updated <=  '" + endTime + "' "
				+ "order by orderId desc";
		return getOrderFull(sql);
	}
	
	public static OrderInfo resultToOrderFull(ResultSet rs) {
		OrderInfo order = new OrderInfo();
		try {
			order.setOrderId(rs.getInt("orderId"));
			order.setOrderTitle(rs.getString("orderTitle"));
			order.setShopId(rs.getInt("shopId"));
			order.setShopUserName(rs.getString("shopUserName"));
			order.setShopName(rs.getString("shopName"));
			
			order.setReceiveAddress(rs.getString("receiveAddress"));
			order.setCustomerAddress(rs.getString("customerAddress"));
			order.setCustomerName(rs.getString("customerName"));
			order.setCustomerPhone(rs.getString("customerPhone"));
			
			order.setDeliveryType(rs.getInt("deliveryType"));
			order.setDeliveryPrice(rs.getLong("deliveryPrice"));
			order.setProductPrice(rs.getLong("productPrice"));
			order.setNoteTime(rs.getString("noteTime"));
			order.setNoteProduct(rs.getString("noteProduct"));
			
			order.setOrderStatus(rs.getInt("orderStatus"));
			
			order.setShipperId(rs.getInt("shipperId"));
			order.setShipperName(rs.getString("shipperName"));
			order.setShipperUserName(rs.getString("shipperUserName"));
			order.setStartTime(rs.getLong("startTime"));
			order.setFinishTime(rs.getLong("finishTime"));
			
			order.setStatusShipperConfirmed(rs.getInt("statusShopConfirmed"));
			order.setStatusShipperConfirmed(rs.getInt("statusShipperConfirmed"));
			
			order.setCreated(rs.getTimestamp("created").toString());
			order.setUpdated(rs.getTimestamp("updated").toString());
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		
		return order;
	}
	
	public static List<OrderInfo> getOrderFull(String sql) {
		List<OrderInfo> result = new ArrayList<OrderInfo>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	OrderInfo order = resultToOrderFull(rs);
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
	
	public static List<ShipperAggregate> getAggregate() {
		List<ShipperAggregate> result = new ArrayList<ShipperAggregate>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            String sql = "";
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	ShipperAggregate aggregate = null;
            	if(aggregate!=null)
            		result.add(aggregate);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
	}
	

}
