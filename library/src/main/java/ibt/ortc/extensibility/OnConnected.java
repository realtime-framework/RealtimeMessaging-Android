/**
 * @fileoverview This file contains the definition of the on connected event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when a connection is established 
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnConnected {
	/**
	 * Fired when the connection is established
	 * @param sender Ortc client instance that fired the event
	 */
	void run(OrtcClient sender);
}
