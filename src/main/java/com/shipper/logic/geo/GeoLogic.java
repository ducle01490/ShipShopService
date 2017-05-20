package com.shipper.logic.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.JSONObject;

import com.shipper.dao.GeoDAO;
import com.shipper.logic.Constant;
import com.shipper.model.CityGeo;

public class GeoLogic {
	
	public static String hanoi = "Hà Nội";
	public static String[] hanoi_provinces = {
	"Quận Ba Đình",
	"Quận Hoàn Kiếm",
	"Quận Tây Hồ",
	"Quận Long Biên",
	"Quận Cầu Giấy",
	"Quận Đống Đa	",
	"Quận Hai Bà Trưng",
	"Quận Hoàng Mai",
	"Quận Thanh Xuân",
	"Huyện Sóc Sơn",
	"Huyện Đông Anh",
	"Huyện Gia Lâm",
	"Quận Nam Từ Liêm",
	"Huyện Thanh Trì",
	"Quận Bắc Từ Liêm",
	"Huyện Mê Linh",
	"Quận Hà Đông",
	"Thị xã Sơn Tây",
	"Huyện Ba Vì",
	"Huyện Phúc Thọ",
	"Huyện Đan Phượng",
	"Huyện Hoài Đức",
	"Huyện Quốc Oai",
	"Huyện Thạch Thất",
	"Huyện Chương Mỹ",
	"Huyện Thanh Oai",
	"Huyện Thường Tín",
	"Huyện Phú Xuyên",
	"Huyện Ứng Hòa",
	"Huyện Mỹ Đức"
	};
	
	
	public static void initGeo() {
		for(int i = 0 ; i < hanoi_provinces.length ; i ++) {
			GeoDAO.createCityGeo(hanoi, hanoi_provinces[i], "");
		}
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray getJSONCity(List<CityGeo> geos) {
		JSONArray r = new JSONArray();
		
		for(CityGeo g: geos) {
			r.add(g.toJSON());
		}
		
		return r;
	}
	
	public static Map<String, List<CityGeo>> cityGeoMap(List<CityGeo> geos) {
		Map<String, List<CityGeo>> r = new HashMap<String, List<CityGeo>>();
		
		for(CityGeo g: geos) {
			if(r.containsKey(g.getCity())) {
				String city = g.getCity();
				List<CityGeo> l = r.get(city);
				l.add(g);
				r.put(city, l);
			} else {
				String city = g.getCity();
				List<CityGeo> l = new ArrayList<CityGeo>();
				l.add(g);
				r.put(city, l);
			}
		}
		
		
		return r;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray cityGeoJSON( Map<String, List<CityGeo>> data) {
		JSONArray r= new JSONArray();
		
		
		for(String city : data.keySet()) {
			JSONObject s = new JSONObject();
			s.put("city", city);
			s.put("provinces", getJSONCity(data.get(city)));
			
			r.add(s);
		}
		
		return r;
	}
	
	public static JSONObject getGeoList() {
		JSONObject result = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject error = new JSONObject();

		List<CityGeo> geos = GeoDAO.getCityGeoList();
		JSONArray a = cityGeoJSON(cityGeoMap(geos));
		
		result.put("status", Constant.status_error);
		
		data.put("cityGeo", a);
		result.put("data", data);

		error.put("code", Constant.error_non);
		error.put("message", "no error");

		result.put("error", error);

		return result;
		
		
	}
}
