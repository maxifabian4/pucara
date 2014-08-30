package com.pucara.core.entities;

/**
 * This class represents a sale made in the system.
 * 
 * @author Maximiliano
 */
public class Sale {
	private Long id;
	private String date;
	private double gain;

	public Sale(Long saleid, String date, double gain) {
		this.id = saleid;
		this.date = date;
		this.gain = gain;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public double getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public double getGain() {
		return gain;
	}

}
