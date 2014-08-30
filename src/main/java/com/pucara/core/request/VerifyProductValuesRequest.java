package com.pucara.core.request;

import com.pucara.core.entities.Category;

/**
 * 
 * @author Maximiliano
 */
public class VerifyProductValuesRequest {
	private String barcode;
	private String description;
	private String cost;
	private String percentage;
	private String minStock;
	private String stock;
	private Category category;

	public VerifyProductValuesRequest(String barcode, String description, String cost,
			String percentage, String stock, String minStock, Category category) {
		this.barcode = barcode;
		this.description = description;
		this.cost = cost;
		this.percentage = percentage;
		this.minStock = minStock;
		this.stock = stock;
		this.category = category;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getDescription() {
		return description;
	}

	public String getCost() {
		return cost;
	}

	public String getPercentage() {
		return percentage;
	}

	public String getStock() {
		return stock;
	}

	public String getMinStock() {
		return minStock;
	}

	public Category getCategory() {
		return category;
	}
}
