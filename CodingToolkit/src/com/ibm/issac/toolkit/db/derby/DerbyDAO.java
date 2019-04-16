package com.ibm.issac.toolkit.db.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.issac.toolkit.DevLog;

public class DerbyDAO {
	private Connection conn = null;
	
	public DerbyDAO(String dbUrl) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		this.init(dbUrl);
	}

	public void init(String dbUrl) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		conn = DriverManager.getConnection(dbUrl);
		DevLog.trace("[DerbyDAO] database connection initiated for url " + dbUrl);
	}

	protected Statement createStmt() throws SQLException {
		return conn.createStatement();
	}

	protected PreparedStatement prepareStmt(String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}

}
