package com.ablesky.dbtool.dao;

import java.util.List;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.Table;

public interface IDataBaseDao {

	List<Table> getTableListByAddressAndDatabase(String address, String database);

	List<Column> getColumnListByAddressAndDatabase(String address, String database);

}
