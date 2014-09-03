package com.pucara.core.entities;

/**
 * 
 * @author Maximiliano
 */
public class PartialElement extends Product {
	private Integer quantity;

	public PartialElement(Product product, Integer quantity) {
		super(product.getBarcode(), product.getDescription(), product.getCost(), product
				.getPercentage(), product.getDate(), product.getStock(), product.getMinStock(),
				product.getCategoryId());
		this.quantity = quantity;
	}

	// public Product getProduct() {
	// return super;
	// }

	public Integer getQuantity() {
		return quantity;
	}

	// public String getBarcode() {
	// return product.getBarcode();
	// }

	public void increaseProduct() {
		quantity++;
	}

	public void decreaseProduct() {
		quantity--;
	}

	@Override
	public String toString() {
		return String.format("PRODUCT: %s QUANTITY: %d", super.toString(), quantity);
	}
}