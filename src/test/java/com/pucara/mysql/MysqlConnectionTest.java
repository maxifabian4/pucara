package com.pucara.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.CommonData;
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
		PropertyFile prop = new PropertyFile(CommonData.DB_PROPERTIES_PATH);

		String dbUrl = prop.getProperty("jdbc.url");
		String dbClass = prop.getProperty("jdbc.class");
		String query = prop.getProperty("jdbc.query");
		String username = prop.getProperty("jdbc.username");
		String password = prop.getProperty("jdbc.password");

		try {
			Class.forName(dbClass);
			Connection connection = DriverManager.getConnection(dbUrl,
					username, password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				LOGGER.debug("Products: {}", resultSet.getString("description"));
			}

			LOGGER.debug("Clossing connection ...");
			connection.close();
			statement.close();
			resultSet.close();
		} catch (ClassNotFoundException cnfe) {
			LOGGER.debug("Class Not Found: {}", cnfe);
		} catch (SQLException se) {
			LOGGER.debug("SQL: {}", se);
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
	
	@Test
	public void extraTestw() {
		String[] namesArray = "".split(";");
		System.out.println(namesArray.length);
	}

}
