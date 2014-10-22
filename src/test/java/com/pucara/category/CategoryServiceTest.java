package com.pucara.category;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.pucara.core.generic.Utilities;
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

		// Assert.assertEquals(user.getEmailId(), createdUser.getEmailId());
		// Assert.assertEquals(user.getPassword(), createdUser.getPassword());
		// Assert.assertEquals(user.getFirstName(), createdUser.getFirstName());
		// Assert.assertEquals(user.getLastName(), createdUser.getLastName());
	}

	@Test
	public void testGetAllCategories() {
		List<Category> categories = CategoryService.getAllCategories();
		Assert.assertNotNull(categories);
		for (Category category : categories) {
			System.out.println(category);
		}
	}

}
