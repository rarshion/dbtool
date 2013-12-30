package com.ablesky.dbtool.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ablesky.dbtool.dao.IDataBaseDao;
import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.DataBase;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.service.IDataBaseService;

@Service
public class DataBaseServiceImpl implements IDataBaseService {

	@Autowired
	private IDataBaseDao dataBaseDao;
	
	@Override
	public DataBase getDataBaseByAddressAndDbName(String address, String dbName) {
		return dataBaseDao.getDataBaseByAddressAndDbName(address, dbName);
	}
	
	@Override
	public List<DataBase> getDataBaseListByAddress(String address) {
		return dataBaseDao.getDataBaseListByAddress(address);
	}

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
		if(CollectionUtils.isEmpty(tableList)) {
			return Collections.emptyMap();
		}
		Table table = tableList.get(0);
		if(StringUtils.isEmpty(table.getTableSchema())) {
			return Collections.emptyMap();
		}
		return fillTablesWithColumns(address, tableList, getColumnListByAddressAndDatabase(address, table.getTableSchema()));
	}
	
	@Override
	public Map<String, Table> fillTablesWithColumns(String address, List<Table> tableList, List<Column> columnList) {
		if(StringUtils.isEmpty(address) || CollectionUtils.isEmpty(tableList)) {
			return Collections.emptyMap();
		}
		Map<String, Table> tableMap = new HashMap<String, Table>();
		for(Table tbl: tableList) {
			tableMap.put(tbl.getTableName(), tbl);
		}
		Table table = null;
		for(Column column: columnList) {
			if(table == null || !table.getTableName().equals(column.getTableName())) {
				table = tableMap.get(column.getTableName());
			}
			table.addColumn(column);
		}
		return tableMap;
	}
	
	/**
	 * 批量填充dataBase中的信息
	 */
	@Override
	public Map<String, DataBase> fillDataBasesWithTables(String address, List<DataBase> dbList) {
		if(StringUtils.isEmpty(address) || CollectionUtils.isEmpty(dbList)) {
			return Collections.emptyMap();
		}
		Map<String, DataBase> dbMap = new HashMap<String, DataBase>();
		for(DataBase db: dbList) {
			List<Table> tblList = getTableListByAddressAndDatabase(address, db.getSchemaName());
			fillTablesWithColumns(address, tblList);
			db.addAllTables(tblList);
			dbMap.put(db.getSchemaName(), db);
		}
		return dbMap;
	}
	
	/**
	 * 为单个dataBase填充表信息和列信息
	 */
	@Override
	public DataBase fillDataBaseWithTables(final String address, final DataBase db) {
		if(StringUtils.isEmpty(address) || db == null) {
			return db;
		}
		List<Table> tblList = getTableListByAddressAndDatabase(address, db.getSchemaName());
		fillTablesWithColumns(address, tblList);
		db.addAllTables(tblList);
		return db;
	}
	
	
}
