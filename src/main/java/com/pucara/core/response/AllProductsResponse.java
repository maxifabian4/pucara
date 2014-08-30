package com.pucara.core.response;

import java.util.List;

import com.pucara.core.entities.Product;

/**
 * 
 * @author Maximiliano
 * 
 */
public class AllProductsResponse extends Response {

	private List<Product> allProducts;

	/**
	 * 
	 * @param allProducts
	 */
	public AllProductsResponse(List<Product> allProducts) {
		super();
		this.allProducts = allProducts;
	}

	/**
	 * 
	 * @param me
	 */
	public AllProductsResponse(ErrorMessage me) {
		super(me);
		allProducts = null;
	}

	/**
	 * 
	 * @return
	 */
	public List<Product> getAllProducts() {
		return allProducts;
	}

}
