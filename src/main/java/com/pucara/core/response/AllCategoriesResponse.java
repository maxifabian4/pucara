package com.pucara.core.response;

import java.util.List;

import com.pucara.persistence.domain.Category;

public class AllCategoriesResponse extends Response {

	private List<Category> allCategories;

	public AllCategoriesResponse(List<Category> allCategories) {
		super();
		this.allCategories = allCategories;
	}

	public AllCategoriesResponse(ErrorMessage me) {
		super(me);
		allCategories = null;
	}

	public List<Category> getAllCategories() {
		return allCategories;
	}

}
