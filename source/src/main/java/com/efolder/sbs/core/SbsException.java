package com.efolder.sbs.core;

public class SbsException extends RuntimeException {
	private static final long serialVersionUID = 7444639364499663014L;
	private ErrorCode code;

	public SbsException(ErrorCode code, String msg) {
		super(msg);
		this.code = code;
	}

	public ErrorCode getCode() {
		return code;
	}
}