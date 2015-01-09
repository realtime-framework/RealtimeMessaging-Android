/**
 * @fileoverview This file contains the implementation of the empty field exception
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility.exception;

public class OrtcEmptyFieldException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3358079199485552190L;

	public OrtcEmptyFieldException(){
		super();
	}
	
	public OrtcEmptyFieldException(String field){
		super(String.format("%s is null or empty",field));
	}
}
