package com.pucara.core.response;

/**
 * 
 * @author Maximiliano
 */
public class ErrorMessage {

	private ErrorType error;
	private String message;

	public ErrorMessage(ErrorType te, String msj) {
		error = te;
		message = msj;
	}

	// Setters...

	public void addMessage(String m) {
		message = m;
	}

	public void addError(ErrorType te) {
		error = te;
	}

	// ...

	// Getters...

	public String getMessage() {
		return message;
	}

	public ErrorType getError() {
		return error;
	}

	// ...

}
