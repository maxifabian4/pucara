package com.pucara.controller.report;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.CommonData;
import com.pucara.common.PropertyFile;
import com.pucara.core.charts.BarChart;
import com.pucara.core.charts.ChartFactory;
import com.pucara.core.charts.LineChart;
import com.pucara.core.charts.RingChart;
import com.pucara.core.entities.report.ChartInfoElement;
import com.pucara.core.generic.Utilities;
import com.pucara.core.response.ChartInfoResponse;
import com.pucara.core.response.DailyExpensesResponse;
import com.pucara.core.response.SaleDailyReportResponse;
import com.pucara.core.services.category.CategoryService;
import com.pucara.core.services.report.ReportService;
import com.pucara.persistence.domain.ProductsCategoryHelper;
import com.pucara.persistence.domain.DailyExpensesHelper;
import com.pucara.view.report.ReportView;

/**
 * This class represents the Report controller in the system.
 * 
 * @author Maximiliano Fabian
 */
public class ReportController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportController.class);
	private ReportView reportView;
	private String[] keys = new String[] { "productos vendidos",
			"ganancia (valor venta)", "costo del d\u00EDa", "caja inicial",
			"caja actual" };
	private Double initialBox;

	public ReportController(ReportView reportView) {
		this.reportView = reportView;
		this.initialBox = getInitialBoxFromProperty();

		addAllInformationToView(generateReportsForValues());
		addSpecificListenersToComponents();
		addAllChartsToView(generateCharts());
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

					// Update report view with the new information.
					reportView.removeAllInformationPanel();
					addAllInformationToView(generateReportsForValues());
					addSpecificListenersToComponents();
					reportView.validate();
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
				DailyExpensesResponse response = ReportService
						.getDailyExpensesReport(new Date());

				if (!response.wasSuccessful()) {
					JOptionPane.showMessageDialog(null,
							"Error tratando de obtener los gastos de sistema.");
				} else {
					List<DailyExpensesHelper> list = response.getExpensesList();

					reportView.displayExpensesInformationList(list,
							createActionForPickerDate());
				}
			}
		};
	}

	private ActionListener createActionForPickerDate() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DailyExpensesResponse response = ReportService
						.getDailyExpensesReport(reportView.getSelectedDate());

				if (!response.wasSuccessful()) {
					JOptionPane.showMessageDialog(null,
							"Error tratando de obtener los gastos de sistema.");
				} else {
					List<DailyExpensesHelper> list = response.getExpensesList();
					reportView.updateExpensesInformationList(list);
				}
			}
		};
	}

	private ChartPanel createLineChartByYear() {
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

		return createChartPanel(lineChart.getChart());
	}

	private ChartPanel createBarChartByDay() {
		BarChart barchart = (BarChart) ChartFactory.createChart(
				ChartFactory.BARCHART, "Ventas/Costo por d\u00EDa",
				"d\u00EDas", "pesos");

		ChartInfoResponse gainResponse = ReportService.getChartByDayInfo(-6,
				CommonData.GAIN_BY_DAY);
		ChartInfoResponse expenseResponse = ReportService.getChartByDayInfo(-6,
				CommonData.EXPENSE_BY_DAY);
		String subDay;

		for (ChartInfoElement chartInfoElement : gainResponse
				.getChartInformation()) {
			subDay = Utilities.getSpanishDay(chartInfoElement.getKey());
			barchart.addValue(chartInfoElement.getValue(), "venta", subDay);
		}

		for (ChartInfoElement chartInfoElement : expenseResponse
				.getChartInformation()) {
			subDay = Utilities.getSpanishDay(chartInfoElement.getKey());
			barchart.addValue(chartInfoElement.getValue(), "costo", subDay);
		}

		barchart.createChart();

		return createChartPanel(barchart.getChart());
	}

	private ChartPanel createCategoryPieChart() {
		RingChart piechart = (RingChart) ChartFactory.createChart(
				ChartFactory.RINGCHART, "Productos por categoría",
				CommonData.EMPTY_STRING, CommonData.EMPTY_STRING);

		List<ProductsCategoryHelper> list = CategoryService
				.getSoldProductsByCategory();

		for (ProductsCategoryHelper item : list) {
			piechart.addValue(
					item.getCategoryName() + " (" + item.getNumberOfProducts()
							+ ")", new Double(item.getNumberOfProducts()));
		}

		piechart.createChart();

		return createChartPanel(piechart.getChart());
	}

	private ChartPanel createChartPanel(JFreeChart freeChart) {
		ChartPanel chartPanel = new ChartPanel(freeChart);

		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		chartPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		chartPanel.setPreferredSize(new Dimension(dim.width / 2 - 50,
				dim.height / 4));
		chartPanel.setBorder(new EmptyBorder(0, 30, 15, 30));

		return chartPanel;
	}

	private String[] generateReportsForValues() {
		Integer soldProducts = 0;
		Double gain = 0.0;
		Double sold = 0.0;
		Double dailyCost = 0.0;
		Double totalBox = 0.0;

		SaleDailyReportResponse saleResponse = ReportService
				.getDailySaleReport();

		if (saleResponse.wasSuccessful()) {
			soldProducts = saleResponse.getQuantity();
			gain = saleResponse.getGain();
			gain = Utilities.adjustDecimals(gain);
			sold = saleResponse.getSold();
			sold = Utilities.adjustDecimals(sold);
		}

		DailyExpensesResponse purchaseResponse = ReportService
				.getDailyExpensesReport(new Date());

		if (purchaseResponse.wasSuccessful()) {
			dailyCost = purchaseResponse.getTotalExpense();
			dailyCost = Utilities.adjustDecimals(dailyCost);
		}

		totalBox = initialBox - dailyCost + sold;

		return new String[] { soldProducts.toString(),
				String.format("%s (%s)", gain.toString(), sold.toString()),
				dailyCost.toString(), initialBox.toString(),
				totalBox.toString() };
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

	private ChartPanel[] generateCharts() {
		return new ChartPanel[] { createBarChartByDay(),
				createLineChartByYear(), createCategoryPieChart() };
	}

	private void addAllChartsToView(ChartPanel[] charts) {
		for (ChartPanel chartPanel : charts) {
			reportView.addNewChartToPanel(chartPanel);
		}
	}

	private void addAllInformationToView(String[] values) {
		for (int i = 0; i < values.length; i++) {
			reportView.addNewInfoToPanel(keys[i], values[i]);
		}
	}

	private void addSpecificListenersToComponents() {
		reportView.addMouseListenerToComponent("caja inicial",
				createMouseListenerForInitialBox());
		reportView.addMouseListenerToComponent("costo del d\u00EDa",
				createMouseListenerForDailyExpenses());
	}

}
