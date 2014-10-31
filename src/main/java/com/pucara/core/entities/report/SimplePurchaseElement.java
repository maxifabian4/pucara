package com.pucara.core.entities.report;

/**
 * Should be removed ...
 * @author Maximiliano Fabian
 */
public class SimplePurchaseElement {
	private int quantity;
	private String productDescription;

	public SimplePurchaseElement(int quantity, String productDescription) {
		this.quantity = quantity;
		this.productDescription = productDescription;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setProductDescription(String ProductDescription) {
		this.productDescription = ProductDescription;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getProductDescription() {
		return productDescription;
	}

}
