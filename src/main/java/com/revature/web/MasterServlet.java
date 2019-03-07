package com.revature.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MasterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final ObjectMapper mapper = new ObjectMapper();
       
    public MasterServlet() {
        super();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req, resp);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.getOutputStream().write(mapper.writeValueAsBytes(MasterDispatcher.process(request, response)));
	}

}
