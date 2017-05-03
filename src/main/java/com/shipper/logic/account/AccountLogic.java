package com.shipper.logic.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.mindrot.BCrypt;

import com.shipper.dao.SessionDAO;
import com.shipper.dao.ShipperDAO;
import com.shipper.dao.ShopDAO;
import com.shipper.logic.Constant;
import com.shipper.model.SessionInfo;
import com.shipper.model.Shipper;
import com.shipper.model.Shop;
import com.shipper.model.User;

public class AccountLogic {

	public static String updateUserSession(String userName, int role) {
		long time = System.currentTimeMillis();
		String sessionKey = hashed(userName + "" + role + "" + time) + time;

		if (checkSessionUser(userName, role))
			SessionDAO.updateUserSession(userName, role, sessionKey);
		else
			SessionDAO.createUserSession(sessionKey, userName, role);

		return sessionKey;
	}

	public static boolean checkSession(String sessionKey) {
		boolean result = true;

		List<SessionInfo> session = SessionDAO.getSessionByKey(sessionKey);
		if (session.size() == 0)
			result = false;

		return result;
	}

	public static boolean checkSessionUser(String userName, int role) {

		List<SessionInfo> session = SessionDAO.getSessionByUser(userName, role);
		if (session.size() == 0)
			return false;

		return true;
	}

	public static boolean checkUserSession(String userName, int role,
			String sessionKey) {
		List<SessionInfo> session = SessionDAO.getSessionByKey(sessionKey);
		if (session.size() == 0)
			return false;

		SessionInfo s = session.get(0);
		if (s.getRole() == role && s.getUserName().equals(userName)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static JSONArray sessToJSON(List<SessionInfo> sess) {
		JSONArray o = new JSONArray();

		for (SessionInfo order : sess) {
			o.add(order.toJSON());
		}

		return o;
	}

	public static JSONObject getSession(String userName, int role) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<SessionInfo> sess = SessionDAO.getSessionByUser(userName, role);

		result.put("status", Constant.status_ok);

		data.put("session", sessToJSON(sess));
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);

		return result;
	}

	public static JSONObject genErrorSession() {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		result.put("status", Constant.status_error);
		result.put("data", data);

		error.put("code", Constant.error_authen);
		error.put("message", "sessionKey error");

		result.put("error", error);

		return result;
	}

	public static final int code_length = 4;

	private static boolean isInit = false;
	private static Map<String, String> userShopCode;
	private static Map<String, String> userShipCode;

	public static synchronized void init() {
		if (!isInit) {
			userShopCode = new HashMap<String, String>();
			userShipCode = new HashMap<String, String>();
			isInit = true;
		}
	}

	public static String genDigit() {
		Random random = new Random();
		int d = random.nextInt(10);
		return String.valueOf(d);
	}

	public static String genCode() {
		String result = "";

		for (int i = 0; i < code_length; i++) {
			result += genDigit();
		}

		return result;

	}

	public static String genShipCode(String userName) {

		if (!isInit) {
			init();
		}

		String code = genCode();
		userShipCode.put(userName, code);

		return code;
	}

	public static boolean checkShipCode(String userName, String code) {
		boolean result = false;

		if (isInit) {
			if (userShipCode.containsKey(userName)) {
				String v = userShipCode.get(userName);
				if (v.equals(code)) {

					result = true;
				}

				userShipCode.remove(userName);
			}
		}

		return result;
	}

	public static String genShopCode(String userName) {

		if (!isInit) {
			init();
		}

		String code = genCode();
		userShopCode.put(userName, code);

		return code;
	}

	public static boolean checkShopCode(String userName, String code) {
		boolean result = false;

		if (isInit) {
			if (userShopCode.containsKey(userName)) {
				String v = userShopCode.get(userName);
				if (v.equals(code)) {

					result = true;
				}

				userShopCode.remove(userName);
			}
		}

		return result;
	}

	/*
	 * status: { ok / error / ...} data: {...} error: { code: 401, message: ....
	 * }
	 */

