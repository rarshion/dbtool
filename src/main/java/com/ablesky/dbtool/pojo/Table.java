package com.ablesky.dbtool.pojo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.jdbc.core.RowMapper;

public class Table implements SchemaInfo {

	private String tableSchema;
	private String tableName;
	private String tableType;
	private String engine;
	private Long tableRows;
	private Long dataLength;
	private Long indexLength;
	private Long autoIncrement;
	private Timestamp createTime;
	private Timestamp updateTime;
	private String tableCollation;
	private String tableComment;
	
	private Map<String, Column> columnMap = new LinkedHashMap<String, Column>();
	
	public String getTableSchema() {
		return tableSchema;
	}
	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public Long getTableRows() {
		return tableRows;
	}
	public void setTableRows(Long tableRows) {
		this.tableRows = tableRows;
	}
	public Long getDataLength() {
		return dataLength;
	}
	public void setDataLength(Long dataLength) {
		this.dataLength = dataLength;
	}
	public Long getIndexLength() {
		return indexLength;
	}
	public void setIndexLength(Long indexLength) {
		this.indexLength = indexLength;
	}
	public Long getAutoIncrement() {
		return autoIncrement;
	}
	public void setAutoIncrement(Long autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getTableCollation() {
		return tableCollation;
	}
	public void setTableCollation(String tableCollation) {
		this.tableCollation = tableCollation;
	}
	public String getTableComment() {
		return tableComment;
	}
	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}
	public void addColumn(Column column) {
		columnMap.put(column.getColumnName(), column);
	}
	public Column getColumn(String columnName) {
		return columnMap.get(columnName);
	}
	public int getColumnCount() {
		return columnMap.size();
	}
	
	public List<Column> getColumnList() {
		return Collections.unmodifiableList(new ArrayList<Column>(columnMap.values()));
	}
	
	public static enum TableField {
		TABLE_SCHEMA,
		TABLE_NAME,
		TABLE_TYPE,
		ENGINE,
		TABLE_ROWS,
		DATA_LENGTH,
		INDEX_LENGTH,
		AUTO_INCREMENT,
		CREATE_TIME,
		UPDATE_TIME,
		TABLE_COLLATION,
		TABLE_COMMENT
		;
	}
	

	@Override
	public ContrastResult contrastTo(SchemaInfo schemaInfo) {
		if(schemaInfo == null) {
			return new ContrastResult(this, ContrastResult.Operation.create);
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || ! (obj instanceof Table)) {
			return false;
		}
		Table tbl = (Table) obj;
		if(!this.tableSchema.equals(tbl.tableSchema)
				|| !this.tableName.equals(tbl.tableName)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		return this.tableSchema + "." + this.tableName;
	}
	
	public String toFullString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public String generateCreateSql() {
		StringBuilder sqlBuff = new StringBuilder()
			.append("CREATE TABLE ")
			.append("`").append(this.tableName).append("`")
			.append(" (").append("\n  ")
		;
		boolean isFirst = true;
		for(Column column: columnMap.values()) {
			if(!isFirst) {
				sqlBuff.append(",\n  ");
			}
			sqlBuff.append(column.generateDeclareSql());
			isFirst = false;
		}
		sqlBuff.append("\n)")
			.append(" ENGINE=").append(this.engine);
		if(!StringUtils.isEmpty(this.tableCollation)) {
			sqlBuff.append(" COLLATE=").append(this.tableCollation);
		}
		return sqlBuff.append(";").toString();
	}
	
	@Override
	public String generateUpdateSql() {
		return "";
	}
	
	public static class TableRowMapper implements RowMapper<Table> {

		@Override
		public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
			Table table = new Table();
			table.setTableSchema(rs.getString(TableField.TABLE_SCHEMA.name()));
			table.setTableName(rs.getString(TableField.TABLE_NAME.name()));
			table.setTableType(rs.getString(TableField.TABLE_TYPE.name()));
			table.setEngine(rs.getString(TableField.ENGINE.name()));
			table.setTableRows(rs.getLong(TableField.TABLE_ROWS.name()));
			table.setDataLength(rs.getLong(TableField.DATA_LENGTH.name()));
			table.setIndexLength(rs.getLong(TableField.INDEX_LENGTH.name()));
			table.setAutoIncrement(rs.getLong(TableField.AUTO_INCREMENT.name()));
			table.setCreateTime(rs.getTimestamp(TableField.CREATE_TIME.name()));
			table.setUpdateTime(rs.getTimestamp(TableField.UPDATE_TIME.name()));
			table.setTableCollation(rs.getString(TableField.TABLE_COLLATION.name()));
			table.setTableComment(rs.getString(TableField.TABLE_COMMENT.name()));
			return table;
		}
		
	}

}
