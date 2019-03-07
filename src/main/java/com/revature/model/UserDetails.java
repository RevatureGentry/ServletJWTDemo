package com.revature.model;

import java.util.List;

public interface UserDetails {

	String getUsername();
	String getEmail();
	List<String> getRoles();
}
