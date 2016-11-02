package ibt.ortc.extensibility;

/**
 * Enumerates the websocket connection protocols
 * 
 * @version 2.1.0 27 Mar 2013
 * @author IBT
 */
public enum ConnectionProtocol {
	/**
	 * Represents a secure connection
	 */
	Secure("wss"),
	/**
	 * Represents a unsecure connection
	 */
	Unsecure("ws");
	
	private ConnectionProtocol(String protocol){
		this.protocol = protocol;
	}
	
	private String protocol;
	
	public String getProtocol(){
		return this.protocol;
	}
}
