package com.ablesky.dbtool.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class DataBase implements SchemaInfo {

	private String schemaName;
	private String defaultCharacterSetName;
	private String defaultCollationName;
	
	private Map<String, Table> tableMap = new LinkedHashMap<String, Table>();
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	public String getDefaultCharacterSetName() {
		return defaultCharacterSetName;
	}
	public void setDefaultCharacterSetName(String defaultCharacterSetName) {
		this.defaultCharacterSetName = defaultCharacterSetName;
	}
	public String getDefaultCollationName() {
		return defaultCollationName;
	}
	public void setDefaultCollationName(String defaultCollationName) {
		this.defaultCollationName = defaultCollationName;
	}
	
	public void addTable(Table table) {
		tableMap.put(table.getTableName(), table);
	}
	
	public void addAllTables(Collection<Table> tables) {
		for(Table table: tables) {
			if(table == null) {
				continue;
			}
			tableMap.put(table.getTableName(), table);
		}
	}
	
	public Table getTable(String tableName) {
		return tableMap.get(tableName);
	}
	
	public int getTableCount() {
		return tableMap.size();
	}
	
	public List<Table> getTableList() {
		return Collections.unmodifiableList(new ArrayList<Table>(tableMap.values()));
	}
	
	public static enum DataBaseField {
		SCHEMA_NAME,
		DEFAULT_CHARACTER_SET_NAME,
		DEFAULT_COLLATION_NAME
		;
	}
	
	public static class DataBaseRowMapper implements RowMapper<DataBase> {

		@Override
		public DataBase mapRow(ResultSet rs, int rowNum) throws SQLException {
			DataBase dataBase = new DataBase();
			dataBase.setSchemaName(rs.getString(DataBaseField.SCHEMA_NAME.name()));
			dataBase.setDefaultCharacterSetName(rs.getString(DataBaseField.DEFAULT_CHARACTER_SET_NAME.name()));
			dataBase.setDefaultCollationName(rs.getString(DataBaseField.DEFAULT_COLLATION_NAME.name()));
			return dataBase;
		}
		
	}
	
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null || ! (obj instanceof DataBase)) {
			return false;
		}
		DataBase db = (DataBase) obj;
		if(!this.schemaName.equals(db.getSchemaName())
				|| !this.defaultCharacterSetName.equals(db.getDefaultCharacterSetName())
				|| !this.defaultCollationName.equals(db.getDefaultCollationName())) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return this.schemaName + "[charset(" + this.defaultCharacterSetName + ") collate(" + this.defaultCollationName + ")]";
	}

	@Override
	public String getTableSchema() {
		return this.schemaName;
	}
	@Override
	public String getTableName() {
		return "";
	}
	@Override
	public String generateCreateSql() {
		return "CREATE DATABASE `" + this.schemaName + "` CHARSET " + this.defaultCharacterSetName + " COLLATE " + this.defaultCollationName + ";";
	}
	@Override
	public String generateUpdateSql() {
		return "ALTER DATABASE `" + this.schemaName + "` CHARSET " + this.defaultCharacterSetName + " COLLATE " + this.defaultCollationName + ";";
	}
	@Override
	public List<ContrastResult> contrastTo(SchemaInfo schemaInfo) {
		List<ContrastResult> crList = new ArrayList<ContrastResult>();
		if(schemaInfo == null) {
			crList.add(new ContrastResult(this, ContrastResult.Operation.create));
			return crList;
		}
		if(! (schemaInfo instanceof DataBase)) {
			return Collections.emptyList();
		}
		DataBase targetDb = (DataBase) schemaInfo;
		if(!equals(targetDb)) {
			crList.add(new ContrastResult(this, ContrastResult.Operation.update));
		}
		for(Table table: this.tableMap.values()) {
			crList.addAll(table.contrastTo(targetDb.getTable(table.getTableName())));
		}
		return crList;
	}
	
}
