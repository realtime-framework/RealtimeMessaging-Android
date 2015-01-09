/**
 * @fileoverview This file contains the definition of the ortc factory interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents a factory of a type of ortc clients.
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OrtcFactory {
	/**
	 * Creates a new instance of a OrtcClient.
	 * @return OrtcClient Ortc client instance
	 */
	public OrtcClient createClient();
}
