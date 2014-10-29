package com.pucara.persistence.domain;

public class ProductsCategoryHelper {
	private String categoryName;
	private int numberOfProducts;

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setNumberOfProducts(int numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}

	public int getNumberOfProducts() {
		return this.numberOfProducts;
	}
}
