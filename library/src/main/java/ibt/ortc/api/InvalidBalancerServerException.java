/**
 * 
 */
package ibt.ortc.api;

/**
 * @author ORTC team members (ortc@ibt.pt) 
 *
 */
public class InvalidBalancerServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4188281781529891608L;
		
	public InvalidBalancerServerException(String server) {
		super(String.format("Server returned invalid server: %s",server));
	}
}
