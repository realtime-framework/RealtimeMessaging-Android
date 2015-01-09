/**
 * @fileoverview This file contains the definition of the on unsubscribed event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when a channel is unsubscribed
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnUnsubscribed {
	/**
	 * Fired when a channel is unsubscribed
	 * 
	 * @param sender Ortc client instance that fired the event
	 * @param channel Channel that was unsubscribed
	 */
	void run(OrtcClient sender, String channel);
}
