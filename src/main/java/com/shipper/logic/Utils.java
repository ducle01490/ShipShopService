package com.shipper.logic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Utils {
	
	public static String sqlTimeFormat(Timestamp date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

}
