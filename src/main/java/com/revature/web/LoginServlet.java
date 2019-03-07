package com.revature.web;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.service.LoginService;
import com.revature.service.LoginServiceImpl;

public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private LoginService service;
	private ObjectMapper mapper;
	 
	
	@Override
	public void init() throws ServletException {
		service = new LoginServiceImpl();
		mapper = new ObjectMapper();
	}
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String token = service.attemptAuthentication(request, response);
		if (token == null) {
			response.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("message", "Invalid Credentials")));
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		response.addHeader("Authorization", "Bearer " + token);
	}

}
