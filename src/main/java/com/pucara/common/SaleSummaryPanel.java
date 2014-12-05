package com.pucara.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.NotUpdatable;
import com.pucara.core.entities.PartialElement;

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
			totalCost += partialProduct.getFinalCost()
					* Integer.valueOf(partialProduct.getQuantity());
		}

		if (number.equals(1)) {
			numberOfProducts = String.format("%d producto", number);
		} else {
			numberOfProducts = String.format("%d productos", number);
		}

		total = String.valueOf(totalCost);
	}

	private void createContent() {
		// Apply properties to the container panel.
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// this.setLayout(new BorderLayout());
		// this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setBackground(Color.WHITE);

		// Create labels and apply format.
		numberOfProdLabel = new JLabel(numberOfProducts);
		numberOfProdLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT,
				Font.PLAIN, 18));
		numberOfProdLabel.setForeground(Color.LIGHT_GRAY);
		numberOfProdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		totalLabel = new JLabel(total);
		totalLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN,
				37));
		totalLabel.setForeground(CommonData.DARK_FONT_COLOR);
		totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Add labels to the container.
		this.add(numberOfProdLabel);
		this.add(totalLabel);
	}

	public void updateContent(Object[] products) {
		generateInformation(products);
		numberOfProdLabel.setText(numberOfProducts);
		totalLabel.setText(total);
		this.validate();
	}

}