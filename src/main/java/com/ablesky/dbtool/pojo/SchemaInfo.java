package com.ablesky.dbtool.pojo;

public interface SchemaInfo {

	String getTableSchema();
	
	String getTableName();
	
	String generateCreateSql();
	
	String generateUpdateSql();
	
	ContrastResult contrastTo(SchemaInfo schemaInfo);
}
