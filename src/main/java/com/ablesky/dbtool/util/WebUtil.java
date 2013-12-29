package com.ablesky.dbtool.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;


public class WebUtil {
	
	public static final int DEFAULT_COOKIE_TIME = 30 * 24 * 60 * 60;
	
	private WebUtil(){}
	
	public static void writeResponse(HttpServletResponse response, Map<String, Object> resultMap) {
		writeResponse(response, JsonUtil.obj2Json(resultMap));
	}

	public static void writeResponse(HttpServletResponse response, String content) {
		writeResponse(response, content, "utf-8");
	}
	
	public static void writeResponse(HttpServletResponse response, String content, String charset) {
		if(charset == "" || charset.trim() == ""){
			charset = "utf-8";
		}
		response.setContentType("text/plain;charset=" + charset);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
	public static String getCookie(HttpServletRequest request, String name){
		Cookie[] cookies = request.getCookies();
		if(cookies == null || name.trim() == ""){
			return "";
		}
		for(Cookie cookie: cookies){
			if(name.equals(cookie.getName())){
				return cookie.getValue();
			}
		}
		return "";
	}
	
	public static void setCookie(HttpServletResponse response, String name, String value){
		Integer expireTime = DEFAULT_COOKIE_TIME;
		setCookie(response, name, value, expireTime);
	}
	
	public static void setCookie(HttpServletResponse response, String name, String value, Integer expireTime){
		if(name == null || name.trim() == "" || expireTime == null){
			return;
		}
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expireTime);
		response.addCookie(cookie);
	}
	
	public static String getIP(HttpServletRequest req) {
        String ip = req.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }
}
