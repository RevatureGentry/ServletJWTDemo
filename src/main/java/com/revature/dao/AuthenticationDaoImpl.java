package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.data.BasicH2ConnectionPool;
import com.revature.data.ConnectionPool;
import com.revature.model.AuthenticatedUser;
import com.revature.model.LoginForm;
import com.revature.model.UserDetails;

public class AuthenticationDaoImpl implements AuthenticationDao {

	private static final AuthenticationDao instance = new AuthenticationDaoImpl();
	
	private final Logger logger = LogManager.getLogger(getClass());
	private final ConnectionPool connectionPool = BasicH2ConnectionPool.getInstance();
	
	private AuthenticationDaoImpl() {}
	
	public static AuthenticationDao getInstance() {
		return instance;
	}
	
	@Override
	public UserDetails attemptLogin(LoginForm form) {
		logger.info("Attempting to authenticate user {} at {}", form.getUsername(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
		AuthenticatedUser user = null;
		try (Connection conn = connectionPool.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("select * from users where username = ? AND password = ?");
			stmt.setString(1, form.getUsername());
			stmt.setString(2, form.getPassword());
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				user = new AuthenticatedUser(rs.getString("username"), rs.getString("email"), Arrays.stream(rs.getString("roles").split(",")).collect(Collectors.toList()));
		} catch (SQLException e) {
			logger.error("SQL Exception caught: {}", e.getLocalizedMessage());
		}
		return user;
	}

}
