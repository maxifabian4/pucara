package com.pucara.view.report;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import org.jfree.chart.ChartPanel;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.SwingListPanel;
import com.pucara.common.SystemPopup;
import com.pucara.core.generic.Utilities;
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
	private SwingListPanel expensesListPanel;
	private Hashtable<String, Component> components;
	private JDatePickerImpl datePicker;

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

	public void displayExpensesInformationList(List<DailyExpensesHelper> list,
			ActionListener action) {
		expensesListPanel = new SwingListPanel(list.toArray(), null,
				new ExpensesCellRenderer());

		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		datePicker = new JDatePickerImpl(datePanel);
		datePicker.addActionListener(action);

//		SystemPopup popup = new SystemPopup(joinPanelAndPicker(
//				expensesListPanel, datePicker), "Gastos del día");
//		popup.setVisible(true);
	}

	public void removeAllInformationPanel() {
		infoPanel.removeAll();
		this.validate();
	}

	public void updateExpensesInformationList(List<DailyExpensesHelper> list) {
		expensesListPanel.populateDataInTheList(list.toArray());
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

	private Component joinPanelAndPicker(SwingListPanel infoPanel,
			JDatePickerImpl datePicker) {
		JPanel generalContainer = new JPanel();
		generalContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		generalContainer.setLayout(new BorderLayout());
		generalContainer.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		headerPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		JLabel subTitle = new JLabel("seleccionar fecha");
		subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		subTitle.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 15));
		subTitle.setForeground(CommonData.DARK_FONT_COLOR);

		headerPanel.add(subTitle);
		headerPanel.add(CommonUIComponents.createNewVerticalSeparatorBox(10));
		headerPanel.add(datePicker);

		generalContainer.add(headerPanel, BorderLayout.PAGE_START);
		generalContainer.add(infoPanel, BorderLayout.CENTER);

		return generalContainer;
	}

	public Date getSelectedDate() {
		if (datePicker == null) {
			return new Date();
		} else {
			return Utilities.getDateFrom(datePicker.getModel().getYear(),
					datePicker.getModel().getMonth() + 1, datePicker.getModel()
							.getDay());
		}
	}
}
