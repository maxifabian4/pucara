package com.pucara.core.response;

import java.util.List;

import com.pucara.core.entities.Product;

/**
 * 
 * @author Maximiliano
 */
public class ProductListResponse extends Response {
	private List<Product> products;

	public ProductListResponse(List<Product> products) {
		super();
		this.products = products;
	}

	public ProductListResponse(ErrorMessage me) {
		super(me);
		products = null;
	}

	public List<Product> getProducts() {
		return products;
	}

}
