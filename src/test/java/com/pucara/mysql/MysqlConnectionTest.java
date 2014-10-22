package com.pucara.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.PropertyFile;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Unit test for simple App.
 */
public class MysqlConnectionTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MysqlConnectionTest.class);

	@Test
	public void testConnection() throws IOException {
		PropertyFile prop = new PropertyFile(
				"src/main/resources/properties/db.properties");

		String dbUrl = prop.getProperty("db.url");
		String dbClass = prop.getProperty("db.class");
		String query = prop.getProperty("db.query");
		String username = prop.getProperty("db.username");
		String password = prop.getProperty("db.password");

		try {
			Class.forName(dbClass);
			Connection connection = DriverManager.getConnection(dbUrl,
					username, password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			// while (resultSet.next()) {
			// String tableName = resultSet.getString(1);
			// LOGGER.debug("Products: {}", resultSet.getRow());
			// }

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

	// remove it
	public void extraTest() {
		double[] xx = { 0, 0.25, 0.5, 0.75, 1 };

		// Truncate in two decimals
		double x = 2.68433475346;
		int i = (int) x;
		x = x - i;

		BigDecimal truncated = new BigDecimal(String.valueOf(x)).setScale(2,
				BigDecimal.ROUND_UP);

		int cont = 0;
		while (cont < xx.length && truncated.doubleValue() > xx[cont]) {
			cont++;
		}

		System.out.println(i + xx[cont]);
	}

}
