package com.overcloud.pay.exception;

public class NotExistException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public NotExistException(String message){
		super(message);
	}

}
