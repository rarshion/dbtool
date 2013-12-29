package com.ablesky.dbtool.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ablesky.dbtool.pojo.ContrastResult;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.service.IDataBaseService;
import com.ablesky.dbtool.util.WebUtil;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private IDataBaseService dataBaseService;
	
	@RequestMapping("/index")
	public String index(HttpServletResponse response) throws IOException {
		return "index";
	}
	
	@RequestMapping("/getSql")
	public void getSql(HttpServletResponse response) throws IOException {
		Map<String, Table> miaov1TblMap = getTableMap("localhost", "miaov1");
		Map<String, Table> miaov2TblMap = getTableMap("localhost", "miaov2");
		List<ContrastResult> crList = new ArrayList<ContrastResult>();
		for(Table tbl2: miaov2TblMap.values()) {
			Table tbl1 = miaov1TblMap.get(tbl2.getTableName());
			crList.addAll(tbl2.contrastTo(tbl1));
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
	
	private Map<String, Table> getTableMap(String address, String database) {
		List<Table> tableList = dataBaseService.getTableListByAddressAndDatabase(address, database);
		return dataBaseService.fillTablesWithColumns(address, tableList);
	}
	
}
