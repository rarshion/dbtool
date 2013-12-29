package com.ablesky.dbtool.service;

import java.util.List;
import java.util.Map;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.DataBase;
import com.ablesky.dbtool.pojo.Table;

public interface IDataBaseService {

	List<Table> getTableListByAddressAndDatabase(String address, String database);

	List<Column> getColumnListByAddressAndDatabase(String address, String database);

	Map<String, Table> fillTablesWithColumns(String address, List<Table> tableList);

	List<DataBase> getDataBaseListByAddress(String address);

	Map<String, DataBase> fillDataBasesWithTables(String address, List<DataBase> dbList);

	DataBase fillDataBaseWithTables(String address, DataBase db);

	DataBase getDataBaseByAddressAndDbName(String address, String dbName);

}
