/**
 * @fileoverview This file contains the channels permissions enumeration
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.api;

/**
 * Enumerates the existent permissions for a channel.
 * 
 * @version 2.1.0 27 Mar 2013
 * @author IBT
 */
public enum ChannelPermissions {
	/**
	 * Read permission to the channel, don't allow write in the channel
	 */
	Read("r"),
	/**
	 * Write permission to the channel, also allows read in the channel
	 */
	Write("w"),
	/**
	 * Presence permission to the channel, allows token to get the number of subscriptions in the channel
	 */
	Presence("p");
	
	private ChannelPermissions(String permission){
		this.permission = permission;
	}
	
	private String permission;
	
	String getPermission(){
		return this.permission;
	}
}
