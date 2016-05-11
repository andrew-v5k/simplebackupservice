package com.efolder.sbs.core;

public enum ErrorCode {
	EXPORT_FAILED(1),
	BAD_TODOS_DATA_CLOSED(2),
	BAD_TODOS_DATA_UNEXPECTED_TOKEN(3),
	CANT_PERSIST_TASK(4),
	CANT_RETRIEVE_TASK(5),
	CANT_UPDATE_STATUS(6);
	
	private int id;

	private ErrorCode(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}