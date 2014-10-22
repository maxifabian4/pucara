package com.pucara.persistence.domain;

public class Category {
	private Integer id;
	private String name;
	private String description;

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return String.format("Category [id:%d, name:%s, description:%s]", id,
				name, description);
	}

}
