package com.pucara.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.PropertyFile;

import junit.framework.TestCase;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class MysqlConnectionTest extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConnectionTest.class);

	public void testConnection() throws IOException {
		PropertyFile prop = new PropertyFile("src/main/resources/properties/db.properties");

		String dbUrl = prop.getProperty("db.url");
		String dbClass = prop.getProperty("db.class");
		String query = prop.getProperty("db.query");
		String username = prop.getProperty("db.username");
		String password = prop.getProperty("db.password");

		try {
			Class.forName(dbClass);
			Connection connection = DriverManager.getConnection(dbUrl, username, password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				String tableName = resultSet.getString(1);
				LOGGER.debug("Product: {}", tableName);
			}

			LOGGER.debug("Clossing connection ...");
			connection.close();
			statement.close();
			resultSet.close();
		} catch (ClassNotFoundException cnfe) {
			LOGGER.debug("Class Not Found: {}", cnfe.getMessage());
		} catch (SQLException se) {
			LOGGER.debug("SQL: {}", se.getMessage());
		}
	}
}
