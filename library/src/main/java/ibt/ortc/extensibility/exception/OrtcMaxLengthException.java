/**
 * @fileoverview This file contains the implementation of the connection metadata max length exception
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility.exception;

public class OrtcMaxLengthException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8725018736412865917L;
	
	public OrtcMaxLengthException(){
		super();
	}
	
	public OrtcMaxLengthException(String field, int maxValue){
        // CAUSE: Incomplete elements for String.format
        super(String.format("%s size exceeds the limit of %d characters", field, maxValue));
	}
}
