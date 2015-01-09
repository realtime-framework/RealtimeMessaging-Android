/**
 * @fileoverview This file contains the implementation of the not connected exception
 * @author ORTC team members (ortc@ibt.pt)
 */
package ibt.ortc.extensibility.exception;

public class OrtcNotConnectedException extends Exception {

	private static final long serialVersionUID = -5717798427917093305L;
	
	public OrtcNotConnectedException(){
		super("Not connected");
	}	
	
	public OrtcNotConnectedException(String message){
		super(message);
	}	
}
