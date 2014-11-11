package com.pucara.core.request;

import com.pucara.persistence.domain.Category;

/**
 * 
 * @author Maximiliano
 */
public class VerifyProductValuesRequest {
	private String barcode;
	private String description;
	private String initialCost;
	private String finalCost;
	private String percentage;
	private String minStock;
	private String stock;
	// Should be an id ...
	private Category category;

	public VerifyProductValuesRequest(String barcode, String description,
			String initialCost, String finalCost, String percentage,
			String stock, String minStock, Category category) {
		this.barcode = barcode;
		this.description = description;
		this.initialCost = initialCost;
		this.finalCost = finalCost;
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

	public String getInitialCost() {
		return initialCost;
	}

	public String getFinalCost() {
		return finalCost;
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
