package com.pucara.core.response;

import java.util.List;

import com.pucara.core.entities.report.ChartInfoElement;

/**
 * 
 * @author Maximiliano Fabián
 */
public class ChartInfoResponse extends Response {
	List<ChartInfoElement> information;

	public ChartInfoResponse(List<ChartInfoElement> information) {
		super();
		this.information = information;
	}

	public ChartInfoResponse(ErrorMessage me) {
		super(me);
	}

	public List<ChartInfoElement> getChartInformation() {
		return information;
	}

}
