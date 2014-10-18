package com.pucara.view.stock;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.pucara.view.render.ProductStockCellRenderer;
import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.ProductView;
import com.pucara.common.SwingListPanel;
import com.pucara.common.SystemForm;
import com.pucara.common.SystemPopup;
import com.pucara.controller.observable.UpdatesSource;
import com.pucara.controller.stock.StockController;
import com.pucara.core.entities.Category;
import com.pucara.core.entities.Product;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.services.category.CategoryService;
import com.pucara.core.services.product.ProductService;

/**
 * 
 * @author Maximiliano Fabian
 */
public class StockView extends ProductView {
	private static final long serialVersionUID = 1L;
	private StockController stockController;
	private SwingListPanel listOfProducts;
	private SystemForm form;
	private SystemPopup popup;
	private String[] textFieldKeys = { "c\u00F3digo", "descripci\u00F3n",
			"costo inicial", "porcentaje", "stock m\u00EDnimo" };
	private String[] textFieldPlusKeys = { "c\u00F3digo", "descripci\u00F3n",
			"costo inicial", "costo final", "stock m\u00EDnimo" };
	private String[] textFieldKeysUpdate = { "descripci\u00F3n",
			"costo inicial", "porcentaje", "stock m\u00EDnimo" };
	private boolean byPercentage;

	public StockView(UpdatesSource subject) {
		byPercentage = false;
		stockController = new StockController(this, subject);
		subject.addObserver(stockController);
		// applyPanelProperties();
		generateContent(textFieldPlusKeys);
	}

	/**
	 * Probably we need to remove it ...
	 */
	// public void selectInputTextField() {
	// inputBarcode.requestFocus();
	// inputBarcode.requestFocusInWindow();
	// }

	// CHANGE !!!!!!!!!!!!
	public String getProductBarcode() {
		JTextField tf = (JTextField) form
				.getComponentFormAt(CommonUIComponents.BARCODE);
		return tf.getText();
	}

	public String getProductDescription() {
		JTextField tf = (JTextField) form
				.getComponentFormAt(CommonUIComponents.DESCRIPTION);
		return tf.getText();
	}

	// public String getProductInitialCost() {
	// JTextField tf = (JTextField) form
	// .getComponentFormAt(CommonUIComponents.COST);
	// return tf.getText();
	// }

	public String getProductPercentage() {
		JTextField tf = (JTextField) form
				.getComponentFormAt(CommonUIComponents.PERCENTAGE);
		return tf.getText();
	}

	public String getProductStockMin() {
		JTextField tf = (JTextField) form
				.getComponentFormAt(CommonUIComponents.MIN_STOCK);
		return tf.getText();
	}

	public Category getProductCategory() {
		JComboBox cb = (JComboBox) form
				.getComponentFormAt(CommonUIComponents.CATEGORY);
		return (Category) cb.getSelectedItem();
	}

	public boolean getNewByPercentage() {
		JCheckBox box = (JCheckBox) form
				.getComponentFormAt(CommonUIComponents.BY_PERCENTAGE);
		return box.isSelected();
	}

	public String getInitialCost() {
		JTextField tf = (JTextField) form
				.getComponentFormAt(CommonUIComponents.INITIAL_COST);
		return tf.getText();
	}

	public String getFinalCost() {
		JTextField tf = (JTextField) form
				.getComponentFormAt(CommonUIComponents.FINAL_COST);
		return tf.getText();
	}

	public List<String> getPopupComponents() {
		return popup.getAllTextFieldValues(textFieldKeysUpdate);
	}

	/**
	 * 
	 * @return String
	 */
	public String getProductToFind() {
		return inputBarcode.getText();
	}

	/**
	 *
	 */
	public void updateProductsList() {
		listOfProducts.populateDataInTheList(ProductService.getAllProducts()
				.getAllProducts().toArray());
	}

	/**
	 * 
	 * @param barcode
	 */
	public void selectProductElementOnList(String barcode) {
		listOfProducts.selectBarcodeOnList(barcode);
	}

	/**
	 * 
	 * @param products
	 * @param descriptionColumn
	 */
	public void selectProductElementsOnList(List<Product> products) {
		listOfProducts.selectMultipleBarcodeOnList(products);
	}

	/**
	 * 
	 */
	public void displayPopup() {
		String barcode = listOfProducts.getSelectedBarcode(getSelectedIndex());
		SearchProductRequest request = new SearchProductRequest(barcode, null);
		Product selectedProduct = ProductService.existsProduct(request)
				.getProducts().get(0);
		String[] values;

		if (selectedProduct.getByPercentage()) {
			values = new String[] { selectedProduct.getDescription(),
					selectedProduct.getInitialCost().toString(),
					selectedProduct.getPercentage().toString(),
					selectedProduct.getMinStock().toString() };
			textFieldKeysUpdate = new String[] { "descripci\u00F3n",
					"costo inicial", "porcentaje", "stock m\u00EDnimo" };
		} else {
			values = new String[] { selectedProduct.getDescription(),
					selectedProduct.getInitialCost().toString(),
					selectedProduct.getFinalCost().toString(),
					selectedProduct.getMinStock().toString() };
			textFieldKeysUpdate = new String[] { "descripci\u00F3n",
					"costo inicial", "costo final", "stock m\u00EDnimo" };
		}
		byPercentage = selectedProduct.getByPercentage();

		popup = new SystemPopup(textFieldKeysUpdate, values);
		popup.addKeyListener(stockController.createKeyListener());
		popup.addKeyListenerAllFields(textFieldKeysUpdate,
				stockController.createKeyListener());
		popup.addConfirmButton("Actualizar",
				stockController.createUpdateProductListener());
		popup.setActionListenerToComponent(textFieldKeysUpdate,
				stockController.createUpdateProductListener());
		popup.setVisible(true);
	}

