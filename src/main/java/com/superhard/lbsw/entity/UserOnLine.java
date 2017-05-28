package com.superhard.lbsw.entity;



public class UserOnLine extends UserInfo {
	private String userName;
	private String userStation;	
	private String userTeam;
	private String userMajor;
	private String sessionId;
	private boolean ifOnLine;
	
	public UserOnLine(String userName, String sessionId) {
		super();
		this.userName = userName;
		this.sessionId = sessionId;
	}
	public UserOnLine() {
		super();
		this.userName = "******";
		
	}
	public boolean isIfOnLine() {
		return ifOnLine;
	}
	public void setIfOnLine(boolean ifOnLine) {
		this.ifOnLine = ifOnLine;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getUserStation() {
		return userStation;
	}
	public void setUserStation(String userStation) {
		this.userStation = userStation;
	}
	public String getUserTeam() {
		return userTeam;
	}
	public void setUserTeam(String userTeam) {
		this.userTeam = userTeam;
	}
	public String getUserMajor() {
		return userMajor;
	}
	public void setUserMajor(String userMajor) {
		this.userMajor = userMajor;
	}

}
