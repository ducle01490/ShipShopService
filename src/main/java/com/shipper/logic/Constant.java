package com.shipper.logic;

public class Constant {
	public static final int status_ok = 0;
	public static final int status_error = 1;

	public static final int error_non = 0;
	public static final int error_request = 1;
	public static final int error_db = 2;
	
	public static final int error_authen = 5;

	public static final int error_verified_code = 3;
	
	public static final int error_order_null = 4;
	
	public static final int error_update_status = 6;
	public static final int error_device_os = 7;
	public static final int error_send_sms = 8;
	public static final int error_phone = 8;
	// JDBC driver name and database URL
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/shipshop?useUnicode=true&characterEncoding=utf-8";

	// Database credentials
	public static final String USER = "root";
	public static final String PASS = null;//"";//"orek@db16";

	
	
	public static final int delivery_normal = 0;
	public static final int delivery_fast = 1;
}
