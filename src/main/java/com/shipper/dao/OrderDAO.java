package com.shipper.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shipper.logic.Constant;
import com.shipper.model.OrderAggregate;
import com.shipper.model.OrderInfo;
import com.shipper.model.ShipperAggregate;
import com.shipper.model.ShopAggregate;
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
			String noteTime, String noteProduct,
			int geoId, String city, String province) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();


			
			// create a sql date object so we can use it in our INSERT statement
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		    // the mysql insert statement
		    String query = "INSERT INTO "
						+ " shipOrder(orderId, orderTitle, shopId, shopUserName, shopName,"
						+ " receiveAddress, customerAddress, customerName, customerPhone,"
						+ " deliveryType, deliveryPrice, productPrice, noteTime, noteProduct,"
						+ " orderStatus, created, geoId, city, province" + ")"
						+ " VALUES ("+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "
						+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "+ " ?, "
						+ " ?, "+ " ?, ?, ?, ? "+ ");";
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
		    preparedStmt.setInt(17, geoId);
		    preparedStmt.setString (18, city);
		    preparedStmt.setString (19, province);

		    
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
						+ " where orderId = " + orderId + ";";
				stmt.executeUpdate(sql);
			} else if(role == User.role_shop) {
				String sql = "UPDATE "
						+ " shipOrder SET"
						+ " orderStatus = " + status + ", "
						+ " statusShopConfirmed = " + status + " "
						+ " where orderId = " + orderId + ";";
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
	
	public static boolean updateShopConfirm(int orderId, int shopConfirm) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			String sql;
			if(shopConfirm != 0) {
				sql = "UPDATE "
						+ " shipOrder SET"
						+ " shopConfirm = " + shopConfirm + ", "
						+ " productPrice = shipProductPrice "
						+ " where orderId = " + orderId + "";
			} else {
				sql = "UPDATE "
						+ " shipOrder SET"
						+ " shopConfirm = " + shopConfirm + ""
						+ " where orderId = " + orderId + "";
			}
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
	
	public static boolean updateShipProductPrice(int orderId, long productPrice) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			String sql = "UPDATE "
					+ " shipOrder SET"
					+ " shipProductPrice = " + productPrice + " "
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
	
	public static boolean updateProductPrice(int orderId, long productPrice) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			String sql = "UPDATE "
					+ " shipOrder SET"
					+ " productPrice = " + productPrice + " "
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
	
	public static boolean updateDeliveryPrice(int orderId, long deliverPrice) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			String sql = "UPDATE "
					+ " shipOrder SET"
					+ " deliverPrice = " + deliverPrice + " "
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
	
	public static boolean updateOrderPaid(int orderId, long orderPaid) {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
			stmt = conn.createStatement();
			String sql = "UPDATE "
					+ " shipOrder SET"
					+ " orderPaid = " + orderPaid + " "
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
	
	public static List<OrderInfo> getShopOrderFullByUpdatedtime(String shopUserName, int orderStatus, String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where shopUserName = '" + shopUserName + "' "
				+ " AND orderStatus = " + orderStatus
				+ " AND created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShopOrderFullByUpdatedtime(String shopUserName, String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where shopUserName = '" + shopUserName + "' "
				+ " AND created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getOrderFullByUpdatedtime(int orderStatus, String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where orderStatus = " + orderStatus + " "
				+ " AND created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getOrderFullByUpdatedtime(String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
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
			order.setOrderPaid(rs.getLong("orderPaid"));
			
			order.setNoteTime(rs.getString("noteTime"));
			order.setNoteProduct(rs.getString("noteProduct"));
			
			order.setOrderStatus(rs.getInt("orderStatus"));
			
			order.setShopConfirm(rs.getBoolean("shopConfirm"));
			order.setShipProductPrice(rs.getLong("shipProductPrice"));
			
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
	
	public static long getAggregate(String sql) {
		long result = 0;
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	result = rs.getLong(1);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
	}
	
	public static ShipperAggregate shipperAggregate(String shipperUserName) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where shipperUserName = '" + shipperUserName + "';"); 
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where shipperUserName = '" + shipperUserName + "';"); 
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where shipperUserName = '" + shipperUserName + "';"); 
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where shipperUserName = '" + shipperUserName + "';"); 
		
		ShipperAggregate r = new ShipperAggregate(shipperUserName, totalOrder, totalMoney, productMoney, paidMoney);
		return r;
	}
	
	
	public static ShopAggregate shopAggregate(String shopUserName) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where shopUserName = '" + shopUserName + "';"); 
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where shopUserName = '" + shopUserName + "';"); 
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where shopUserName = '" + shopUserName + "';"); 
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where shopUserName = '" + shopUserName + "';"); 
		
		long finishOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_finish + ";"); 
		long cancelOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_cancel + ";");
		
		ShopAggregate r = new ShopAggregate(shopUserName, totalOrder, totalMoney, productMoney, paidMoney, finishOrder, cancelOrder);
		return r;
	}
	
	
	public static ShipperAggregate shipperAggregate(String shipperUserName, int orderStatus) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus + ";"); 
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus + ";"); 
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus + ";"); 
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus + ";"); 
		
		ShipperAggregate r = new ShipperAggregate(shipperUserName, totalOrder, totalMoney, productMoney, paidMoney);
		return r;
	}
	
	
	public static ShopAggregate shopAggregate(String shopUserName, int orderStatus) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus + ";"); 
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus + ";"); 
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus + ";"); 
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus + ";"); 
		
		long finishOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_finish + ";"); 
		long cancelOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_cancel + ";"); 
		
		ShopAggregate r = new ShopAggregate(shopUserName, totalOrder, totalMoney, productMoney, paidMoney, finishOrder, cancelOrder);
		return r;
	}
	
	
	public static ShipperAggregate shipperAggregate(String shipperUserName, int orderStatus, String startTime, String endTime) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where "
				+ "shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where "
				+ " shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus + ""
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;"); 
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where "
				+ "shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where "
				+ "shipperUserName = '" + shipperUserName + "' and orderStatus = " + orderStatus 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;"); 
		
		ShipperAggregate r = new ShipperAggregate(shipperUserName, totalOrder, totalMoney, productMoney, paidMoney);
		return r;
	}
	
	
	public static ShopAggregate shopAggregate(String shopUserName, int orderStatus, String startTime, String endTime) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;"); 
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' and orderStatus = " + orderStatus 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		
		long finishOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_finish 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long cancelOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_cancel 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		
		ShopAggregate r = new ShopAggregate(shopUserName, totalOrder, totalMoney, productMoney, paidMoney, finishOrder, cancelOrder);
		return r;
	}
	
	public static ShipperAggregate shipperAggregate(String shipperUserName, String startTime, String endTime) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where "
				+ "shipperUserName = '" + shipperUserName + "' "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where "
				+ " shipperUserName = '" + shipperUserName + "' "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;"); 
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where "
				+ "shipperUserName = '" + shipperUserName + "' "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where "
				+ "shipperUserName = '" + shipperUserName + "' "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;"); 
		
		ShipperAggregate r = new ShipperAggregate(shipperUserName, totalOrder, totalMoney, productMoney, paidMoney);
		return r;
	}
	
	
	public static ShopAggregate shopAggregate(String shopUserName, String startTime, String endTime) {
		long totalOrder = getAggregate(
				"select count(*) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;"); 
		long totalMoney = getAggregate(
				"select  SUM(deliveryPrice) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' and "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long productMoney = getAggregate(
				"select  SUM(productPrice) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' and "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long paidMoney = getAggregate(
				"select  SUM(orderPaid) from shipOrder where "
				+ "shopUserName = '" + shopUserName + "' "
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		
		long finishOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_finish 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		long cancelOrder = getAggregate(
				"select  count(*) from shipOrder where shopUserName = '" + shopUserName + "' and orderStatus = " + OrderInfo.order_cancel 
				+ " and created >= '" + startTime + "' and created <= '" + endTime + "' ;");
		
		ShopAggregate r = new ShopAggregate(shopUserName, totalOrder, totalMoney, productMoney, paidMoney, finishOrder, cancelOrder);
		return r;
	}
	
	
	

	public static List<OrderInfo> getGeoOrder(int geoId, int status,
			String startTime, String endTime, int offset, int numb) {
		
		String sql = "SELECT * from shipOrder "
				+ " where geoId = " + geoId
				+ " AND orderStatus = " + status 
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		
//		String sql = "SELECT * from shipOrder inner join shop on shop.userName = shipOrder.shopUserName "
//				+ " where shop.cityGeoId = " + geoId
//				+ " AND orderStatus = " + status 
//				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getGeoOrder(int geoId, int offset, int numb) {
		String sql = "SELECT * from shipOrder "
				+ " where geoId = " + geoId
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";		
//		String sql = "SELECT * from shipOrder inner join shop on shop.userName = shipOrder.shopUserName "
//				+ " where shop.cityGeoId = " + geoId
//				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}

	
	public static List<OrderInfo> getGeoStatusOrder(int geoId, int orderStatus, int offset, int numb) {
		String sql = "SELECT * from shipOrder "
				+ " where geoId = " + geoId
				+ " AND orderStatus = " + orderStatus 
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
//		String sql = "SELECT * from shipOrder inner join shop on shop.userName = shipOrder.shopUserName "
//				+ " where shop.cityGeoId = " + geoId
//				+ " AND orderStatus = " + orderStatus 
//				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	
	
	
	
	
	
	
	
	
	public static List<OrderInfo> getShipOrderFull(int offset, int numb) {
		String sql = "SELECT * from shipOrder order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShipOrderFull(String shipperUserName, int offset, int numb) {
		
		String sql = "SELECT * from shipOrder where shipperUserName = '" + shipperUserName + "' "
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShipOrderFullByStatus(int orderStatus, int offset, int numb) {
		String sql = "SELECT * from shipOrder where orderStatus = " + orderStatus
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShipOrderFullByStatus(String shipperUserName, int orderStatus, int offset, int numb) {
		String sql = "SELECT * from shipOrder where shipperUserName = '" + shipperUserName + "' "
				+ " AND orderStatus = " + orderStatus 
				+ " order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShipOrderFullByUpdatedtime(String shipperUserName, int orderStatus, 
			String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where shipperUserName = '" + shipperUserName + "' "
				+ " AND orderStatus = " + orderStatus
				+ " AND created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShipOrderFullByUpdatedtime(String shipUserName, String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where shipperUserName = '" + shipUserName + "' "
				+ " AND created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShipOrderFullByUpdatedtime(int orderStatus, String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where orderStatus = " + orderStatus + " "
				+ " AND created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	public static List<OrderInfo> getShipOrderFullByUpdatedtime(String startTime, String endTime, int offset, int numb) {
		String sql = "SELECT * from shipOrder where created >= '" + startTime + "' AND created <=  '" + endTime + "' "
				+ "order by orderId desc limit " + offset  + ", " + numb + ";";
		return getOrderFull(sql);
	}
	
	
	
	
	
	
	
	
	
	public static List<OrderAggregate> getDateAggregate(String sql) {
		List<OrderAggregate> result = new ArrayList<OrderAggregate>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	OrderAggregate o = new OrderAggregate(
            			rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4), rs.getString(5));
            	result.add(o);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
	}
	
	
	public static List<OrderAggregate> orderShopDateAggregate(String shopUserName, String startTime, String endTime) {
		return getDateAggregate("select count(*), sum(deliveryPrice), sum(productPrice), sum(orderPaid), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and shopUserName = '" + shopUserName + "' "
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	public static List<OrderAggregate> orderShipperDateAggregate(String shipperUserName, String startTime, String endTime) {
		return getDateAggregate("select count(*), sum(deliveryPrice), sum(productPrice), sum(orderPaid), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and shipperUserName = '" + shipperUserName + "' "
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}

	public static List<OrderAggregate> orderShopDateAggregate(String shopUserName, int orderStatus, String startTime, String endTime) {
		return getDateAggregate("select count(*), sum(deliveryPrice), sum(productPrice), sum(orderPaid), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and orderStatus = " + orderStatus
				+ " and shopUserName = '" + shopUserName + "' "
				+ " order by created desc"
				+ ";");
	}
	
	public static List<OrderAggregate> orderShipperDateAggregate(String shipperUserName, int orderStatus, String startTime, String endTime) {
		return getDateAggregate("select count(*), sum(deliveryPrice), sum(productPrice), sum(orderPaid), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and orderStatus = " + orderStatus
				+ " and shipperUserName = '" + shipperUserName + "' "
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	public static List<OrderAggregate> orderDateAggregate(int orderStatus, String startTime, String endTime) {
		return getDateAggregate("select count(*), sum(deliveryPrice), sum(productPrice), sum(orderPaid), Date(created) from shipOrder "
				
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and orderStatus = " + orderStatus
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	public static List<OrderAggregate> orderDateAggregate(String startTime, String endTime) {
		return getDateAggregate("select count(*), sum(deliveryPrice), sum(productPrice), sum(orderPaid), Date(created) from shipOrder "
				
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	
	
	
	
	
	public static Map<String, Long> getMapDateAggregate(String sql) {
		Map<String, Long> result = new HashMap<String, Long>();
		Connection conn = null;
		Statement stmt = null;
        try {
        	Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Constant.DB_URL, Constant.USER, Constant.PASS);
            stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
            	result.put(rs.getString(2),rs.getLong(1));
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        
        return result;
	}
	
	
	
	public static Map<String, Long> orderShopMapDateAggregate(String shopUserName, String startTime, String endTime) {
		return getMapDateAggregate("select count(*), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and shopUserName = '" + shopUserName + "' "
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	public static Map<String, Long> orderShipperMapDateAggregate(String shipperUserName, String startTime, String endTime) {
		return getMapDateAggregate("select count(*), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and shipperUserName = '" + shipperUserName + "' "
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}

	public static Map<String, Long> orderShopMapDateAggregate(String shopUserName, int orderStatus, String startTime, String endTime) {
		return getMapDateAggregate("select count(*), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and orderStatus = " + orderStatus
				+ " and shopUserName = '" + shopUserName + "' "
				+ " order by created desc"
				+ ";");
	}
	
	public static Map<String, Long> orderShipperMapDateAggregate(String shipperUserName, int orderStatus, String startTime, String endTime) {
		return getMapDateAggregate("select count(*), Date(created) from shipOrder"
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and orderStatus = " + orderStatus
				+ " and shipperUserName = '" + shipperUserName + "' "
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	public static Map<String, Long> orderMapDateAggregate(int orderStatus, String startTime, String endTime) {
		return getMapDateAggregate("select count(*), Date(created) from shipOrder "
				
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " and orderStatus = " + orderStatus
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	public static Map<String, Long> orderMapDateAggregate(String startTime, String endTime) {
		return getMapDateAggregate("select count(*), Date(created) from shipOrder "
				
				+ " where created >= '" + startTime + "' and created <= '" + endTime + "'"
				+ " group by DATE(created)"
				+ " order by created desc"
				+ ";");
	}
	
	
	
	
	
	
	
	
	
	

}
