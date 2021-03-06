package com.shipper.logic.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.shipper.dao.BillDAO;
import com.shipper.dao.ConfigDAO;
import com.shipper.dao.OrderDAO;
import com.shipper.dao.OrderLogDAO;
import com.shipper.dao.ShipperDAO;
import com.shipper.dao.ShipperProvinceDAO;
import com.shipper.dao.ShopDAO;
import com.shipper.logic.Constant;
import com.shipper.logic.account.AccountLogic;
import com.shipper.model.CityGeo;
import com.shipper.model.OrderAggregate;
import com.shipper.model.OrderInfo;
import com.shipper.model.ShipBill;
import com.shipper.model.Shipper;
import com.shipper.model.ShipperAggregate;
import com.shipper.model.Shop;
import com.shipper.model.ShopAggregate;
import com.shipper.model.User;

public class OrderLogic {
	
	private static Map<Integer, Object> orderLock;
	private static boolean isInitOrderLock = false;
	
	public static synchronized void initLock() {
		if(!isInitOrderLock) {
			orderLock = new HashMap<Integer, Object>();
			isInitOrderLock = true;
		}
	}
	
	public static synchronized Object getOrderLock(int orderId) {
		initLock();
		if(orderLock.containsKey(orderId)) {
			return orderLock.get(orderId);
		} else {
			createOrderLock(orderId);
			return orderLock.get(orderId);
		}
	}
	
	public static void createOrderLock(int orderId) {
		initLock();
		orderLock.put(orderId, new Object());
	}
	
	public static synchronized void removeOrderLock(int orderId) {
		initLock();
		orderLock.remove(orderId);
	}
	
	public static JSONObject synBidOrder(int orderId, String shipperUserName) {
		JSONObject result;
		Object lock = getOrderLock(orderId);
		
		if(lock != null) {
			synchronized(lock) {
				result = bidOrder(orderId, shipperUserName);
			}
		} else {
			result = new JSONObject();
			JSONObject data = new JSONObject();
			JSONObject error = new JSONObject();
			
			result.put("status", Constant.status_ok);
			
			data.put("bidded", false);
			data.put("message", "fail to bid order");
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}
		
		return result;
	}
	
	public static JSONObject synCancelOrder(int orderId, String shopUserName) {
		JSONObject result;
		Object lock = getOrderLock(orderId);
		
		if(lock != null) {
			synchronized(lock) {
				result = cancelOrder(orderId, shopUserName);
			}
		} else {
			result = new JSONObject();
			JSONObject data = new JSONObject();
			JSONObject error = new JSONObject();
			
			result.put("status", Constant.status_ok);
			
			data.put("bidded", false);
			data.put("message", "fail to bid order");
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}
		
		return result;
	}
	
	
	

	public static synchronized JSONObject updateOrderStatus(int orderId) {
		JSONObject o = new JSONObject();

		return o;
	}

	public static JSONObject bidOrder(int orderId, String shipperUserName) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<Shipper> shipperList = ShipperDAO.getShipperByUserName(shipperUserName);
		boolean checkShipperNull = (shipperList.size() == 0);
		if (checkShipperNull || shipperUserName == null
				|| shipperUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "shipper not existed | shipper is null");

			result.put("error", error);

