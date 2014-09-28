package com.pucara.core.entities.report;

import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * @author Maximiliano Fabian
 */
public class PurchaseDailyReport {
	private double expense;
	private String purchaseDescription;
	private Timestamp date;
	private List<SimplePurchaseElement> products;

	public PurchaseDailyReport(double expense, String purchaseDescription,
			Timestamp date, List<SimplePurchaseElement> products) {
		super();
		this.expense = expense;
		this.purchaseDescription = purchaseDescription;
		this.date = date;
		this.products = products;
	}

	public double getExpense() {
		return expense;
	}

	public String getPurchaseDescription() {
		return purchaseDescription;
	}

	public Timestamp getDate() {
		return date;
	}

	public List<SimplePurchaseElement> getProducts() {
		return products;
	}

}
