package com.pucara.core.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.pucara.common.CommonData;

public class LineChart extends Chart {
	protected DefaultCategoryDataset dataset;

	public LineChart(String title, String yaxis, String varId) {
		this.title = title;
		this.yaxis = yaxis;
		this.varId = varId;
		dataset = new DefaultCategoryDataset();
	}

	public void addValue(double value, String variableid, String variableindex) {
		dataset.addValue(value, variableid, variableindex);
	}

	@Override
	public void createChart() {
		chart = org.jfree.chart.ChartFactory.createLineChart(title, varId,
				yaxis, (CategoryDataset) dataset, PlotOrientation.VERTICAL,
				true, true, false);
		chart.setBackgroundPaint(Color.white);
		chart.getTitle().setPaint(Color.DARK_GRAY);
		chart.getTitle().setFont(
				new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 22));
//		chart.setAntiAlias(true);
		chart.getLegend().setBorder(0.0, 0.0, 0.0, 0.0);
		chart.getLegend().setBackgroundPaint(
				CommonData.GENERAL_BACKGROUND_COLOR);
		chart.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinePaint(Color.GRAY);
		plot.setOutlineVisible(false);

		NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
		numberaxis.setTickUnit(new NumberTickUnit(500));
		numberaxis.setAutoRangeIncludesZero(false);
		numberaxis.setUpperMargin(0.2D);
		numberaxis.setTickMarksVisible(false);

		// LegendTitle legend = chart.getLegend(0);
		// legend.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);
		// legend.setFrame(new BlockBorder(Color.gray));

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.BOLD, 15));
		domainAxis.setLabelPaint(CommonData.DARK_FONT_COLOR);
		domainAxis.setTickLabelFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, 15));
		domainAxis.setTickMarksVisible(false);

		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) plot
				.getRenderer();
		lineandshaperenderer.setBaseShapesVisible(true);
		lineandshaperenderer.setBaseItemLabelsVisible(true);
		lineandshaperenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineandshaperenderer.setSeriesStroke(0, new BasicStroke(2f));
		lineandshaperenderer.setSeriesStroke(1, new BasicStroke(2f));

		CategoryItemRenderer r = plot.getRenderer();
		r.setSeriesPaint(0, new Color(154, 204, 3));
		r.setSeriesPaint(1, new Color(234, 206, 28));
	}

	@Override
	public void displayChart(int width, int height) {
	}

}
