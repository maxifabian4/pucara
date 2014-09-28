package com.pucara.controller.report;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.PropertyFile;
import com.pucara.core.charts.BarChart;
import com.pucara.core.charts.ChartFactory;
import com.pucara.core.charts.LineChart;
import com.pucara.core.entities.report.ChartInfoElement;
import com.pucara.core.entities.report.PurchaseDailyReport;
import com.pucara.core.generic.Utilities;
import com.pucara.core.response.ChartInfoResponse;
import com.pucara.core.response.PurchaseDailyReportResponse;
import com.pucara.core.response.SaleDailyReportResponse;
import com.pucara.core.services.report.ReportService;
import com.pucara.view.report.DynamicReportPanel;
import com.pucara.view.report.ReportView;

/**
 * This class represents the Category controller in the system.
 * 
 * @author Maximiliano Fabian
 */
public class ReportController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportController.class);
	private ReportView reportView;
	private String[] keys = new String[] { "productos vendidos", "ganancia",
			"costo del d\u00EDa", "caja inicial", "ganancia total" };
	private String[] values;
	private Double initialBox;

	public ReportController(ReportView reportView) {
		this.reportView = reportView;
		// Debe almacenarse utilizando el servicio de reportes !!!
		this.initialBox = getInitialBoxFromProperty();
		this.values = generateReportsForValues();
	}

	public MouseListener createMouseListenerForInitialBox() {
		return new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JLabel label = (JLabel) e.getSource();
				label.setForeground(CommonData.DARK_FONT_COLOR);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JLabel label = (JLabel) e.getSource();
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
				label.setForeground(CommonData.DEFAULT_SELECTION_COLOR);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				String[] options = { "OK" };
				JPanel panel = new JPanel();
				JLabel lbl = new JLabel("caja inicial: ");
				JTextField txt = new JTextField(initialBox.toString());
				panel.add(lbl);
				panel.add(txt);
				int selectedOption = JOptionPane
						.showOptionDialog(null, panel, "Ingresar caja inicial",
								JOptionPane.NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);

				if (selectedOption == 0) {
					initialBox = Utilities.getDoubleValue(txt.getText());
					saveInitialBoxIntoProperties(initialBox);
					values = generateReportsForValues();
					reportView.updateViewInformation(keys, values);
				}
			}
		};
	}

	public MouseListener createMouseListenerForDailyExpenses() {
		return new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JLabel label = (JLabel) e.getSource();
				label.setForeground(CommonData.DARK_FONT_COLOR);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JLabel label = (JLabel) e.getSource();
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
				label.setForeground(CommonData.DEFAULT_SELECTION_COLOR);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				PurchaseDailyReportResponse response = ReportService
						.getDailyPurchaseReport();

				if (!response.wasSuccessful()) {
					JOptionPane.showMessageDialog(null,
							"Error tratando de obtener los gastos de sistema.");
				} else {
					List<PurchaseDailyReport> list = response
							.getPurchasesList();

					if (list.isEmpty()) {
						JOptionPane.showMessageDialog(null,
								"No hay gastos en el día actual.");
					} else {
						reportView.displayExpenseInformationList(list);
					}
				}
			}
		};
	}

	public JFreeChart createLineChartByYear() {
		LineChart lineChart = (LineChart) ChartFactory.createChart(
				ChartFactory.LINECHART, "Ganancia/Costo por a\u00F1o", "pesos",
				"meses");

		ChartInfoResponse gainResponse = ReportService.getChartByYearInfo(2014,
				CommonData.GAIN_BY_YEAR);
		ChartInfoResponse expenseResponse = ReportService.getChartByYearInfo(
				2014, CommonData.EXPENSE_BY_YEAR);
		String month;

		for (ChartInfoElement chartInfoElement : gainResponse
				.getChartInformation()) {
			month = Utilities.getSpanishMonth(chartInfoElement.getKey());
			lineChart.addValue(chartInfoElement.getValue(), "ganancia", month);
		}

		for (ChartInfoElement chartInfoElement : expenseResponse
				.getChartInformation()) {
			month = Utilities.getSpanishMonth(chartInfoElement.getKey());
			lineChart.addValue(chartInfoElement.getValue(), "costo", month);
		}

		lineChart.createChart();

		return lineChart.getChart();
	}

	public JFreeChart createBarChartByDay() {
		BarChart barchart = (BarChart) ChartFactory.createChart(
				ChartFactory.BARCHART, "Ganancia/Costo por d\u00EDa",
				"d\u00EDas", "pesos");

		ChartInfoResponse gainResponse = ReportService.getChartByDayInfo(-6,
				CommonData.GAIN_BY_DAY);
		ChartInfoResponse expenseResponse = ReportService.getChartByDayInfo(-6,
				CommonData.EXPENSE_BY_DAY);
		String subDay;

		for (ChartInfoElement chartInfoElement : gainResponse
				.getChartInformation()) {
			subDay = Utilities.getSpanishDay(chartInfoElement.getKey());
			barchart.addValue(chartInfoElement.getValue(), "ganancia", subDay);
		}

		for (ChartInfoElement chartInfoElement : expenseResponse
				.getChartInformation()) {
			subDay = Utilities.getSpanishDay(chartInfoElement.getKey());
			barchart.addValue(chartInfoElement.getValue(), "costo", subDay);
		}

		barchart.createChart();

		return barchart.getChart();
	}

	public void addDailyInfoToPanel(DynamicReportPanel byDayPanel) {
		JLabel titleLabel, valueLabel;

		for (int i = 0; i < keys.length; i++) {
			titleLabel = CommonUIComponents.createReportLabel(keys[i],
					Font.PLAIN, 17, CommonData.DARK_FONT_COLOR);
			titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

			valueLabel = CommonUIComponents.createReportLabel(values[i],
					Font.BOLD, 25, CommonData.DARK_FONT_COLOR);
			valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

			byDayPanel.addComponent(titleLabel, valueLabel);
		}

	}

	public void addYearInfoToPanel(DynamicReportPanel byYearPanel) {
		JLabel titleLabel = CommonUIComponents.createReportLabel("a\u00F1o",
				Font.PLAIN, 17, CommonData.DARK_FONT_COLOR);
		titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JLabel valueLabel = CommonUIComponents.createReportLabel("2014",
				Font.BOLD, 25, CommonData.DARK_FONT_COLOR);
		valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		byYearPanel.addComponent(titleLabel, valueLabel);
	}

	private String[] generateReportsForValues() {
		Integer soldProducts = 0;
		Double gain = 0.0;
		Double dailyCost = 0.0;
		Double totalGain = 0.0;

		SaleDailyReportResponse saleResponse = ReportService
				.getDailySaleReport();

		if (saleResponse.wasSuccessful()) {
			soldProducts = saleResponse.getQuantity();
			gain = saleResponse.getGain();
			gain = Utilities.adjustDecimals(gain);
		}

		PurchaseDailyReportResponse purchaseResponse = ReportService
				.getDailyPurchaseReport();

		if (purchaseResponse.wasSuccessful()) {
			dailyCost = purchaseResponse.getTotalExpense();
			dailyCost = Utilities.adjustDecimals(dailyCost);
		}

		totalGain = gain - initialBox - dailyCost;

		// return new String[] { soldProducts.toString(),
		// Utilities.truncateDecimal(gain, 2).toString(),
		// Utilities.truncateDecimal(dailyCost, 2).toString(),
		// initialBox.toString(),
		// Utilities.truncateDecimal(totalGain, 2).toString() };

		return new String[] { soldProducts.toString(), gain.toString(),
				dailyCost.toString(), initialBox.toString(),
				totalGain.toString() };
	}

	/**
	 * Returns the initial box value.
	 * 
	 * @return Initial box value taken from properties file.
	 */
	private Double getInitialBoxFromProperty() {
		try {
			PropertyFile property = new PropertyFile(
					CommonData.REPORT_PROPERTIES_PATH);
			String value = property.getProperty("initial.box");

			return new Double(value);
		} catch (IOException e) {
			LOGGER.error("An IO Exception has been fired. Path: {}, {}",
					CommonData.REPORT_PROPERTIES_PATH, e.getMessage());
			return 0.0;
		}
	}

	/**
	 * Store the new initial box value into the properties.
	 * 
	 * @param initialBox
	 *            Initial box value.
	 */
	private void saveInitialBoxIntoProperties(Double initialBox) {
		try {
			PropertyFile property = new PropertyFile(
					CommonData.REPORT_PROPERTIES_PATH);
			property.saveProperty(CommonData.REPORT_PROPERTIES_PATH,
					"initial.box", initialBox.toString());
		} catch (IOException e) {
			LOGGER.error("An IO Exception has been fired. Path: {}, {}",
					CommonData.REPORT_PROPERTIES_PATH, e.getMessage());
		}
	}
}
