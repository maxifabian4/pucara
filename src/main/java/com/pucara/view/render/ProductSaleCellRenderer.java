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
import com.pucara.core.entities.PartialElement;

/**
 * 
 * @author Maximiliano Fabian
 */
public class ProductSaleCellRenderer extends JPanel implements
		ListCellRenderer<Object> {
	private static final long serialVersionUID = 1L;

	public ProductSaleCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		removeAll();
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		PartialElement entry = (PartialElement) value;

		JLabel description = new JLabel(entry.getDescription());
		JLabel barcode = new JLabel(entry.getBarcode());
		JLabel finalCost = new JLabel(entry.getFinalCost().toString());
		JLabel initialCost = new JLabel(entry.getInitialCost().toString());
		JLabel stock = new JLabel(String.format("%s elemento/s en stock",
				entry.getStock()));

		description.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD,
				16));
		stock.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.ITALIC, 14));
		barcode.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 14));
		finalCost
				.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN, 24));
		initialCost.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD,
				19));

		JPanel iconContainer = new JPanel();
		iconContainer.setLayout(new GridBagLayout());
		iconContainer.setBorder(new EmptyBorder(0, 10, 0, 0));
		iconContainer.setBackground(new Color(0, 0, 255, 0));

		ImageIcon icon = CommonUIComponents
				.createImageIcon(CommonData.IMAGES_PATH + "withstock.png");
		JLabel iconLabel = new JLabel("", icon, JLabel.LEFT);
		iconLabel.setBackground(new Color(0, 0, 255, 0));
		iconLabel.setOpaque(true);
		iconContainer.add(iconLabel);
		iconContainer.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(10));

		if (Integer.valueOf(entry.getStock()).equals(0)) {
			iconLabel.setIcon(CommonUIComponents
					.createImageIcon(CommonData.IMAGES_PATH
							+ "withoutstock.png"));
		} else if (Integer.valueOf(entry.getStock()) <= entry.getMinStock()) {
			iconLabel.setIcon(CommonUIComponents
					.createImageIcon(CommonData.IMAGES_PATH + "minstock.png"));
		}

		JPanel container = new JPanel();
		container.setBorder(new EmptyBorder(5, 5, 5, 5));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBackground(CommonData.DEFAULT_SELECTION_COLOR);

		JLabel starLabel = getStarLabel(entry.getQuantity());
		container.add(starLabel, Component.LEFT_ALIGNMENT);
		container.add(description, Component.LEFT_ALIGNMENT);
		container.add(barcode, Component.LEFT_ALIGNMENT);
		container.add(stock, Component.LEFT_ALIGNMENT);

		// ordenar !!!
		setBackground(Color.WHITE);
		container.setBackground(Color.WHITE);
		description.setForeground(CommonData.DARK_FONT_COLOR);
		barcode.setForeground(Color.LIGHT_GRAY);
		finalCost.setForeground(CommonData.DARK_FONT_COLOR);
		initialCost.setForeground(Color.LIGHT_GRAY);
		stock.setForeground(CommonData.DARK_FONT_COLOR);

		if (isSelected) {

			MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 3, 0,
					0, CommonData.DEFAULT_SELECTION_COLOR);
			setBorder(matteBorder);
		} else {

			MatteBorder matteBorder = BorderFactory.createMatteBorder(0, 3, 0,
					0, Color.WHITE);
			setBorder(matteBorder);
		}

		this.add(iconContainer, BorderLayout.LINE_START);
		this.add(container, BorderLayout.CENTER);

		JPanel pricesContainer = new JPanel();
		pricesContainer.setBorder(new EmptyBorder(0, 0, 0, 10));
		pricesContainer.setLayout(new BoxLayout(pricesContainer,
				BoxLayout.X_AXIS));
		pricesContainer.setBackground(Color.WHITE);
		finalCost.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		pricesContainer.add(finalCost);
		pricesContainer.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(10));
		initialCost.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		pricesContainer.add(initialCost);
		this.add(pricesContainer, BorderLayout.LINE_END);

		return this;
	}

	private JLabel getStarLabel(int numberOfProducts) {
		ImageIcon starIcon = CommonUIComponents
				.createImageIcon(CommonData.IMAGES_PATH + "start.png");
		JLabel starLabel = new JLabel("" + numberOfProducts, starIcon,
				JLabel.LEFT);

		starLabel
				.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 16));
		starLabel.setBackground(new Color(0, 0, 255, 0));
		starLabel.setForeground(CommonData.DARK_FONT_COLOR);
		starLabel.setOpaque(true);

		return starLabel;
	}

}