	/**
	 * 
	 */
	public void closeUpdatePopup() {
		popup.dispose();
	}

	public void cleanAllForm() {
		if (byPercentage) {
			form.cleanTextFields(textFieldKeys);
		} else {
			form.cleanTextFields(textFieldPlusKeys);
		}
	}

	public void cleanSearchField() {
		inputBarcode.setText(CommonData.EMPTY_STRING);
	}

	public String getSelectedBarcode() {
		return listOfProducts.getSelectedBarcode(getSelectedIndex());
	}

	public void changeTextFields() {
		byPercentage = !byPercentage;
		this.removeAll();

		if (byPercentage) {
			generateContent(textFieldKeys);
		} else {
			generateContent(textFieldPlusKeys);
		}

		this.revalidate();
	}

	public boolean isByPercentage() {
		return byPercentage;
	}

	/**
	 * Generates the Stock content panel. It includes a list of all products
	 * loaded from the database, as well as the option to add new products.
	 * 
	 * @param textFieldKeys
	 */
	private void generateContent(String[] textFieldKeys) {
		createLeftPanel(textFieldKeys);
		createCenterPanel();
	}

	private void createLeftPanel(String[] textFieldKeys) {
		JPanel leftPanel = new JPanel();

		// Set properties to the panel.
		leftPanel.setBorder(new EmptyBorder(40, 30, 40, 30));
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		leftPanel.setPreferredSize(new Dimension(dim.width / 4 + dim.width / 8,
				0));

		// Add page start panel containing input text field and form.
		leftPanel.add(createPageStartPanel(textFieldKeys),
				BorderLayout.PAGE_START);

		this.add(leftPanel, BorderLayout.LINE_START);
	}

	private JPanel createPageStartPanel(String[] textFieldKeys) {
		JPanel pageStartPanel = new JPanel();
		pageStartPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		pageStartPanel
				.setLayout(new BoxLayout(pageStartPanel, BoxLayout.Y_AXIS));

		// Add text field for input with custom listeners.
		inputBarcode = CommonUIComponents.createInputTextField(
				stockController.createSearchProductListener(), null);
		inputBarcode.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				inputBarcode = SystemForm.applySelectedProperties(inputBarcode);
				listOfProducts.cleanSelection();
			}

			@Override
			public void focusLost(FocusEvent e) {
				inputBarcode = SystemForm
						.applyUnselectedProperties(inputBarcode);
			}
		});

		pageStartPanel.add(inputBarcode, Component.LEFT_ALIGNMENT);
		pageStartPanel
				.add(CommonUIComponents
						.createNewVerticalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE * 2),
						Component.LEFT_ALIGNMENT);

		// Add a new-product form.
		String[] values = { "", "", "", "", "", "" };
		form = new SystemForm(textFieldKeys, values);

		// Add combo box.
		form.addComboBox(CommonUIComponents.CATEGORY, Utilities
				.generateArrayCategories(CategoryService.getAllCategories()
						.getAllCategories()));

		// Add check box.
		form.addCheckBox(CommonUIComponents.BY_PERCENTAGE, byPercentage,
				stockController.createCostListener());

		// Add confirm button.
		form.addConfirmButton("Agregar",
				stockController.createNewProductListener());
		form.setActionListenerToComponent(textFieldKeys,
				stockController.createNewProductListener(), "c\u00F3digo");

		JPanel formContainer = new JPanel(new BorderLayout());
		formContainer.add(form, BorderLayout.CENTER);
		pageStartPanel.add(formContainer);

		return pageStartPanel;
	}

	/**
	 * Generates a panel with all the products stored in the database.
	 */
	private void createCenterPanel() {
		JPanel container = new JPanel();

		// Apply properties.
		container.setBorder(new EmptyBorder(40, 30, 40, 30));
		container.setLayout(new BorderLayout());
		container.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		container.setPreferredSize(new Dimension(dim.width / 4 + dim.width / 8,
				0));

		// Create list of products.
		Object[] products = ProductService.getAllProducts().getAllProducts()
				.toArray();
		listOfProducts = new SwingListPanel(products, this,
				new ProductStockCellRenderer());
		listOfProducts.addListMouseListener(stockController
				.generateListMouseListener());

		container.add(listOfProducts, BorderLayout.CENTER);
		this.add(container, BorderLayout.CENTER);
	}

	@Override
	public void cleanSummaryLabel() {
		listOfProducts.cleanSummaryLabel();
	}

}
