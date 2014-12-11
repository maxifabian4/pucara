package com.pucara.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pucara.core.entities.PartialElement;

/**
 * @author Maximiliano Fabian
 * 
 */
public class SaleSummaryPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String numberOfProducts;
	private String total;
	private String totalInitial;
	private JLabel numberOfProdLabel;
	private JLabel totalLabel;
	private JLabel totalInitialLabel;

	public SaleSummaryPanel(Object[] products) {
		generateInformation(products);
		createContent();
	}

	private void generateInformation(Object[] products) {
		Integer number = 0;
		Double totalCost = 0.0;
		Double totalInitialCost = 0.0;
		PartialElement partialProduct;

		for (int i = 0; i < products.length; i++) {
			partialProduct = (PartialElement) products[i];
			number += Integer.valueOf(partialProduct.getQuantity());
			totalCost += partialProduct.getFinalCost()
					* Integer.valueOf(partialProduct.getQuantity());
			totalInitialCost += partialProduct.getInitialCost()
					* Integer.valueOf(partialProduct.getQuantity());
		}

		if (number.equals(1)) {
			numberOfProducts = String.format("%d producto", number);
		} else {
			numberOfProducts = String.format("%d productos", number);
		}

		total = String.valueOf(totalCost);
		totalInitial = String.valueOf(totalInitialCost);
	}

	private void createContent() {
		// Apply properties to the container panel.
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);

		// Create label to show the number of products.
		numberOfProdLabel = new JLabel(numberOfProducts);
		numberOfProdLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, 18));
		numberOfProdLabel.setForeground(Color.LIGHT_GRAY);
		numberOfProdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Create label to show the total final price.
		totalLabel = new JLabel(total);
		totalLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN,
				37));
		totalLabel.setForeground(CommonData.DARK_FONT_COLOR);
		totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Create label to show the total initial price.
		totalInitialLabel = new JLabel(totalInitial);
		totalInitialLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, 29));
		totalInitialLabel.setForeground(Color.LIGHT_GRAY);
		totalInitialLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Add labels to the container.
		this.add(numberOfProdLabel);
		this.add(totalLabel);
		this.add(totalInitialLabel);
	}

	public void updateContent(Object[] products) {
		generateInformation(products);
		numberOfProdLabel.setText(numberOfProducts);
		totalLabel.setText(total);
		this.validate();
	}

}