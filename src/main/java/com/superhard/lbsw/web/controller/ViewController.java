package com.superhard.lbsw.web.controller;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.gson.Gson;

@Controller
@RequestMapping("/view")	
public class ViewController {

		
	private static Logger logger = LogManager.getLogger(SysController.class); 
	private Gson gson = new Gson();
	@Value("#{configProperties['userPageSize']}")
	private String userPageSize; 
	
	@RequestMapping(value = "/test", method=RequestMethod.GET)
	public String test(Model model){
		model.addAttribute("foo", "bar");
		model.addAttribute("fruit", "apple");
		logger.info(userPageSize);
		return "views/webSocket";
	}
	@RequestMapping(value = "/home", method=RequestMethod.GET)
	public String home(){
		logger.info("home");
		return "home";
	}
}
