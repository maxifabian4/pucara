package com.pucara.core.response;

import java.util.List;

import com.pucara.core.entities.Product;

/**
 * 
 * @author Maximiliano
 */
public class ProductResponse extends Response {
	private Product product;

	public ProductResponse(Product product) {
		super();
		this.product = product;
	}

	public ProductResponse(ErrorMessage me) {
		super(me);
		product = null;
	}

	public ProductResponse(List<ErrorMessage> mes) {
		super(mes);
		product = null;
	}

	public Product getProduct() {
		return product;
	}

}
