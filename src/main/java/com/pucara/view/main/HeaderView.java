package com.pucara.view.main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.controller.main.HeaderController;

/**
 * REMOVE!
 * Header view, implemented with JPanel.
 * 
 * @author Maximiliano
 */
public class HeaderView extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String ORIENTATION = BorderLayout.NORTH;
	private HeaderController headerController;
	// Labels represent the menu system options.
	// private JLabel categoryLabel;
	private JLabel saleLabel;
	private JLabel stockLabel;
	private JLabel reportsLabel;
	private JLabel purchaseLabel;
	private JLabel configurationLabel;
	private JLabel closeLabel;

	public HeaderView(MainView mainView) {
		headerController = new HeaderController(this, mainView);
		generateContent();
	}

	public void changeToBold(JLabel label) {
		cleanAllLabels();
		// label.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD,
		// CommonData.GENERAL_FONT_SIZE_LABEL));
		label.setForeground(CommonData.DEFAULT_SELECTION_COLOR);
	}

	public void changeToLight(JLabel label) {
		// label.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.PLAIN,
		// CommonData.GENERAL_FONT_SIZE_LABEL));
		label.setForeground(CommonData.DARK_FONT_COLOR);
	}

	public void changeCursorLabel(JLabel label) {
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public JLabel getSaleLabel() {
		return saleLabel;
	}

	public JLabel getPurchaseLabel() {
		return purchaseLabel;
	}

	// public JLabel getCategoryLabel() {
	// return categoryLabel;
	// }

	public JLabel getStockLabel() {
		return stockLabel;
	}

	public JLabel getReportsLabel() {
		return reportsLabel;
	}

	public JLabel getCloseLabel() {
		return closeLabel;
	}

	public JLabel getConfigurationLabel() {
		return configurationLabel;
	}

	private void cleanAllLabels() {
		changeToLight(saleLabel);
		changeToLight(purchaseLabel);
		changeToLight(stockLabel);
		changeToLight(reportsLabel);
		changeToLight(configurationLabel);
		// changeToLight(closeLabel);
	}

	/**
	 * Generates all components inside Header panel.
	 * 
	 * @param frame
	 */
	private void generateContent() {
		// Configure a Flow layout, set all items at right ...
		FlowLayout flowLayout = (FlowLayout) this.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);

		// Add Empty border and background ...
		this.setBorder(new EmptyBorder(10, 10, 10, 15));
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Add each label depending of the menu ...

		// Sale
		saleLabel = CommonUIComponents.createMenuLabel("ventas",
				headerController.createSaleListener());
		// saleLabel.setFont(new Font(CommonData.ROBOTO_LIGHT_FONT, Font.BOLD,
		// CommonData.GENERAL_FONT_SIZE_LABEL));
		saleLabel.setForeground(CommonData.DEFAULT_SELECTION_COLOR);
		this.add(saleLabel);

		/**
		 * Separate panels.
		 */
		this.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE));

		// Purchase
		purchaseLabel = CommonUIComponents.createMenuLabel("gastos",
				headerController.createPurchaseListener());
		this.add(purchaseLabel);

		/**
		 * Separate panels.
		 */
		this.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE));

		// Stock
		stockLabel = CommonUIComponents.createMenuLabel("stock",
				headerController.createStockListener());
		this.add(stockLabel);

		/**
		 * Separate panels.
		 */
		this.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE));

		// Reports
		reportsLabel = CommonUIComponents.createMenuLabel("reportes",
				headerController.createReportsListener());
		this.add(reportsLabel);

		/**
		 * Separate panels.
		 */
		this.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE));

		// Configuration
		configurationLabel = CommonUIComponents.createMenuLabel("opciones",
				headerController.createConfigurationListener());
		this.add(configurationLabel);

		/**
		 * Separate panels.
		 */
		this.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE));

		// Close current frame
		closeLabel = CommonUIComponents.createMenuIconLabel("powerblack.png",
				headerController.createCloseListener());
		this.add(closeLabel);
	}

}
