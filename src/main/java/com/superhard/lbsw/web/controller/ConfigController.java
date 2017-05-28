package com.superhard.lbsw.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class ConfigController {
	@Value("#{configProperties['userPageSize']}")
	private String userPageSize; 
}
