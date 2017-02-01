package ibt.ortc.extensibility;

import java.util.Map;

/**
 * Created by joaocaixinha on 24/01/17.
 */
public interface OnMessageWithOptions {
    /**
     * Fired when a message was received in the specified channel
     * @param sender Ortc client instance that fired the event
     * @param msgOptions Dictionary where the message data was received
     */

    public void run(OrtcClient sender, Map msgOptions);
}
