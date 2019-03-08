package com.revature.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.AuthenticationDao;
import com.revature.dao.AuthenticationDaoImpl;
import com.revature.model.LoginForm;
import com.revature.model.UserDetails;

public class LoginServiceImpl implements LoginService {
	
	private final TokenService service = TokenServiceImpl.getInstance();
	private final AuthenticationDao dao = AuthenticationDaoImpl.getInstance();
	private final ObjectMapper mapper = new ObjectMapper();
	private final Logger logger = LogManager.getLogger(getClass());

	@Override
	public String attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		logger.info("Inside LoginServiceImpl");
		LoginForm form = null;
		try {
			form = mapper.readValue(request.getInputStream(), LoginForm.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		final LoginForm form = new LoginForm(request.getParameter("username"), request.getParameter("password"));
		final UserDetails details = dao.attemptLogin(form);
		if (details != null) {
			logger.info("Authentication success! Return token");
			return service.generateToken(details);
		}
		return null;
	}

}
