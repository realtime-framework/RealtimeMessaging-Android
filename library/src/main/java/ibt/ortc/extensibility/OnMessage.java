/**
 * @fileoverview This file contains the definition of the on message event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when a message was received in the specified channel
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnMessage {
	/**
	 * Fired when a message was received in the specified channel
	 * @param sender Ortc client instance that fired the event
	 * @param channel Channel where the message was received
	 * @param message Content of the received message
	 */
	public void run(OrtcClient sender, String channel, String message);
}
