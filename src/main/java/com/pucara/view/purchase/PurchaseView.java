package com.pucara.view.purchase;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.pucara.persistence.domain.Category;
import com.pucara.view.render.ProductPurchaseCellRenderer;
import com.pucara.view.render.ProductSalePotentialCellRenderer;
import com.pucara.view.render.object.ListSalePotentialProduct;
import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.ProductView;
import com.pucara.common.SwingListPanel;
import com.pucara.common.SystemForm;
import com.pucara.common.SystemPopup;
import com.pucara.controller.observable.UpdatesSource;
import com.pucara.controller.purchase.PurchaseController;
import com.pucara.core.entities.Product;
import com.pucara.core.generic.Utilities;
import com.pucara.core.request.SearchProductRequest;
import com.pucara.core.services.category.CategoryService;
import com.pucara.core.services.product.ProductService;

/**
 * Sale view of the application, it allows visualize all partial products and
 * search by barcode/description.
 * 
 * @author Maximiliano
 */
public class PurchaseView extends ProductView {
	private static final long serialVersionUID = 1L;
	private PurchaseController purchaseController;
	private SwingListPanel listOfPartialProducts;
	private SwingListPanel listOfPotentialProducts;
	private JPanel centerContainer;
	private JPanel panelLeft;
	private SystemForm expenseForm;
	private SystemPopup popup;
	private String[] textFieldKeys = { "descripci\u00F3n", "costo" };
	private String[] textFieldKeysUpdate;
	private boolean byPercentage;

	public PurchaseView(UpdatesSource subject) {
		this.purchaseController = new PurchaseController(this, subject);
		subject.addObserver(this.purchaseController);
		generateContent();
	}

	public String getBarcodeFromTextField() {
		return inputBarcode.getText();
	}

	public void setBarcodeToTextField(String barcode) {
		inputBarcode.setText(barcode);
	}

	public String getPurchaseCost() {
		JTextField tf = (JTextField) expenseForm
				.getComponentFormAt(CommonUIComponents.PURCHASE_COST);
		return tf.getText();
	}

	public String getPurchaseDescription() {
		JTextField tf = (JTextField) expenseForm
				.getComponentFormAt(CommonUIComponents.PURCHASE_DESCRIPTION);
		return tf.getText();
	}

	/**
	 * Retrieves the selected product from the list.
	 * 
	 * @return barcode
	 */
	public String getSelectedProduct() {
		if (listOfPartialProducts != null) {
			if (getSelectedIndex() != -1) {
				return listOfPartialProducts
						.getSelectedBarcode(getSelectedIndex());
			} else {
				return listOfPartialProducts.getSelectedBarcode(0);
			}
		} else {
			return CommonData.EMPTY_STRING;
		}
	}

	/**
	 * 
	 */
	public void cleanInputTextField() {
		inputBarcode.setText(CommonData.EMPTY_STRING);
	}

	/**
	 * 
	 */
	public void selectInputTextField() {
		inputBarcode.requestFocus();
		inputBarcode.requestFocusInWindow();
	}

	public void populateProductsOnList(ListSalePotentialProduct[] products) {
		JPanel potentialContainer = new JPanel();

		// Apply properties.
		potentialContainer.setLayout(new BorderLayout());
		potentialContainer.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);

		// Create list of products.
		listOfPotentialProducts = new SwingListPanel(products, this,
				new ProductSalePotentialCellRenderer());
		listOfPotentialProducts.addListMouseListener(purchaseController
				.createPotentialMouseListener());
		listOfPotentialProducts.addListKeyListener(purchaseController
				.generateListPotentialKeyListener());

