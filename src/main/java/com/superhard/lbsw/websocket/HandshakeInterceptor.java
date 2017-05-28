package com.superhard.lbsw.websocket;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

	
	/**
	 * Created by Mr.Yangxiufeng on 2016/5/27.
	 * Time:9:57
	 * ProjectName:SpringWebApp
	 */
	public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor{
		private static Logger logger = LogManager.getLogger(HandshakeInterceptor.class);
	    @Override
	    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
	    	logger.info("beforeHandshake");
	    	if (getSession(request) != null) {
	            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
	            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
	            attributes.put("userId", httpServletRequest.getParameter("userId"));
	        }
	        return super.beforeHandshake(request, response, wsHandler, attributes);
	    }

	    @Override
	    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
	    	logger.info("afterHandshake");
	        super.afterHandshake(request, response, wsHandler, ex);
	    }
	    
	    private HttpSession getSession(ServerHttpRequest request) {
	    	logger.info("getSession");
	        if (request instanceof ServletServerHttpRequest) {
	            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
	            return serverRequest.getServletRequest().getSession(false);
	        }
	        return null;
	    }
	}