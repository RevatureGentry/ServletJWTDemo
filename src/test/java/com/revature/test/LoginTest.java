package com.revature.test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.AuthenticationDao;
import com.revature.dao.AuthenticationDaoImpl;
import com.revature.data.BasicH2ConnectionPool;
import com.revature.data.ConnectionPool;
import com.revature.model.LoginForm;
import com.revature.model.UserDetails;

public class LoginTest {

	private static final Logger logger = LogManager.getLogger(LoginTest.class);
	private static final ConnectionPool connectionPool = BasicH2ConnectionPool.getInstance();
	private static final String CREATE_USER_DDL_STRING = "create table users(username varchar(255) primary key, password varchar(255) not null, email varchar(255), roles varchar(255))";
	private static final String INSERT_USER_DML_STRING = "insert into users values (?, ?, ?, ?)";
	private static final AuthenticationDao authDao = AuthenticationDaoImpl.getInstance();
	
	@BeforeClass
	public static void setUpDatabase() {
		try (Connection conn = connectionPool.getConnection()) {
			Statement stmt = conn.createStatement();
			if (!stmt.execute(CREATE_USER_DDL_STRING))
				logger.info("Table created!");
			
			PreparedStatement pStmt = conn.prepareStatement(INSERT_USER_DML_STRING);
			pStmt.setString(1, "william");
			pStmt.setString(2, "Password123!");
			pStmt.setString(3, "william@test.com");
			pStmt.setString(4, "ROLE_READ_ALL,ROLE_WRITE_RESTRICTED");
			
			assertEquals(1, pStmt.executeUpdate());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void loginTest() {
		final LoginForm form = new LoginForm();
		form.setUsername("william");
		form.setPassword("Password123!");
		UserDetails details = authDao.attemptLogin(form);
		assertEquals("william@test.com", details.getEmail());
		assertEquals(Arrays.asList("ROLE_READ_ALL", "ROLE_WRITE_RESTRICTED"), details.getRoles());
	}
}
