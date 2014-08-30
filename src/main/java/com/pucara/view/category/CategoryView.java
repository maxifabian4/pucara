package com.pucara.view.category;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import com.pucara.common.CommonData;
import com.pucara.common.CommonUIComponents;
import com.pucara.common.SystemForm;

/**
 * Category view of the application, it allows visualize all categories from the
 * database and create a new one.
 * 
 * @author Maximiliano
 */
public class CategoryView extends JPanel {
	private static final long serialVersionUID = 1L;
	// private CategoryController categoryController;
	// private SystemTablePanel categoriesTablePanel;
	private SystemForm newCategoryForm;

	public CategoryView() {
		// categoryController = new CategoryController(this);
		generateContent();
	}

	/**
	 * Returns the category name from the view.
	 * 
	 * @return String
	 */
	public String getCategoryNameFromView() {
		// JTextField tf = (JTextField) newCategoryForm.getComponentFormAt(0);
		// return tf.getText();
		return "";
	}

	/**
	 * Returns the category description from the view.
	 * 
	 * @return String
	 */
	public String getCategoryDescriptionFromView() {
		// JTextField tf = (JTextField) newCategoryForm.getComponentFormAt(1);
		// return tf.getText();

		return "";
	}

	/**
	 * Refresh the JTable in the view, in order to retrieve last changes in the
	 * database.
	 */
	public void updateCategoriesTable() {
		// categoriesTablePanel.populateDataInTheView(Utilities
		// .generateArrayRowsCategory(CategoryService.getAllCategories().getAllCategories()));
	}

	/**
	 * 
	 * @return String
	 */
	public String getCategoryToFind() {
		// return newCategoryForm.getTextFieldValueFindElement();
		return "";
	}

	/**
	 * 
	 * @param category
	 * @param column
	 */
	public void selectElementOnTable(String category, int column) {
		// int posInTable = categoriesTablePanel.getPositionOf(category,
		// column);
		// categoriesTablePanel.selectElementAt(posInTable);
	}

	/**
	 * Generates the Categories content panel. It includes a list of all
	 * categories loaded from the database, as well as the option to add new
	 * categories.
	 */
	private void generateContent() {
		/**
		 * Category content panel includes a form to add new categories to the
		 * system.
		 */
		generateCategoryPanel();

		/**
		 * Categories panel includes, all the categories stored in the database,
		 * displayed in a table.
		 */
		generateAllCategoriesPanel();
	}

	/**
	 * Generates a panel with all the categories stored in the database.
	 */
	private void generateAllCategoriesPanel() {
		// categoriesTablePanel = new
		// SystemTablePanel("categor\u00EDas almacenadas",
		// CommonData.CATEGORY_COLUMNS, CommonData.CATEGORY_COLUMNS_EDITABLE,
		// CommonData.CATEGORY_COLUMNS_SCALABLE,
		// Utilities.generateArrayRowsCategory(CategoryService.getAllCategories()
		// .getAllCategories()));
		// this.add(categoriesTablePanel);
	}

	/**
	 * Generates the general category panel in the view. It contains a form to
	 * create new categories and a list of categories stored in the database.
	 */
	private void generateCategoryPanel() {
		this.setBackground(CommonData.GENERAL_BACKGROUND_COLOR);
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		/**
		 * Create content panel to the form.
		 */
		// List<Component> components = new ArrayList<Component>();
		// components.add(CommonUIComponents.createNewTextField("nombre"));
		// components.add(CommonUIComponents
		// .createNewTextField("descripci\u00F3n"));
		// newCategoryForm = new SystemForm(
		// categoryController.createSearchCategoryListener(),
		// categoryController.createNewCategoryListener(),
		// "nueva categor\u00EDa", components, true);
		this.add(newCategoryForm);

		/**
		 * Separate panels.
		 */
		this.add(CommonUIComponents
				.createNewHorizontalSeparatorBox(CommonUIComponents.VERTICAL_STRUT_VALUE));
	}

}
