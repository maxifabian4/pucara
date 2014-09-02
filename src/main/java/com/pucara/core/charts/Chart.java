package com.pucara.core.charts;

import java.io.File;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public abstract class Chart {
	protected String title;
	protected String yaxis;
	protected String varId;
	protected int id = -1;
	protected JFreeChart chart;

	// public Chart(String title, String yaxis, String varId) {
	// }

	public JFreeChart getChart() {
		return chart;
	}

	// public void initXYSeries(String variableid, int variableindex, int flag)
	// {
	// The XY charts need to implement this
	// }

	// public void addValue(double tpsd, String variableid, String
	// variableindex, int flag) {
	// // All charts need to implement this
	// }

	// public void addSeries(int flag) {
	// The XY charts need to implement this
	// }

	public abstract void createChart();

	public abstract void displayChart(int width, int height);

	public String saveChart(String outfileprefix, int width, int height) {
		String filename = null;
		String fullpath = "/home/pucara/Escritorio";

		try {
			if (chart == null) {
				filename = "public_error_700x300.png";
				return filename;
			}

			if (outfileprefix != null) {
				// filename = fullpath + File.separator + outfileprefix +
				// ".jpg";
				filename = outfileprefix + ".jpg";
				File imgfile = new File(filename);
				ChartUtilities.saveChartAsJPEG(imgfile, chart, width, height);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filename;
	}

}
