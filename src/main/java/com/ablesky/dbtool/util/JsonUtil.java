package com.ablesky.dbtool.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonUtil {
	
	private static JsonConfig config = new JsonConfig();
	private static final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static {
		config.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if(value == null){
					return "";
				}
				if(value instanceof java.sql.Date){
					Calendar cale = Calendar.getInstance();
					cale.setTime((Date)value);
					int year = cale.get(Calendar.YEAR);
					int month = cale.get(Calendar.MONTH) + 1;
					String monthStr = month >= 10? String.valueOf(month): "0" + month;
					int date = cale.get(Calendar.DATE);
					String dateStr = date >= 10? String.valueOf(date): "0" + date;
					String str = year + "-" + monthStr + "-" + dateStr;
					return str;
				}
				return value.toString();
			}
			
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return null;
			}
		});
		
		config.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if(value == null){
					return "";
				}
				if(value instanceof java.util.Date){
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					return format.format((Date)value);
				}
				return value.toString();
			}
			
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return null;
			}
		});
		
		config.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessor() {
			
			public Object processObjectValue(String key, Object value, JsonConfig arg2) {
				if(value == null){
					return "";
				}
				if(value instanceof java.sql.Timestamp){
					return timestampFormat.format((Date)value);
				}
				return value.toString();
			}
			
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return null;
			}
		});
	}
	
	private JsonUtil(){}
	
	public static <T> String obj2Json(T obj){
		JSONObject jsonObject = JSONObject.fromObject(obj, config);
		return jsonObject.toString();
	}
	
	public static JSONObject json2Obj(String json) {
		return JSONObject.fromObject(json);
	}
	
	public static JSONArray json2Arr(String json) {
		return JSONArray.fromObject(json);
	}
	
	public static <T> String list2Json(List<T> list) {
		JSONArray arr = JSONArray.fromObject(list);
		return arr.toString();
	}
	
}
