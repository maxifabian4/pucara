package com.pucara.common;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * TO BE REMOVED !!
 * @author pucara
 *
 */
public class ReportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Component> components;
	private JPanel leftPanel;

	public ReportPanel(String[] keys, String[] values, JFreeChart freeChart) {
		components = new Hashtable<String, Component>();

		// Apply panel properties.
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		this.setLayout(new BorderLayout());

		// Create left panel.
		leftPanel = new JPanel();

		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		leftPanel.setBorder(new EmptyBorder(60, 10, 10, 20));
		leftPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		leftPanel.setPreferredSize(new Dimension(dim.width / 2 - 150, 0));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

		// Add labels and save keys.
		addNewLabels(leftPanel, keys, values);
		this.add(leftPanel, BorderLayout.LINE_START);

		// Add a chart panel in order to contain a JFreeChart.
		this.add(createChartPanel(freeChart, dim), BorderLayout.CENTER);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @param mouseListener
	 */
	public void addMouseListenerToComponent(String key, MouseListener mouseListener) {
		components.get(key).addMouseListener(mouseListener);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
//	public void updateLabelValueByKey(String key, String value) {
//		JLabel label = (JLabel) components.get(key);
//		label.setText(value);
//	}

	public void updateLabelValues(String[] keys, String[] values) {
		JLabel label;

		for (int i = 0; i < keys.length; i++) {
			label = (JLabel) components.get(keys[i]);
			label.setText(values[i]);
		}
	}

	/**
	 * 
	 * @param freeChart
	 * @param dim
	 * @return
	 */
	private ChartPanel createChartPanel(JFreeChart freeChart, Dimension dim) {
		ChartPanel chartPanel = new ChartPanel(freeChart);

		chartPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		chartPanel.setPreferredSize(new Dimension(dim.width / 2 - 50, dim.height / 2));
		chartPanel.setBorder(new EmptyBorder(20, 10, 30, 100));

		return chartPanel;
	}

	/**
	 * 
	 * @param leftPanel
	 * @param keys
	 * @param values
	 */
	private void addNewLabels(JPanel leftPanel, String[] keys, String[] values) {
		JLabel titleLabel, valueLabel;

		for (int i = 0; i < keys.length; i++) {
			titleLabel = CommonUIComponents.createReportLabel(keys[i], Font.PLAIN, 17,
					CommonData.DARK_FONT_COLOR);
			titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

			valueLabel = CommonUIComponents.createReportLabel(values[i], Font.BOLD, 25,
					CommonData.DARK_FONT_COLOR);
			valueLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

			leftPanel.add(titleLabel);
			leftPanel.add(CommonUIComponents.createNewVerticalSeparatorBox(5));
			leftPanel.add(valueLabel);
			leftPanel.add(CommonUIComponents.createNewVerticalSeparatorBox(20));

			// Save element inside the hash table.
			components.put(keys[i], valueLabel);
		}
	}

}
