package com.pucara.core.services.sale;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pucara.common.CommonMessageError;
import com.pucara.common.CustomLogger;
import com.pucara.common.CustomLogger.LoggerLevel;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.entities.PartialElement;
import com.pucara.core.entities.Product;
import com.pucara.core.entities.ProductsCollection;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.response.ByIdResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.Response;
import com.pucara.core.response.SaleResultResponse;
import com.pucara.core.response.StatementResponse;
import com.pucara.core.services.product.ProductService;

/**
 * This class provides the methods to create a sale in the database.
 * 
 * @author Maximiliano
 */
public class SaleService {

	private static ProductsCollection productsCollection;

	/**
	 * Adds an existing product to the list in order to perform a sale. If the
	 * product already exists in the partial list, it will be increased in one
	 * unit.
	 * 
	 * @param barcode
	 * @return Response
	 */
	public static Response addProductToList(String input) {
		ProductListResponse response = ProductService.existsProduct(new SearchProductRequest(input,
				null));

		if (response.wasSuccessful()) {
			// Initialize product collection if it doesn't exists.
			if (productsCollection == null) {
				productsCollection = new ProductsCollection();
			}

			if (hasSufficientStock(response.getProducts().get(0))) {
				productsCollection.addProduct(response.getProducts().get(0));

				return new Response();
			} else {
				CustomLogger.log(
						null,
						LoggerLevel.WARNING,
						String.format("Insufficient stock for the product [%s]", response
								.getProducts().get(0).getBarcode()));

				return new Response(new ErrorMessage(ErrorType.INSUFFICIENT_STOCK, String.format(
						CommonMessageError.INSUFFICIENT_STOCK, response.getProducts().get(0)
								.getBarcode())));
			}
		} else {
			// This implementation has been added to consider an extra sale
			// without an associated product.
			if (isExtraSale(input)) {
				Product extraProduct = createFalseProduct(input);

				if (extraProduct != null) {
					productsCollection.addProduct(extraProduct);
					return new Response();
				} else {
					return new Response(new ErrorMessage(ErrorType.INVALID_DATA_FORMAT,
							String.format(CommonMessageError.INVALID_DOUBLE_FORMAT, input)));
				}
			} else {
				return new Response(new ErrorMessage(ErrorType.ELEMENT_NOT_FOUND, String.format(
						CommonMessageError.BARCODE_NOT_FOUND, input)));
			}
		}
	}

	/**
	 * 
	 * @param barcode
	 * @return Response
	 */
	public static Response decreaseRequiredProduct(String barcode) {
		return productsCollection.decreaseQuantityProduct(barcode);
	}

	/**
	 * Removes a product from the partial list.
	 * 
	 * @param barcode
	 * @return Response
	 */
	public static Response removeProductFromList(String barcode) {
		if (productsCollection.removeProduct(barcode)) {
			// Clean collection when is not used.
			// if (productsCollection.getSize() == 0) {
			// productsCollection = null;
			// }

			return new Response();
		} else {
			return new Response(new ErrorMessage(ErrorType.ELEMENT_NOT_FOUND, String.format(
					CommonMessageError.BARCODE_NOT_FOUND, barcode)));
		}
	}

