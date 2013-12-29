package com.ablesky.dbtool.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ablesky.dbtool.dao.IDataBaseDao;
import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.service.IDataBaseService;

@Service
public class DataBaseServiceImpl implements IDataBaseService {

	@Autowired
	private IDataBaseDao dataBaseDao;

	@Override
	public List<Table> getTableListByAddressAndDatabase(String address, String database) {
		return dataBaseDao.getTableListByAddressAndDatabase(address, database);
	}

	@Override
	public List<Column> getColumnListByAddressAndDatabase(String address, String database) {
		return dataBaseDao.getColumnListByAddressAndDatabase(address, database);
	}
	
	@Override
	public Map<String, Table> fillTablesWithColumns(String address, List<Table> tableList) {
		if(StringUtils.isEmpty(address) || CollectionUtils.isEmpty(tableList)) {
			return Collections.emptyMap();
		}
		Table table = tableList.get(0);
		if(StringUtils.isEmpty(table.getTableSchema())) {
			return Collections.emptyMap();
		}
		List<Column> columnList = getColumnListByAddressAndDatabase(address, table.getTableSchema());
		Map<String, Table> tableMap = new HashMap<String, Table>();
		for(Table tbl: tableList) {
			tableMap.put(tbl.getTableName(), tbl);
		}
		table = null;
		for(Column column: columnList) {
			if(table == null || !table.getTableName().equals(column.getTableName())) {
				table = tableMap.get(column.getTableName());
			}
			table.addColumn(column);
		}
		return tableMap;
	}
	
	
}
