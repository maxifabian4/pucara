package com.pucara.core.response;

import java.util.List;

import com.pucara.persistence.domain.Category;

/**
 * @author Maximiliano
 */
public class CategoryResponse extends Response {
	private Category category;

	public CategoryResponse(Category category) {
		super();
		this.category = category;
	}

	public CategoryResponse(ErrorMessage me) {
		super(me);
	}

	public CategoryResponse(List<ErrorMessage> mes) {
		super(mes);
	}

	public Category getCategory() {
		return category;
	}

}
