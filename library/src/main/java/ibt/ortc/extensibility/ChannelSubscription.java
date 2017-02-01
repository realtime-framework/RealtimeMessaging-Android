/**
 * @fileoverview This file contains the class to create ortc factories
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.extensibility;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a channel subscription
 *  
 * How to use:
 * <pre>
 * subscribedChannel = new ChannelSubscription(subscribeOnReconnect, onMessageEventHandler);
 * </pre>
 * @version 2.1.0 27 Mar 2013
 * @author IBT
 * 
 */
public class ChannelSubscription {
    private String subscriberId;
    private boolean isSubscribing;
	private boolean isSubscribed;
	private boolean subscribeOnReconnected;
	private OnMessage onMessage;
	private OnMessageWithPayload onMessageWithPayload;
    private OnMessageWithFilter onMessageWithFilter;
    private OnMessageWithOptions onMessageWithOptions;
	private boolean isWithPayload;
	private boolean withNotification;
	private boolean withFilter;
	private boolean withOptions;
	private String filter;


    /**
	 * Creates an instance of a channel subscription
	 * @param subscribeOnReconnected Indicates if the channel should be subscribe if a reconnect happens
	 * @param onMessageT Event handler that is fired when a message is received in the channel
	 * @param withNotification If true, GCM will be used
	 */
	public <T> ChannelSubscription(boolean subscribeOnReconnected,T onMessageT, boolean withNotification, boolean withOptions, String subscriberId, boolean withFilter, String filter){
		this.subscribeOnReconnected = subscribeOnReconnected;

		
		this.onMessageWithPayload = null;
		if(onMessageT instanceof OnMessage){
			this.onMessage = (OnMessage) onMessageT;
			this.isWithPayload = false;
            this.withFilter = false;
		} else if(onMessageT instanceof OnMessageWithOptions){
            this.onMessageWithOptions = (OnMessageWithOptions) onMessageT;
            this.withOptions = withOptions;
            this.subscriberId = subscriberId;
            this.isWithPayload = false;
        } else if(onMessageT instanceof OnMessageWithFilter){
            this.onMessageWithFilter = (OnMessageWithFilter) onMessageT;
            this.withFilter = true;
            this.isWithPayload = false;
        } else {
			this.onMessageWithPayload = (OnMessageWithPayload) onMessageT;
			this.isWithPayload = true;
            this.withFilter = false;
		}
		this.isSubscribed = false;
		this.isSubscribing = false;
		this.withNotification = withNotification;
		this.withFilter = withFilter;
		this.filter = filter;
	}
	
	/**
	 * Indicates whether the channel is being subscribed or not
	 * @return boolean True if is subscribing the channel otherwise false
	 */
	public boolean isSubscribing() {
		return isSubscribing;
	}

	/**
	 * Changes the channel subscribing status
	 * @param isSubscribing True indicates the channel is being subscribed
	 */
	public void setSubscribing(boolean isSubscribing) {
		this.isSubscribing = isSubscribing;
	}

	/**
	 * Indicates whether the channel is subscribed or not
	 * @return boolean True if the channel is subscribed otherwise false
	 */
	public boolean isSubscribed() {
		return isSubscribed;
	}

	/**
	 * Changes the channel subscribed status
	 * @param isSubscribed True indicates the channel is being subscribed
	 */
	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	/**
	 * Fires the event handler that is associated the subscribed channel
	 * @param channel 
	 * @param sender
	 * @param message 
	 */
	public void runHandler(OrtcClient sender, String channel, String message){
		this.runHandler(sender, channel, message, false, null);
	}
	/**
	 * Fires the event handler that is associated the subscribed channel (with payload)
	 * @param channel 
	 * @param sender
	 * @param message 
	 * @param payload
	 */
	public void runHandler(OrtcClient sender, String channel, String message, Object filtered, Object payload){
		if(this.withOptions){
            Map msgOptions = new HashMap();
            msgOptions.put("channel", channel);
            msgOptions.put("message", message);
			if (payload instanceof String){
            	msgOptions.put("seqId", ((String) payload));
			}else if (payload instanceof Map){
				msgOptions.put("seqId", (String)((Map) payload).get("seqId"));
			}
            if (filtered != null){
                msgOptions.put("filter", ((Boolean) filtered).booleanValue());
            }
            this.onMessageWithOptions.run(sender, msgOptions);
		} else if(this.withFilter){
            this.onMessageWithFilter.run(sender, channel, (Boolean) filtered, message);
        } else if(this.isWithPayload){
            this.onMessageWithPayload.run(sender, channel, message, (Map)payload);
        } else{
			this.onMessage.run(sender, channel, message);
		}
	}
	
	/**
	 * Indicates where the channel should be subscribed if a reconnect happens
	 * @return boolean True if should be subscribed otherwise false
	 */
	public boolean subscribeOnReconnected() {
		return subscribeOnReconnected;
	}
	
	/**
	 * Indicates where the channel should be subscribed if a reconnect happens
	 */
	public void setSubscribeOnReconnected(boolean value) {
		subscribeOnReconnected = value;
	}
	
	public boolean isWithNotification(){
		return this.withNotification;
	}

	public boolean isWithFilter(){
		return this.withFilter;
	}

	public String getFilter(){
		return this.filter;
	}

	public boolean isWithOptions(){
		return withOptions;
	}

    public String getSubscriberId(){
        return subscriberId;
    }
}
