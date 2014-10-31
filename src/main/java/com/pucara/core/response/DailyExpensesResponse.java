package com.pucara.core.response;

import java.util.List;

import com.pucara.persistence.domain.DailyExpensesHelper;

/**
 * 
 * @author Maximiliano Fabian
 */
public class DailyExpensesResponse extends Response {
	private List<DailyExpensesHelper> expenses;

	public DailyExpensesResponse(List<DailyExpensesHelper> expenses) {
		super();
		this.expenses = expenses;
	}

	public DailyExpensesResponse(ErrorMessage me) {
		super(me);
		this.expenses = null;
	}

	public List<DailyExpensesHelper> getExpensesList() {
		return this.expenses;
	}

	public double getTotalExpense() {
		double value = 0.0;

		for (DailyExpensesHelper expense : expenses) {
			value += expense.getExpense();
		}

		return value;
	}

}
