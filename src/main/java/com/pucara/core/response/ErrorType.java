package com.pucara.core.response;

/**
 * This enumeration represents all possible errors in 
 * the system.
 * 
 * @author Maximiliano
 */
public enum ErrorType {
	MYSQL_ERROR, 
	STATEMENT_ERROR, 
	UPDATE_CATEGORY_ERROR,
	UPDATE_PRODUCT_ERROR,
	ELEMENT_NOT_FOUND,
	INVALID_DATA_FORMAT, 
	DATABASE_DUPLICATED_KEY,
	EMPTY_PARTIAL_LIST, 
	INSUFFICIENT_STOCK,
	EMPTY_VALUE
}
