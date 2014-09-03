package com.pucara.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class MysqlConnectionTest extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConnectionTest.class);

	public void testConnection() {
		String dbUrl = "jdbc:mysql://192.168.1.69:3306/mysql";
		String dbClass = "com.mysql.jdbc.Driver";
		String query = "select distinct(table_name) from INFORMATION_SCHEMA.TABLES";
		String username = "root";
		String password = "pucara";

		try {
			Class.forName(dbClass);
			Connection connection = DriverManager.getConnection(dbUrl, username, password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String tableName = resultSet.getString(1);
				System.out.println("Table name : " + tableName);
			}

			LOGGER.info("All table have been displayed! {}", true);
			LOGGER.error("Error!");
			LOGGER.debug("Debug!");
			connection.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
