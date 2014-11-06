package com.pucara.category;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.core.generic.Utilities;
import com.pucara.core.request.NewCategoryRequest;
import com.pucara.core.request.UpdateCategoryRequest;
import com.pucara.core.response.AllCategoriesResponse;
import com.pucara.core.response.CategoryResponse;
import com.pucara.core.services.category.CategoryService;
import com.pucara.persistence.domain.Category;

public class CategoryServiceTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CategoryServiceTest.class);

	@Test
	public void testInsertCategory() {
		Category category = new Category();
		category.setName(Utilities.generateRandomData(5));
		category.setDescription("new description");

		CategoryResponse response = CategoryService
				.insertCategory(new NewCategoryRequest(category));

		if (response.wasSuccessful()) {
			Assert.assertTrue(category.getId() != null);
			Category createdCategory = CategoryService.getCategoryById(category
					.getId());
			Assert.assertNotNull(createdCategory);
		}
	}

	@Test
	public void testInsertDuplicatedCategory() {
		Category category = new Category();
		category.setName("kiosco");
		category.setDescription("new description");

		CategoryResponse response = CategoryService
				.insertCategory(new NewCategoryRequest(category));

		Assert.assertFalse(response.wasSuccessful());
		LOGGER.debug(response.getErrorsMessages().get(0).getMessage());
	}

	@Test
	public void testUpdateCategory() {
		String newName = Utilities.generateRandomData(5);

		UpdateCategoryRequest request = new UpdateCategoryRequest("T9MLn",
				newName, Utilities.generateRandomData(10));
		CategoryResponse response = CategoryService.updateCategory(request);

		Assert.assertTrue(response.wasSuccessful());

		Category categoryResponse = CategoryService.getCategoryByName(newName);

		Assert.assertNotNull(categoryResponse);
		Assert.assertEquals(categoryResponse.getName(), newName);
	}

	@Test
	public void testUpdateDuplicatedCategory() {
		String newName = "kiosco";

		UpdateCategoryRequest request = new UpdateCategoryRequest("T9MLn",
				newName, Utilities.generateRandomData(10));
		CategoryResponse response = CategoryService.updateCategory(request);

		Assert.assertFalse(response.wasSuccessful());
		LOGGER.debug(response.getErrorsMessages().get(0).getMessage());
	}

	@Test
	public void testGetAllCategories() {
		AllCategoriesResponse categories = CategoryService.getAllCategories();
		Assert.assertNotNull(categories);

		for (Category category : categories.getAllCategories()) {
			LOGGER.debug(category.toString());
		}
	}

	@Test
	public void A() {
	}
}
