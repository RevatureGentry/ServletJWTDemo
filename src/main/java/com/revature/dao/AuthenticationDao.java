package com.revature.dao;

import com.revature.model.LoginForm;
import com.revature.model.UserDetails;

public interface AuthenticationDao {

	UserDetails attemptLogin(LoginForm form);
}
