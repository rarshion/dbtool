package com.ablesky.dbtool.pojo;

public class ContrastResult {
	
	private Operation oper;
	private SchemaInfo schemaInfo;
	
	public ContrastResult(SchemaInfo schemaInfo, Operation oper) {
		this.schemaInfo = schemaInfo;
		this.oper = oper;
	}
	
	public Operation getOper() {
		return oper;
	}

	public void setOper(Operation oper) {
		this.oper = oper;
	}

	public SchemaInfo getSchemaInfo() {
		return schemaInfo;
	}

	public void setSchemaInfo(SchemaInfo schemaInfo) {
		this.schemaInfo = schemaInfo;
	}
	
	public String generateSql() {
		if(oper == Operation.create) {
			return schemaInfo.generateCreateSql();
		} else if(oper == Operation.update) {
			return schemaInfo.generateUpdateSql();
		} else {
			return "";
		}
	}

	public static enum Operation {
		create, update;
	}
}
