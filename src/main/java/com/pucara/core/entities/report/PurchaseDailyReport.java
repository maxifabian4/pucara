package com.pucara.core.entities.report;

import java.util.List;

/**
 * 
 * @author Maximiliano Fabian
 */
public class PurchaseDailyReport {
	private double expense;
	private String purchaseDescription;
	private List<SimplePurchaseElement> products;

	public PurchaseDailyReport(double expense, String purchaseDescription,
			List<SimplePurchaseElement> products) {
		super();
		this.expense = expense;
		this.purchaseDescription = purchaseDescription;
		this.products = products;
	}

	public double getExpense() {
		return expense;
	}

	public String getPurchaseDescription() {
		return purchaseDescription;
	}

	public List<SimplePurchaseElement> getProducts() {
		return products;
	}

}
