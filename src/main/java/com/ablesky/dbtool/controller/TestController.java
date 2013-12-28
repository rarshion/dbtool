package com.ablesky.dbtool.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ablesky.dbtool.util.WebUtil;

@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/index")
	public void index(HttpServletResponse response) throws IOException {
		WebUtil.writeResponse(response, "success");
	}
	
}
