package com.pucara.core.request;

/**
 * This class allows to create a new request in order to update an existing
 * Category.
 * 
 * @author Maximiliano
 */
public class UpdateCategoryRequest {
	private String oldName;
	private String newName;
	private String newDescription;

	public UpdateCategoryRequest(String oldName, String newName,
			String newDescription) {
		this.oldName = oldName;
		this.newName = newName;
		this.newDescription = newDescription;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldName() {
		return this.oldName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewName() {
		return this.newName;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}

	public String getNewDescription() {
		return this.newDescription;
	}

}
