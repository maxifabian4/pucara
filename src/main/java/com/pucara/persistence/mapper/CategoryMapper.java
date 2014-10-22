package com.pucara.persistence.mapper;

//import java.util.List;

import java.util.List;

import com.pucara.persistence.domain.Category;

public interface CategoryMapper {
	public void insertCategory(Category category);

	public List<Category> getAllCategories();

	public Category getCategoryById(Integer categoryId);

	// Not implemented yet.
	//
	//
	// public void updateCategory(Category category);
	//
	// public void deleteCategory(Integer categoryId);
}
