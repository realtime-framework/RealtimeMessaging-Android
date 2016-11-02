/**
 * @fileoverview This file contains the definition of the on exception event interface
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

/**
 * Represents the Ortc event when a exception occurs
 *  
 * @version 2.1.0 27 Mar 2013 2012
 * @author IBT
 *
 */
public interface OnException {
	/**
	 * Fired when a exception occurs
	 * @param send Ortc client instance that fired the event
	 * @param ex The instance of the exception that occurred
	 */
	void run(OrtcClient send, Exception ex);
}
