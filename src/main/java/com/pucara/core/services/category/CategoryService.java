package com.pucara.core.services.category;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import com.pucara.common.CommonMessageError;
import com.pucara.core.request.NewCategoryRequest;
import com.pucara.core.request.UpdateCategoryRequest;
import com.pucara.core.response.AllCategoriesResponse;
import com.pucara.core.response.CategoryResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
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

	public static CategoryResponse updateCategory(UpdateCategoryRequest request) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			CategoryMapper categoryMapper = sqlSession
					.getMapper(CategoryMapper.class);

			Category requiredCategory = new Category();

			if (request.getNewName() != null) {
				requiredCategory.setName(request.getNewName());
			}

			if (request.getNewDescription() != null) {
				requiredCategory.setDescription(request.getNewDescription());
			}

			categoryMapper.updateCategory(requiredCategory,
					request.getOldName());
			sqlSession.commit();

			return new CategoryResponse(requiredCategory);
		} catch (PersistenceException pe) {
			final Throwable cause = pe.getCause();

			if (cause instanceof SQLException) {
				return new CategoryResponse(new ErrorMessage(
						ErrorType.MYSQL_ERROR, String.format(
								CommonMessageError.DUPLICATED_CATEGORY,
								request.getNewName())));
			}

			return new CategoryResponse(new ErrorMessage(ErrorType.MYSQL_ERROR,
					pe.getMessage()));
		} finally {
			sqlSession.close();
		}
	}

	public static CategoryResponse insertCategory(NewCategoryRequest request) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			CategoryMapper categoryMapper = sqlSession
					.getMapper(CategoryMapper.class);

			if (categoryMapper.getCategoryByName(request.getCategory()
					.getName()) == null) {
				categoryMapper.insertCategory(request.getCategory());
				sqlSession.commit();

				return new CategoryResponse(request.getCategory());
			} else {
				return new CategoryResponse(new ErrorMessage(
						ErrorType.DATABASE_DUPLICATED_KEY, String.format(
								CommonMessageError.DUPLICATED_CATEGORY, request
										.getCategory().getName())));
			}
		} finally {
			sqlSession.close();
		}
	}

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

	public static Category getCategoryByName(String name) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			CategoryMapper categoryMapper = sqlSession
					.getMapper(CategoryMapper.class);
			return categoryMapper.getCategoryByName(name);
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

}
