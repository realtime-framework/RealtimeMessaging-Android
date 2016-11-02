/**
 * @fileoverview This file contains the definition of the on subscribed event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when a channel is subscribed
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnSubscribed {
	/**
	 * Fired when a channel is subscribed
	 * 
	 * @param sender Ortc client instance that fired the event
	 * @param channel Channel that was subscribed
	 */
	void run(OrtcClient sender, String channel);
}
