package com.pucara.core.response;

/**
 * 
 * @author Maximiliano Fabian
 */
public class SaleDailyReportResponse extends Response {
	private double gain;
	private double sold;
	private int quantity;

	public SaleDailyReportResponse(double gain, double sold, int quantity) {
		super();
		this.gain = gain;
		this.sold = sold;
		this.quantity = quantity;
	}

	public SaleDailyReportResponse(ErrorMessage me) {
		super(me);
		this.gain = 0.0;
		this.quantity = 0;
	}

	public double getGain() {
		return gain;
	}

	public double getSold() {
		return sold;
	}

	public int getQuantity() {
		return quantity;
	}

}
