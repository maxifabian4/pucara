package com.pucara.core.response;

/**
 * 
 * @author Maximiliano
 */
public class ByIdResponse extends Response {

	private Long id;

	public ByIdResponse(Long value) {
		super();
		id = value;
	}

	public ByIdResponse(ErrorMessage me) {
		super(me);
		id = null;
	}

	public Long getId() {
		return id;
	}

}
