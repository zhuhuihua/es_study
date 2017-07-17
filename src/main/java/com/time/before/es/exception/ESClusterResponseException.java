package com.time.before.es.exception;

public class ESClusterResponseException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public String args ;
	
	public ESClusterResponseException(){
		
	}
	
	public ESClusterResponseException(String args){
		
		this.args = args;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public synchronized Throwable getCause() {
		return super.getCause();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}
	
	

}
