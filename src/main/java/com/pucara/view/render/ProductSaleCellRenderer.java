package com.pucara.view.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.core.entities.PartialElement;
import com.pucara.core.generic.Utilities;

/**
 * 
 * @author Maximiliano Fabian
 */
public class ProductSaleCellRenderer extends JPanel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;

	public ProductSaleCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		removeAll();
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		PartialElement entry = (PartialElement) value;

		JLabel description = new JLabel(entry.getDescription());
		JLabel barcode = new JLabel(entry.getBarcode());
		JLabel price = new JLabel(Utilities.getProductSalePrice(entry).toString());
		JLabel stock = new JLabel(String.format("%s elemento/s en stock", entry.getStock()));

		description.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 18));
		stock.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.ITALIC, 16));
		barcode.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 16));
		price.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 24));

		JPanel iconContainer = new JPanel();
		iconContainer.setLayout(new GridBagLayout());
		iconContainer.setBackground(new Color(0, 0, 255, 0));

		ImageIcon icon = createImageIcon(CommonData.RESOURCES_PATH + "withstock.png");
		JLabel iconLabel = new JLabel("", icon, JLabel.LEFT);
		iconLabel.setBackground(new Color(0, 0, 255, 0));
		iconLabel.setOpaque(true);
		iconContainer.add(iconLabel);
		iconContainer.add(CommonUIComponents.createNewHorizontalSeparatorBox(10));

		if (Integer.valueOf(entry.getStock()).equals(0)) {
			iconLabel.setIcon(createImageIcon(CommonData.RESOURCES_PATH + "withoutstock.png"));
		} else if (Integer.valueOf(entry.getStock()) <= entry.getMinStock()) {
			iconLabel.setIcon(createImageIcon(CommonData.RESOURCES_PATH + "minstock.png"));
		}

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBackground(CommonData.DEFAULT_SELECTION_COLOR);

		JLabel starLabel = getStarLabel(entry.getQuantity());
		container.add(starLabel, Component.LEFT_ALIGNMENT);
		container.add(description, Component.LEFT_ALIGNMENT);
		container.add(barcode, Component.LEFT_ALIGNMENT);
		container.add(stock, Component.LEFT_ALIGNMENT);

		if (isSelected) {
			setBackground(CommonData.DEFAULT_SELECTION_COLOR);
			iconLabel.setBackground(CommonData.DEFAULT_SELECTION_COLOR);
			starLabel.setBackground(CommonData.DEFAULT_SELECTION_COLOR);
			container.setBackground(CommonData.DEFAULT_SELECTION_COLOR);
			description.setForeground(CommonData.LIGHT_FONT_COLOR);
			barcode.setForeground(CommonData.LIGHT_FONT_COLOR);
			price.setForeground(CommonData.LIGHT_FONT_COLOR);
			starLabel.setForeground(CommonData.LIGHT_FONT_COLOR);
			stock.setForeground(CommonData.LIGHT_FONT_COLOR);
		} else {
			setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
			container.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
			description.setForeground(CommonData.DARK_FONT_COLOR);
			barcode.setForeground(Color.LIGHT_GRAY);
			price.setForeground(CommonData.DARK_FONT_COLOR);
			stock.setForeground(CommonData.DARK_FONT_COLOR);
		}

		this.add(iconContainer, BorderLayout.LINE_START);
		this.add(container, BorderLayout.CENTER);
		this.add(price, BorderLayout.LINE_END);

		return this;
	}

	private JLabel getStarLabel(int numberOfProducts) {
		ImageIcon starIcon = createImageIcon("resources/start.png");
		JLabel starLabel = new JLabel("" + numberOfProducts, starIcon, JLabel.LEFT);

		starLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 18));
		starLabel.setBackground(new Color(0, 0, 255, 0));
		starLabel.setForeground(CommonData.DARK_FONT_COLOR);
		starLabel.setOpaque(true);

		return starLabel;
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	private ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
