package com.pucara.core.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.core.entities.Category;
import com.pucara.core.entities.Product;
import com.pucara.core.entities.ProductsCollection;
import com.pucara.core.response.CategoryResponse;
import com.pucara.core.response.DatabaseResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.ByIdResponse;
import com.pucara.core.response.StatementResponse;
import com.pucara.core.services.sale.SaleService;

/**
 * This class manages the main access to the database. All operation results are
 * retrieved based on a {@link com.pucara.core.response.Response} object.
 * 
 * @author Maximiliano Fabian
 */
public class MySqlAccess {
	private static final Logger LOGGER = LoggerFactory.getLogger(MySqlAccess.class);
	private static Connection mySqlConnect;
	private static Statement statement;
	private static ResultSet resultSet;

	/**
	 * Searches a category by an specific condition in the database.
	 * 
	 * @param condition
	 *            Condition to be considered.
	 * @return A category object wrapped in a Response.
	 */
	public static CategoryResponse findCategoryCondition(String condition) {
		try {
			// Result set get the result of the SQL query.
			resultSet = statement.executeQuery(String.format(
					"SELECT id, name, description FROM pucaratest.category %s", condition));
			resultSet.next();

			return new CategoryResponse(getCategory(resultSet));
		} catch (SQLException e) {
			return new CategoryResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			closeResultSet();
		}
	}

