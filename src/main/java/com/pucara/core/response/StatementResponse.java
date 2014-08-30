package com.pucara.core.response;

public class StatementResponse extends Response {

	private int affectedRows;

	public StatementResponse(int aRows) {
		super();
		affectedRows = aRows;
	}

	public StatementResponse(ErrorMessage me) {
		super(me);
		affectedRows = 0;
	}

	public int getAffectedRows() {
		return affectedRows;
	}

}
