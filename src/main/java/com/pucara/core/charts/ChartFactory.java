package com.pucara.core.charts;

public class ChartFactory {
	public static final int XYLINE = 1;
	public static final int XYSCATTER = 2;
	public static final int RATIOXYLINE = 4;
	public static final int BARCHART = 5;
	public static final int LINECHART = 6;
	public static final int PIECHART = 7;
	public static final int RINGCHART = 8;

	public static Chart createChart(int id, String title, String yaxis,
			String varId) {
		switch (id) {
		case XYLINE:
			return null;
			// return new XYLineChart(type, title, yaxis, varId, displayChart,
			// savetofile);
		case XYSCATTER:
			return null;
			// return new XYScatterChart(type, title, yaxis, varId,
			// displayChart, savetofile);
		case RATIOXYLINE:
			return null;
			// return new RatioXYLineChart(type, title, yaxis, varId,
			// displayChart, savetofile);
		case BARCHART:
			return new BarChart(title, yaxis, varId);
		case LINECHART:
			return new LineChart(title, yaxis, varId);
		case PIECHART:
			return new PieChart(title);
		case RINGCHART:
			return new RingChart(title);
		}

		return null;
	}

}
