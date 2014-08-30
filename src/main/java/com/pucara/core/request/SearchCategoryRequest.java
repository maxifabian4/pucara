package com.pucara.core.request;

/**
 * 
 * @author Maximiliano
 */
public class SearchCategoryRequest {
	private String id;
	private String name;

	public SearchCategoryRequest(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getCategoryId() {
		return this.id;
	}

	public String getCategoryName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		if (id != null) {
			return id;
		} else if (name != null) {
			return name;
		} else {
			return "-";
		}
	}

}
