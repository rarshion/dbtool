package com.ablesky.dbtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	/**
	 * 不知为何，jetty下"/"会走这里，而tomcat下"/"会走welcome file
	 */
	@RequestMapping({"/", "/index"})
	public String index(){
		return "forward:/db/index";
	}
}