	/**
	 * Makes a sale in the system, performing the necessary calcs.
	 * 
	 * @param saleRequest
	 * @return Response
	 */
	public static Response makeASale() {
		// Get the gain of the sale ...
		double gain = getTotalGain();

		if (gain != 0) {
			ByIdResponse saleResponse = MySqlAccess.addNewSale(Utilities.getCurrentDate(), gain);

			if (saleResponse.wasSuccessful()) {
				ByIdResponse saleDetailResponse = MySqlAccess.addNewSaleDetail(SaleService
						.getTotalNumberOfProducts());

				if (saleDetailResponse.wasSuccessful()) {
					ByIdResponse xSaleSaleDetailResponse = MySqlAccess.addNewSaleSaleDetail(
							saleResponse.getId(), saleDetailResponse.getId());

					if (xSaleSaleDetailResponse.wasSuccessful()) {
						return finalSaleStep(saleResponse, saleDetailResponse);
					} else {
						CustomLogger.log(null, CustomLogger.LoggerLevel.ERROR,
								xSaleSaleDetailResponse.getErrorsMessages().get(0).getMessage());

						// Rollback of sale and sale_detail ...
						StatementResponse response = MySqlAccess.removeSaleSaleDetail(
								saleResponse.getId(), saleDetailResponse.getId());
						CustomLogger.log(null, CustomLogger.LoggerLevel.ERROR, response
								.getErrorsMessages().get(0).getMessage());

						return xSaleSaleDetailResponse;
					}
				} else {
					CustomLogger.log(null, CustomLogger.LoggerLevel.ERROR, saleDetailResponse
							.getErrorsMessages().get(0).getMessage());

					// Rollback of sale ...
					StatementResponse response = MySqlAccess.removeSale(saleResponse.getId());
					CustomLogger.log(null, CustomLogger.LoggerLevel.ERROR, response
							.getErrorsMessages().get(0).getMessage());

					return saleDetailResponse;
				}
			} else {
				CustomLogger.log(null, CustomLogger.LoggerLevel.ERROR, saleResponse
						.getErrorsMessages().get(0).getMessage());

				return saleResponse;
			}
		} else {
			return new Response(new ErrorMessage(ErrorType.EMPTY_PARTIAL_LIST,
					CommonMessageError.EMPTY_PARTIAL_LIST));
		}
	}

	/**
	 * Returns the stored list of partial elements.
	 * 
	 * @return List of PartialElement
	 */
	public static List<PartialElement> getPartialList() {
		return productsCollection.getAllProducts();
	}

	/**
	 * Returns the number of products included in a sale. UP!!
	 * 
	 * @return Integer
	 */
	public static Integer getTotalNumberOfProducts() {
		Integer count = 0;

		if (productsCollection != null) {
			for (int i = 0; i < productsCollection.getSize(); i++) {
				count += productsCollection.getQuantityOfProductAt(i);
			}
		}

		return count;
	}

	/**
	 * Returns the number of distinct products included in a sale. UP!!
	 * 
	 * @return Number of distinct products.
	 */
	public static Integer getNumberOfDistinctProducts() {
		return productsCollection.getSize();
	}

	/**
	 * 
	 * @param barcode
	 * @return PartialElement
	 */
	public static PartialElement getPartialProduct(String barcode) {
		return productsCollection.alreadyExists(barcode);
	}

	/**
	 * 
	 */
	public static void cleanPartialList() {
		productsCollection = new ProductsCollection();
	}

	// ups
	public static void updatePartialElement(String barcode, String description, String cost,
			String percentage, String minStock) {
		Product product = productsCollection.getProductBy(barcode);

		product.setDescription(description);
		product.setCost(Double.valueOf(cost));
		product.setPercentage(Integer.valueOf(percentage));
		product.setMinStock(Integer.valueOf(minStock));
	}

	/**
	 * Returns the total price of the product collection.
	 * 
	 * @return double
	 */
	public static double getTotalPrice() {
		double price = 0;

		for (int i = 0; i < productsCollection.getSize(); i++) {
			price += productsCollection.getProductAt(i).getCost()
					* productsCollection.getQuantityOfProductAt(i);
		}

		return price;
	}

	/**
	 * Determines if the current string contains the character <code>@</code>.
	 * 
	 * @param pattern
	 *            - String expression sent from the UI
	 * @return <code>true</code> if the current string contains <code>@</code>
	 *         character; <code>false</code> otherwise.
	 */
	public static boolean isExtraSale(String input) {
		Pattern pattern = Pattern.compile("^@.");
		Matcher matcher = pattern.matcher(input);

		return matcher.lookingAt();
	}

