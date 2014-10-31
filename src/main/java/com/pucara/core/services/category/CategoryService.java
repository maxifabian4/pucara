package com.pucara.core.services.category;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pucara.core.request.NewCategoryRequest;
import com.pucara.core.request.SearchCategoryRequest;
import com.pucara.core.request.UpdateCategoryRequest;
import com.pucara.core.response.AllCategoriesResponse;
import com.pucara.core.response.CategoryResponse;
import com.pucara.core.services.mybatis.MyBatisUtil;
import com.pucara.persistence.domain.Category;
import com.pucara.persistence.domain.ProductsCategoryHelper;
import com.pucara.persistence.mapper.CategoryMapper;

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
		// if (!CategoryService
		// .existsCategory(
		// new SearchCategoryRequest(null, request.getCategory()
		// .getName())).wasSuccessful()) {
		// StatementResponse response = MySqlAccess.insertNewCategory(request
		// .getCategory());
		//
		// if (response.wasSuccessful())
		// return new CategoryResponse(request.getCategory());
		// else
		// return new CategoryResponse(response.getErrorsMessages());
		// } else {
		// return new CategoryResponse(new ErrorMessage(
		// ErrorType.DATABASE_DUPLICATED_KEY, String.format(
		// CommonMessageError.DUPLICATED_CATEGORY, request
		// .getCategory().getName())));
		// }
		return null;
	}

	/**
	 * Removes a category from the database.
	 * 
	 * @param name
	 * @return {@link CategoryResponse}
	 */
	public static CategoryResponse removeCategory(String name) {
		// StatementResponse response = MySqlAccess.removeCategory(name);
		//
		// if (response.wasSuccessful()) {
		// return new CategoryResponse(new Category());
		// } else {
		// return new CategoryResponse(response.getErrorsMessages());
		// }
		return null;
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
		// StatementResponse response = MySqlAccess.updateCategory(
		// request.getOldName(), request.getNewName(),
		// request.getNewDescription());
		//
		// if (response.wasSuccessful() && response.getAffectedRows() == 1) {
		// Category categoryResponse = new Category();
		// categoryResponse.setName(request.getNewName());
		// categoryResponse.setDescription(request.getNewDescription());
		//
		// return new CategoryResponse(categoryResponse);
		// } else {
		// return new CategoryResponse(response.getErrorsMessages());
		// }

		return null;
	}

	/**
	 * Verifies if a category exists.
	 * 
	 * @param name
	 * @return boolean
	 */
	public static CategoryResponse existsCategory(
			SearchCategoryRequest searchRequest) {
		// CategoryResponse response = new CategoryResponse(new Category());
		//
		// if (searchRequest.getCategoryId() != null) {
		// response = MySqlAccess
		// .findCategoryCondition(String.format(
		// CommonData.CATEGORY_WHERE_ID,
		// searchRequest.getCategoryId()));
		// } else if (searchRequest.getCategoryName() != null) {
		// response = MySqlAccess.findCategoryCondition("WHERE name like '%"
		// + searchRequest.getCategoryName() + "%'");
		// }
		//
		// if (response.wasSuccessful()
		// && (response.getCategory().getIdentifier() != null || response
		// .getCategory().getName() != null)) {
		// return response;
		// } else {
		// return new CategoryResponse(new ErrorMessage(
		// ErrorType.ELEMENT_NOT_FOUND, String.format(
		// CommonMessageError.ELEMENT_NOT_FOUND,
		// searchRequest.toString())));
		// }
		return null;
	}

	/**
	 * MyBatis Section.
	 */
	public static void insertCategory(Category category) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			CategoryMapper categoryMapper = sqlSession
					.getMapper(CategoryMapper.class);
			categoryMapper.insertCategory(category);
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}

	// public User getUserById(Integer userId) {
	// SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
	// .openSession();
	// try {
	// UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
	// return userMapper.getUserById(userId);
	// } finally {
	// sqlSession.close();
	// }
	// }

	public static AllCategoriesResponse getAllCategories() {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			CategoryMapper categoryMapper = sqlSession
					.getMapper(CategoryMapper.class);
			return new AllCategoriesResponse(categoryMapper.getAllCategories());
		} finally {
			sqlSession.close();
		}
	}

	public static Category getCategoryById(Integer id) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			CategoryMapper categoryMapper = sqlSession
					.getMapper(CategoryMapper.class);
			return categoryMapper.getCategoryById(id);
		} finally {
			sqlSession.close();
		}

	}

	public static List<ProductsCategoryHelper> getSoldProductsByCategory() {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			CategoryMapper categoryMapper = sqlSession
					.getMapper(CategoryMapper.class);
			return categoryMapper.getSoldProductsByCategory();
		} finally {
			sqlSession.close();
		}
	}

	// public void updateUser(User user) {
	// SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
	// .openSession();
	// try {
	// UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
	// userMapper.updateUser(user);
	// sqlSession.commit();
	// } finally {
	// sqlSession.close();
	// }
	//
	// }
	//
	// public void deleteUser(Integer userId) {
	// SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
	// .openSession();
	// try {
	// UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
	// userMapper.deleteUser(userId);
	// sqlSession.commit();
	// } finally {
	// sqlSession.close();
	// }
	//
	// }
}
