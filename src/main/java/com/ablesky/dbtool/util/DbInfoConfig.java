package com.ablesky.dbtool.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class DbInfoConfig {

	public static final Properties DB_INFO_CONFIG = new Properties();
	private static final Pattern PROPERTIE_KEY_PATTERN = Pattern.compile("(.+)\\.([^.]+)$");
	
	static {
		try {
			DB_INFO_CONFIG.load(DbInfoConfig.class.getClassLoader().getResourceAsStream("DbInfo.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getUsernameViaAddress(String address) {
		String username = DB_INFO_CONFIG.getProperty(address + ".username");
		return StringUtils.isEmpty(username)? "": username;
	}
	
	public static String getPasswordViaAddress(String address) {
		String password = DB_INFO_CONFIG.getProperty(address + ".password");
		return StringUtils.isEmpty(password)? "": password;
	}
	
	public static List<Map<String, String>> getDbInfoList() {
		String key = "", value = "", address = "", subkey = "";
		Map<String, Map<String, String>> dbInfoMap = new LinkedHashMap<String, Map<String,String>>();
		for(Entry<Object, Object> entry: DB_INFO_CONFIG.entrySet()) {
			key = entry.getKey().toString();
			value = entry.getValue().toString();
			Matcher matcher = PROPERTIE_KEY_PATTERN.matcher(key);
			if(!matcher.matches()) {
				continue;
			}
			address = matcher.group(1);
			subkey = matcher.group(2);
			Map<String, String> dbInfo = dbInfoMap.get(address);
			if(dbInfo == null) {
				dbInfo = new HashMap<String, String>();
				dbInfo.put("address", address);
				dbInfoMap.put(address, dbInfo);
			}
			dbInfo.put(subkey, value);
		}
		List<Map<String, String>> dbInfoList = new ArrayList<Map<String, String>>(dbInfoMap.values());
		Collections.sort(dbInfoList, new Comparator<Map<String, String>>() {
			@Override
			public int compare(Map<String, String> info1, Map<String, String> info2) {
				// 其实只要小于10，level用字符串比也没什么的
				int level1 = Integer.valueOf(info1.get("level"));
				int level2 = Integer.valueOf(info2.get("level"));
				if(level1 != level2) {
					return level1 - level2;
				}
				String name1 = info1.get("name");
				String name2 = info2.get("name");
				return name1.compareTo(name2);
			}
		});
		return dbInfoList;
	}
	
	public static void main(String[] args) {
	}

}
