package com.ablesky.dbtool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.ContrastResult;
import com.ablesky.dbtool.pojo.DataBase;
import com.ablesky.dbtool.pojo.SchemaInfo;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.service.IDataBaseService;
import com.ablesky.dbtool.util.WebUtil;

@Controller
@RequestMapping("/db")
public class DataBaseController {
	
	@Autowired
	private IDataBaseService dataBaseService;
	
	@RequestMapping("/index")
	public String index(){
		return "db/index";
	}

	@RequestMapping("/list")
	public void list(String address, HttpServletResponse response) {
		List<DataBase> dbList = dataBaseService.getDataBaseListByAddress(address);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("dbList", dbList);
		WebUtil.writeResponse(response, resultMap);
	}
	
	@RequestMapping("/compare")
	public void compare(
			String sampleAddress,
			String sampleDbName,
			String targetAddress,
			String targetDbName,
			HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		DataBase sampleDb = getLoadedDb(sampleAddress, sampleDbName);
		DataBase targetDb = getLoadedDb(targetAddress, targetDbName);
		if(sampleDb == null) {
			resultMap.put("success", false);
			resultMap.put("message", "样本数据库不存在!");
			WebUtil.writeResponse(response, resultMap);
			return;
		}
		List<ContrastResult> crList = sampleDb.contrastTo(targetDb);
		List<String> sqlList = new ArrayList<String>(crList.size());
		List<String> tblSqlList = new ArrayList<String>();
		List<String> colSqlList = new ArrayList<String>();
		SchemaInfo si = null;
		for(ContrastResult cr: crList) {
			si = cr.getSchemaInfo();
			if(si instanceof Table) {
				tblSqlList.add(cr.generateSql());
			} else if(si instanceof Column) {
				colSqlList.add(cr.generateSql());
			}
		}
		sqlList.addAll(tblSqlList);
		sqlList.addAll(colSqlList);
		resultMap.put("sqlList", sqlList);
		resultMap.put("success", true);
		WebUtil.writeResponse(response, resultMap);
	}
	
	private DataBase getLoadedDb(String address, String dbName) {
		DataBase db = dataBaseService.getDataBaseByAddressAndDbName(address, dbName);
		return dataBaseService.fillDataBaseWithTables(address, db);
	}
}
