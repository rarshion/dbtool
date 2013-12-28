package com.ablesky.dbtool.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.util.WebUtil;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@RequestMapping("/index")
	public void index(HttpServletResponse response) throws IOException {
		String sql = "select * from information_schema.columns where table_schema = 'ajaxableskydb' order by table_name, ordinal_position";
		List<Column> list = jdbcTemplate.getJdbcOperations().query(sql, new Column.ColumnRowMapper());
		for(Column column: list) {
			System.out.println(column);
		}
//		Map<String, Object> m = list.get(0);
//		for(Map<String, Object> mm: list) {
////			if(mm.get("CHARACTER_SET_NAME") != null) {
////				m = mm;
////				System.out.println("yes");
////				break;
////			}
//			Object obj = mm.get("COLUMN_DEFAULT");
//			if(obj == null) {
//				continue;
//			}
//			System.out.println(obj.getClass().getName() + "(" + obj + ")");
//		}
//		for(Entry<String, Object> entry: m.entrySet()) {
//			String classType = entry.getValue() != null? entry.getValue().getClass().getName(): "null";
//			System.out.println(entry.getKey() + ": " + classType + "(" + entry.getValue() + ")");
//		}
		WebUtil.writeResponse(response, "success");
	}
	
}