	public static boolean checkShopNull(String userName) {
		List<Shop> res = ShopDAO.getShopByUserName(userName);
		if (res.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkShipperNull(String userName) {
		List<Shipper> res = ShipperDAO.getShipperByUserName(userName);
		if (res.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String hashed(String input) {
		return BCrypt.hashpw(input, BCrypt.gensalt());
	}

	public static String hashPassword(String password) {
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return hashed;
	}

	public static boolean checkPassword(String password, String hashed) {
		return (BCrypt.checkpw(password, hashed));
	}

	public static JSONObject register(String userName, String password,
			int userType) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		if (userType == User.role_shop) {
			boolean checkShop = checkShopNull(userName);
			if (!checkShop || userName == null || userName.length() == 0
					|| password == null || password.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message",
						"userName existed | userName is null | password is null");

				result.put("error", error);

				return result;
			}

			Shop shop = new Shop(userName, hashPassword(password));
			boolean r = ShopDAO.createShopAccount(shop);
			if (r) {
				String session = updateUserSession(userName, User.role_shop);
				result.put("status", Constant.status_ok);

				data.put("result", "success");
				data.put("sessionKey", session);
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
		} else if (userType == User.role_shipper) {
			boolean checkShip = checkShipperNull(userName);
			if (!checkShip || userName == null || userName.length() == 0
					|| password == null || password.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message",
						"userName existed | userName is null | password is null");

				result.put("error", error);

				return result;
			}

			Shipper shipper = new Shipper(userName, hashPassword(password));
			boolean r = ShipperDAO.createShipperAccount(shipper);
			if (r) {
				String session = updateUserSession(userName, User.role_shipper);
				result.put("status", Constant.status_ok);

				data.put("result", "success");
				data.put("sessionKey", session);
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
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_request);
			error.put("message", "wrong role");

			result.put("error", error);

		}

		return result;
	}

	public static JSONObject login(String userName, String passWord,
			int userType) {

		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		if (userType == User.role_shop) {
			List<Shop> r = ShopDAO.getShopByUserName(userName);
			if (r.size() == 0 || userName == null || userName.length() == 0
					|| passWord == null || passWord.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "username not existed");

				result.put("error", error);

				return result;
			}

			Shop shop = r.get(0);
			boolean res = passWord.equals(shop.getPassword());
			if (res == false)
				res = checkPassword(passWord, shop.getPassword());

			if (res) {
				String session = updateUserSession(userName, User.role_shop);

				result.put("status", Constant.status_ok);

				data.put("result", "success");
				data.put("sessionKey", session);
				data.put("userStatus", shop.getStatus());
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
			} else {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "wrong password");

				result.put("error", error);
			}
		} else if (userType == User.role_shipper) {
			List<Shipper> r = ShipperDAO.getShipperByUserName(userName);
			if (r.size() == 0 || userName == null || userName.length() == 0
					|| passWord == null || passWord.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "username not existed");

				result.put("error", error);

				return result;
			}

			Shipper shipper = r.get(0);
			boolean res = passWord.equals(shipper.getPassword());
			if (res == false)
				res = checkPassword(passWord, shipper.getPassword());
			if (res) {
				String session = updateUserSession(userName, User.role_shipper);
				result.put("status", Constant.status_ok);

				data.put("result", "success");
				data.put("sessionKey", session);
				data.put("userStatus", shipper.getStatus());
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
			} else {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "wrong password");

				result.put("error", error);
			}
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_request);
			error.put("message", "wrong role");

			result.put("error", error);

		}

		return result;

	}

	public static JSONObject getProfile(String userName, int userType) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		if (userType == User.role_shop) {
			boolean checkShop = checkShopNull(userName);
			if (checkShop || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "userName nnt existed | userName is null");

				result.put("error", error);

				return result;
			} else {
				Shop shop = ShopDAO.getShopByUserName(userName).get(0);
				shop.setPassword("");
				result.put("status", Constant.status_ok);

				data.put("profile", shop.toJSON());
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
			}
		} else if (userType == User.role_shipper) {
			boolean checkShip = checkShipperNull(userName);
			if (checkShip || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message",
						"userName not existed | userName is null | password is null");

				result.put("error", error);

				return result;
			} else {

				Shipper shipper = ShipperDAO.getShipperByUserName(userName)
						.get(0);
				shipper.setPassword("");
				result.put("status", Constant.status_ok);

				data.put("profile", shipper.toJSON());
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);

			}
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_request);
			error.put("message", "wrong role");

			result.put("error", error);

		}

		return result;
	}

	public static JSONObject resetPassword(String userName, int userType) {

		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		if (userType == User.role_shop) {
			boolean checkShopNull = checkShopNull(userName);
			if (checkShopNull || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "userName not existed | userName is null");

				result.put("error", error);

				return result;
			} else {
				String code = genShopCode(userName);
				data.put("code", code);

				result.put("status", Constant.status_ok);
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);

				return result;
			}

		} else if (userType == User.role_shipper) {
			boolean checkShipNull = checkShipperNull(userName);
			if (checkShipNull || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message",
						"userName not existed | userName is null | password is null");

				result.put("error", error);

				return result;
			} else {
				String code = genShipCode(userName);
				data.put("code", code);

				result.put("status", Constant.status_ok);
				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);

				return result;
			}
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_request);
			error.put("message", "wrong role");

			result.put("error", error);

		}

		return result;
	}

	public static JSONObject updatePassword(String userName,
			String newPassword, String verifiedCode, int userType) {

		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		if (userType == User.role_shop) {
			boolean checkShopNull = checkShopNull(userName);
			if (checkShopNull || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "userName not existed | userName is null");

				result.put("error", error);

				return result;
			} else {
				boolean checkCode = checkShopCode(userName, verifiedCode);

				if (checkCode) {
					// Update password
					boolean r = ShopDAO.updateShopPassword(userName,
							hashPassword(newPassword));

					if (r) {
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
						error.put("message", "db error");

						result.put("error", error);

						return result;
					}
				} else {
					result.put("status", Constant.status_error);
					result.put("data", data);

					error.put("code", Constant.error_verified_code);
					error.put("message", "error verified code");

					result.put("error", error);

					return result;
				}
			}

		} else if (userType == User.role_shipper) {
			boolean checkShipNull = checkShipperNull(userName);
			if (checkShipNull || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message",
						"userName not existed | userName is null | password is null");

				result.put("error", error);

				return result;
			} else {
				boolean checkCode = checkShipCode(userName, verifiedCode);

				if (checkCode) {
					// Update password
					boolean r = ShipperDAO.updateShipperPassword(userName,
							hashPassword(newPassword));

					if (r) {

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
						error.put("message", "db error");

						result.put("error", error);

						return result;
					}
				} else {
					result.put("status", Constant.status_error);
					result.put("data", data);

					error.put("code", Constant.error_verified_code);
					error.put("message", "error verified code");

					result.put("error", error);

					return result;
				}
			}
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_request);
			error.put("message", "wrong role");

			result.put("error", error);

		}

		return result;
	}

	public static JSONObject updatePhone(String userName, String phoneNumber,
			int userType) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		if (userType == User.role_shop) {
			boolean checkShop = checkShopNull(userName);
			if (checkShop || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "userName not existed | userName is null");

				result.put("error", error);

				return result;
			} else {
				boolean r = ShopDAO.updateShopPhone(userName, phoneNumber);

				if (r) {
					String code = genShopCode(userName);
					data.put("code", code);

					result.put("status", Constant.status_ok);

					result.put("data", data);

					error.put("code", Constant.error_non);
					error.put("message", "no error");

					result.put("error", error);
				} else {
					result.put("status", Constant.status_error);

					result.put("data", data);

					error.put("code", Constant.error_db);
					error.put("message", "error db");

					result.put("error", error);
				}
			}
		} else if (userType == User.role_shipper) {
			boolean checkShip = checkShipperNull(userName);
			if (checkShip || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "userName not existed | userName is null");

				result.put("error", error);

				return result;
			} else {
				boolean r = ShipperDAO
						.updateShipperPhone(userName, phoneNumber);

				if (r) {
					String code = genShipCode(userName);
					data.put("code", code);

					result.put("status", Constant.status_ok);

					result.put("data", data);

					error.put("code", Constant.error_non);
					error.put("message", "no error");

					result.put("error", error);
				} else {
					result.put("status", Constant.status_error);

					result.put("data", data);

					error.put("code", Constant.error_db);
					error.put("message", "error db");

					result.put("error", error);
				}
			}
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_request);
			error.put("message", "wrong role");

			result.put("error", error);

		}

		return result;
	}

	public static JSONObject verifyPhone(String userName, String verifiedCode,
			int userType) {

		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		if (userType == User.role_shop) {
			boolean checkShopNull = checkShopNull(userName);
			if (checkShopNull || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "userName not existed | userName is null");

				result.put("error", error);

				return result;
			} else {
				boolean checkCode = checkShopCode(userName, verifiedCode);

				if (checkCode) {
					// Update status
					boolean r = ShopDAO.updateShopStatus(userName,
							User.s_activated);

					if (r) {
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
						error.put("message", "db error");

						result.put("error", error);

						return result;
					}
				} else {
					result.put("status", Constant.status_error);
					result.put("data", data);

					error.put("code", Constant.error_verified_code);
					error.put("message", "error verified code");

					result.put("error", error);

					return result;
				}
			}

		} else if (userType == User.role_shipper) {
			boolean checkShipNull = checkShipperNull(userName);
			if (checkShipNull || userName == null || userName.length() == 0) {
				result.put("status", Constant.status_error);
				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message",
						"userName not existed | userName is null | password is null");

				result.put("error", error);

				return result;
			} else {
				boolean checkCode = checkShipCode(userName, verifiedCode);

				if (checkCode) {
					boolean r = ShipperDAO.updateShipperStatus(userName,
							User.s_activated);

					if (r) {

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
						error.put("message", "db error");

						result.put("error", error);

						return result;
					}
				} else {
					result.put("status", Constant.status_error);
					result.put("data", data);

					error.put("code", Constant.error_verified_code);
					error.put("message", "error verified code");

					result.put("error", error);

					return result;
				}
			}
		} else {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_request);
			error.put("message", "wrong role");

			result.put("error", error);

		}

		return result;
	}

	public static JSONObject updateShopProfile(String userName,
			String shopName, String address, String bankInfo, String facebook,
			String zalo) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		boolean checkShop = checkShopNull(userName);
		if (checkShop || userName == null || userName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName not existed | userName is null");

			result.put("error", error);

			return result;
		} else {
			boolean r = ShopDAO.updateShopProfile(userName, shopName, address,
					bankInfo, facebook, zalo);

			if (r) {
				result.put("status", Constant.status_ok);

				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
			} else {
				result.put("status", Constant.status_error);

				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "error db");

				result.put("error", error);
			}
		}

		return result;
	}

	public static JSONObject updateShipperProfile(String userName,
			String shipperName, String motorNumber, String birthDay,
			String address, String idNumber) {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		boolean checkShipper = checkShipperNull(userName);
		if (checkShipper || userName == null || userName.length() == 0) {
			result.put("status", Constant.status_error);
			result.put("data", data);

			error.put("code", Constant.error_db);
			error.put("message", "userName not existed | userName is null");

			result.put("error", error);

			return result;
		} else {
			boolean r = ShipperDAO.updateShipperProfile(userName, shipperName,
					motorNumber, birthDay, address, idNumber);

			if (r) {
				result.put("status", Constant.status_ok);

				result.put("data", data);

				error.put("code", Constant.error_non);
				error.put("message", "no error");

				result.put("error", error);
			} else {
				result.put("status", Constant.status_error);

				result.put("data", data);

				error.put("code", Constant.error_db);
				error.put("message", "error db");

				result.put("error", error);
			}
		}

		return result;
	}

}
