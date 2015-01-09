/**
 * @fileoverview This file contains the implementation of the empty field exception
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility.exception;

public class OrtcAlreadyConnectedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3358079199485552190L;

	public OrtcAlreadyConnectedException(){
		super("Already connected");
	}
}
