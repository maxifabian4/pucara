package com.pucara.core.charts;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import com.pucara.common.CommonData;

public class BarChart extends Chart {
	protected DefaultCategoryDataset dataset;

	public BarChart(String title, String yaxis, String varId) {
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
		chart = ChartFactory.createBarChart(title, // chart title
				yaxis, // domain axis label
				varId, // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);
		plot.setDomainGridlinePaint(Color.GRAY);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinePaint(Color.GRAY);
		plot.setOutlineVisible(false);

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		
		CategoryItemRenderer r = plot.getRenderer();
		r.setSeriesPaint(0, new Color(154, 204, 3));
		r.setSeriesPaint(1, new Color(234, 206, 28));

		LegendTitle legend = chart.getLegend(0);
		legend.setBackgroundPaint(CommonData.GENERAL_BACKGROUND_COLOR);
		legend.setFrame(new BlockBorder(Color.gray));

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 15));
		domainAxis.setLabelPaint(CommonData.DARK_FONT_COLOR);
		domainAxis.setTickLabelFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 15));
		domainAxis.setTickMarksVisible(false);

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 15));
		rangeAxis.setLabelPaint(CommonData.DARK_FONT_COLOR);
		rangeAxis.setTickLabelFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 15));
		rangeAxis.setTickMarksVisible(false);
	}

	@Override
	public void displayChart(int width, int height) {
	}

}
