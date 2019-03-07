package com.revature.web;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationFilter implements Filter {

	private ObjectMapper mapper;
	
    public JwtAuthenticationFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		final String token = httpRequest.getHeader("Authorization");
		
		if (token == null) {
			response.resetBuffer();
			response.setContentType("application/json");
			response.getOutputStream().write(mapper.writeValueAsBytes(Collections.singletonMap("message","You must log in to view this resource")));
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		// pass the request along the filter chain
		chain.doFilter(httpRequest, httpResponse);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		mapper = new ObjectMapper();
	}

}
