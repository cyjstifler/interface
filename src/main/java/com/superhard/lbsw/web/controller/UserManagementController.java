package com.superhard.lbsw.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




public class UserManagementController {

	private String userName,passWord;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return passWord;
	}

	public void setUserPassword(String passWord) {
		this.passWord = passWord;
	}

	private HttpServletRequest request;
	private HttpServletResponse response;

	
	

	public void editPassWordInfo() throws Exception {
		System.out.println("");
		 request.getParameter("userName");
		
		try {
			this.response.setContentType("text/json;charset=utf-8");
			this.response.setCharacterEncoding("UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
