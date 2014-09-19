package com.pucara.core.entities;

/**
 * Represents the <b>Product</b> entity.
 * 
 * @author Maximiliano
 */
public class Product {
	private String barcode;
	private String description;
	private Double initialCost;
	private Double finalCost;
	private Double percentage;
	private String date;
	private Integer stock;
	private Integer minStock;
	private Integer categoryId;
	private boolean byPercentage;

	/**
	 * Class constructor. Allows to create an instance based of their arguments.
	 * 
	 * @param barcode
	 * @param description
	 * @param cost
	 * @param percentage
	 * @param date
	 * @param stock
	 * @param minStock
	 * @param categoryId
	 */
	public Product(String barcode, String description, Double initialCost,
			Double finalCost, Double percentage, String date, Integer stock,
			Integer minStock, Integer categoryId, boolean byPercentage) {
		this.barcode = barcode;
		this.description = description;
		this.initialCost = initialCost;
		this.finalCost = finalCost;
		this.percentage = percentage;
		this.date = date;
		this.stock = stock;
		this.minStock = minStock;
		this.categoryId = categoryId;
		this.byPercentage = byPercentage;
	}

	/**
	 * Empty constructor of the class.
	 */
	public Product() {

	}

	/**
	 * Allows to add a barcode to a product.
	 * 
	 * @param barcode
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * Allows to add a description to a product.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Allows to add an initial cost to a product.
	 * 
	 * @param cost
	 */
	public void setInitialCost(Double initialCost) {
		this.initialCost = initialCost;
	}

	/**
	 * Allows to add a final cost to a product.
	 * 
	 * @param cost
	 */
	public void setFinalCost(Double finalCost) {
		this.finalCost = finalCost;
	}

	/**
	 * Allows to add a percentage to a product.
	 * 
	 * @param percentage
	 */
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	/**
	 * Allows to add a date of entry to the system.
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Allows to add a stock value to a product.
	 * 
	 * @param stock
	 */
	public void setStock(Integer stock) {
		this.stock = stock;
	}

	/**
	 * Allows to add a min stock value to a product.
	 * 
	 * @param minStock
	 */
	public void setMinStock(Integer minStock) {
		this.minStock = minStock;
	}

	/**
	 * Allows to add a category identifier to a product.
	 * 
	 * @param idCategory
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * 
	 * @param byPercentage
	 */
	public void setByPercentage(boolean byPercentage) {
		this.byPercentage = byPercentage;
	}

	/**
	 * Returns the barcode value associated to a product.
	 * 
	 * @return String
	 */
	public String getBarcode() {
		return this.barcode;
	}

	/**
	 * Returns the description of the product.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the initial cost associated to a product.
	 * 
	 * @return Double
	 */
	public Double getInitialCost() {
		return this.initialCost;
	}

	/**
	 * Returns the final cost associated to a product.
	 * 
	 * @return Double
	 */
	public Double getFinalCost() {
		return this.finalCost;
	}

	/**
	 * Returns the percentage associated to a product.
	 * 
	 * @return Double
	 */
	public Double getPercentage() {
		return this.percentage;
	}

	/**
	 * Returns the date of entry associated to a product.
	 * 
	 * @return String
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * Returns the stock associated to a product.
	 * 
	 * @return Integer
	 */
	public Integer getStock() {
		return this.stock;
	}

	/**
	 * Returns the stock associated to a product.
	 * 
	 * @return Integer
	 */
	public Integer getMinStock() {
		return this.minStock;
	}

	/**
	 * Returns the category identifier associated to a product.
	 * 
	 * @return Integer
	 */
	public Integer getCategoryId() {
		return this.categoryId;
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean getByPercentage() {
		return this.byPercentage;
	}

	/**
	 * Allows to get the main attributes of the product.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return "[" + this.barcode + "] - " + "[" + this.description + " - "
				+ this.finalCost + "]";
	}

}
