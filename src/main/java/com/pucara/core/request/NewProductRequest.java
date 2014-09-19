package com.pucara.core.request;

import com.pucara.core.entities.Product;

/**
 * This class allows to create a new request in order to create a new Product.
 * 
 * @author Maximiliano
 */
public class NewProductRequest {
	private Product product;

	public NewProductRequest(Product product) {
		if (product.getByPercentage()) {
			Double finalCost = product.getInitialCost()
					* (product.getPercentage() / 100)
					+ product.getInitialCost();
			// We need to truncate finalCost.
			product.setFinalCost(finalCost);
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
