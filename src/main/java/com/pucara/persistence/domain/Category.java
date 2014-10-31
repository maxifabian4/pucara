package com.pucara.persistence.domain;

public class Category {
	private Integer id;
	private String name;
	private String description;

	public Category(Integer id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Category() {
		this.id = null;
		this.name = null;
		this.description = null;
	}

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
