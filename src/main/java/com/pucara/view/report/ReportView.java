package com.pucara.view.report;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.JFreeChart;

import com.pucara.common.CommonData;
import com.pucara.common.SwingListPanel;
import com.pucara.common.SystemPopup;
import com.pucara.controller.report.ReportController;
import com.pucara.core.entities.report.PurchaseDailyReport;
import com.pucara.view.render.ExpensesCellRenderer;

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
		
		// change

		JFreeChart chart = reportController.createCategoryPieChart();
		DynamicReportPanel panel = new DynamicReportPanel(chart, false);
		this.add(panel);
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
		byDayPanel.addMouseListenerToComponent("costo del d\u00EDa",
				reportController.createMouseListenerForDailyExpenses());

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

	public void displayExpenseInformationList(List<PurchaseDailyReport> list) {
		SwingListPanel panel = new SwingListPanel(list.toArray(), null,
				new ExpensesCellRenderer());
		SystemPopup popup = new SystemPopup(panel);
		popup.setVisible(true);
	}

}
