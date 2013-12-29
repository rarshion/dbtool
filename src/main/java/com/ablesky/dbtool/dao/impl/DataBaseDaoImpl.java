package com.ablesky.dbtool.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ablesky.dbtool.dao.IDataBaseDao;
import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.util.JdbcTemplateManager;

@Repository
public class DataBaseDaoImpl implements IDataBaseDao {

	@Override
	public List<Table> getTableListByAddressAndDatabase(String address, String database) {
		if(StringUtils.isEmpty(address) || StringUtils.isEmpty(database)) {
			return Collections.emptyList();
		}
		NamedParameterJdbcTemplate template = JdbcTemplateManager.getNamedParameterJdbcTemplateByAddress(address);
		if(template == null) {
			return Collections.emptyList();
		}
		String sql = "select * from information_schema.tables where table_schema = :database order by table_name ";;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("database", database);
		return template.query(sql, params, new Table.TableRowMapper());
	}
	
	@Override
	public List<Column> getColumnListByAddressAndDatabase(String address, String database) {
		if(StringUtils.isEmpty(address) || StringUtils.isEmpty(database)) {
			return Collections.emptyList();
		}
		NamedParameterJdbcTemplate template = JdbcTemplateManager.getNamedParameterJdbcTemplateByAddress(address);
		if(template == null) {
			return Collections.emptyList();
		}
		String sql = "select * from information_schema.columns where table_schema = :database order by table_name, ordinal_position";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("database", database);
		return template.query(sql, params, new Column.ColumnRowMapper());
	}
	
}
