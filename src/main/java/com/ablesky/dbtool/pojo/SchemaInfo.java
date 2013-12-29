package com.ablesky.dbtool.pojo;

import java.util.List;

public interface SchemaInfo {

	String getTableSchema();
	
	String getTableName();
	
	String generateCreateSql();
	
	String generateUpdateSql();
	
	/**
	 * 如果自身与schemaInfo的内容不一致，则返回自身的部分
	 */
	List<ContrastResult> contrastTo(SchemaInfo schemaInfo);
}
