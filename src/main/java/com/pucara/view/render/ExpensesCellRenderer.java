package com.pucara.view.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.core.entities.Product;
import com.pucara.core.entities.report.PurchaseDailyReport;

/**
 * 
 * @author Maximiliano Fabian
 */
public class ExpensesCellRenderer extends JPanel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;

	public ExpensesCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		removeAll();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		PurchaseDailyReport entry = (PurchaseDailyReport) value;

		// Creates labels.
		JLabel dateLabel = new JLabel(entry.getDate().toString());
		dateLabel
				.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 15));
		dateLabel.setForeground(CommonData.DARK_FONT_COLOR);
		dateLabel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		JLabel descLabel = new JLabel(entry.getPurchaseDescription());
		descLabel
				.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 15));
		descLabel.setForeground(CommonData.DARK_FONT_COLOR);
		descLabel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		JLabel expenseLabel = new JLabel(String.valueOf(entry.getExpense()));
		expenseLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN,
				15));
		expenseLabel.setForeground(CommonData.DARK_FONT_COLOR);
		expenseLabel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Create center container.
		JPanel containerLeft = new JPanel();
		containerLeft.setLayout(new BoxLayout(containerLeft, BoxLayout.Y_AXIS));
		containerLeft.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Add labels to the center container.
		containerLeft.add(dateLabel, Component.LEFT_ALIGNMENT);

		JPanel containerCenter = new JPanel();
		containerCenter.setLayout(new BoxLayout(containerCenter,
				BoxLayout.Y_AXIS));
		containerCenter.setBorder(new EmptyBorder(0, 20, 0, 0));
		containerCenter.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Add labels to the center container.
		containerCenter.add(descLabel, Component.LEFT_ALIGNMENT);

		JPanel containerRight = new JPanel();
		containerRight
				.setLayout(new BoxLayout(containerRight, BoxLayout.Y_AXIS));
		containerRight.setBorder(new EmptyBorder(0, 20, 0, 0));
		containerRight.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Add labels to the center container.
		containerRight.add(expenseLabel, Component.RIGHT_ALIGNMENT);

		if (isSelected) {
			setBackground(CommonData.DEFAULT_SELECTION_COLOR);
			dateLabel.setForeground(CommonData.LIGHT_FONT_COLOR);
			containerLeft.setBackground(CommonData.DEFAULT_SELECTION_COLOR);

			descLabel.setForeground(CommonData.LIGHT_FONT_COLOR);
			containerCenter.setBackground(CommonData.DEFAULT_SELECTION_COLOR);

			expenseLabel.setForeground(CommonData.LIGHT_FONT_COLOR);
			containerRight.setBackground(CommonData.DEFAULT_SELECTION_COLOR);
		} else {
			setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
			dateLabel.setForeground(CommonData.DARK_FONT_COLOR);
			containerLeft.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

			descLabel.setForeground(CommonData.DARK_FONT_COLOR);
			containerCenter.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

			expenseLabel.setForeground(CommonData.DARK_FONT_COLOR);
			containerRight.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		}

		this.add(containerLeft, BorderLayout.LINE_START);
		this.add(containerCenter, BorderLayout.CENTER);
		this.add(containerRight, BorderLayout.LINE_END);

		return this;
	}
}
