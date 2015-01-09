/**
 * @fileoverview This file contains the definition of the on reconnecting event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when the connection is trying to be reestablished after being closed unexpectedly
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnReconnecting {
	/**
	 * Fired when the connection is trying to be reestablished after being closed unexpectedly
	 * @param sender Ortc client instance that fired the event
	 */
	void run(OrtcClient sender);
}
