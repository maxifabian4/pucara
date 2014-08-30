package com.pucara.core.entities.report;

public class ChartInfoElement {
	private String key;
	private Double value;

	public ChartInfoElement(String key, Double value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return this.key;
	}

	public Double getValue() {
		return this.value;
	}

}
