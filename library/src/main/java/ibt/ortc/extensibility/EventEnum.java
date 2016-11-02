/**
 * @fileoverview This file contains the Ortc events
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Enumerates the existent Ortc events.
 * 
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 */
public enum EventEnum {
	/**
	 * Fired when a connection is established
	 */
	OnConnected,
	/**
	 * Fired when a connection is closed
	 */
	OnDisconnected,
	/**
	 * Fired when a exception occurs
	 */
	OnException,
	/**
	 * Fired when a connection is reestablished after being closed unexpectedly
	 */
	OnReconnected,
	/**
	 * Fired when a connection is trying to be reestablished after being closed unexpectedly
	 */
	OnReconnecting,
	/**
	 * Fired when a channel is subscribed
	 */
	OnSubscribed,
	/**
	 * Fired when a channel is unsubscribed
	 */
	OnUnsubscribed,
	/**
	 * Fired when a message is received
	 */
	OnReceived,
}
