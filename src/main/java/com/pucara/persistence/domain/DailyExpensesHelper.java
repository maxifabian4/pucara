package com.pucara.persistence.domain;

import java.sql.Timestamp;

/**
 * 
 * @author Maximiliano Fabian
 */
public class DailyExpensesHelper {
	private String description;
	private Double expense;
	private Timestamp date;

	public String getDescription() {
		return this.description;
	}

	public Double getExpense() {
		return this.expense;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExpense(Double expense) {
		this.expense = expense;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
