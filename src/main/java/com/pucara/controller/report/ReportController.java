package com.pucara.controller.report;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.JFreeChart;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.core.charts.BarChart;
import com.pucara.core.charts.ChartFactory;
import com.pucara.core.charts.LineChart;
import com.pucara.core.entities.report.ChartInfoElement;
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
	private ReportView reportView;
	private String[] keys = new String[] { "productos vendidos", "ganancia", "costo del día",
			"caja inicial", "ganancia total" };
	private String[] values;
	private Double initialBox;

	public ReportController(ReportView reportView) {
		this.reportView = reportView;
		// Debe almacenarse utilizando el servicio de reportes !!!
		this.initialBox = 300.0;
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
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JLabel label = (JLabel) e.getSource();
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				String[] options = { "OK" };
				JPanel panel = new JPanel();
				JLabel lbl = new JLabel("caja inicial: ");
				JTextField txt = new JTextField("000");
				panel.add(lbl);
				panel.add(txt);
				int selectedOption = JOptionPane.showOptionDialog(null, panel, "The Title",
						JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (selectedOption == 0) {
					initialBox = Utilities.getDoubleValue(txt.getText());
					values = generateReportsForValues();
					reportView.updateViewInformation(keys, values);
				}
			}
		};
	}

	public JFreeChart createLineChartByYear() {
		LineChart lineChart = (LineChart) ChartFactory.createChart(ChartFactory.LINECHART,
				"Ganancia/Costo por año", "pesos", "meses");

		ChartInfoResponse gainResponse = ReportService.getChartByYearInfo(2014,
				CommonData.GAIN_BY_YEAR);
		ChartInfoResponse expenseResponse = ReportService.getChartByYearInfo(2014,
				CommonData.EXPENSE_BY_YEAR);
		String month;

		for (ChartInfoElement chartInfoElement : gainResponse.getChartInformation()) {
			month = Utilities.getSpanishMonth(chartInfoElement.getKey());
			lineChart.addValue(chartInfoElement.getValue(), "ganancia", month);
		}

		for (ChartInfoElement chartInfoElement : expenseResponse.getChartInformation()) {
			month = Utilities.getSpanishMonth(chartInfoElement.getKey());
			lineChart.addValue(chartInfoElement.getValue(), "costo", month);
		}

		lineChart.createChart();

		return lineChart.getChart();
	}

	public JFreeChart createBarChartByDay() {
		BarChart barchart = (BarChart) ChartFactory.createChart(ChartFactory.BARCHART,
				"Ganancia/Costo por día", "días", "pesos");

		ChartInfoResponse gainResponse = ReportService
				.getChartByDayInfo(-6, CommonData.GAIN_BY_DAY);
		ChartInfoResponse expenseResponse = ReportService.getChartByDayInfo(-6,
				CommonData.EXPENSE_BY_DAY);
		String subDay;

		for (ChartInfoElement chartInfoElement : gainResponse.getChartInformation()) {
			subDay = Utilities.getSpanishDay(chartInfoElement.getKey());
			barchart.addValue(chartInfoElement.getValue(), "ganancia", subDay);
		}

		for (ChartInfoElement chartInfoElement : expenseResponse.getChartInformation()) {
			subDay = Utilities.getSpanishDay(chartInfoElement.getKey());
			barchart.addValue(chartInfoElement.getValue(), "costo", subDay);
		}

		barchart.createChart();
//		barchart.saveChart("soyunbarchart", 500, 500);

		return barchart.getChart();
	}

	public void addDailyInfoToPanel(DynamicReportPanel byDayPanel) {
		JLabel titleLabel, valueLabel;

		for (int i = 0; i < keys.length; i++) {
			titleLabel = CommonUIComponents.createReportLabel(keys[i], Font.PLAIN, 17,
					CommonData.DARK_FONT_COLOR);
			titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

			valueLabel = CommonUIComponents.createReportLabel(values[i], Font.BOLD, 25,
					CommonData.DARK_FONT_COLOR);
			valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

			byDayPanel.addComponent(titleLabel, valueLabel);
		}

	}

	private String[] generateReportsForValues() {
		Integer soldProducts = 0;
		Double gain = 0.0;
		Double dailyCost = 0.0;
		Double totalGain = 0.0;

		SaleDailyReportResponse saleResponse = ReportService.getDailySaleReport();

		if (saleResponse.wasSuccessful()) {
			soldProducts = saleResponse.getQuantity();
			gain = saleResponse.getGain();
		}

		PurchaseDailyReportResponse purchaseResponse = ReportService.getDailyPurchaseReport();

		if (purchaseResponse.wasSuccessful()) {
			dailyCost = purchaseResponse.getTotalExpense();
		}

		totalGain = gain - initialBox - dailyCost;

		return new String[] { soldProducts.toString(),
				Utilities.truncateDecimal(gain, 2).toString(),
				Utilities.truncateDecimal(dailyCost, 2).toString(), initialBox.toString(),
				Utilities.truncateDecimal(totalGain, 2).toString() };
	}

	public void addYearInfoToPanel(DynamicReportPanel byYearPanel) {
		JLabel titleLabel = CommonUIComponents.createReportLabel("año", Font.PLAIN, 17,
				CommonData.DARK_FONT_COLOR);
		titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		JLabel valueLabel = CommonUIComponents.createReportLabel("2014", Font.BOLD, 25,
				CommonData.DARK_FONT_COLOR);
		valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		byYearPanel.addComponent(titleLabel, valueLabel);
	}

}