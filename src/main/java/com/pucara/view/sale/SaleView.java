package com.pucara.view.sale;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.pucara.view.render.ProductSaleCellRenderer;
import com.pucara.view.render.ProductSalePotentialCellRenderer;
import com.pucara.view.render.object.ListSalePotentialProduct;
import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.ProductView;
import com.pucara.common.SaleSummaryPanel;
import com.pucara.common.SwingListPanel;
import com.pucara.common.SystemForm;
import com.pucara.controller.sale.SaleController;

/**
 * Sale view of the application, it allows to visualize all partial products and
 * search by barcode/description.
 * 
 * @author Maximiliano Fabian
 */
public class SaleView extends ProductView {
	private static final long serialVersionUID = 1L;
	private SaleController saleController;
	private SwingListPanel listOfPartialProducts;
	private SwingListPanel listOfPotentialProducts;
	private JPanel centerContainer;
	private JPanel panelLeft;
	private SaleSummaryPanel summaryPanel;

	public SaleView() {
		saleController = new SaleController(this);
		generateContent();
	}

	/**
	 * 
	 * @param partialProductRows
	 */
	public void addPartialListToPanel(Object[] products) {
		if (listOfPartialProducts == null) {
			createCenterPanel(products);
			createFooterPanel(products);
		} else if (products.length > 0) {
			listOfPartialProducts.populateDataInTheList(products);
			summaryPanel.updateContent(products);
			cleanInputTextField();
		}
	}

	/**
	 * 
	 * @param index
	 */
	public void selectPartialElement(int index) {
		listOfPartialProducts.selectItemByIndex(index);
	}

	public void selectPartialElementByBarcode(String barcode) {
		listOfPartialProducts.selectBarcodeOnList(barcode);
	}

	/**
	 * 
	 */
	public void cleanInputTextField() {
		inputBarcode.setText(CommonData.EMPTY_STRING);
	}

	/**
	 * 
	 * @param listSaleProducts
	 * @param partialProductRows
	 */
	public void updatePartialElements(Object[] products) {
		if (products == null || products.length == 0) {
			this.remove(centerContainer);
			this.remove(summaryPanel);
			listOfPartialProducts = null;
			centerContainer = null;
			summaryPanel = null;
			this.validate();
			this.repaint();
		} else {
			listOfPartialProducts.populateDataInTheList(products);
			summaryPanel.updateContent(products);
		}
	}

	/**
	 * 
	 * @return String
	 */
	public String getSelectedProduct() {
		if (listOfPartialProducts != null) {
			return listOfPartialProducts.getSelectedBarcode(getSelectedIndex());
		} else {
			return CommonData.EMPTY_STRING;
		}
	}

	public String getBarcodeFromTextField() {
		return inputBarcode.getText();
	}

	public void setBarcodeToTextField(String barcode) {
		inputBarcode.setText(barcode);
	}

	public void cleanListSelection() {
		listOfPartialProducts.cleanSelection();
	}

	public boolean isFocusOnTextField() {
		return inputBarcode.hasFocus();
	}

	public void populateProductsOnList(ListSalePotentialProduct[] products) {
		JPanel potentialContainer = new JPanel();

		// Apply properties.
		potentialContainer.setBorder(new EmptyBorder(30, 0, 0, 0));
		potentialContainer.setLayout(new BorderLayout());
		potentialContainer.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Create list of products.
		listOfPotentialProducts = new SwingListPanel(products, this,
				new ProductSalePotentialCellRenderer());
		listOfPotentialProducts.addListMouseListener(saleController
				.createPotentialMouseListener());
		listOfPotentialProducts.addListKeyListener(saleController
				.generateListPotentialKeyListener());

		potentialContainer.add(listOfPotentialProducts, BorderLayout.CENTER);
		panelLeft.add(potentialContainer, BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}

	public void addPotentialProductsOnList(Object[] products) {
		listOfPotentialProducts.populateDataInTheList(products);
	}

	public String getPotentialItem() {
		return listOfPotentialProducts.getSelectedBarcode(getSelectedIndex());
	}

	public void selectPotentialElement(int index) {
		listOfPotentialProducts.requestFocusOnList();
		listOfPotentialProducts.selectItemByIndex(index);
	}

	public boolean isPotentialListPresent() {
		return listOfPotentialProducts.getNumberOfElements() > 0;
	}

	private void createFooterPanel(Object[] products) {
		summaryPanel = new SaleSummaryPanel(products);
		this.add(summaryPanel, BorderLayout.PAGE_END);
		this.validate();
	}

	/**
	 * 
	 */
	private void generateContent() {
		// Left panel - input text field and potential products list.
		createLeftPanel();
		populateProductsOnList(new ListSalePotentialProduct[] {});
	}

	/**
	 * @param partialProductRows
	 * 
	 */
	private void createCenterPanel(Object[] products) {
		centerContainer = new JPanel();

		// Apply properties.
		centerContainer.setBorder(new EmptyBorder(40, 30, 40, 30));
		centerContainer.setLayout(new BorderLayout());
		centerContainer.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		centerContainer.setPreferredSize(new Dimension(dim.width / 4
				+ dim.width / 8, 0));

		// Create list of products.
		listOfPartialProducts = new SwingListPanel(products, this,
				new ProductSaleCellRenderer());
		listOfPartialProducts.addListKeyListener(saleController
				.generateListKeyListener());

		centerContainer.add(listOfPartialProducts, BorderLayout.CENTER);
		this.add(centerContainer, BorderLayout.CENTER);
		this.validate();
	}

	/**
	 * 
	 */
	private void createLeftPanel() {
		panelLeft = new JPanel();

		// Set properties to the panel.
		panelLeft.setBorder(new EmptyBorder(40, 30, 40, 30));
		panelLeft.setLayout(new BorderLayout());
		panelLeft.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		panelLeft.setPreferredSize(new Dimension(dim.width / 4 + dim.width / 8,
				0));

		// Add text field for input with custom listeners.
		inputBarcode = CommonUIComponents.createInputTextField(null,
				saleController.createInputKeyListener());
		inputBarcode.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				inputBarcode = SystemForm.applySelectedProperties(inputBarcode);

				if (listOfPartialProducts != null) {
					listOfPartialProducts.cleanSelection();
				}

				if (listOfPotentialProducts != null) {
					listOfPotentialProducts.cleanSelection();
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				inputBarcode = SystemForm
						.applyUnselectedProperties(inputBarcode);
			}
		});

		panelLeft.add(inputBarcode, BorderLayout.PAGE_START);

		this.add(panelLeft, BorderLayout.LINE_START);
	}
}