	/**
	 * Searches a product by single conditions.
	 * 
	 * @param barcode
	 * @return {@link ByIdResponse}
	 */
	public static ProductListResponse findProductByCondition(String whereCondition) {
		// Statements allow to issue SQL queries to the database
		try {
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery(String.format(
					"SELECT barcode, description, cost, percentage, date, stock, minstock, categoryid "
							+ "FROM pucaratest.product %s", whereCondition));
			return new ProductListResponse(getProductsList(resultSet));
		} catch (SQLException e) {
			return new ProductListResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					e.getMessage()));
		} finally {
			closeResultSet();
		}
	}

	/**
	 * Updates an existing category in the database by name.
	 * 
	 * @param oldName
	 * @param newName
	 * @param newDescription
	 * @return StatementResponse
	 */
	public static StatementResponse updateCategory(String oldName, String newName,
			String newDescription) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate("UPDATE pucaratest.category SET name = '"
					+ newName + "', description = '" + newDescription + "' WHERE name = '"
					+ oldName + "'");

			if (affectedRows == 1) {
				return new StatementResponse(affectedRows);
			} else {
				return new StatementResponse(new ErrorMessage(ErrorType.UPDATE_CATEGORY_ERROR,
						"No rows have been affected during the update."));
			}
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Updates an existing product in the database by barcode.
	 * 
	 * @param productToUpdate
	 * @return StatementResponse
	 */
	public static StatementResponse updateProduct(Product productToUpdate) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate(makeUpdateSentece(productToUpdate));

			if (affectedRows == 1) {
				return new StatementResponse(affectedRows);
			} else {
				return new StatementResponse(new ErrorMessage(ErrorType.UPDATE_PRODUCT_ERROR,
						"No rows have been affected during the update."));
			}
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Removes a category in the database by name.
	 * 
	 * @param name
	 * @return StatementResponse
	 */
	public static StatementResponse removeCategory(String name) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement
					.executeUpdate("DELETE FROM pucaratest.category WHERE name = '" + name + "'");

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param id
	 * @return StatementResponse
	 */
	public static StatementResponse removeSale(Long id) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate(String.format(
					"DELETE FROM %s.%s WHERE %s = '%s'", CommonData.DATABASE_NAME,
					CommonData.SALE_TABLE, CommonData.SALE_ID_COLUMN,
					CommonData.SALE_DETAIL_ID_COLUMN, id));

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param saleId
	 * @param saleDetailId
	 * @return StatementResponse
	 */
	public static StatementResponse removeSaleSaleDetail(Long saleId, Long saleDetailId) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate(String.format(
					"DELETE FROM %s.%s WHERE %s = '%s' AND %s = '%s'", CommonData.DATABASE_NAME,
					CommonData.X_SALE_SALE_DETAIL_TABLE, CommonData.SALE_ID_COLUMN,
					CommonData.SALE_DETAIL_ID_COLUMN, saleId, saleDetailId));

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param saleId
	 * @param saleDetailId
	 * @return StatementResponse
	 */
	public static StatementResponse removeSaleSaleDetailProduct(Long saleId, Long saleDetailId) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate(String.format(
					"DELETE FROM %s.%s WHERE %s = '%s' AND %s = '%s'", CommonData.DATABASE_NAME,
					CommonData.X_SALE_SALE_DETAIL_PRODUCT_TABLE, CommonData.SALE_ID_COLUMN,
					CommonData.SALE_DETAIL_ID_COLUMN, saleId, saleDetailId));

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Removes a product in the database by barcode.
	 * 
	 * @param name
	 * @return StatementResponse
	 */
	public static StatementResponse removeProduct(String barcode) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement
					.executeUpdate("DELETE FROM pucaratest.product WHERE barcode = '" + barcode
							+ "'");

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Creates a new product in the database.
	 * 
	 * @param product
	 * @return StatementResponse
	 */
	public static StatementResponse insertNewProduct(Product product) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate("INSERT INTO pucaratest.product VALUES('"
					+ product.getBarcode() + "', '" + product.getDescription() + "', '"
					+ product.getCost() + "', " + product.getPercentage() + ", '"
					+ product.getDate() + "', " + product.getStock() + ", " + product.getMinStock()
					+ ", " + product.getCategoryId() + ")");

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Creates a new category in the database.
	 * 
	 * @param category
	 * @return StatementResponse
	 */
	public static StatementResponse insertNewCategory(Category category) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			int affectedRows = statement
					.executeUpdate("INSERT INTO pucaratest.category (name, description) VALUES('"
							+ category.getName() + "', '" + category.getDescription() + "')");

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Establishes the connection with the database.
	 * 
	 * @return RespuestaBaseDeDatos
	 * @throws Exception
	 */
	public static DatabaseResponse establishConection() {
		boolean mysqlConnected = true;

		if (mysqlConnected) {
			boolean isConnected = connectDatabase();

			if (isConnected) {
				return new DatabaseResponse();
			} else {
				return new DatabaseResponse(new ErrorMessage(ErrorType.MYSQL_ERROR,
						"Error trying to connect ...."));
			}
		} else {
			return new DatabaseResponse(new ErrorMessage(ErrorType.MYSQL_ERROR,
					"No se ha levantado el proceso ...."));
		}
	}

	/**
	 * Allows close the current ResultSet.
	 */
	public static void closeResultSet() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				// statement.close();
			}

		} catch (Exception e) {

		}
	}

	/**
	 * Close the current connection.
	 */
	public static void closeMySqlConnect() {
		if (mySqlConnect != null) {
			try {
				mySqlConnect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns all gains/expenses associated to N days before.
	 * 
	 * @return ResultSet
	 */
	public static ResultSet getCharInformationFromSchema(String statement) {
		return performStatement(statement);
	}

	/**
	 * Returns all categories stored in the database.
	 * 
	 * @return ResultSet
	 */
	public static ResultSet getAllCategories() {
		return performStatement("SELECT id, name, description FROM pucaratest.category");
	}

	/**
	 * Returns all products stored in the database.
	 * 
	 * @return ResultSet
	 */
	public static ResultSet getAllProducts() {
		return performStatement("SELECT barcode, description, cost, percentage, date, stock, minstock, categoryid FROM pucaratest.product");
	}

	/**
	 * Returns all products stored in the database using view.
	 * 
	 * @return ResultSet
	 */
	public static ResultSet getAllProductsFromView() {
		return performStatement("SELECT * FROM product_view");
	}

	/**
	 * 
	 * @return ResultSet
	 */
	public static ResultSet getDailySaleReportFromView() {
		return performStatement("SELECT * FROM daily_report_view");
	}

	/**
	 * 
	 * @return
	 */
	public static ResultSet getDailyPurchaseReportFromView() {
		return performStatement("SELECT * FROM daily_purchase_report_view");
	}

	/**
	 * Stores a new purchase in the database. The following information is
	 * stored:
	 * <ul>
	 * <li>description
	 * <li>current date
	 * <li>expense value
	 * </ul>
	 * 
	 * @param description
	 *            Description of the purchase.
	 * @param currentDate
	 *            Expense value to be stored.
	 * @param expense
	 *            Expense value to be stored.
	 * @return An implementation of {@link com.pucara.core.response.Response}
	 *         saving identifier value.
	 */
	public static ByIdResponse addNewPurchase(String description, String currentDate, String expense) {
		try {
			// Result set gets the result of the SQL query.
			int rowCount = statement
					.executeUpdate(String
							.format("INSERT INTO pucaratest.purchase (description, date, expense) VALUES ('%s', '%s', '%s')",
									description, currentDate, expense));

			if (rowCount == 0) {
				LOGGER.debug(String.format("Error trying to insert a new purchase: [%s-%s-%s]",
						description, currentDate, expense));
				return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
						CommonMessageError.INSERTION_ERROR));
			}

			Long longValue = getLastInsert("purchase", "id");
			// TODO Add toString() method, from an update request object.
			LOGGER.info(String.format("New purchase has been created: %s, [%s-%s-%s]", longValue,
					description, currentDate, expense));

			return new ByIdResponse(longValue);
		} catch (SQLException e) {
			LOGGER.debug(String.format(
					"An exception has been fired trying to create a new purchase ... [%s,%s] - {}",
					description, expense), e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					CommonMessageError.INSERTION_ERROR));
		} finally {
			LOGGER.info("Result set closed");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param totalNumberOfProducts
	 * @return ByIdResponse
	 */
	public static ByIdResponse addNewPurchaseDetail(Integer totalNumberOfProducts) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			statement.executeUpdate(String.format(
					"INSERT INTO pucaratest.purchase_detail (number_of_products) VALUES ('%s')",
					totalNumberOfProducts));

			// Log
			Long longValue = getLastInsert("purchase_detail", "id");
			// CustomLogger.log(LoggerLevel.INFO,
			// String.format("New purchase detail [%s] has been created",
			// longValue));
			return new ByIdResponse(longValue);
		} catch (SQLException e) {
			// Log
			// CustomLogger.log(e, LoggerLevel.ERROR,
			// "An exception has been fired trying to create a new purchase detail ...");
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					CommonMessageError.INSERTION_ERROR));
		} finally {
			// Log
			// CustomLogger.log(LoggerLevel.INFO, "Closing result set ...");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param purchaseId
	 * @param purchaseDetailId
	 * @return ByIdResponse
	 */
	public static ByIdResponse addNxNPurchase(Long purchaseId, Long purchaseDetailId) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			statement
					.executeUpdate(String
							.format("INSERT INTO pucaratest.x_purchase_purchase_detail (purchase_id, purchase_detail_id) VALUES ('%s', '%s')",
									purchaseId, purchaseDetailId));

			// Log
			// CustomLogger.log(LoggerLevel.INFO,
			// "New NxN purchase has been created");
			return new ByIdResponse(CommonData.DEFAULT_LONG_IDENTIFIER);
		} catch (SQLException e) {
			// Log
			// CustomLogger
			// .log(e,
			// LoggerLevel.ERROR,
			// String.format(
			// "An exception has been fired trying to create a new NxN purchase [purchaseId %s - purchaseDetailId %s].",
			// purchaseId, purchaseDetailId));
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					CommonMessageError.INSERTION_ERROR));
		} finally {
			// Log
			// CustomLogger.log(LoggerLevel.INFO, "Closing result set ...");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Adds a new sale in the system.
	 * 
	 * @param currentDate
	 * @param gain
	 * @return ByIdResponse
	 */
	public static ByIdResponse addNewSale(String currentDate, double gain) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			statement.executeUpdate(String.format(
					"INSERT INTO pucaratest.sale (date, gain) VALUES ('%s', '" + gain + "')",
					currentDate));

			return new ByIdResponse(getLastInsert("sale", "id"));
		} catch (SQLException e) {
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR, "[sale] "
					+ e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Adds a new sale detail in the system.
	 * 
	 * @param totalNumberOfProducts
	 * @return ByIdResponse
	 */
	public static ByIdResponse addNewSaleDetail(Integer totalNumberOfProducts) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			statement.executeUpdate(String.format(
					"INSERT INTO pucaratest.sale_detail (number_of_products) VALUES ('%d')",
					totalNumberOfProducts));

			return new ByIdResponse(getLastInsert("sale_detail", "id"));
		} catch (SQLException e) {
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR, "[sale_detail] "
					+ e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Adds a detail for each sale.
	 * 
	 * @param saleId
	 * @param saleDetailId
	 * @return ByIdResponse
	 */
	public static ByIdResponse addNewSaleSaleDetail(Long saleId, Long saleDetailId) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			statement
					.executeUpdate(String
							.format("INSERT INTO pucaratest.x_sale_sale_detail (sale_id, sale_detail_id) VALUES (%d, %d)",
									saleId, saleDetailId));

			return new ByIdResponse(0L);
		} catch (SQLException e) {
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					"[x_sale_sale_detail] " + e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Adds each detail by product in the database.
	 * 
	 * @param products
	 * @param numberOfProducts
	 * @param saleId
	 * @param saleDetailId
	 * @return ByIdResponse
	 */
	public static ByIdResponse addNewSaleSaleDetailProduct(ProductsCollection products,
			Long saleId, Long saleDetailId) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			String barcode;

			for (int i = 0; i < products.getSize(); i++) {
				barcode = products.getProductAt(i).getBarcode();

				if (!SaleService.isExtraSale(barcode)) {
					// Result set get the result of the SQL query
					statement.executeUpdate(String.format(
							"INSERT INTO pucaratest.x_sale_sale_detail_product (sale_id, sale_detail_id, barcode, count) "
									+ "VALUES (%d, %d, '%s', %d)", saleId, saleDetailId, barcode,
							products.getQuantityOfProductAt(i)));
				}
			}

			return new ByIdResponse(0L);
		} catch (SQLException e) {
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					"[x_sale_sale_detail_product] " + e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param products
	 * @param purchaseId
	 * @param purchaseDetailId
	 * @return
	 */
	public static ByIdResponse addNewPurchasePurchaseDetailProduct(ProductsCollection products,
			Long purchaseId, Long purchaseDetailId) {
		try {
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();

			for (int i = 0; i < products.getSize(); i++) {
				// Result set get the result of the SQL query
				statement
						.executeUpdate(String
								.format("INSERT INTO pucaratest.x_purchase_purchase_detail_product (purchase_id, purchase_detail_id, barcode, count) "
										+ "VALUES (%d, %d, '%s', %d)", purchaseId,
										purchaseDetailId, products.getProductAt(i).getBarcode(),
										products.getQuantityOfProductAt(i)));
			}

			// Log
			// CustomLogger
			// .log(LoggerLevel.INFO,
			// "New PurchasePurchaseDetailProduct has been created");
			return new ByIdResponse(0L);
		} catch (SQLException e) {
			// Log
			// CustomLogger
			// .log(e,
			// LoggerLevel.ERROR,
			// String.format(
			// "An exception has been fired trying to create a new PurchasePurchaseDetailProduct [purchaseId %s - purchaseDetailId %s].",
			// purchaseId, purchaseDetailId));
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					"[x_purchase_purchase_detail_product] " + e.getMessage()));
		} finally {
			// Log
			// CustomLogger.log(LoggerLevel.INFO, "Closing result set ...");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Stops mysql process.
	 */
	public static void stopMySqlServer() {
		String[] command = { "/bin/sh", "-c", "echo pucara | sudo -S /opt/lampp/lampp stopmysql" };
		String out = executeCommand(command);
		// CustomLogger.log(nCull, LoggerLevel.INFO, out);
	}

	/**
	 * 
	 * @param products
	 * @return
	 */
	public static StatementResponse modifyProductStocks(ProductsCollection products,
			boolean increase) {
		try {
			int affectedRows = 0;
			// Statements allow to issue SQL queries to the database
			// statement = mySqlConnect.createStatement();
			String barcode;

			for (int i = 0; i < products.getSize(); i++) {
				barcode = products.getAllProducts().get(i).getBarcode();

				if (!SaleService.isExtraSale(barcode)) {
					if (increase) {
						// Result set get the result of the SQL query
						affectedRows += statement
								.executeUpdate(String
										.format("UPDATE pucaratest.product SET stock = stock + %d WHERE barcode = '%s'",
												products.getQuantityOfProductAt(i), products
														.getProductAt(i).getBarcode()));
					} else {
						affectedRows += statement
								.executeUpdate(String
										.format("UPDATE pucaratest.product SET stock = stock - %d WHERE barcode = '%s'",
												products.getQuantityOfProductAt(i), products
														.getProductAt(i).getBarcode()));
					}
				} else {
					affectedRows++;
				}
			}

			if (affectedRows > 0) {
				return new StatementResponse(affectedRows);
			} else {
				return new StatementResponse(new ErrorMessage(ErrorType.UPDATE_PRODUCT_ERROR,
						"No rows have been affected during the update."));
			}
		} catch (SQLException e) {
			return new StatementResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	public static Connection getConnection() {
		return mySqlConnect;
	}

	/**
	 * Allows execute a new statement.
	 * 
	 * @param stringStatement
	 * @return ResultSet
	 */
	private static ResultSet performStatement(String stringStatement) {
		try {
			// Statements allow to issue SQL queries to the database
			// //statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery(stringStatement);

			return resultSet;
		} catch (SQLException e) {
			LOGGER.error("SQL Exception: {}", e);
			return null;
		}
	}

	/**
	 * Makes the update sentence.
	 * 
	 * @param productToUpdate
	 * @return String
	 */
	private static String makeUpdateSentece(Product productToUpdate) {
		String sentence = null;

		if (productToUpdate.getDescription() != null) {
			sentence = String.format("UPDATE pucaratest.product SET description = '%s'",
					productToUpdate.getDescription());
		}

		if (productToUpdate.getCost() != null) {
			sentence = String.format(sentence + ", cost = '%s'", productToUpdate.getCost());
		}

		if (productToUpdate.getPercentage() != null) {
			sentence = String.format(sentence + ", percentage = '%d'",
					productToUpdate.getPercentage());
		}

		if (productToUpdate.getDate() != null) {
			sentence = String.format(sentence + ", date = '%s'", productToUpdate.getDate());
		}

		// if (productToUpdate.getStock() != null) {
		// sentence = String.format(sentence + ", stock = '%d'",
		// productToUpdate.getStock());
		// }

		if (productToUpdate.getMinStock() != null) {
			sentence = String.format(sentence + ", minstock = '%d'", productToUpdate.getMinStock());
		}

		sentence = String.format(sentence + " WHERE barcode = '%s'", productToUpdate.getBarcode());

		return sentence;
	}

	/**
	 * Initializes the MySql windows process.
	 * 
	 * @return boolean
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	// private static boolean startMySqlServer() {
	// String[] command = { "/bin/sh", "-c",
	// "echo pucara | sudo -S /opt/lampp/lampp startmysql" };
	// String out = executeCommand(command);
	// CustomLogger.log(null, LoggerLevel.INFO, out);
	//
	// try {
	// Thread.sleep(3000);
	//
	// return true;
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// return false;
	// }
	// }

	/**
	 * Returns a Product object based on a result set.
	 * 
	 * @param resultSet
	 * @return Product
	 * @throws SQLException
	 */
	private static List<Product> getProductsList(ResultSet resultSet) throws SQLException {
		List<Product> listOfProducts = new ArrayList<Product>();
		Product product;
		String barcode, description, date;
		double cost;
		int percentage, stock, minstock, categoryid;

		while (resultSet.next()) {
			barcode = resultSet.getString("barcode");
			description = resultSet.getString("description");
			cost = resultSet.getDouble("cost");
			percentage = resultSet.getInt("percentage");
			date = resultSet.getString("date");
			stock = resultSet.getInt("stock");
			minstock = resultSet.getInt("minstock");
			categoryid = resultSet.getInt("categoryid");

			product = new Product(barcode, description, cost, percentage, date, stock, minstock,
					categoryid);

			listOfProducts.add(product);
		}

		return listOfProducts;
	}

	/**
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private static Category getCategory(ResultSet resultSet) throws SQLException {
		Integer id = resultSet.getInt("id");
		String name = resultSet.getString("name");
		String description = resultSet.getString("description");

		return new Category(id, name, description);
	}

	/**
	 * Retrieves the last field stored in a specific table.
	 * 
	 * @param tableName
	 * @param columnName
	 * @return Long
	 * @throws SQLException
	 */
	private static Long getLastInsert(String tableName, String columnName) throws SQLException {
		ResultSet result = performStatement(String.format(
				"SELECT %s FROM pucaratest.%s ORDER BY %s DESC", columnName, tableName, columnName));
		result.next();

		return resultSet.getLong(columnName);
	}

	/**
	 * Execute a Linux command. TODO: REMOVE!!
	 * 
	 * @param command
	 * @return String
	 */
	private static String executeCommand(String[] command) {
		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			return output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Connects to the local database.
	 * 
	 * @return boolean
	 */
	private static boolean connectDatabase() {
		String dbURL = "jdbc:mysql://localhost:3306/pucaratest";
		String username = "root";
		String password = "pucara";

		mySqlConnect = null;
		statement = null;

		try {
			// getting database connection to MySQL server
			Class.forName("com.mysql.jdbc.Driver");
			mySqlConnect = DriverManager.getConnection(dbURL, username, password);
			statement = mySqlConnect.createStatement();

			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
