package com.revature.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.dao.AuthenticationDao;
import com.revature.dao.AuthenticationDaoImpl;
import com.revature.model.LoginForm;
import com.revature.model.UserDetails;

public class LoginServiceImpl implements LoginService {
	
	private final TokenService service = TokenServiceImpl.getInstance();
	private final AuthenticationDao dao = AuthenticationDaoImpl.getInstance();

	@Override
	public String attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		final LoginForm form = new LoginForm(request.getParameter("username"), request.getParameter("password"));
		final UserDetails details = dao.attemptLogin(form);
		if (details != null) {
			return service.generateToken(details);
		}
		return null;
	}

}
