package com.storeshop.scheduleportal.exceptions;

public class ResourceAlreadyAssignedException extends Exception {
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyAssignedException(String msg) {
		super(msg);
	}
}
