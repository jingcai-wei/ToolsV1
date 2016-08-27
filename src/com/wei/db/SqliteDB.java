package com.wei.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wei.tools.Tools;

public abstract class SqliteDB {
	
	public static String DB_DIR = "db/";
	
	private String name;
	
	public SqliteDB(String name) {
		this.name = name;
		
		if (name == null 
				|| name.trim().length() <= 0) {
			throw new NullPointerException();
		}
		
		initDB();
	}
	
	private void initDB() {
		
		File dbDir = new File(DB_DIR);
		if (!dbDir.isDirectory()) {	
			dbDir.mkdirs();
		}
		
		File dbFile = new File(dbDir, name);
		if (!dbFile.isFile()) {
			Connection conn = null;
			Statement statement = null;
			try {
				conn = getConnection();
				statement = conn.createStatement();
				create(statement);
			} catch (ClassNotFoundException e) {
				Tools.log.error("ClassNotFoundException", e);
			} catch (SQLException e) {
				Tools.log.error("SQLException", e);
			} finally {
				closeStatement(statement);
				closeConnection(conn);
			}
		}
	}
	
	/**
	 * 获取数据库连接
	 * @return 返回相应的数据库连接
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("org.sqlite.JDBC");
		/* 连接相应的数据库 */
		Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_DIR + name);
		
		return conn;	/* 返回相应的数据库连接 */
	}
	
	/**
	 * @return 返回数据库文件
	 */
	public File getDBFile() {
		return new File(DB_DIR + name);
	}
	
	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				Tools.log.error("SQLException", e);
			}
		}
	}
	
	public void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				Tools.log.error("SQLException", e);
			}
		}
	}
	
	public void closeStatement(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				Tools.log.error("SQLException", e);
			}
		}
	}
	
	public void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				Tools.log.error("SQLException", e);
			}
		}
	}
	
	public abstract void create(Statement statement);
}
