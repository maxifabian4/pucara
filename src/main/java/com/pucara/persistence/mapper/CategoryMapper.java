package com.pucara.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pucara.persistence.domain.Category;
import com.pucara.persistence.domain.ProductsCategoryHelper;

public interface CategoryMapper {

	public void insertCategory(Category category);

	public void updateCategory(@Param("category") Category category,
			@Param("oldName") String oldName);

	public List<Category> getAllCategories();

	public Category getCategoryById(Integer categoryId);

	public Category getCategoryByName(String name);

	public List<ProductsCategoryHelper> getSoldProductsByCategory();

}
