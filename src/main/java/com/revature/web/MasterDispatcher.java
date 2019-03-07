package com.revature.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.service.TestService;
import com.revature.service.TestServiceImpl;

public class MasterDispatcher {

	private static final TestService service = new TestServiceImpl();
	
	private MasterDispatcher() {}
	
	public static Object process(HttpServletRequest request, HttpServletResponse response) {
		if (request.getRequestURI().contains("test"))
			return service.process(request, response);
		else 
			return "Not yet implemented";
	}
}
