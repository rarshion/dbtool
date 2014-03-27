package com.ablesky.dbtool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ablesky.dbtool.pojo.Column;
import com.ablesky.dbtool.pojo.ContrastResult;
import com.ablesky.dbtool.pojo.DataBase;
import com.ablesky.dbtool.pojo.SchemaInfo;
import com.ablesky.dbtool.pojo.Table;
import com.ablesky.dbtool.service.IDataBaseService;
import com.ablesky.dbtool.util.DbInfoConfig;
import com.ablesky.dbtool.util.WebUtil;

@Controller
@RequestMapping("/db")
public class DataBaseController {
	
	@Autowired
	private IDataBaseService dataBaseService;
	
	@RequestMapping("/index")
	public String index(Model model){
		model.addAttribute("dbInfoList", DbInfoConfig.getDbInfoList());
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
	public void compare (
			String sampleAddress,
			String sampleDbName,
			String targetAddress,
			String targetDbName,
			HttpServletResponse response) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Future<DataBase> sampleDbFuture = getLoadedDbFuture(sampleAddress, sampleDbName);
		Future<DataBase> targetDbFuture = getLoadedDbFuture(targetAddress, targetDbName);
		DataBase sampleDb = sampleDbFuture.get();
		DataBase targetDb = targetDbFuture.get();
		if(sampleDb == null) {
			resultMap.put("success", false);
			resultMap.put("message", "样本数据库不存在!");
			WebUtil.writeResponse(response, resultMap);
			return;
		}
		resultMap.put("sqlList", compare(sampleDb, targetDb));
		resultMap.put("success", true);
		WebUtil.writeResponse(response, resultMap);
	}
	
	private List<String> compare(DataBase sampleDb, DataBase targetDb) {
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
		return sqlList;
	}
	
	private DataBase getLoadedDb(String address, String dbName) {
		DataBase db = dataBaseService.getDataBaseByAddressAndDbName(address, dbName);
		return dataBaseService.fillDataBaseWithTables(address, db);
	}
	
	private Future<DataBase> getLoadedDbFuture(final String address, final String dbName) {
		return Executors.newSingleThreadScheduledExecutor().submit(new Callable<DataBase>() {
			@Override
			public DataBase call() throws Exception {
				return getLoadedDb(address, dbName);
			}
		});
	}
}
