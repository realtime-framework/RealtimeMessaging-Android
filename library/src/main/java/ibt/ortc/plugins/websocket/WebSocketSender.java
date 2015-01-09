package ibt.ortc.plugins.websocket;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class WebSocketSender {

	private static class Sender implements Runnable {
		private WebSocketConnection connection;
		private PrintStream output;
		
		public Queue<String> q = new LinkedBlockingQueue<String>(); 
				
		public Sender send(String text){
			q.add(text);
			return this;
		}
		
		public Sender(PrintStream output, WebSocketConnection connection) {
			this.connection = connection;
			this.output = output;
		}
		
		public void run() {
			if(q.size() > 0) {
				String text = q.poll();
				try {
					sendAsync(text);
				} catch (WebSocketException e) {
					
				}
			}
		}
		
		private void sendAsync(String data) throws WebSocketException{
			if (!connection.isConnected()) {
				throw new WebSocketException(
						"error while sending text data: not connected");
			}
			
			try {
				output.write(0x00);
				output.write(data.getBytes(("UTF-8")));
				output.write(0xff);
			} catch (UnsupportedEncodingException uee) {
				throw new WebSocketException("error while sending text data: unsupported encoding", uee);
			} catch (IOException ioe) {
				throw new WebSocketException("error while sending text data", ioe);
			}
		}
	}
	
	private Sender sender;
	private ExecutorService exec;
	
	public WebSocketSender(PrintStream output, WebSocketConnection connection)  {
		exec = Executors.newSingleThreadExecutor();
		sender = new Sender(output, connection);
	}	
	
	@Override
	protected void finalize() throws Throwable {		
		super.finalize();
		if(exec != null && !exec.isShutdown() && !exec.isTerminated()){
			exec.shutdown();
		}
	}
	
	public void send(final String data) throws WebSocketException {
		if(exec != null && !exec.isShutdown() && !exec.isTerminated() && sender != null && data != null){
			exec.execute(sender.send(data));
		}
	}
}
