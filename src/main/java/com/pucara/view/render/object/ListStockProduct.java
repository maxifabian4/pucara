package com.pucara.view.render.object;

/**
 * TODO Talk about this object, how it connects with a render. REMOVE
 * 
 * @author Maximiliano Fabian
 */
public class ListStockProduct {
	private String barcode;
	private String description;
	private String stock;
	private Integer minStock;
	private String price;

	public ListStockProduct(String barcode, String description, String stock, Integer minStock,
			String price) {
		this.barcode = barcode;
		this.description = description;
		this.stock = stock;
		this.minStock = minStock;
		this.price = price;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getDescription() {
		return description;
	}

	public String getStock() {
		return stock;
	}

	public Integer getMinStock() {
		return minStock;
	}

	public String getPrice() {
		return price;
	}

}
