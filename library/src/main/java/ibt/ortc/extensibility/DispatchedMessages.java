package ibt.ortc.extensibility;

import java.util.HashMap;

class DispatchedMessages {
	private static final int LIMIT = 1024;
	private int currentIdx;
	private String[] ids;
	private HashMap<String, Integer> hMap;
	
	public DispatchedMessages(){
		this.currentIdx = 0;
		this.ids = new String[LIMIT];
		this.hMap = new HashMap<String, Integer>();
	}
	
	protected void addMessageId(String messageId){
		currentIdx++;
		if(currentIdx>=LIMIT)
			currentIdx = 0;
		if(ids[currentIdx] != null){
			hMap.remove(ids[currentIdx]);
		}
		hMap.put(messageId, currentIdx);
		ids[currentIdx] = messageId;
	}
	
	protected boolean checkIfDispatched(String messageId){
		return hMap.containsKey(messageId);
	}
}
