package com.pucara.view.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;

public class DynamicReportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Hashtable<String, Component> components;
	private JPanel leftPanel;

	public DynamicReportPanel(JFreeChart freeChart, boolean createInfoPanel) {
		components = new Hashtable<String, Component>();

		// Apply panel properties.
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		this.setLayout(new BorderLayout());

		// Create left panel.
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		if (createInfoPanel) {
			leftPanel = new JPanel();
			leftPanel.setBorder(new EmptyBorder(60, 100, 10, 20));
			leftPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
//			leftPanel.setPreferredSize(new Dimension(dim.width / 2 - 250, 0));
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

			this.add(leftPanel, BorderLayout.LINE_START);
		}

		// Add a chart panel in order to contain a JFreeChart.
		this.add(createChartPanel(freeChart, dim), BorderLayout.CENTER);
	}

	public void addComponent(JLabel label, Component component) {
		if (leftPanel != null) {
			leftPanel.add(label);
			leftPanel.add(CommonUIComponents.createNewVerticalSeparatorBox(5));

			leftPanel.add(component);
			leftPanel.add(CommonUIComponents.createNewVerticalSeparatorBox(5));

			components.put(label.getText(), component);
		}
	}

	public void addMouseListenerToComponent(String key, MouseListener mouseListener) {
		if (components != null && components.get(key) != null) {
			components.get(key).addMouseListener(mouseListener);
		}
	}

	public void updateLabelValues(String[] keys, String[] values) {
		JLabel label;

		for (int i = 0; i < keys.length; i++) {
			label = (JLabel) components.get(keys[i]);
			label.setText(values[i]);
		}
	}

	private ChartPanel createChartPanel(JFreeChart freeChart, Dimension dim) {
		ChartPanel chartPanel = new ChartPanel(freeChart);

		chartPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		chartPanel.setPreferredSize(new Dimension(dim.width / 2 - 50, dim.height / 2));
		chartPanel.setBorder(new EmptyBorder(20, 10, 30, 100));

		return chartPanel;
	}

}
