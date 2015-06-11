/**
 * @fileoverview This file contains the definition of the on message event interface
 * @author ORTC team members (ortc@ibt.pt)
 */
package ibt.ortc.extensibility;

import java.util.Map;

/**
 * Represents the Ortc event when a message (with payload) was received in the specified channel
 *  
 * @version 2.1.26 24 Sep 2014 2012
 * @author IBT
 *
 */
public interface OnMessageWithPayload{
	/**
	 * Fired when a message was received in the specified channel
	 * @param sender Ortc client instance that fired the event
	 * @param channel Channel where the message was received
	 * @param message Content of the received message
	 * @param payload Content of the payload (received with GCM notification)
	 */
	public void run(OrtcClient sender, String channel, String message, Map payload);
}
