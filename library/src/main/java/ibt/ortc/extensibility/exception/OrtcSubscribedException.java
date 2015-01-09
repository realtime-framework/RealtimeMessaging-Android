/**
 * @fileoverview This file contains the implementation of the subscribed channel exception
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility.exception;

public class OrtcSubscribedException extends Exception {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1943884636989115239L;

	public OrtcSubscribedException(String message){
		super(message);
	}
}
