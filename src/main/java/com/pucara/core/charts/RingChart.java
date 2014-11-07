package com.pucara.core.charts;

import java.awt.Color;
import java.awt.Font;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.RingPlot;

import com.pucara.common.CommonData;

public class RingChart extends Chart {
	private DefaultPieDataset dataset;

	public RingChart(String title) {
		this.title = title;
		dataset = new DefaultPieDataset();
	}

	public void addValue(Comparable<String> key, Double value) {
		dataset.setValue(key, value);
	}

	@Override
	public void createChart() {
		chart = ChartFactory
				.createRingChart(title, dataset, true, false, false);
		chart.getTitle().setPaint(Color.DARK_GRAY);
		chart.getTitle().setFont(
				new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 22));
		chart.getLegend().setBorder(0.0, 0.0, 0.0, 0.0);
		chart.getLegend().setBackgroundPaint(
				CommonData.GENERAL_BACKGROUND_COLOR);
		chart.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);

		RingPlot ringplot = (RingPlot) chart.getPlot();
		ringplot.setNoDataMessage("No hay información disponible");
		ringplot.setSectionDepth(0.15D);
		ringplot.setShadowPaint(null);
		ringplot.setLabelGenerator(null);
		ringplot.setSeparatorsVisible(false);
		ringplot.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);
		ringplot.setSectionOutlinesVisible(false);
		ringplot.setOutlineVisible(false);
	}

	@Override
	public void displayChart(int width, int height) {
	}

}
