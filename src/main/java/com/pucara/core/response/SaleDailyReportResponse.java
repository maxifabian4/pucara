package com.pucara.core.response;

/**
 * 
 * @author Maximiliano Fabian
 */
public class SaleDailyReportResponse extends Response {
	private double gain;
	private int quantity;

	public SaleDailyReportResponse(double gain, int quantity) {
		super();
		this.gain = gain;
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

	public int getQuantity() {
		return quantity;
	}

}
