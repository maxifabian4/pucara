package com.pucara.core.services.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.response.ByIdResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.Response;
import com.pucara.core.services.product.ProductService;

/**
 * 
 * @author Maximiliano Fabian
 */
public class PurchaseService extends AbstractTransactionService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PurchaseService.class);
	private String purchaseDescription;
	private String purchaseExpense;

	public PurchaseService() {
		cleanPartialList();
		purchaseDescription = CommonData.EMPTY_STRING;
		purchaseExpense = CommonData.EMPTY_STRING;
	}

	@Override
	public Response addProductToList(String barcode) {
		ProductListResponse response = ProductService
				.existsProduct(new SearchProductRequest(barcode, null));

		if (response.wasSuccessful()) {
			// Initialize product collection if it doesn't exists.
			productsCollection.addProduct(response.getProducts().get(0));

			return new Response();
		} else {
			return new Response(
					new ErrorMessage(ErrorType.ELEMENT_NOT_FOUND,
							String.format(CommonMessageError.BARCODE_NOT_FOUND,
									barcode)));
		}
	}

	@Override
	public Response performTransaction() {
		ByIdResponse purchaseResponse = MySqlAccess.addNewPurchase(
				purchaseDescription, Utilities.getCurrentDate(),
				purchaseExpense);

		if (!purchaseResponse.wasSuccessful()) {
			return new Response(purchaseResponse.getErrorsMessages());
		}

		ByIdResponse purchaseDetailsResponse = MySqlAccess
				.addNewPurchaseDetail(getTotalNumberOfProducts());

		if (!purchaseDetailsResponse.wasSuccessful()) {
			return new Response(purchaseDetailsResponse.getErrorsMessages());
		}

		ByIdResponse purchaseNxNResponse = MySqlAccess.addNxNPurchase(
				purchaseResponse.getId(), purchaseDetailsResponse.getId());

		if (!purchaseNxNResponse.wasSuccessful()) {
			return new Response(purchaseNxNResponse.getErrorsMessages());
		}

		ByIdResponse purchaseFinal = MySqlAccess
				.addNewPurchasePurchaseDetailProduct(productsCollection,
						purchaseResponse.getId(),
						purchaseDetailsResponse.getId());

		if (!purchaseFinal.wasSuccessful()) {
			// CustomLogger
			// .log(LoggerLevel.ERROR,
			// String.format(
			// "Error creating new detail expense: product collection size: %d",
			// productsCollection.getSize()));
			return new Response(purchaseFinal.getErrorsMessages());
		}

		// Increase stock all products.
		if (productsCollection.getSize() > 0) {
			Response updateStockResponse = MySqlAccess.modifyProductStocks(
					productsCollection, true);

			if (!updateStockResponse.wasSuccessful()) {
				return new Response(updateStockResponse.getErrorsMessages());
			}
		}

		LOGGER.info("Transaction performed successfully ...");
		return new Response();
	}

	public void setPurchaseDescription(String purchaseDescription) {
		this.purchaseDescription = purchaseDescription;
	}

	public void setPurchaseExpense(String purchaseExpense) {
		this.purchaseExpense = purchaseExpense;
	}

	@Override
	public double getTotalValue() {
		double price = 0;

		for (int i = 0; i < productsCollection.getSize(); i++) {
			price += productsCollection.getProductAt(i).getCost()
					* productsCollection.getQuantityOfProductAt(i);
			price = Utilities.truncateDecimal(price, 2).doubleValue();
		}

		return price;
	}

}
