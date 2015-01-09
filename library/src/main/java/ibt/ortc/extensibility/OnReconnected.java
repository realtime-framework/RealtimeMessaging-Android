/**
 * @fileoverview This file contains the definition of the on reconnected event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when a connection is reestablished after being closed unexpectedly
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnReconnected {
	/**
	 * Fired when a connection is reestablished after being closed unexpectedly
	 * @param sender Ortc client instance that fired the event
	 */
	void run(OrtcClient sender);
}
