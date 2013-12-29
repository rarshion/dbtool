package com.ablesky.dbtool.dao;

import java.util.List;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.DataBase;
import com.ablesky.dbtool.pojo.Table;

public interface IDataBaseDao {

	List<DataBase> getDataBaseListByAddress(String address);
	
	List<Table> getTableListByAddressAndDatabase(String address, String database);

	List<Column> getColumnListByAddressAndDatabase(String address, String database);

	DataBase getDataBaseByAddressAndDbName(String address, String dbName);

}
