package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

public class BasicH2ConnectionPool implements ConnectionPool {

	private final Logger logger = LogManager.getLogger(getClass());
	private final JdbcConnectionPool connectionPool;
	private static BasicH2ConnectionPool instance;
	
	private BasicH2ConnectionPool() {
		// Create connection pool
		this.connectionPool = JdbcConnectionPool.create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
		this.connectionPool.setMaxConnections(50);
		
		// Prepopulate with test data
		try (Connection conn = connectionPool.getConnection()) {
			Statement stmt = conn.createStatement();
			if (!stmt.execute("create table users(username varchar(255) primary key, password varchar(255) not null, email varchar(255), roles varchar(255))"))
				logger.info("Table created!");
			
			PreparedStatement pStmt = conn.prepareStatement("insert into users values (?, ?, ?, ?)");
			pStmt.setString(1, "william");
			pStmt.setString(2, "Password123!");
			pStmt.setString(3, "william@test.com");
			pStmt.setString(4, "ROLE_READ_ALL,ROLE_WRITE_RESTRICTED");
			
			if (pStmt.executeUpdate() == 1)
				logger.info("User added!");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ConnectionPool getInstance() {
		if (instance == null)
			instance = new BasicH2ConnectionPool();
		return instance;
	}
	
	@Override
	public Connection getConnection() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void shutdown() {
		this.connectionPool.dispose();
		instance = null;
	}
	
}