	/**
	 * Returns the gain of a sale. SHOULD BE ABSTRACT !!!
	 * 
	 * @return double
	 */
	private static double getTotalGain() {
		double gain = 0;

		for (int i = 0; i < productsCollection.getSize(); i++) {
			if (!isExtraSale(productsCollection.getProductAt(i).getBarcode())) {
				gain += Utilities.getProductGain(productsCollection.getProductAt(i),
						productsCollection.getQuantityOfProductAt(i));
			} else {
				gain += productsCollection.getProductAt(i).getCost();
			}
		}

		return gain;
	}

	/**
	 * 
	 * @param saleResponse
	 * @param saleDetailResponse
	 * @param xSaleSaleDetailResponse
	 * @return Response
	 */
	private static Response finalSaleStep(ByIdResponse saleResponse, ByIdResponse saleDetailResponse) {
		ByIdResponse xSaleSaleDetailProductResponse = MySqlAccess.addNewSaleSaleDetailProduct(
				productsCollection, saleResponse.getId(), saleDetailResponse.getId());

		if (xSaleSaleDetailProductResponse.wasSuccessful()) {
			Response decreaseProductsResult = decreaseStockForProducts();

			if (decreaseProductsResult.wasSuccessful()) {
				CustomLogger
						.log(null,
								CustomLogger.LoggerLevel.INFO,
								String.format("Sale performed succesfully "
										+ "[saleid, saleDetailId] " + "[%d, %d] ...",
										saleResponse.getId(), saleDetailResponse.getId()));

				String allProducts = productsCollection.toString();
				productsCollection = new ProductsCollection();
				;

				return new SaleResultResponse(saleResponse.getId(), saleDetailResponse.getId(),
						allProducts);
			} else {
				return decreaseProductsResult;
			}
		} else {
			CustomLogger.log(null, CustomLogger.LoggerLevel.ERROR, xSaleSaleDetailProductResponse
					.getErrorsMessages().get(0).getMessage());

			// Rollback of x_sale_sale_detail_product
			StatementResponse response = MySqlAccess.removeSaleSaleDetailProduct(
					saleResponse.getId(), saleDetailResponse.getId());
			CustomLogger.log(null, CustomLogger.LoggerLevel.ERROR, response.getErrorsMessages()
					.get(0).getMessage());

			return xSaleSaleDetailProductResponse;
		}
	}

	/**
	 * 
	 * @return Response
	 */
	private static Response decreaseStockForProducts() {
		Response response = MySqlAccess.modifyProductStocks(productsCollection, false);

		if (!response.wasSuccessful()) {
			// Perform roll-back manually ...
			return new Response(new ErrorMessage(ErrorType.UPDATE_PRODUCT_ERROR,
					CommonMessageError.UPDATE_PRODUCTS_ERROR));
		}

		return new Response();
	}

	/**
	 * 
	 * @param barcode
	 * @return boolean
	 */
	private static boolean hasSufficientStock(Product product) {
		PartialElement requestedProduct = productsCollection.alreadyExists(product.getBarcode());

		if (requestedProduct != null) {
			// Do we have the sufficient stock, considering the required
			// product?
			return product.getStock() >= requestedProduct.getQuantity() + 1;
		} else {
			return product.getStock() > 0;
		}
	}

	/**
	 * 
	 * @param input
	 *            - <code>String</code> information sent from UI
	 * @return A <code>Product</code> instance with non existing data
	 */
	private static Product createFalseProduct(String input) {
		String barcode = "@" + Utilities.generateRandomData(10);
		String[] both = input.substring(1).split("@");
		String description = both[0];
		Double gain = Utilities.getDoubleValue(both[1]);

		if (gain == null) {
			return null;
		}

		return new Product(barcode, description, gain, 0, null, 0, 0, 0);
	}
}
