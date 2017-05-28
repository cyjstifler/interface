package com.superhard.lbsw.service.impl;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.superhard.lbsw.dao.UserInfoMapper;
import com.superhard.lbsw.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService{
	private final static Logger logger = LogManager.getLogger(UserServiceImpl.class);
	private Gson gson = new Gson();
	private static final  double CONSTANT_VALUE = 360/(Math.PI*6378.137);
	@Resource
	UserInfoMapper userDao;
//	@Override
	public boolean checkPassword(String userName, String password) {
		logger.info("enter to check user name and password");
		// TODO 
		userDao.selectByUserName(userName);
		return false;
	}

}
