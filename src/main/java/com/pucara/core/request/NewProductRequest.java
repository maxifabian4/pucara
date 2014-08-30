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
