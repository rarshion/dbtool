package com.ablesky.dbtool.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.util.WebUtil;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping("/index")
	public void index(HttpServletResponse response) throws IOException {
		String columnSql = "select * from information_schema.columns where table_schema = 'ajaxableskydb' order by table_name, ordinal_position";
		List<Column> columnList = jdbcTemplate.getJdbcOperations().query(columnSql, new Column.ColumnRowMapper());
		String tableSql = "select * from information_schema.tables where table_schema = 'ajaxableskydb' order by table_name ";
		List<Table> tableList = jdbcTemplate.getJdbcOperations().query(tableSql, new Table.TableRowMapper());
		Map<String, Table> tableMap = new HashMap<String, Table>();
		for(Table table: tableList) {
			tableMap.put(table.getTableName(), table);
		}
		Table table = null;
		for(Column column: columnList) {
			if(table == null || !table.getTableName().equals(column.getTableName())) {
				table = tableMap.get(column.getTableName());
			}
			table.addColumn(column);
		}
//		for(Table t: tableList) {
//			System.out.println(t.getTableName() + ": " + t.getColumnCount());
//		}
		for(Column c: columnList) {
			System.out.println(c.getDataType() + " | " + c.getColumnType());
		}
		WebUtil.writeResponse(response, "success");
	}
	
}