			return result;
		} else {
			List<OrderInfo> orderList = OrderDAO.getOrderInfoById(orderId);
			
			if(orderList.size() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_order_null);
				error.put("message", "order not existed");

				result.put("error", error);

				return result;
			}
			Shipper shipper = shipperList.get(0);
			OrderInfo order = orderList.get(0);
			int status = order.getOrderStatus();
			
			if (status == OrderInfo.order_wait) {
				// Update status
				boolean r = OrderDAO.bidOrder(orderId, shipper.getShipperId(), shipper.getUserName(), shipper.getShipperName());

				if (r) {
					
					OrderPush.pushOrder(order.getShopUserName(), order.getShipperUserName(), orderId, OrderInfo.order_bidded);
					
					result.put("status", Constant.status_ok);
					
					data.put("bidded", r);
					data.put("message", "success bid order");
					result.put("data", data);

					error.put("code", Constant.error_non);
					error.put("message", "no error");

					result.put("error", error);

					return result;
				} else {
					result.put("status", Constant.status_error);
					
					
					result.put("data", data);

					error.put("code", Constant.error_db);
					error.put("message", "db error");
					result.put("error", error);

					return result;
				}
			} else {
				result.put("status", Constant.status_ok);
				
				data.put("bidded", false);
				data.put("message", "fail to bid order");
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);

				return result;
			}
		}

	}
	
	
	public static void updateDeliveryPrice(int orderId, int orderStatus, long deliveryPrice) {
		if(orderStatus == OrderInfo.order_wait 
				|| orderStatus == OrderInfo.order_bidded) {
			OrderDAO.updateDeliveryPrice(orderId, 0);
		}
		if(orderStatus == OrderInfo.order_shipped
				|| orderStatus == OrderInfo.order_received
				) {
			OrderDAO.updateDeliveryPrice(orderId, deliveryPrice / 2);
		}
	}

	public static JSONObject cancelOrder(int orderId, String shopUserName) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<Shop> shopList = ShopDAO.getShopByUserName(shopUserName);
		boolean checkShopNull = (shopList.size() == 0);
		if (checkShopNull || shopUserName == null
				|| shopUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "shop not existed | shop is null");

			result.put("error", error);

			return result;
		} else {
			List<OrderInfo> orderList = OrderDAO.getOrderFullById(orderId);
			
			if(orderList.size() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_order_null);
				error.put("message", "order not existed");

				result.put("error", error);

				return result;
			}
			Shop shop = shopList.get(0);
			OrderInfo order = orderList.get(0);
			int status = order.getOrderStatus();
			long deliveryPrice = order.getDeliveryPrice();
			
			if ((status == OrderInfo.order_wait 
					|| status == OrderInfo.order_bidded 
					|| status == OrderInfo.order_shipped) 
					&& order.getShopUserName().equals(shop.getUserName())) {
				
				boolean r = OrderDAO.cancelOrder(orderId);

				if (r) {
					
					OrderPush.pushOrder(order.getShopUserName(), order.getShipperUserName(), orderId, OrderInfo.order_cancel);
					// Update orderPrice
					updateDeliveryPrice(orderId, status, deliveryPrice);
					
					result.put("status", Constant.status_ok);
					
					data.put("cancel", r);
					data.put("message", "success cancel order");
					result.put("data", data);

					error.put("code", Constant.error_non);
					error.put("message", "no error");

					result.put("error", error);

					return result;
				} else {
					result.put("status", Constant.status_error);
					
					
					result.put("data", data);

					error.put("code", Constant.error_db);
					error.put("message", "db error");
					result.put("error", error);

					return result;
				}
			} else {
				result.put("status", Constant.status_ok);
				
				data.put("cancel", false);
				data.put("message", "fail to cancel order status: " + status + " -- userName: " + shop.getUserName());
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);

				return result;
			}
		}
	}

	private static int id_counter = 0;
	private static boolean isInitCounter = false;

	public static synchronized int genOrderId() {
		if (!isInitCounter) {
			id_counter = OrderDAO.getCurrentId();
			isInitCounter = true;
		}
		id_counter++;
		return id_counter;
	}

	public static JSONObject createOrder(String orderTitle, String userName,
			String receiveAddress, String customerAddress, String customerName,
			String customerPhone, int deliveryType, long deliveryPrice,
			long productPrice, String noteTime, String noteProduct,
			int geoId, String city, String province) {

		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<Shop> shops = ShopDAO.getShopByUserName(userName);
		boolean checkShopNull = (shops.size() == 0);
		if (checkShopNull || userName == null || userName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message",
					"shop not existed | userName is null | password is null");

			result.put("error", error);

			return result;
		}
		int order_id = genOrderId();
		int shopId = shops.get(0).getShopId();
		String shopName = shops.get(0).getShopName();
		boolean r = OrderDAO.createOrder(order_id, orderTitle, shopId, userName, shopName,
				receiveAddress, customerAddress, customerName, customerPhone,
				deliveryType, deliveryPrice, productPrice, noteTime,
				noteProduct,
				geoId, city, province);

		if (r) {
			createOrderLock(order_id);
			OrderPush.pushShipNewOrder(order_id);
			
			
			result.put("status", Constant.status_ok);

			data.put("orderId", order_id);
			data.put("result", "success");
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "database error");

			result.put("error", error);
		}

		return result;
	}

	public static JSONObject getOrder(int orderId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders = OrderDAO.getOrderInfoById(orderId);
		boolean orderNull = orders.size() == 0;
		if (orderNull) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		} else {
			OrderInfo order = orders.get(0);
			result.put("status", Constant.status_ok);

			data.put("order", order.infoToJSON());
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}

		return result;
	}
	
	public static JSONObject confirmOrderStatus(int orderId, int orderStatus, int role) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
		//boolean r1 = OrderDAO.updateOrderStatus(orderId, orderStatus, role);
		boolean r1 = OrderDAO.confirmOrderStatus(orderId, orderStatus, role);

		if(r1) {
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	public static boolean checkUpdateStatus(int currentStatus, int updateStatus) {
		if(updateStatus == OrderInfo.order_wait 
				|| updateStatus == OrderInfo.order_bidded 
				|| updateStatus == OrderInfo.order_cancel
				|| currentStatus == OrderInfo.order_wait
				|| currentStatus == OrderInfo.order_finish
				|| currentStatus == OrderInfo.order_cancel)
			return false;
		
		return true;
	}
	
	public static JSONObject updateOrderStatus(int orderId, int orderStatus, int role) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		OrderInfo order = orders.get(0);
		int currentStatus = order.getOrderStatus();
		
		boolean r0 = checkUpdateStatus(currentStatus, orderStatus);
		if(!r0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_update_status);
			error.put("message", "can not update from current status");

			result.put("error", error);

			return result;
		}
		
		boolean r1 = OrderDAO.updateOrderStatus(orderId, orderStatus, role);
		//boolean r2 = OrderDAO.confirmOrderStatus(orderId, orderStatus, role);

		if(r1) {
			OrderLogDAO.logOrderStatus(orderId, orderStatus, role);
			OrderPush.pushOrder(order.getShopUserName(), order.getShipperUserName(), orderId, orderStatus);
			
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	
	public static JSONObject updateOrderPaid(int orderId, long orderPaid, int role) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		boolean r1 = OrderDAO.updateOrderPaid(orderId, orderPaid);

		if(r1) {
			OrderLogDAO.logOrderStatus(orderId, OrderInfo.order_paid, role);
			
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	public static JSONObject updateShopConfirm(int orderId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		boolean r1 =OrderDAO.updateShopConfirm(orderId, 1);
		
		if(r1) {
			OrderInfo o = orders.get(0);
			OrderLogDAO.logOrderUpdate(orderId, "shopConfirm", 1 + "", User.role_shop);
			OrderPush.pushShipUpdate(o.getShipperUserName(), orderId);
			
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	public static JSONObject updateShipProductPrice(int orderId, long productPrice) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		boolean r1 = OrderDAO.updateShipProductPrice(orderId, productPrice);
		OrderDAO.updateShopConfirm(orderId, 0);

		if(r1) {
			OrderInfo o = orders.get(0);
			OrderLogDAO.logOrderUpdate(orderId, "productPrice", productPrice + "", User.role_shipper);
			OrderPush.pushShopUpdate(o.getShopUserName(), orderId);
			
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	public static JSONObject updateProductPrice(int orderId, long productPrice, int role) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		
		if(orders.size() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		boolean r1 = OrderDAO.updateProductPrice(orderId, productPrice);
		OrderDAO.updateShopConfirm(orderId, 0);

		if(r1) {
			OrderInfo o = orders.get(0);
			OrderLogDAO.logOrderUpdate(orderId, "productPrice", productPrice + "", role);
			OrderPush.pushShipUpdate(o.getShipperUserName(), orderId);
			
			result.put("status", Constant.status_ok);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			return result;
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		}
		
	}
	
	
	
	
	public static JSONObject getOrderFull(int orderId) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders = OrderDAO.getOrderFullById(orderId);
		boolean orderNull = orders.size() == 0;
		if (orderNull) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "order not existed");

			result.put("error", error);

			return result;
		} else {
			OrderInfo order = orders.get(0);
			
			
			
			result.put("status", Constant.status_ok);
			
			JSONObject d = orderShipShop(order);
			data.put("order", d);
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray orderListToJSON(List<OrderInfo> orders) {
		JSONArray o = new JSONArray();
		
		for(OrderInfo order : orders) {
			o.add(orderShipShop(order));
		}
		
		return o;
	}
	
	public static JSONObject shopShort(String userName) {
		JSONObject s = new JSONObject();
		
		s.put("id", "");
		s.put("userName", "");
		s.put("phone", "");
		s.put("address", "");
		
		List<Shop> shops = ShopDAO.getShopByUserName(userName);
		if(shops.size() > 0) {
			Shop shop = shops.get(0);
			s.put("id", shop.getShopId());
			s.put("userName", shop.getShopName());
			s.put("phone", shop.getPhoneNumber());
			s.put("address", shop.getAddress());
		}
		
		return s;
	}
	
	public static JSONObject shipperShort(String userName) {
		JSONObject s = new JSONObject();
		
		s.put("id", "");
		s.put("userName", "");
		s.put("phone", "");
		s.put("address", "");
		
		List<Shipper> shippers = ShipperDAO.getShipperByUserName(userName);
		if(shippers.size() > 0) {
			Shipper ship = shippers.get(0);
			s.put("id", ship.getShipperId());
			s.put("userName", ship.getShipperName());
			s.put("phone", ship.getPhoneNumber());
			s.put("address", ship.getAddress());
		}
		
		return s;
	}
	
	public static JSONObject orderShipShop(OrderInfo order) {
		JSONObject o = order.fullToJSON();
		
		o.put("shopInfo", shopShort(order.getShopUserName()));
		o.put("shipperInfo", shipperShort(order.getShipperUserName()));
		
		return o;
	}
	
	
	public static JSONObject getOrderFullByTime(String shopUserName, String startTime, String endTime, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFullByUpdatedtime(startTime, endTime, offset, numb);
		} else {
			orders = OrderDAO.getShopOrderFullByUpdatedtime(shopUserName, startTime, endTime, offset, numb);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getOrderFullByTime(String shopUserName, int orderStatus, String startTime, String endTime, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFullByUpdatedtime(orderStatus, startTime, endTime, offset, numb);
		} else {
			orders = OrderDAO.getShopOrderFullByUpdatedtime(shopUserName, orderStatus, startTime, endTime, offset, numb);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}

	
	public static JSONObject getOrderFullList(String shopUserName, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFull(offset, numb);
		} else {
			orders = OrderDAO.getShopOrderFull(shopUserName, offset, numb);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getOrderFullList(String shopUserName, int orderStatus, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		List<OrderInfo> orders;
		
		if(shopUserName.length() == 0) {
			orders = OrderDAO.getOrderFullByStatus(orderStatus, offset, numb);
		} else {
			orders = OrderDAO.getOrderFullByStatus(shopUserName, orderStatus, offset, numb);
		}
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	
	
	public static JSONObject getShipperAggregate(String shipperUserName, int orderStatus) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShipperNull(shipperUserName);
		if(checkNull || shipperUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName null");

			result.put("error", error);

			return result;
		} 
		ShipperAggregate a;
		if(orderStatus == -1) {
			a = OrderDAO.shipperAggregate(shipperUserName);
		} else {
			a = OrderDAO.shipperAggregate(shipperUserName, orderStatus);
		}
		
		result.put("status", Constant.status_ok);

		data.put("count", a.toJSON());
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	public static JSONObject getShopAggregate(String shopUserName, int orderStatus) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShopNull(shopUserName);
		if(checkNull || shopUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName null");

			result.put("error", error);

			return result;
		} 
		
		ShopAggregate a; 
		if(orderStatus == -1) {
			a = OrderDAO.shopAggregate(shopUserName);
		} else {
			a = OrderDAO.shopAggregate(shopUserName, orderStatus);
		}
		result.put("status", Constant.status_ok);

		data.put("count", a.toJSON());
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getShipperAggregateByTime(String shipperUserName, int orderStatus, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShipperNull(shipperUserName);
		if(checkNull || shipperUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName null");

			result.put("error", error);

			return result;
		} 
		ShipperAggregate a;
		if(orderStatus == -1) {
			a = OrderDAO.shipperAggregate(shipperUserName, startTime, endTime);
		} else {
			a = OrderDAO.shipperAggregate(shipperUserName, orderStatus, startTime, endTime);
		}
		
		result.put("status", Constant.status_ok);

		data.put("count", a.toJSON());
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	public static JSONObject getShopAggregateByTime(String shopUserName, int orderStatus, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShopNull(shopUserName);
		if(checkNull || shopUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName null");

			result.put("error", error);

			return result;
		} 
		
		ShopAggregate a; 
		if(orderStatus == -1) {
			a = OrderDAO.shopAggregate(shopUserName, startTime, endTime);
		} else {
			a = OrderDAO.shopAggregate(shopUserName, orderStatus, startTime, endTime);
		}
		result.put("status", Constant.status_ok);

		data.put("count", a.toJSON());
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	
	
	
	


	
	
	public static JSONObject getDateAggregate(int orderStatus, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		List<OrderAggregate> a; 
		if(orderStatus == -1) {
			a = OrderDAO.orderDateAggregate(startTime, endTime);
		} else {
			a = OrderDAO.orderDateAggregate(orderStatus, startTime, endTime);
		}
		result.put("status", Constant.status_ok);
		data.put("count", orderAggregateToJSON(a));
		result.put("data", data);
		error.put("code", Constant.error_non);
		error.put("message", "no error");
		result.put("error", error);
		
		return result;
	}
	
	
	
	public static JSONObject getShipperDateAggregate(String shipperUserName, int orderStatus, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShipperNull(shipperUserName);
		if(checkNull || shipperUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);
			error.put("code", Constant.error_db);
			error.put("message", "userName null");
			result.put("error", error);

			return result;
		} 
		List<OrderAggregate> a; 
		if(orderStatus == -1) {
			a = OrderDAO.orderShipperDateAggregate(shipperUserName, startTime, endTime);
		} else {
			a = OrderDAO.orderShipperDateAggregate(shipperUserName, orderStatus, startTime, endTime);
		}
		
		result.put("status", Constant.status_ok);
		data.put("count", orderAggregateToJSON(a));
		result.put("data", data);
		error.put("code", Constant.error_non);
		error.put("message", "no error");
		result.put("error", error);	
		
		return result;
	}
	
	
	public static JSONObject getShopDateAggregate(String shopUserName, int orderStatus, String startTime, String endTime) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShopNull(shopUserName);
		if(checkNull || shopUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);
			error.put("code", Constant.error_db);
			error.put("message", "userName null");
			result.put("error", error);

			return result;
		} 
		
		List<OrderAggregate> a; 
		if(orderStatus == -1) {
			a = OrderDAO.orderShopDateAggregate(shopUserName, startTime, endTime);
		} else {
			a = OrderDAO.orderShopDateAggregate(shopUserName, orderStatus, startTime, endTime);
		}
		result.put("status", Constant.status_ok);
		data.put("count", orderAggregateToJSON(a));
		result.put("data", data);
		error.put("code", Constant.error_non);
		error.put("message", "no error");
		result.put("error", error);
		
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public static JSONArray mapToJSON(Map<String, Long> map) {
		JSONArray o = new JSONArray();
		
		for(String key : map.keySet()) {
			JSONObject a = new JSONObject();
			a.put("date", key);
			a.put("count", map.get(key));
			
			o.add(a);
		}
		
		return o;
	}
	
	
	@SuppressWarnings("unchecked")
	public static JSONArray orderAggregateToJSON(List<OrderAggregate> as) {
		JSONArray o = new JSONArray();
		
		for(OrderAggregate order : as) {
			o.add(order.toJSON());
		}
		
		return o;
	}
	
	
	
	
	
	
	
	
	
	public static JSONObject getShipperOrderFullByTime(String shipUserName, String startTime, String endTime, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shipUserName.length() == 0) {
			orders = OrderDAO.getShipOrderFullByUpdatedtime(startTime, endTime, offset, numb);
		} else {
			orders = OrderDAO.getShipOrderFullByUpdatedtime(shipUserName, startTime, endTime, offset, numb);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getShipperOrderFullByTime(String shipUserName, int orderStatus, String startTime, String endTime,
			int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shipUserName.length() == 0) {
			orders = OrderDAO.getShipOrderFullByUpdatedtime(orderStatus, startTime, endTime, offset, numb);
		} else {
			orders = OrderDAO.getShipOrderFullByUpdatedtime(shipUserName, orderStatus, startTime, endTime, offset, numb);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	
	
	public static JSONObject getShipperProvinceOrderFullList(String shipUserName, int status,
			String startTime, String endTime, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders = new ArrayList<OrderInfo>();
		if(shipUserName.length() == 0) {
			result.put("status", Constant.status_ok);

			data.put("orders", orderListToJSON(orders));
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			
			
			return result;
		} 
		
		List<CityGeo> geoList = ShipperProvinceDAO.getCityGeoByUser(shipUserName);
		
		for(CityGeo geo : geoList) {
			List<OrderInfo> os = OrderDAO.getGeoOrder(geo.getId(), status, startTime, endTime, offset, numb);
			orders.addAll(os);
		}
		
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getShipperProvinceOrderFullList(String shipUserName, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders = new ArrayList<OrderInfo>();
		if(shipUserName.length() == 0) {
			result.put("status", Constant.status_ok);

			data.put("orders", orderListToJSON(orders));
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			
			
			return result;
		} 
		
		List<CityGeo> geoList = ShipperProvinceDAO.getCityGeoByUser(shipUserName);
		
		for(CityGeo geo : geoList) {
			List<OrderInfo> os = OrderDAO.getGeoOrder(geo.getId(), offset, numb);
			orders.addAll(os);
		}
		
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getShipperProvinceOrderFullList(String shipUserName, int orderStatus, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		List<OrderInfo> orders = new ArrayList<OrderInfo>();
		
		if(shipUserName.length() == 0) {
			result.put("status", Constant.status_ok);

			data.put("orders", orderListToJSON(orders));
			result.put("data", data);

			error.put("code", Constant.error_non);
			error.put("message", "no error");

			result.put("error", error);
			
			
			return result;
		}
		
		List<CityGeo> geoList = ShipperProvinceDAO.getCityGeoByUser(shipUserName);
		
		for(CityGeo geo : geoList) {
			List<OrderInfo> os = OrderDAO.getGeoStatusOrder(geo.getId(), orderStatus, offset, numb);
			orders.addAll(os);
		}
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	
	
	
	
	public static JSONObject getGeoOrderFullList(int geoId, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		
		orders = OrderDAO.getGeoOrder(geoId, offset, numb);
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getGeoOrderFullList(int geoId, int orderStatus, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		List<OrderInfo> orders;
		
		orders = OrderDAO.getGeoStatusOrder(geoId, orderStatus, offset, numb);
		
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	
	

	
	public static JSONObject getShipperOrderFullList(String shipUserName, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<OrderInfo> orders;
		if(shipUserName.length() == 0) {
			orders = OrderDAO.getShipOrderFull(offset, numb);
		} else {
			orders = OrderDAO.getShipOrderFull(shipUserName, offset, numb);
		}
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	public static JSONObject getShipperOrderFullList(String shipUserName, int orderStatus, int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		List<OrderInfo> orders;
		
		if(shipUserName.length() == 0) {
			orders = OrderDAO.getShipOrderFullByStatus(orderStatus, offset, numb);
		} else {
			orders = OrderDAO.getShipOrderFullByStatus(shipUserName, orderStatus, offset, numb);
		}
		
		result.put("status", Constant.status_ok);

		data.put("orders", orderListToJSON(orders));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	public static boolean updateConfig(String key, String value) {
		return ConfigDAO.createUpdateConfig(key, value);
	}
	
	public static JSONObject getConfig() {
		JSONObject r = new JSONObject();
	
		Map<String, String> data = ConfigDAO.getConfig();
		for(String key: data.keySet()) {
			r.put(key, data.get(key));
		}
		
		return r;
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public static JSONArray billToJSON(List<ShipBill> as) {
		JSONArray o = new JSONArray();
		
		for(ShipBill bill : as) {
			o.add(bill.toJSON());
		}
		
		return o;
	}
	public static JSONObject getShopBill(String shopUserName, String startTime, String endTime, 
			int offset, int numb) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();
		
		boolean checkNull = AccountLogic.checkShopNull(shopUserName);
		if(checkNull || shopUserName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName null");

			result.put("error", error);

			return result;
		} 
		
		List<ShipBill> bills = BillDAO.getShipBillById(shopUserName, startTime, endTime, offset, numb);
		result.put("status", Constant.status_ok);

		data.put("count", billToJSON(bills));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);
		
		
		return result;
	}
	
	
	
}
