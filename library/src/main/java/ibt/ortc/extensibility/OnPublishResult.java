package ibt.ortc.extensibility;

/**
 * Created by joaocaixinha on 25/01/17.
 */
public interface OnPublishResult {
    /**
     * Fired when a message seId arrives from the server or publish timeout expires
     * @param error Message not publish, error description
     * @param seqId The message sequence identifier
     */

    public void run(String error, String seqId);
}
