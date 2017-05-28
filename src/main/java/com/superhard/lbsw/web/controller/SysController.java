package com.superhard.lbsw.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/sys")
public class SysController {
	private static Logger logger = LogManager.getLogger(SysController.class); 
	@Value("#{configProperties['userPageSize']}")
	private String userPageSize; 
	//TODO
	@RequestMapping(value = "/getKey", method=RequestMethod.POST)
	public @ResponseBody String getKey(){
		logger.info(userPageSize);
		return null;
	}
	
	@RequestMapping(value = "/init", method=RequestMethod.POST)
	public @ResponseBody String init(@RequestParam String str ,@RequestHeader("W-Session-Id") String sid ){
		return null;
	}
}