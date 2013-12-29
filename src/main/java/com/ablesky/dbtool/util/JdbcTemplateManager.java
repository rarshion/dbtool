package com.ablesky.dbtool.util;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcTemplateManager {
	
	private static Map<String, NamedParameterJdbcTemplate> jdbcTemplatePool = new HashMap<String, NamedParameterJdbcTemplate>();
	
	public static JdbcTemplate getJdbcTemplateByAddress(String address) {
		NamedParameterJdbcTemplate npjt = getNamedParameterJdbcTemplateByAddress(address);
		return npjt == null? null: (JdbcTemplate) npjt.getJdbcOperations();
	}
	
	public static JdbcTemplate getJdbcTemplateByAddress(String address, String username, String password) {
		NamedParameterJdbcTemplate npjt = getNamedParameterJdbcTemplateByAddress(address, username, password);
		return npjt == null? null: (JdbcTemplate) npjt.getJdbcOperations();
	}
	
	public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplateByAddress(String address) {
		return getNamedParameterJdbcTemplateByAddress(address, DbInfoConfig.getUsernameViaAddress(address), DbInfoConfig.getPasswordViaAddress(address));
	}
	
	public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplateByAddress(String address, String username, String password) {
		if(StringUtils.isEmpty(address)) {
			return null;
		}
		NamedParameterJdbcTemplate template = jdbcTemplatePool.get(address);
		if(template != null) {
			return template;
		}
		DataSource dataSource = SpringUtil.addDataSourceBean(address, username, password);
		if(dataSource == null) {
			return null;
		}
		template = new NamedParameterJdbcTemplate(dataSource);
		jdbcTemplatePool.put("address", template);
		return template;
	}
	
}
