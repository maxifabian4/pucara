package com.pucara.view.report;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;

import com.pucara.common.CommonData;
import com.pucara.controller.report.ReportController;

/**
 * 
 * @author Maximiliano Fabian
 */
public class ReportView extends JPanel {
	private static final long serialVersionUID = 1L;
	private ReportController reportController;
	private DynamicReportPanel byDayPanel;
	private DynamicReportPanel byYearPanel;

	public ReportView() {
		reportController = new ReportController(this);

		// Apply properties to the view.
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		generateContent();

		// Add report information by day.
		this.add(byDayPanel);
		// Add report information by year.
		this.add(byYearPanel);
	}

	/**
	 * 
	 */
	private void generateContent() {
		JFreeChart chartByDay = reportController.createBarChartByDay();
		byDayPanel = new DynamicReportPanel(chartByDay, true);
		reportController.addDailyInfoToPanel(byDayPanel);
		byDayPanel.addMouseListenerToComponent("caja inicial",
				reportController.createMouseListenerForInitialBox());

		JFreeChart chartByYear = reportController.createLineChartByYear();
		byYearPanel = new DynamicReportPanel(chartByYear, true);
		reportController.addYearInfoToPanel(byYearPanel);
	}

	/**
	 * 
	 * @param values
	 * @param keys
	 * @param value
	 */
	public void updateViewInformation(String[] keys, String[] values) {
		byDayPanel.updateLabelValues(keys, values);
		this.revalidate();
	}

}
