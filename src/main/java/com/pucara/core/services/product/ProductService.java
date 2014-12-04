package com.pucara.core.services.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.entities.Product;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.NewProductRequest;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.request.UpdateProductRequest;
import com.pucara.core.request.VerifyProductValuesRequest;
import com.pucara.core.response.AllProductsResponse;
import com.pucara.core.response.DailyExpensesResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.ProductResponse;
import com.pucara.core.response.StatementResponse;
import com.pucara.core.services.mybatis.MyBatisUtil;
import com.pucara.persistence.mapper.ProductMapper;
import com.pucara.persistence.mapper.ReportMapper;

/**
 * This class provides the methods to create and update a product in the
 * database.
 * 
 * @author Maximiliano
 */
public class ProductService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProductService.class);

	/**
	 * 
	 * @param nuevoPedidoProducto
	 * @return ProductResponse
	 */
	public static ProductResponse addProduct(NewProductRequest newProductRequest) {
		SearchProductRequest searchRequest = new SearchProductRequest(
				newProductRequest.getProduct().getBarcode(), null);

		if (!ProductService.existsProduct(searchRequest).wasSuccessful()) {
			StatementResponse response = MySqlAccess
					.insertNewProduct(newProductRequest.getProduct());

			if (response.wasSuccessful()) {
				return new ProductResponse(newProductRequest.getProduct());
			} else {
				return new ProductResponse(response.getErrorsMessages());
			}
		} else {
			LOGGER.error(
					"Existing product in the database. Barcode: {} Description: {}",
					newProductRequest.getProduct().getBarcode(),
					newProductRequest.getProduct().getDescription());
			return new ProductResponse(new ErrorMessage(
					ErrorType.DATABASE_DUPLICATED_KEY, String.format(
							CommonMessageError.DUPLICATED_PRODUCT,
							newProductRequest.getProduct().getBarcode(),
							newProductRequest.getProduct().getDescription())));
		}
	}

	/**
	 * Removes a product from the database.
	 * 
	 * @param barcode
	 * @return {@link ProductResponse}
	 */
	public static ProductResponse removeProduct(String barcode) {
		StatementResponse response = MySqlAccess.removeProduct(barcode);

		if (response.wasSuccessful())
			return new ProductResponse(new Product());
		return new ProductResponse(response.getErrorsMessages());
	}

	/**
	 * Should be removed?
	 * 
	 * @return AllProductsResponse
	 */
	public static AllProductsResponse getAllProducts() {
		List<Product> allProducts = new ArrayList<Product>();
		ResultSet productsStatement = MySqlAccess.getAllProducts();
		Product newProduct = new Product();

		try {
			while (productsStatement.next()) {
				newProduct.setBarcode(productsStatement.getString("barcode"));
				newProduct.setDescription(productsStatement
						.getString("description"));
				newProduct.setInitialCost(productsStatement
						.getDouble("initialcost"));
				newProduct.setFinalCost(productsStatement
						.getDouble("finalcost"));
				newProduct.setPercentage(productsStatement
						.getDouble("percentage"));
				newProduct.setDate(productsStatement.getString("date"));
				newProduct.setStock(productsStatement.getInt("stock"));
				newProduct.setMinStock(productsStatement.getInt("minstock"));
				newProduct
						.setCategoryId(productsStatement.getInt("categoryid"));

				if (productsStatement.getInt("bypercentage") == 1) {
					newProduct.setByPercentage(true);
				} else {
					newProduct.setByPercentage(false);
				}

				allProducts.add(newProduct);
				newProduct = new Product();
			}

			return new AllProductsResponse(allProducts);
		} catch (SQLException e) {
			return new AllProductsResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @return
	 */
	public static AllProductsResponse getAllProductsFromView() {
		List<Product> allProducts = new ArrayList<Product>();
		ResultSet productsStatement = MySqlAccess.getAllProductsFromView();
		Product newFinalProduct = new Product();

		try {
			while (productsStatement.next()) {
				newFinalProduct.setBarcode(productsStatement
						.getString("barcode"));
				newFinalProduct.setDescription(productsStatement
						.getString("description"));
				newFinalProduct.setStock(productsStatement.getInt("stock"));
				newFinalProduct.setMinStock(productsStatement
						.getInt("minstock"));
				newFinalProduct.setFinalCost(productsStatement
						.getDouble("finalcost"));

				allProducts.add(newFinalProduct);
				newFinalProduct = new Product();
			}

			return new AllProductsResponse(allProducts);
		} catch (SQLException e) {
			return new AllProductsResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Verifies and returns a lists of products that match with a condition.
	 * 
	 * @param barcode
	 * @return ProductResponse
	 */
	public static ProductListResponse existsProduct(
			SearchProductRequest searchRequest) {
		ProductListResponse response = new ProductListResponse(
				new ArrayList<Product>());

		if (searchRequest.getBarcode() != null) {
			response = MySqlAccess.findProductByCondition(String.format(
					CommonData.PRODUCT_WHERE_BARCODE,
					searchRequest.getBarcode()));
		} else if (searchRequest.getDescription() != null) {
			response = MySqlAccess
					.findProductByCondition("WHERE description LIKE '%"
							+ searchRequest.getDescription() + "%'");
		}

		if (response.wasSuccessful() && !response.getProducts().isEmpty()) {
			return response;
		} else {
			return new ProductListResponse(new ErrorMessage(
					ErrorType.ELEMENT_NOT_FOUND,
					CommonMessageError.ELEMENT_NOT_FOUND));
		}
	}

	/**
	 * 
	 * @param request
	 * @return ProductResponse
	 */
	public static ProductResponse updateProduct(UpdateProductRequest request) {
		Product productToUpdate = request.getProduct();

		StatementResponse response = MySqlAccess.updateProduct(productToUpdate);

		if (response.wasSuccessful() && response.getAffectedRows() == 1) {
			return new ProductResponse(productToUpdate);
		}

		return new ProductResponse(response.getErrorsMessages());
	}

	/**
	 * 
	 * @param productValuesRequest
	 * @return ProductResponse
	 */
	public static ProductResponse getValidatedProduct(
			VerifyProductValuesRequest request) {
		Product newProduct = new Product();

		if (Utilities.areAllDigits(request.getBarcode())) {
			if (!request.getBarcode().trim().equals("0")) {
				newProduct.setBarcode(request.getBarcode());
			} else {
				newProduct.setBarcode(ProductService.getAssignedBarcode());
			}

			if (!request.getDescription().trim()
					.equals(CommonData.EMPTY_STRING)
					&& !Utilities.isTooLong(request.getDescription(),
							CommonData.MAX_LONG_VALUE_PRODUCT_DESCRIPTION)
					&& !request.getDescription().contains("@")) {
				// Verify the description has not special characters.
				newProduct.setDescription(request.getDescription());

				try {
					newProduct.setInitialCost(Double.valueOf(request
							.getInitialCost()));
					newProduct.setFinalCost(Double.valueOf(request
							.getFinalCost()));

					try {
						newProduct.setPercentage(Double.valueOf(request
								.getPercentage()));

						try {
							newProduct.setStock(Integer.valueOf(request
									.getStock()));

							try {
								newProduct.setMinStock(Integer.valueOf(request
										.getMinStock()));

								if (request.getCategory() != null) {
									newProduct.setCategoryId(request
											.getCategory().getId());
								}

								newProduct.setDate(Utilities.getCurrentDate());

								return new ProductResponse(newProduct);
							} catch (NumberFormatException ne) {
								return new ProductResponse(
										new ErrorMessage(
												ErrorType.INVALID_DATA_FORMAT,
												String.format(
														CommonMessageError.INVALID_INTEGER_FORMAT,
														request.getMinStock())));
							}
						} catch (NumberFormatException ne) {
							return new ProductResponse(
									new ErrorMessage(
											ErrorType.INVALID_DATA_FORMAT,
											String.format(
													CommonMessageError.INVALID_INTEGER_FORMAT,
													request.getStock())));
						}
					} catch (NumberFormatException ne) {
						return new ProductResponse(
								new ErrorMessage(
										ErrorType.INVALID_DATA_FORMAT,
										String.format(
												CommonMessageError.INVALID_DOUBLE_FORMAT,
												request.getPercentage())));
					}
				} catch (NumberFormatException ne) {
					return new ProductResponse(new ErrorMessage(
							ErrorType.INVALID_DATA_FORMAT, String.format(
									CommonMessageError.INVALID_DOUBLE_FORMAT,
									request.getInitialCost())));
				}
			} else {
				if (Utilities.isTooLong(request.getDescription(),
						CommonData.MAX_LONG_VALUE_PRODUCT_DESCRIPTION)) {
					return new ProductResponse(
							new ErrorMessage(
									ErrorType.INVALID_DATA_FORMAT,
									String.format(
											CommonMessageError.INVALID_DESCRIPTION_FORMAT,
											CommonData.MAX_LONG_VALUE_PRODUCT_DESCRIPTION)));
				} else {
					return new ProductResponse(new ErrorMessage(
							ErrorType.EMPTY_VALUE,
							CommonMessageError.DESCRIPTION_EMPTY_VALUE));
				}
			}
		} else {
			return new ProductResponse(new ErrorMessage(
					ErrorType.INVALID_DATA_FORMAT,
					CommonMessageError.INVALID_BARCODE_FORMAT));
		}
	}

	private static String getAssignedBarcode() {
		String assignedBarcode = "997" + Utilities.generateRandomNumber(10);
		ProductListResponse barcodeResponse = ProductService
				.existsProduct(new SearchProductRequest(assignedBarcode, null));

		while (barcodeResponse.wasSuccessful()) {
			assignedBarcode = "997" + Utilities.generateRandomNumber(100);
			barcodeResponse = ProductService
					.existsProduct(new SearchProductRequest(assignedBarcode,
							null));
		}

		return assignedBarcode;
	}

	/**
	 * This method uses mappers.
	 */
	public static List<String> getAllDescriptions() {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			ProductMapper productMapper = sqlSession
					.getMapper(ProductMapper.class);
			return productMapper.getAllDescriptions();
		} finally {
			sqlSession.close();
		}
	}

	public static String getBarcodeByDescription(String description) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			ProductMapper productMapper = sqlSession
					.getMapper(ProductMapper.class);
			return productMapper.getBarcodeByDescription(description);
		} finally {
			sqlSession.close();
		}
	}

}
