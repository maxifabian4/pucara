package com.pucara.core.request;

/**
 * 
 * @author Maximiliano
 */
public class SearchProductRequest {
	private String barcode;
	private String description;

	public SearchProductRequest(String barcode, String description) {
		this.barcode = barcode;
		this.description = description;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public String getDescription() {
		return this.description;
	}

}
