/**
 *
 */
package ibt.ortc.plugins.IbtRealtimeSJ;

//NOTE: maybe this should be in the extensibility package, think about it later
public class OrtcServerErrorException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 3853804246516721679L;

    public enum OrtcServerErrorOperation{
        Unexpected,
        Validate,
        Subscribe,
        Subscribe_MaxSize,
        Unsubscribe_MaxSize,
        Send_MaxSize
    }

    private OrtcServerErrorOperation operation;
    private String channel;

    public OrtcServerErrorException(OrtcServerErrorOperation operation,String channel,String message) {
        super(message);
        this.operation = operation;
        this.channel = channel;
    }

    public OrtcServerErrorOperation getOperation() {
        return operation;
    }


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
