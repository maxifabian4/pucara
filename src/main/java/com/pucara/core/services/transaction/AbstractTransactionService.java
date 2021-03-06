package com.pucara.core.services.transaction;

import java.util.List;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.entities.PartialElement;
import com.pucara.core.entities.Product;
import com.pucara.core.entities.ProductsCollection;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.ProductListResponse;
import com.pucara.core.response.Response;

/**
 * ???
 * 
 * @author Maximiliano Fabian
 */
public abstract class AbstractTransactionService {

	protected ProductsCollection productsCollection;

	/**
	 * Adds an existing product to the list in order to perform a sale. If the
	 * product already exists in the partial list, it will be increased in one
	 * unit.
	 * 
	 * @param barcode
	 * @return {@link Response}
	 */
	public abstract Response addProductToList(String barcode);

	/**
	 * Makes a sale in the system, performing the necessary calcs.
	 * 
	 * @param saleRequest
	 * @return {@link Response}
	 */
	public abstract Response performTransaction();

	/**
	 * Returns the total price/gain of the product collection.
	 * 
	 * @return Double value
	 */
	public abstract double getTotalValue();

	/**
	 * 
	 * @param barcode
	 * @return {@link Response}
	 */
	public Response decreaseRequiredProduct(String barcode) {
		return productsCollection.decreaseQuantityProduct(barcode);
	}
	
	public Response increaseRequiredProduct(String barcode, int n) {
		return productsCollection.increaseQuantityProduct(barcode, n);
	}

	/**
	 * Removes a product from the partial list.
	 * 
	 * @param barcode
	 * @return Response
	 */
	public Response removeProductFromList(String barcode) {
		if (productsCollection.removeProduct(barcode)) {
			return new Response();
		} else {
			return new Response(
					new ErrorMessage(ErrorType.ELEMENT_NOT_FOUND,
							String.format(CommonMessageError.BARCODE_NOT_FOUND,
									barcode)));
		}
	}

	/**
	 * Returns the stored list of partial elements.
	 * 
	 * @return List of PartialElement
	 */
	public List<PartialElement> getPartialList() {
		return productsCollection.getAllProducts();
	}

	/**
	 * Returns the number of products included in a sale.
	 * 
	 * @return Integer
	 */
	public Integer getTotalNumberOfProducts() {
		Integer count = 0;

		if (productsCollection != null) {
			for (int i = 0; i < productsCollection.getSize(); i++) {
				count += productsCollection.getQuantityOfProductAt(i);
			}
		}

		return count;
	}

	/**
	 * 
	 * @param barcode
	 * @return PartialElement
	 */
	public PartialElement getPartialProduct(String barcode) {
		return productsCollection.alreadyExists(barcode);
	}

	/**
	 * 
	 */
	public void cleanPartialList() {
		productsCollection = new ProductsCollection();
	}

	/**
	 * Update a current product in the list.
	 * 
	 * @param barcode
	 *            Product identifier
	 * @param description
	 *            Product description
	 * @param cost
	 *            Product cost
	 * @param percentage
	 *            Product percentage
	 * @param minStock
	 *            Product minimal stock
	 */
	public void updatePartialElement(String barcode, String description,
			String initialCost, String finalCost, String percentage,
			String minStock) {
		Product product = productsCollection.getProductBy(barcode);

		product.setDescription(description);
		product.setInitialCost(Double.valueOf(initialCost));
		product.setFinalCost(Double.valueOf(finalCost));
		product.setPercentage(Double.valueOf(percentage));
		product.setMinStock(Integer.valueOf(minStock));
	}

	/**
	 * Returns the number of distinct products included in a sale.
	 * 
	 * @return Number of distinct products.
	 */
	public Integer getNumberOfDistinctProducts() {
		return productsCollection.getSize();
	}

	/**
	 * Updates a partial product from the database searching by the barcode.
	 * 
	 * @param barcode
	 *            Product barcode.
	 */
	public void updateProductFromList(String barcode) {
		if (productsCollection.alreadyExists(barcode) != null) {
			Product partialProduct = productsCollection.getProductBy(barcode);

			ProductListResponse responseFromDB = MySqlAccess
					.findProductByCondition(String.format(
							CommonData.PRODUCT_WHERE_BARCODE, barcode));

			if (responseFromDB.wasSuccessful()) {
				Product productFromDB = responseFromDB.getProducts().get(0);
				partialProduct.setDescription(productFromDB.getDescription());
				partialProduct.setInitialCost(productFromDB.getInitialCost());
				partialProduct.setFinalCost(productFromDB.getFinalCost());
				partialProduct.setPercentage(productFromDB.getPercentage());
				partialProduct.setStock(productFromDB.getStock());
				partialProduct.setMinStock(productFromDB.getMinStock());
			}
		}
	}

}
