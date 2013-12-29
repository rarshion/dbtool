package com.ablesky.dbtool.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class DbInfoConfig {

	public static final Properties DB_INFO_CONFIG = new Properties();
	
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
}
