package com.pucara.category;

import org.junit.Assert;
import org.junit.Test;

import com.pucara.core.generic.Utilities;
import com.pucara.core.response.AllCategoriesResponse;
import com.pucara.core.services.category.CategoryService;
import com.pucara.persistence.domain.Category;

public class CategoryServiceTest {

	@Test
	public void testInsertCategory() {
		Category category = new Category();
		category.setName(Utilities.generateRandomData(5));
		category.setDescription("new description");

		CategoryService.insertCategory(category);

		Assert.assertTrue(category.getId() != null);
		Category createdCategory = CategoryService.getCategoryById(category
				.getId());
		Assert.assertNotNull(createdCategory);
	}

	@Test
	public void testGetAllCategories() {
		AllCategoriesResponse categories = CategoryService.getAllCategories();
		Assert.assertNotNull(categories);
		for (Category category : categories.getAllCategories()) {
			System.out.println(category);
		}
	}

}
