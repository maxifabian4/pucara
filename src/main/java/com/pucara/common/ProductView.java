package com.pucara.common;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class ProductView extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JTextField inputBarcode;
	private int selectedBarcodeIndex;

	public ProductView() {
		// Main configuration panel/view.
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		this.setLayout(new BorderLayout());
	}

	public void saveSelectedIndex(int minSelectionIndex) {
		selectedBarcodeIndex = minSelectionIndex;
	}

	public int getSelectedIndex() {
		return selectedBarcodeIndex;
	}

	public void setFocusOnInput() {
		inputBarcode.requestFocusInWindow();
	}
	
	public abstract void cleanSummaryLabel();
}
