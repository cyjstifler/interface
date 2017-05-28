package com.superhard.lbsw.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.superhard.lbsw.service.UserService;

@Controller
@RequestMapping("/user")	
public class UserController {
	
	private static Logger logger = LogManager.getLogger(SysController.class); 
	private Gson gson = new Gson();
	@SuppressWarnings("restriction")
	@Resource
	private UserService userService;	
	@Value("#{configProperties['userPageSize']}")
	private String userPageSize; 
	//TODO
	@RequestMapping(value = "/post", method=RequestMethod.POST)
	public @ResponseBody String testPost(){
		return null;
	}
	
	@RequestMapping(value = "/login", method=RequestMethod.POST,consumes="application/json; charset=UTF-8",
			produces="application/json; charset=UTF-8")
	@ResponseBody 
	public String login(HttpServletRequest request,@RequestBody String str){
//		Map <String ,String > map = gson.fromJson(str, new TypeToken<Map<String,String>>(){}.getType());
//		String userName = map.get("userName");
//		String password = map.get("password");
//		userService.checkPassword(userName, password);
		logger.info(userPageSize);
		return null;
	}
	
	@RequestMapping(value = "/test", method=RequestMethod.GET)
	public @ResponseBody String test(){
		logger.info(userPageSize);
		return null;
	}
}
