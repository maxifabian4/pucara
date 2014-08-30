package com.pucara.core.services.category;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pucara.common.CommonData;
import com.pucara.common.CommonMessageError;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.entities.Category;
import com.pucara.core.request.NewCategoryRequest;
import com.pucara.core.request.SearchCategoryRequest;
import com.pucara.core.request.UpdateCategoryRequest;
import com.pucara.core.response.AllCategoriesResponse;
import com.pucara.core.response.CategoryResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.StatementResponse;

/**
 * This class provides the methods to create, removed, update a category in the
 * database.
 * 
 * @author Maximiliano
 */
public class CategoryService {

	/**
	 * Creates a category in the database.
	 * 
	 * @param name
	 * @param description
	 * @return {@link CategoryResponse}
	 */
	public static CategoryResponse addCategory(NewCategoryRequest request) {
		if (!CategoryService
				.existsCategory(
						new SearchCategoryRequest(null, request.getCategory()
								.getName())).wasSuccessful()) {
			StatementResponse response = MySqlAccess.insertNewCategory(request
					.getCategory());

			if (response.wasSuccessful())
				return new CategoryResponse(request.getCategory());
			else
				return new CategoryResponse(response.getErrorsMessages());
		} else {
			return new CategoryResponse(new ErrorMessage(
					ErrorType.DATABASE_DUPLICATED_KEY, String.format(
							CommonMessageError.DUPLICATED_CATEGORY, request
									.getCategory().getName())));
		}
	}

	/**
	 * Returns all categories from the database.
	 * 
	 * @return AllCategoriesResponse
	 */
	public static AllCategoriesResponse getAllCategories() {
		List<Category> allCategories = new ArrayList<Category>();
		ResultSet categoriesStatement = MySqlAccess.getAllCategories();
		Category newCategory = new Category();

		try {
			while (categoriesStatement.next()) {
				newCategory.setIdentifier(categoriesStatement.getInt("id"));
				newCategory.setName(categoriesStatement.getString("name"));
				newCategory.setDescription(categoriesStatement
						.getString("description"));

				allCategories.add(newCategory);
				newCategory = new Category();
			}

			return new AllCategoriesResponse(allCategories);
		} catch (SQLException e) {
			return new AllCategoriesResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * Removes a category from the database.
	 * 
	 * @param name
	 * @return {@link CategoryResponse}
	 */
	public static CategoryResponse removeCategory(String name) {
		StatementResponse response = MySqlAccess.removeCategory(name);

		if (response.wasSuccessful()) {
			return new CategoryResponse(new Category());
		} else {
			return new CategoryResponse(response.getErrorsMessages());
		}
	}

	/**
	 * Updates a category from the database.
	 * 
	 * @param oldName
	 * @param newName
	 * @param newDescription
	 * @return CategoryResponse
	 */
	public static CategoryResponse updateCategory(UpdateCategoryRequest request) {
		StatementResponse response = MySqlAccess.updateCategory(
				request.getOldName(), request.getNewName(),
				request.getNewDescription());

		if (response.wasSuccessful() && response.getAffectedRows() == 1) {
			Category categoryResponse = new Category();
			categoryResponse.setName(request.getNewName());
			categoryResponse.setDescription(request.getNewDescription());

			return new CategoryResponse(categoryResponse);
		} else {
			return new CategoryResponse(response.getErrorsMessages());
		}
	}

	/**
	 * Verifies if a category exists.
	 * 
	 * @param name
	 * @return boolean
	 */
	public static CategoryResponse existsCategory(
			SearchCategoryRequest searchRequest) {
		CategoryResponse response = new CategoryResponse(new Category());

		if (searchRequest.getCategoryId() != null) {
			response = MySqlAccess
					.findCategoryCondition(String.format(
							CommonData.CATEGORY_WHERE_ID,
							searchRequest.getCategoryId()));
		} else if (searchRequest.getCategoryName() != null) {
			response = MySqlAccess.findCategoryCondition("WHERE name like '%"
					+ searchRequest.getCategoryName() + "%'");
		}

		if (response.wasSuccessful()
				&& (response.getCategory().getIdentifier() != null || response
						.getCategory().getName() != null)) {
			return response;
		} else {
			return new CategoryResponse(new ErrorMessage(
					ErrorType.ELEMENT_NOT_FOUND, String.format(
							CommonMessageError.ELEMENT_NOT_FOUND,
							searchRequest.toString())));
		}
	}

}
