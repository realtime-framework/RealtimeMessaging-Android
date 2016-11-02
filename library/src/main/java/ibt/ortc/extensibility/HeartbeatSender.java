package ibt.ortc.extensibility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HeartbeatSender {
	private static class Sender implements Runnable {
		
		private OrtcClient client;
		private boolean isStoping;
		
		public Sender(OrtcClient client) {
			this.client = client;
			isStoping = false;
		}
		
		public void run() {
			int counter = 0;
			while(!isStoping && client.getIsConnected()){
				try {
					if(counter >= client.getHeartbeatTime()*10){
						client.sendHeartbeat();
						counter = 0;						
					} else {
						counter++;
					}
					Thread.sleep(100);
					//client.sendHeartbeat();
					//Thread.sleep(client.getHeartbeatTime()*1000);					
				} catch (InterruptedException e) {
				}
			}
		}

		public void setStoping(boolean isStoping) {
			this.isStoping = isStoping;
		}
	}
	
	private Sender sender;
	private ExecutorService exec;
	
	public HeartbeatSender(OrtcClient client)  {
		exec = Executors.newSingleThreadExecutor();
		sender = new Sender(client);
		exec.execute(sender);
	}
	
	public void stop(){
		sender.setStoping(true);
	}
	
	@Override
	protected void finalize() throws Throwable {		
		super.finalize();
		sender.setStoping(true);
		if(exec != null && !exec.isShutdown() && !exec.isTerminated()){
			exec.shutdownNow();
		}
	}
}
