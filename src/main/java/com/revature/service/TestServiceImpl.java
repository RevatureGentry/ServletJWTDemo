package com.revature.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServiceImpl implements TestService {

	private final TokenService tokenService = TokenServiceImpl.getInstance();
	
	@Override
	public Object process(HttpServletRequest request, HttpServletResponse response) {
		return tokenService.getTokenId(request.getHeader("Authorization"));
	}

}
