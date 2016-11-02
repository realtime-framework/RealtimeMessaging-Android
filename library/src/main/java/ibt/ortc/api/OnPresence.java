package ibt.ortc.api;



public interface OnPresence {
	public void run(Exception error, Presence presence);
}
