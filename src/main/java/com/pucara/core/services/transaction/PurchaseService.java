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

		if (productsCollection.getSize() > 0) {
			ByIdResponse purchaseDetailsResponse = MySqlAccess
					.addNewPurchaseDetail(getTotalNumberOfProducts());

			if (!purchaseDetailsResponse.wasSuccessful()) {
				LOGGER.error(
						"Error trying to add Purchase Detail information. Removing purchase: {}",
						purchaseResponse.getId());
				// TODO Add method in order to perform roll-back ...
				return new Response(purchaseDetailsResponse.getErrorsMessages());
			}

			ByIdResponse purchaseNxNResponse = MySqlAccess.addNxNPurchase(
					purchaseResponse.getId(), purchaseDetailsResponse.getId());

			if (!purchaseNxNResponse.wasSuccessful()) {
				LOGGER.error(
						"Error trying to add Purchase Detail information. Removing purchase: {} and purchase detail: {}",
						purchaseResponse.getId(),
						purchaseDetailsResponse.getId());
				// TODO Add method in order to perform roll-back ...
				return new Response(purchaseNxNResponse.getErrorsMessages());
			}

			ByIdResponse purchaseFinal = MySqlAccess
					.addNewPurchasePurchaseDetailProduct(productsCollection,
							purchaseResponse.getId(),
							purchaseDetailsResponse.getId());

			if (!purchaseFinal.wasSuccessful()) {
				LOGGER.error(
						"Error creating new purchase detail: Product collection size: {}",
						productsCollection.getSize());
				// TODO Add method in order to perform roll-back ...
				LOGGER.error(
						"Removing purchase: {}, purchase detail: {} and their relationship.",
						purchaseResponse.getId(),
						purchaseDetailsResponse.getId());
				return new Response(purchaseFinal.getErrorsMessages());
			}

			// Increase all products stock value.
			Response updateStockResponse = MySqlAccess.modifyProductStocks(
					productsCollection, true);

			if (!updateStockResponse.wasSuccessful()) {
				LOGGER.error("Error trying to modify the product's stock information. Removing all purchase information.");
				// TODO Add method in order to perform roll-back for all, Do we
				// need to add an roll-back strategy.
				return new Response(updateStockResponse.getErrorsMessages());
			}
		} else {
			LOGGER.info("No products associated to a new purchase ...");
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
			price += productsCollection.getProductAt(i).getInitialCost()
					* productsCollection.getQuantityOfProductAt(i);
		}

		return Utilities.truncateDecimal(price, 2);
	}

}
