package com.pucara.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Unit test for simple App.
 */
public class MysqlConnectionTest extends TestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(MysqlConnectionTest.class);

	public void testConnection() throws IOException {
		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/resources/properties/db.properties");
		// Load a properties file.
		prop.load(input);

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
				LOGGER.debug("Porduct: {}", tableName);
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
