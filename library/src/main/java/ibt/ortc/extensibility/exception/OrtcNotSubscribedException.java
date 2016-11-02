/**
 * @fileoverview This file contains the ortc client abstract class
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility.exception;

public class OrtcNotSubscribedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8279573967778696464L;

	public OrtcNotSubscribedException(String channel){
		super(String.format("Not subscribed to channel %s.", channel));
	}
}
