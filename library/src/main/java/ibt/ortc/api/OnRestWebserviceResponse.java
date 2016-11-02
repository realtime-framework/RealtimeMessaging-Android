package ibt.ortc.api;

public interface OnRestWebserviceResponse {
	/** 
	 * @param error error exception
	 * @param response result body
	 */
	public void run(Exception error, String response);
}
