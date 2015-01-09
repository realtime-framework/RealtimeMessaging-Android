package ibt.ortc.api;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Presence {
	
	private long subscriptions;
	private Map<String,Long> metadata;
	
	private Presence(){
		this.subscriptions = 0;
        // CAUSE: Instantiating collection without specified initial capacity
        this.metadata = new HashMap<String, Long>(16);
	}	
	
	private void setSubscriptions(long subscriptions){
		this.subscriptions = subscriptions;
	}
	
	public long getSubscriptions(){
		return subscriptions;
	}
	
    public Map<String,Long> getMetadata(){
		return metadata;
	}
	
    
    // CAUSE: Method names should start with a lower case letter
    private static Presence deserialize(String message){
		Presence result = new Presence();
		
		if(!Strings.isNullOrEmpty(message))
		{
			JSONObject json = (JSONObject) (JSONValue.parse(message));
			
			if (json != null) {
				result.setSubscriptions((Long) json.get("subscriptions"));
				
				JSONObject jsonMetadata = (JSONObject)json.get("metadata");
				if(jsonMetadata != null){
					@SuppressWarnings("unchecked")
					Iterator<?> iter = ((Map<String, Long>) jsonMetadata).entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iter.next();
						
						result.metadata.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}		
		
		return result;
	}
	
	protected static void getPresence(String url, Boolean isCluster, final String applicationKey, final String authenticationToken, final String channel, final OnPresence callback){
		try {
			Balancer.getServerUrlAsyn(url, isCluster, applicationKey, new OnRestWebserviceResponse() {			
				@Override
				public void run(Exception error, String presenceUrl) {
					if(error != null){
						callback.run(error, null);
					}else{
						// CAUSE: Prefer String.format to +
				        presenceUrl = Strings.isNullOrEmpty(presenceUrl) ? presenceUrl : presenceUrl.charAt(presenceUrl.length() -1) == '/' ? presenceUrl : String.format("%s/", presenceUrl);
						
				        // CAUSE: Prefer String.format to +
				        presenceUrl += String.format("presence/%s/%s/%s", applicationKey, authenticationToken, channel);
						
						try {
							URL requestUrl = new URL(presenceUrl);
							
							RestWebservice.getAsync(requestUrl, new OnRestWebserviceResponse() {
								
								@Override
								public void run(Exception error, String response) {
									if(error != null){
										callback.run(error, null);
									}else{
				                        Presence result = Presence.deserialize(response);
										callback.run(null, result);
									}					
								}
							});			
				        } catch (IOException e) {
							callback.run(e, null);
						}
					}
				}
			});
		} catch (MalformedURLException e) {
			callback.run(e, null);
		}
	}
	
	protected static void enablePresence(String url,Boolean isCluster,final String applicationKey, final String privateKey, final String channel, final Boolean metadata,final OnEnablePresence callback){
		try {
			Balancer.getServerUrlAsyn(url, isCluster, applicationKey, new OnRestWebserviceResponse() {			
				@Override
				public void run(Exception error, String presenceUrl) {
					if(error != null){
						callback.run(error, null);
					}else{
						presenceUrl = Strings.isNullOrEmpty(presenceUrl) ? presenceUrl : presenceUrl.charAt(presenceUrl.length() -1) == '/' ? presenceUrl : String.format("%s/", presenceUrl);
						
				        // CAUSE: Prefer String.format to +
				        presenceUrl += String.format("presence/enable/%s/%s", applicationKey, channel);
						
				        // CAUSE: Prefer String.format to +
				        String content = String.format("privatekey=%s", privateKey);
						if(metadata){
							content += "&metadata=1";
						}
						
						try {
							URL requestUrl = new URL(presenceUrl);
							
							RestWebservice.postAsync(requestUrl,content, new OnRestWebserviceResponse() {
								
								@Override
								public void run(Exception error, String response) {
									if(error != null){
										callback.run(error, null);
									}else{
										callback.run(null, response);
									}					
								}
							});			
				        } catch (IOException e) {
							callback.run(e, null);
						}
					}
				}
			});
		} catch (MalformedURLException e) {
			callback.run(e, null);
		}
	}
	
	protected static void disablePresence(String url,Boolean isCluster,final String applicationKey, final String privateKey, final String channel, final OnDisablePresence callback){
		try {
			Balancer.getServerUrlAsyn(url, isCluster, applicationKey, new OnRestWebserviceResponse() {			
				@Override
				public void run(Exception error, String presenceUrl) {
					if(error != null){
						callback.run(error, null);
					}else{
						// CAUSE: Prefer String.format to +
				        presenceUrl = Strings.isNullOrEmpty(presenceUrl) ? presenceUrl : presenceUrl.charAt(presenceUrl.length() -1) == '/' ? presenceUrl : String.format("%s/", presenceUrl);
						
				        // CAUSE: Prefer String.format to +
				        presenceUrl += String.format("presence/disable/%s/%s", applicationKey, channel);
						
				        // CAUSE: Prefer String.format to +
				        String content = String.format("privatekey=%s", privateKey);
						
						try {
							URL requestUrl = new URL(presenceUrl);
							
							RestWebservice.postAsync(requestUrl,content, new OnRestWebserviceResponse() {
								
								@Override
								public void run(Exception error, String response) {
									if(error != null){
										callback.run(error, null);
									}else{
										callback.run(null, response);
									}					
								}
							});			
				        } catch (IOException e) {
							callback.run(e, null);
						}
					}
				}
			});
		} catch (MalformedURLException e) {
			callback.run(e, null);
		}
	}
}
