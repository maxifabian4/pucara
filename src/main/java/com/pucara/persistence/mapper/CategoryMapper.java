package com.pucara.persistence.mapper;

//import java.util.List;

import java.util.List;

import com.pucara.persistence.domain.Category;
import com.pucara.persistence.domain.ProductsCategoryHelper;

public interface CategoryMapper {
	public void insertCategory(Category category);

	public List<Category> getAllCategories();

	public Category getCategoryById(Integer categoryId);

	public List<ProductsCategoryHelper> getSoldProductsByCategory();

	// Not implemented yet.
	//
	//
	// public void updateCategory(Category category);
	//
	// public void deleteCategory(Integer categoryId);
}
