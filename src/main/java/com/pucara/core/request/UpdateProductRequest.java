package com.pucara.core.request;

import com.pucara.core.entities.Product;
import com.pucara.core.generic.Utilities;

/**
 * This class allows to create a new request in order to update an existing
 * Product.
 * 
 * @author Maximiliano
 */
public class UpdateProductRequest {
	private Product product;

	public UpdateProductRequest(Product product) {
		if (product.getByPercentage()) {
			Double finalCost = product.getInitialCost()
					* (product.getPercentage() / 100)
					+ product.getInitialCost();
			// We need to truncate finalCost.
			product.setFinalCost(Utilities.adjustDecimals(finalCost));
		} else {
			Double percentage = (product.getFinalCost() - product
					.getInitialCost()) / product.getInitialCost();
			product.setPercentage(percentage * 100);
		}

		this.product = product;
	}

	/**
	 * Allows to add a product to the request.
	 * 
	 * @param product
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * Returns the product associated to the request.
	 * 
	 * @return Product
	 */
	public Product getProduct() {
		return this.product;
	}

}
