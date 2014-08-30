package com.pucara.core.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the errors included in the transaction. If no errors were catched up, the response is successful.
 */
public class Response {

	private List<ErrorMessage> errors;

	public Response() {
		errors = new ArrayList<ErrorMessage>();
	}

	public Response(ErrorMessage e) {
		errors = new ArrayList<ErrorMessage>();
		addError(e);
	}

	public Response(List<ErrorMessage> es) {
		errors = es;
	}

	public void addError(ErrorMessage e) {
		errors.add(e);
	}

	public void addErrors(List<ErrorMessage> es) {
		errors.addAll(es);
	}

	/**
	 * If true, the transaction has been successful.
	 * 
	 * @return boolean
	 */
	public boolean wasSuccessful() {
		return errors.size() == 0;
	}

	public List<ErrorMessage> getErrorsMessages() {
		return errors;
	}

}
