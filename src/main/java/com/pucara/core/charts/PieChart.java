package com.pucara.core.charts;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PiePlot;

import com.pucara.common.CommonData;

public class PieChart extends Chart {
	private DefaultPieDataset dataset;

	public PieChart(String title) {
		this.title = title;
		dataset = new DefaultPieDataset();
	}

	public void addValue(Comparable<String> key, Double value) {
		dataset.setValue(key, value);
	}

	@Override
	public void createChart() {
		chart = ChartFactory.createPieChart(title, dataset);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSimpleLabels(true);
		plot.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);
		plot.setSectionOutlinesVisible(false);
		plot.setOutlineVisible(false);
	}

	@Override
	public void displayChart(int width, int height) {
	}

}
