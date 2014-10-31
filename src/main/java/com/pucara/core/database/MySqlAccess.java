package com.pucara.core.database;

import java.io.IOException;
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
import com.pucara.common.PropertyFile;
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
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MySqlAccess.class);
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
					"SELECT id, name, description FROM %s.category %s",
					getPropertyFromFile("db.database"), condition));
			resultSet.next();

			return new CategoryResponse(getCategory(resultSet));
		} catch (SQLException e) {
			return new CategoryResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
	public static ProductListResponse findProductByCondition(
			String whereCondition) {
		// Statements allow to issue SQL queries to the database
		try {
			// statement = mySqlConnect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery(String
							.format("SELECT barcode, description, initialcost, finalcost, percentage, date, stock, minstock, categoryid, bypercentage "
									+ "FROM %s.product %s",
									getPropertyFromFile("db.database"),
									whereCondition));
			return new ProductListResponse(getProductsList(resultSet));
		} catch (SQLException e) {
			return new ProductListResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
	public static StatementResponse updateCategory(String oldName,
			String newName, String newDescription) {
		try {
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate("UPDATE "
					+ getPropertyFromFile("db.database")
					+ ".category SET name = '" + newName + "', description = '"
					+ newDescription + "' WHERE name = '" + oldName + "'");

			if (affectedRows == 1) {
				return new StatementResponse(affectedRows);
			} else {
				return new StatementResponse(new ErrorMessage(
						ErrorType.UPDATE_CATEGORY_ERROR,
						"No rows have been affected during the update."));
			}
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
			// Result set get the result of the SQL query
			int affectedRows = statement
					.executeUpdate(makeUpdateSentece(productToUpdate));

			if (affectedRows == 1) {
				LOGGER.info(
						"Existing product has been succesfully updated. Barcode {} Description '{}'",
						productToUpdate.getBarcode(),
						productToUpdate.getDescription());
				return new StatementResponse(affectedRows);
			} else {
				LOGGER.error("No rows have been affected during the update.",
						productToUpdate.getBarcode(),
						productToUpdate.getDescription());
				return new StatementResponse(
						new ErrorMessage(
								ErrorType.UPDATE_PRODUCT_ERROR,
								String.format(
										"Error trantando de actualizar el producto [%s] con descripcion [%s].",
										productToUpdate.getBarcode(),
										productToUpdate.getDescription())));
			}
		} catch (SQLException e) {
			LOGGER.error("SQL exception. {}", e.getMessage());
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			LOGGER.info("Result set closed");
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
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate("DELETE FROM "
					+ getPropertyFromFile("db.database")
					+ ".category WHERE name = '" + name + "'");

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate(String.format(
					"DELETE FROM %s.%s WHERE %s = '%s'",
					getPropertyFromFile("db.database"), CommonData.SALE_TABLE,
					CommonData.SALE_ID_COLUMN,
					CommonData.SALE_DETAIL_ID_COLUMN, id));

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
	public static StatementResponse removeSaleSaleDetail(Long saleId,
			Long saleDetailId) {
		try {
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate(String.format(
					"DELETE FROM %s.%s WHERE %s = '%s' AND %s = '%s'",
					getPropertyFromFile("db.database"),
					CommonData.X_SALE_SALE_DETAIL_TABLE,
					CommonData.SALE_ID_COLUMN,
					CommonData.SALE_DETAIL_ID_COLUMN, saleId, saleDetailId));

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
	public static StatementResponse removeSaleSaleDetailProduct(Long saleId,
			Long saleDetailId) {
		try {
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate(String.format(
					"DELETE FROM %s.%s WHERE %s = '%s' AND %s = '%s'",
					getPropertyFromFile("db.database"),
					CommonData.X_SALE_SALE_DETAIL_PRODUCT_TABLE,
					CommonData.SALE_ID_COLUMN,
					CommonData.SALE_DETAIL_ID_COLUMN, saleId, saleDetailId));

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate("DELETE FROM "
					+ getPropertyFromFile("db.database")
					+ ".product WHERE barcode = '" + barcode + "'");

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
			int byPercentage = 0;
			if (product.getByPercentage()) {
				byPercentage = 1;
			}

			// Result set get the result of the SQL query
			int rowCount = statement.executeUpdate("INSERT INTO "
					+ getPropertyFromFile("db.database") + ".product VALUES('"
					+ product.getBarcode() + "', '" + product.getDescription()
					+ "', '" + product.getInitialCost() + "', '"
					+ product.getFinalCost() + "', '" + product.getPercentage()
					+ "', '" + product.getDate() + "', " + product.getStock()
					+ ", " + product.getMinStock() + ", "
					+ product.getCategoryId() + ", " + byPercentage + ")");

			LOGGER.info(
					"A new product has been added. Barcode {} Description '{}'",
					product.getBarcode(), product.getDescription());
			return new StatementResponse(rowCount);
		} catch (SQLException e) {
			LOGGER.error("SQL exception. {}", e.getMessage());
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			LOGGER.info("Result set closed");
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
			// Result set get the result of the SQL query
			int affectedRows = statement.executeUpdate("INSERT INTO "
					+ getPropertyFromFile("db.database")
					+ ".category (name, description) VALUES('"
					+ category.getName() + "', '" + category.getDescription()
					+ "')");

			return new StatementResponse(affectedRows);
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
			boolean isConnected = false;

			try {
				isConnected = connectDatabase();
			} catch (IOException e) {
				LOGGER.error("Error trying to connect to the database.");
				return new DatabaseResponse(new ErrorMessage(
						ErrorType.MYSQL_ERROR,
						"Error al conectar con la base de datos."));
			}

			if (isConnected) {
				return new DatabaseResponse();
			} else {
				return new DatabaseResponse(new ErrorMessage(
						ErrorType.MYSQL_ERROR, "Error trying to connect."));
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
		return performStatement("SELECT id, name, description FROM "
				+ getPropertyFromFile("db.database") + ".category");
	}

	/**
	 * Returns all products stored in the database.
	 * 
	 * @return ResultSet
	 */
	public static ResultSet getAllProducts() {
		return performStatement("SELECT barcode, description, initialcost, finalcost, percentage, date, stock, minstock, categoryid, bypercentage FROM "
				+ getPropertyFromFile("db.database")
				+ ".product "
				+ "ORDER BY  `product`.`description` ASC");
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
	 * Should be removed ... remove view from db too
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
	public static ByIdResponse addNewPurchase(String description,
			String currentDate, String expense) {
		try {
			// Result set gets the result of the SQL query.
			int rowCount = statement
					.executeUpdate(String
							.format("INSERT INTO %s.purchase (description, date, expense) VALUES ('%s', '%s', '%s')",
									getPropertyFromFile("db.database"),
									description, currentDate, expense));

			if (rowCount == 0) {
				LOGGER.error(String.format(
						"Error trying to insert a new purchase: [%s,%s,%s]",
						description, currentDate, expense));
				return new ByIdResponse(new ErrorMessage(
						ErrorType.STATEMENT_ERROR,
						CommonMessageError.INSERTION_ERROR));
			}

			Long longValue = getLastInsert("purchase", "id");
			// TODO Add toString() method, from an update request object.
			LOGGER.info("New purchase has been created: {} - {},{},{}",
					new String[] { longValue.toString(), description,
							currentDate, expense });
			return new ByIdResponse(longValue);
		} catch (SQLException e) {
			LOGGER.error(
					String.format(
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
	 * Stores the number of products associated to a new purchase.
	 * 
	 * @param number
	 *            Total number of products.
	 * @return An implementation of {@link com.pucara.core.response.Response}
	 *         saving identifier value.
	 */
	public static ByIdResponse addNewPurchaseDetail(Integer number) {
		try {
			// Result set get the result of the SQL query.
			int rowCount = statement
					.executeUpdate(String
							.format("INSERT INTO %s.purchase_detail (number_of_products) VALUES ('%s')",
									getPropertyFromFile("db.database"), number));

			if (rowCount == 0) {
				LOGGER.error("Error trying to insert a new purchase detail.");
				return new ByIdResponse(new ErrorMessage(
						ErrorType.STATEMENT_ERROR,
						CommonMessageError.INSERTION_ERROR));
			}

			Long longValue = getLastInsert("purchase_detail", "id");
			LOGGER.info(
					"Purchase detail {} with {} products has been created.",
					longValue, number);
			return new ByIdResponse(longValue);
		} catch (SQLException e) {
			LOGGER.error(
					"An exception has been fired trying to create a new purchase detail. {}",
					e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					CommonMessageError.INSERTION_ERROR));
		} finally {
			LOGGER.info("Result set closed");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Stores the relationship between a purchase an their detail.
	 * 
	 * @param purchaseId
	 *            Purchase identifier value.
	 * @param purchaseDetailId
	 *            Purchase detail identifier value.
	 * @return An implementation of {@link com.pucara.core.response.Response}
	 *         saving identifier value.
	 */
	public static ByIdResponse addNxNPurchase(Long purchaseId,
			Long purchaseDetailId) {
		try {
			// Result set get the result of the SQL query.
			int rowCount = statement
					.executeUpdate(String
							.format("INSERT INTO %s.x_purchase_purchase_detail (purchase_id, purchase_detail_id) VALUES ('%s', '%s')",
									getPropertyFromFile("db.database"),
									purchaseId, purchaseDetailId));

			if (rowCount == 0) {
				LOGGER.error(
						"Error trying to insert a new purchase detail. Purchase Id: {} - Purchase Detail Id: {}",
						purchaseId, purchaseDetailId);
				return new ByIdResponse(new ErrorMessage(
						ErrorType.STATEMENT_ERROR,
						CommonMessageError.INSERTION_ERROR));
			}

			LOGGER.info(
					"Purchase information {}, has been stored with Purchase Detail information {}.",
					purchaseId, purchaseDetailId);
			return new ByIdResponse(CommonData.DEFAULT_LONG_IDENTIFIER);
		} catch (SQLException e) {
			LOGGER.error(
					"An exception has been fired trying to create a new purchase detail. {}",
					e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					CommonMessageError.INSERTION_ERROR));
		} finally {
			LOGGER.info("Result set closed");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Stores all the information regarding a new purchase and all products
	 * associated. If a simple purchase is created (without products), just a
	 * new purchase is stored.
	 * 
	 * @param products
	 *            Collection of products to be stored.
	 * @param purchaseId
	 *            Purchase identifier value.
	 * @param purchaseDetailId
	 *            Purchase detail identifier value.
	 * @return An implementation of {@link com.pucara.core.response.Response}
	 *         saving identifier value.
	 */
	public static ByIdResponse addNewPurchasePurchaseDetailProduct(
			ProductsCollection products, Long purchaseId, Long purchaseDetailId) {
		try {
			int rowCount = 0;

			for (int i = 0; i < products.getSize(); i++) {
				// Result set get the result of the SQL query.
				rowCount += statement
						.executeUpdate(String
								.format("INSERT INTO %s.x_purchase_purchase_detail_product (purchase_id, purchase_detail_id, barcode, count) "
										+ "VALUES (%d, %d, '%s', %d)",
										getPropertyFromFile("db.database"),
										purchaseId, purchaseDetailId, products
												.getProductAt(i).getBarcode(),
										products.getQuantityOfProductAt(i)));
			}

			if (rowCount < products.getSize()) {
				LOGGER.error("No all products has been stored.");
			}

			LOGGER.info(
					"New PurchasePurchaseDetailProduct has been created. Purchase Id: {} - Purchase Detail Id: {}",
					purchaseId, purchaseDetailId);
			return new ByIdResponse(CommonData.DEFAULT_LONG_IDENTIFIER);
		} catch (SQLException e) {
			LOGGER.error(
					"An exception has been fired trying to create a new purchase detail. {}",
					e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					CommonMessageError.INSERTION_ERROR));
		} finally {
			LOGGER.info("Result set closed");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Adds a new sale in the system.
	 * 
	 * @param currentDate
	 *            Current date for the sale.
	 * @param gain
	 *            Total gain of a sale.
	 * @return An implementation of {@link com.pucara.core.response.Response}
	 *         saving identifier value.
	 */
	public static ByIdResponse addNewSale(String currentDate, double gain) {
		try {
			// Result set get the result of the SQL query.
			int rowCount = statement.executeUpdate(String.format(
					"INSERT INTO %s.sale (date, gain) VALUES ('%s', '" + gain
							+ "')", getPropertyFromFile("db.database"),
					currentDate));

			if (rowCount == 0) {
				LOGGER.error(
						"Error trying to insert a new sale. Current date: {} - Gain: {}",
						currentDate, gain);
				return new ByIdResponse(new ErrorMessage(
						ErrorType.STATEMENT_ERROR,
						CommonMessageError.INSERTION_ERROR));
			}

			Long longValue = getLastInsert("sale", "id");
			LOGGER.info("New sale has been created: {} - {},{}", new String[] {
					longValue.toString(), currentDate, String.valueOf(gain) });
			return new ByIdResponse(longValue);
		} catch (SQLException e) {
			LOGGER.error(
					"An exception has been fired trying to create a new sale. {}",
					e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					"[sale] " + e.getMessage()));
		} finally {
			LOGGER.info("Result set closed");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Adds a new sale detail in the system.
	 * 
	 * @param totalNumberOfProducts
	 * @return ByIdResponse
	 */
	public static ByIdResponse addNewSaleDetail(Integer number) {
		try {
			// Result set get the result of the SQL query.
			int rowCount = statement
					.executeUpdate(String
							.format("INSERT INTO %s.sale_detail (number_of_products) VALUES ('%d')",
									getPropertyFromFile("db.database"), number));

			if (rowCount == 0) {
				LOGGER.error("Error trying to insert a new sale detail.");
				return new ByIdResponse(new ErrorMessage(
						ErrorType.STATEMENT_ERROR,
						CommonMessageError.INSERTION_ERROR));
			}

			Long longValue = getLastInsert("sale_detail", "id");
			LOGGER.info("Sale detail {} with {} products has been created.",
					longValue, number);
			return new ByIdResponse(longValue);
		} catch (SQLException e) {
			LOGGER.error(
					"An exception has been fired trying to create a new sale detail. {}",
					e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					"[sale_detail] " + e.getMessage()));
		} finally {
			LOGGER.info("Result set closed");
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
	public static ByIdResponse addNewSaleSaleDetail(Long saleId,
			Long saleDetailId) {
		try {
			// Result set get the result of the SQL query.
			int rowCount = statement
					.executeUpdate(String
							.format("INSERT INTO %s.x_sale_sale_detail (sale_id, sale_detail_id) VALUES (%d, %d)",
									getPropertyFromFile("db.database"), saleId,
									saleDetailId));

			if (rowCount == 0) {
				LOGGER.error("Error trying to insert a new sale detail.");
				return new ByIdResponse(new ErrorMessage(
						ErrorType.STATEMENT_ERROR,
						CommonMessageError.INSERTION_ERROR));
			}

			LOGGER.info(
					"Sale information {}, has been stored with Sale Detail information {}.",
					saleId, saleDetailId);
			return new ByIdResponse(CommonData.DEFAULT_LONG_IDENTIFIER);
		} catch (SQLException e) {
			LOGGER.error(
					"An exception has been fired trying to create a new sale detail. {}",
					e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					"[x_sale_sale_detail] " + e.getMessage()));
		} finally {
			LOGGER.info("Result set closed");
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
	public static ByIdResponse addNewSaleSaleDetailProduct(
			ProductsCollection products, Long saleId, Long saleDetailId) {
		try {
			String barcode;
			int rowCount = 0;

			for (int i = 0; i < products.getSize(); i++) {
				barcode = products.getProductAt(i).getBarcode();

				if (!SaleService.isExtraSale(barcode)) {
					// Result set get the result of the SQL query
					rowCount += statement
							.executeUpdate(String
									.format("INSERT INTO %s.x_sale_sale_detail_product (sale_id, sale_detail_id, barcode, count) "
											+ "VALUES (%d, %d, '%s', %d)",
											getPropertyFromFile("db.database"),
											saleId, saleDetailId, barcode,
											products.getQuantityOfProductAt(i)));
				}
			}

			if (rowCount < products.getSize()) {
				LOGGER.error("No all products has been stored.");
			}

			LOGGER.info(
					"New SaleSaleDetailProduct has been created. Sale Id: {} - Sale Detail Id: {}",
					saleId, saleDetailId);
			return new ByIdResponse(CommonData.DEFAULT_LONG_IDENTIFIER);
		} catch (SQLException e) {
			LOGGER.error(
					"An exception has been fired trying to create a new sale detail. {}",
					e.getMessage());
			return new ByIdResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					"[x_sale_sale_detail_product] " + e.getMessage()));
		} finally {
			LOGGER.info("Result set closed");
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param products
	 * @return
	 */
	public static StatementResponse modifyProductStocks(
			ProductsCollection products, boolean increase) {
		try {
			int affectedRows = 0;
			String barcode;

			for (int i = 0; i < products.getSize(); i++) {
				barcode = products.getAllProducts().get(i).getBarcode();

				if (!SaleService.isExtraSale(barcode)) {
					if (increase) {
						// Result set get the result of the SQL query
						affectedRows += statement
								.executeUpdate(String
										.format("UPDATE %s.product SET stock = stock + %d WHERE barcode = '%s'",
												getPropertyFromFile("db.database"),
												products.getQuantityOfProductAt(i),
												products.getProductAt(i)
														.getBarcode()));
					} else {
						affectedRows += statement
								.executeUpdate(String
										.format("UPDATE %s.product SET stock = stock - %d WHERE barcode = '%s'",
												getPropertyFromFile("db.database"),
												products.getQuantityOfProductAt(i),
												products.getProductAt(i)
														.getBarcode()));
					}
				} else {
					affectedRows++;
				}
			}

			if (affectedRows > 0) {
				return new StatementResponse(affectedRows);
			} else {
				return new StatementResponse(new ErrorMessage(
						ErrorType.UPDATE_PRODUCT_ERROR,
						"No rows have been affected during the update."));
			}
		} catch (SQLException e) {
			return new StatementResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
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
			sentence = String.format(
					"UPDATE %s.product SET description = '%s'",
					getPropertyFromFile("db.database"),
					productToUpdate.getDescription());
		}

		if (productToUpdate.getInitialCost() != null) {
			sentence = String.format(sentence + ", initialcost = '%s'",
					productToUpdate.getInitialCost());
		}

		if (productToUpdate.getFinalCost() != null) {
			sentence = String.format(sentence + ", finalcost = '%s'",
					productToUpdate.getFinalCost());
		}

		if (productToUpdate.getPercentage() != null) {
			sentence = String.format(sentence + ", percentage = '%s'",
					productToUpdate.getPercentage());
		}

		if (productToUpdate.getDate() != null) {
			sentence = String.format(sentence + ", date = '%s'",
					productToUpdate.getDate());
		}

		if (productToUpdate.getMinStock() != null) {
			sentence = String.format(sentence + ", minstock = '%d'",
					productToUpdate.getMinStock());
		}

		sentence = String.format(sentence + " WHERE barcode = '%s'",
				productToUpdate.getBarcode());

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
	private static List<Product> getProductsList(ResultSet resultSet)
			throws SQLException {
		List<Product> listOfProducts = new ArrayList<Product>();
		Product product;
		String barcode, description, date;
		double initialCost, finalCost, percentage;
		int stock, minstock, categoryid;
		boolean byPercentage;

		while (resultSet.next()) {
			barcode = resultSet.getString("barcode");
			description = resultSet.getString("description");
			initialCost = resultSet.getDouble("initialcost");
			finalCost = resultSet.getDouble("finalcost");
			percentage = resultSet.getDouble("percentage");
			date = resultSet.getString("date");
			stock = resultSet.getInt("stock");
			minstock = resultSet.getInt("minstock");
			categoryid = resultSet.getInt("categoryid");

			if (resultSet.getInt("bypercentage") == 1) {
				byPercentage = true;
			} else {
				byPercentage = false;
			}

			product = new Product(barcode, description, initialCost, finalCost,
					percentage, date, stock, minstock, categoryid, byPercentage);

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
	private static Category getCategory(ResultSet resultSet)
			throws SQLException {
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
	private static Long getLastInsert(String tableName, String columnName)
			throws SQLException {
		ResultSet result = performStatement(String.format(
				"SELECT %s FROM %s.%s ORDER BY %s DESC", columnName,
				getPropertyFromFile("db.database"), tableName, columnName));
		result.next();

		return resultSet.getLong(columnName);
	}

	/**
	 * Connects to the local database.
	 * 
	 * @return boolean
	 * @throws IOException
	 */
	private static boolean connectDatabase() throws IOException {
		String dbUrl = getPropertyFromFile("db.url");
		// String dbClass = getPropertyFromFile("db.class");
		String username = getPropertyFromFile("db.username");
		String password = getPropertyFromFile("db.password");

		mySqlConnect = null;
		statement = null;

		try {
			// Class.forName(dbClass);
			mySqlConnect = DriverManager.getConnection(dbUrl, username,
					password);
			statement = mySqlConnect.createStatement();

			return true;
		}
		// catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// return false;
		// }
		catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the associated value of an specific key.
	 * 
	 * @param key
	 *            String value.
	 * @return An string value from a properties file.
	 */
	private static String getPropertyFromFile(String key) {
		try {
			PropertyFile properties = new PropertyFile(
					CommonData.DB_PROPERTIES_PATH);
			return properties.getProperty(key);
		} catch (IOException e) {
			LOGGER.error("Error trying to retrieve a property from file. {}",
					e.getMessage());
			return null;
		}
	}

}
