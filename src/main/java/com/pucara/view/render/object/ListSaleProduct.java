package com.pucara.view.render.object;

/**
 * TODO Talk about this object, how it connects with a render. // REMOVE!!
 * 
 * @author Maximiliano Fabian
 */
public class ListSaleProduct extends ListStockProduct {
	private Integer numberOfProducts;

	public ListSaleProduct(String barcode, String description, String stock, Integer minStock,
			String price, Integer numberOfProducts) {
		super(barcode, description, stock, minStock, price);
		this.numberOfProducts = numberOfProducts;
	}

	public Integer getNumberOfProducts() {
		return this.numberOfProducts;
	}

}
