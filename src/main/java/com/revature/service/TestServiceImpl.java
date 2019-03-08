package com.revature.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestServiceImpl implements TestService {

	private final TokenService tokenService = TokenServiceImpl.getInstance();
	private final Logger logger = LogManager.getLogger(getClass());
	
	@Override
	public Object process(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside TestServiceImpl");
		return tokenService.getUserDetailsFromToken(request.getHeader("Authorization"));
	}

}
