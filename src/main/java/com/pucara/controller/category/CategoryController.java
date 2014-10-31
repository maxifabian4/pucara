package com.pucara.controller.category;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.pucara.common.CommonData;

import com.pucara.core.request.NewCategoryRequest;
import com.pucara.core.request.SearchCategoryRequest;
import com.pucara.core.response.CategoryResponse;
import com.pucara.core.services.category.CategoryService;

import com.pucara.persistence.domain.Category;
import com.pucara.view.category.CategoryView;

/**
 * This class represents the Category controller in the system.
 * 
 * @author Maximiliano
 */
public class CategoryController {
	private CategoryView categoryView;

	public CategoryController(CategoryView categoryView) {
		this.categoryView = categoryView;
	}

	/**
	 * Creates a new listener in order to create a new category.
	 * 
	 * @return ActionListener
	 */
	public ActionListener createNewCategoryListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Category newCategory = new Category();
				newCategory.setName(categoryView.getCategoryNameFromView().toLowerCase());
				newCategory.setDescription(categoryView.getCategoryDescriptionFromView()
						.toLowerCase());
				CategoryResponse response = CategoryService.addCategory(new NewCategoryRequest(
						newCategory));

				if (!response.wasSuccessful()) {
					JOptionPane.showMessageDialog(null, response.getErrorsMessages().get(0)
							.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					categoryView.updateCategoriesTable();
					categoryView.selectElementOnTable(newCategory.getName(), 0);
				}
			}
		};
	}

	/**
	 * Creates a new listener in order to search an existing category.
	 * 
	 * @return ActionListener
	 */
	public ActionListener createSearchCategoryListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String categoryFromView = categoryView.getCategoryToFind().toLowerCase();

				CategoryResponse nameResponse = CategoryService
						.existsCategory(new SearchCategoryRequest(null, categoryFromView));

				if (nameResponse.wasSuccessful()) {
					categoryView.selectElementOnTable(nameResponse.getCategory().getName(),
							CommonData.NAME_CATEGORY_COLUMN);
				} else {
					JOptionPane.showMessageDialog(null, nameResponse.getErrorsMessages().get(0)
							.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		};
	}

}
