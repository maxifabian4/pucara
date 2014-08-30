package com.pucara.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pucara.core.entities.PartialElement;
import com.pucara.core.generic.Utilities;

/**
 * @author Maximiliano Fabian
 * 
 */
public class SaleSummaryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String numberOfProducts;
	private String total;
	private JLabel numberOfProdLabel;
	private JLabel totalLabel;

	public SaleSummaryPanel(Object[] products) {
		generateInformation(products);
		createContent();
	}

	private void generateInformation(Object[] products) {
		Integer number = 0;
		Double totalCost = 0.0;
		PartialElement partialProduct;

		for (int i = 0; i < products.length; i++) {
			partialProduct = (PartialElement) products[i];
			number += Integer.valueOf(partialProduct.getQuantity());
			totalCost += Utilities.getProductSalePrice(partialProduct)
					* Integer.valueOf(partialProduct.getQuantity());
		}

		if (number.equals(1)) {
			numberOfProducts = String.format("%d producto", number);
		} else {
			numberOfProducts = String.format("%d productos", number);
		}

		total = String.format("%s", Utilities.truncateDecimal(totalCost, 2));
	}

	private void createContent() {
		// Apply properties to the container panel.
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Create labels and apply format.
		numberOfProdLabel = new JLabel(numberOfProducts);
		numberOfProdLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 20));
		numberOfProdLabel.setForeground(Color.LIGHT_GRAY);

		totalLabel = new JLabel(total);
		totalLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD, 60));
		totalLabel.setForeground(CommonData.DARK_FONT_COLOR);

		// Add labels to the container.
		this.add(numberOfProdLabel, Component.LEFT_ALIGNMENT);
		this.add(totalLabel, Component.LEFT_ALIGNMENT);
	}

	public void updateContent(Object[] products) {
		generateInformation(products);
		numberOfProdLabel.setText(numberOfProducts);
		totalLabel.setText(total);
		this.validate();
	}

}