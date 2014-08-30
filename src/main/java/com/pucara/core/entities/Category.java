package com.pucara.core.entities;

/**
 * Clase creada para representar una categor�a en el sistema.
 * 
 * @author Maximiliano
 * 
 */
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

	}

	// Setters...

	public void setIdentifier(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// ...

	// Getters...

	public Integer getIdentifier() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	// ...

	@Override
	public String toString() {
		return name;
	}
}
