package com.ablesky.dbtool.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.ContrastResult;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.util.WebUtil;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping("/index")
	public String index(HttpServletResponse response) throws IOException {
		return "index";
	}
	
	@RequestMapping("/getSql")
	public void getSql(HttpServletResponse response) throws IOException {
		Map<String, Table> miaov1TblMap = getTableMap("miaov1");
		Map<String, Table> miaov2TblMap = getTableMap("miaov2");
		List<ContrastResult> crList = new ArrayList<ContrastResult>();
		for(Table tbl2: miaov2TblMap.values()) {
			Table tbl1 = miaov1TblMap.get(tbl2.getTableName());
			ContrastResult cr = tbl2.contrastTo(tbl1);
			if(cr != null) {
				crList.add(cr);
				continue;
			}
			for(Column col2: tbl2.getColumnList()) {
				Column col1 = tbl1.getColumn(col2.getColumnName());
				cr = col2.contrastTo(col1);
				if(cr != null) {
					crList.add(cr);
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> sqlList = new ArrayList<String>();
		for(ContrastResult cr: crList) {
			System.out.println(cr.generateSql());
			sqlList.add(cr.generateSql());
		}
		resultMap.put("sqlList", sqlList);
		WebUtil.writeResponse(response, resultMap);
	}
	
	private Map<String, Table> getTableMap(String tableName) {
		String columnSql = "select * from information_schema.columns where table_schema = '" + tableName + "' order by table_name, ordinal_position";
		List<Column> columnList = jdbcTemplate.getJdbcOperations().query(columnSql, new Column.ColumnRowMapper());
		String tableSql = "select * from information_schema.tables where table_schema = '" + tableName + "' order by table_name ";
		List<Table> tableList = jdbcTemplate.getJdbcOperations().query(tableSql, new Table.TableRowMapper());
		Map<String, Table> tableMap = new LinkedHashMap<String, Table>();
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
		return tableMap;
	}
	
}
