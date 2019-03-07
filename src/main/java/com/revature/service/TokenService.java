package com.revature.service;

import com.revature.model.UserDetails;

public interface TokenService {

	String generateToken(UserDetails details);
	boolean validateToken(String token);
	UserDetails getUserDetailsFromToken(String token);
	String getTokenId(String token);
}