		potentialContainer.add(listOfPotentialProducts, BorderLayout.CENTER);
		panelLeft.add(potentialContainer, BorderLayout.CENTER);
		this.validate();
		this.repaint();
	}

	/**
	 * 
	 * @param partialProductRows
	 */
	public void addPartialListToPanel(Object[] products) {
		if (listOfPartialProducts == null) {
			createCenterPanel(products);
		} else if (products.length > 0) {
			listOfPartialProducts.populateDataInTheList(products);
		}
	}

	public String getPotentialItem() {
		return listOfPotentialProducts.getSelectedBarcode(getSelectedIndex());
	}

	public boolean isPotentialListPresent() {
		return listOfPotentialProducts.getNumberOfElements() > 0;
	}

	public void selectPotentialElement(int index) {
		listOfPotentialProducts.requestFocusOnList();
		listOfPotentialProducts.selectItemByIndex(index);
	}

	public boolean isFocusOnTextField() {
		return inputBarcode.hasFocus();
	}

	public void selectPartialElementByBarcode(String barcode) {
		listOfPartialProducts.selectBarcodeOnList(barcode);
	}

	/**
	 * 
	 * @param index
	 */
	public void selectPartialElement(int index) {
		listOfPartialProducts.selectItemByIndex(index);
	}

	public void cleanListSelection() {
		listOfPartialProducts.cleanSelection();
	}

	/**
	 * 
	 * @param listSaleProducts
	 * @param partialProductRows
	 */
	public void updatePartialElements(Object[] products) {
		if (products == null || products.length == 0) {
			if (centerContainer != null) {
				this.remove(centerContainer);
			}
			listOfPartialProducts = null;
			centerContainer = null;
			this.validate();
			this.repaint();
		} else {
			listOfPartialProducts.populateDataInTheList(products);
		}
	}

	public void addPotentialProductsOnList(Object[] products) {
		listOfPotentialProducts.populateDataInTheList(products);
	}

	/**
	 * 
	 */
	public void displayPopup() {
		String barcode = listOfPartialProducts
				.getSelectedBarcode(getSelectedIndex());
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
		popup.addKeyListener(purchaseController.createKeyListener());
		popup.addKeyListenerAllFields(textFieldKeysUpdate,
				purchaseController.createKeyListener());
		popup.addComboBox(Utilities.generateArrayCategories(CategoryService
				.getAllCategories().getAllCategories()), selectedProduct.getCategoryId());
		popup.addConfirmButton("Actualizar",
				purchaseController.createUpdateProductListener());
		popup.setActionListenerToComponent(textFieldKeysUpdate,
				purchaseController.createUpdateProductListener());
		popup.setVisible(true);
	}

	public String getSelectedPurchaseBarcode() {
		return listOfPartialProducts.getSelectedSaleBarcode(getSelectedIndex());
	}

	public boolean isByPercentage() {
		return byPercentage;
	}

	/**
	 * 
	 * @param barcode
	 */
	public void selectProductElementOnList(String barcode) {
		listOfPartialProducts.selectBarcodeOnList(barcode);
	}

	/**
	 * 
	 * @param products
	 * @param descriptionColumn
	 */
	public void selectProductElementsOnList(List<Product> products) {
		listOfPartialProducts.selectMultipleBarcodeOnList(products);
	}

	/**
	 *
	 */
	public void updateProductsList() {
		if (listOfPartialProducts != null) {
			listOfPartialProducts.populateDataInTheList(purchaseController
					.generatePartialProductRows());
		}
	}

	/**
	 * 
	 */
	public void closeUpdatePopup() {
		popup.dispose();
	}

	public List<String> getFormValues() {
		return expenseForm.getAllTextFieldValues(textFieldKeys);
	}

	public void cleanPurchaseTextFields() {
		expenseForm.cleanTextFields(textFieldKeys);
		this.validate();
	}

	public List<String> getPopupValues() {
		return popup.getAllTextFieldValues(textFieldKeysUpdate);
	}

	public void updateCostField(double totalCost) {
		JTextField textField = (JTextField) expenseForm
				.getComponentFormAt(CommonUIComponents.PURCHASE_COST);
		textField.setText(String.valueOf(totalCost));
	}

	@Override
	public void cleanSummaryLabel() {
	}

	public Category getCategoryFromView() {
		JComboBox cb = (JComboBox) popup.getForm().getComponentFormAt(
				CommonUIComponents.CATEGORY);
		return (Category) cb.getSelectedItem();
	}

	/**
	 * 
	 */
	private void generateContent() {
		// Left panel - input text field and potential products list.
		createLeftPanel();
	}

	private void createLeftPanel() {
		panelLeft = new JPanel();

		// Set properties to the panel.
		panelLeft.setBorder(new EmptyBorder(40, 30, 40, 30));
		panelLeft.setLayout(new BorderLayout());
		panelLeft.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		panelLeft.setPreferredSize(new Dimension(dim.width / 4 + dim.width / 8,
				0));

		// Add page start panel containing input text field, form and potential
		// list.
		panelLeft.add(createPageStartPanel(), BorderLayout.PAGE_START);
		populateProductsOnList(new ListSalePotentialProduct[] {});
		panelLeft.add(createFormPanel(), BorderLayout.PAGE_END);

		this.add(panelLeft, BorderLayout.LINE_START);
	}

	private JPanel createFormPanel() {
		// Add a new-product form.
		String[] values = { "", "" };
		expenseForm = new SystemForm(textFieldKeys, values);
		expenseForm.addConfirmButton("Agregar",
				purchaseController.createNewPurchaseListener());
		expenseForm.setActionListenerToComponent(textFieldKeys,
				purchaseController.createNewPurchaseListener());

		return expenseForm;
	}

	private JPanel createPageStartPanel() {
		JPanel pageStartPanel = new JPanel();
		pageStartPanel.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		pageStartPanel
				.setLayout(new BoxLayout(pageStartPanel, BoxLayout.Y_AXIS));

		// Add text field for input with custom listeners.
		inputBarcode = CommonUIComponents.createInputTextField(null,
				purchaseController.createInputKeyListener());
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

		pageStartPanel.add(inputBarcode, Component.LEFT_ALIGNMENT);
		pageStartPanel
				.add(CommonUIComponents
						.createNewVerticalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE * 2),
						Component.LEFT_ALIGNMENT);

		return pageStartPanel;
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
				new ProductPurchaseCellRenderer());
		listOfPartialProducts.addListKeyListener(purchaseController
				.generateListKeyListener());
		listOfPartialProducts.addListMouseListener(purchaseController
				.generateListMouseListener());

		centerContainer.add(listOfPartialProducts, BorderLayout.CENTER);
		this.add(centerContainer, BorderLayout.CENTER);
		this.validate();
	}

}
