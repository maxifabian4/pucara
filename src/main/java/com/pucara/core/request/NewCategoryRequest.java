package com.pucara.core.request;

import com.pucara.core.entities.Category;

/**
 * This class allows to make a new request in order to create a new Category.
 * 
 * @author Maximiliano
 */
public class NewCategoryRequest {
	private Category category;

	public NewCategoryRequest(Category category) {
		this.category = category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return this.category;
	}

}
