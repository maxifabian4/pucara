package com.pucara.view.report;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartPanel;

import com.pucara.common.CommonData;
import com.pucara.common.SwingListPanel;
import com.pucara.common.SystemPopup;
import com.pucara.core.entities.report.PurchaseDailyReport;
import com.pucara.persistence.domain.DailyExpensesHelper;
import com.pucara.view.render.ExpensesCellRenderer;

/**
 * 
 * @author Maximiliano Fabian
 */
public class ReportView extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int TOP_INFOPANEL_VALUE = 60;
	private JPanel infoPanel;
	private JPanel chartsPanel;
	private Hashtable<String, Component> components;

	public ReportView() {
		// Apply properties to the report view.
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Initialize the information panel.
		initializeInfoPanel();

		// Initialize the information panel.
		chartsPanel = new JPanel();
		chartsPanel.setLayout(new GridLayout(1, 0));
		chartsPanel.setBorder(new EmptyBorder(TOP_INFOPANEL_VALUE, 0, 0, 0));
		chartsPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Add both panels.
		this.add(chartsPanel);
		this.add(infoPanel);

		// Initialize hash for components.
		components = new Hashtable<String, Component>();
	}

	public void addNewInfoToPanel(String key, String value) {
		infoPanel.add(newInfoPanel(key, value));
	}

	public void addNewChartToPanel(ChartPanel chart) {
		chartsPanel.add(chart);
	}

	public void addMouseListenerToComponent(String key,
			MouseListener mouseListener) {
		if (components != null && components.get(key) != null) {
			components.get(key).addMouseListener(mouseListener);
		}
	}

	// Should be removed.
	public void displayExpenseInformationList(List<PurchaseDailyReport> list) {
		SwingListPanel panel = new SwingListPanel(list.toArray(), null,
				new ExpensesCellRenderer());
		SystemPopup popup = new SystemPopup(panel);
		popup.setVisible(true);
	}

	public void displayExpensesInformationList(List<DailyExpensesHelper> list) {
		SwingListPanel panel = new SwingListPanel(list.toArray(), null,
				new ExpensesCellRenderer());
		SystemPopup popup = new SystemPopup(panel);
		popup.setVisible(true);
	}

	public void removeAllInformationPanel() {
		infoPanel.removeAll();
		this.validate();
	}

	private void initializeInfoPanel() {
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1, 0));
		infoPanel.setBorder(new EmptyBorder(TOP_INFOPANEL_VALUE, 0, 0, 0));
		infoPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
	}

	private JPanel newInfoPanel(String label, String number) {
		JPanel infoContainer = new JPanel();

		infoContainer.setLayout(new BoxLayout(infoContainer, BoxLayout.Y_AXIS));
		infoContainer.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		JLabel key = new JLabel(label);
		key.setAlignmentX(Component.CENTER_ALIGNMENT);
		key.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 15));
		key.setForeground(CommonData.DARK_FONT_COLOR);

		JLabel value = new JLabel(number);
		value.setAlignmentX(Component.CENTER_ALIGNMENT);
		value.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 30));
		value.setForeground(CommonData.DARK_FONT_COLOR);

		infoContainer.add(key);
		infoContainer.add(value);

		// Add this values to the hash.
		components.put(key.getText(), value);

		return infoContainer;
	}

}
