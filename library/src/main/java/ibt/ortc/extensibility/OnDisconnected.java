/**
 * @fileoverview This file contains the definition of the on disconnected event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when the connection is closed
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnDisconnected {
	/**
	 * Fired when the connection is closed
	 * @param sender Ortc client instance that fired the event
	 */
	void run(OrtcClient sender);
}
