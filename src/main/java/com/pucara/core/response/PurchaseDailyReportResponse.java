package com.pucara.core.response;

import java.util.List;

import com.pucara.core.entities.report.PurchaseDailyReport;

/**
 * 
 * @author Maximiliano Fabian
 */
public class PurchaseDailyReportResponse extends Response {
	private List<PurchaseDailyReport> purchases;

	public PurchaseDailyReportResponse(List<PurchaseDailyReport> purchases) {
		super();
		this.purchases = purchases;
	}

	public PurchaseDailyReportResponse(ErrorMessage me) {
		super(me);
		this.purchases = null;
	}

	public List<PurchaseDailyReport> getPurchasesList() {
		return this.purchases;
	}

	public double getTotalExpense() {
		double expense = 0.0;

		for (PurchaseDailyReport purchase : purchases) {
			expense += purchase.getExpense();
		}

		return expense;
	}

}
