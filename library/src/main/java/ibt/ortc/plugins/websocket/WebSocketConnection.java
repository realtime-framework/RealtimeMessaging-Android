/*
 *  Copyright (C) 2011 Roderick Baier
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */

/*
 * 09/02/2011 - Emory Myers - 	printing stacktraces for debugging purposes
 * 								added some logging
 * 								implemented isConnected method
 * 								modified send method
 */

package ibt.ortc.plugins.websocket;

import static android.util.Log.DEBUG;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

public class WebSocketConnection implements WebSocket {
	private static final String TAG = "WSConnection";
	private URI url = null;
	private WebSocketEventHandler eventHandler = null;

	private volatile boolean connected = false;

	private Socket socket = null;
	private InputStream input = null;
	private PrintStream output = null;

	private WebSocketReceiver receiver = null;
	private WebSocketSender sender = null;
	private WebSocketHandshake handshake = null;
	
	private boolean trustAllCerts;

	public WebSocketConnection(URI url) throws WebSocketException {
		this(url, null);
	}

	public WebSocketConnection(URI url, String protocol)
			throws WebSocketException {
		this.url = url;
		handshake = new WebSocketHandshake(url, protocol);
	}
	
	/**
	 * @param trustAllCerts
	 */
	public void setTrustAllCerts(boolean trustAllCerts) {
		this.trustAllCerts = trustAllCerts;
	}

	public void setEventHandler(WebSocketEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public WebSocketEventHandler getEventHandler() {
		return this.eventHandler;
	}

	public void connect() throws WebSocketException {
		try {
			if (connected) {
				throw new WebSocketException("already connected");
			}

			socket = createSocket();
			input = socket.getInputStream();
			output = new PrintStream(socket.getOutputStream());

			output.write(handshake.getHandshake());

			boolean handshakeComplete = false;
			boolean header = true;
			int len = 2000;
			byte[] buffer = new byte[len];
			int pos = 0;
			ArrayList<String> handshakeLines = new ArrayList<String>();

			byte[] serverResponse = new byte[16];

			while (!handshakeComplete) {
				if(pos == len){
					throw new WebSocketException("Unable to handshake");
				}else{
					int b = input.read();
					buffer[pos] = (byte) b;
					pos += 1;

					if (!header) {
						serverResponse[pos - 1] = (byte) b;
						if (pos == 16) {
							handshakeComplete = true;
						}
					} else if (buffer[pos - 1] == 0x0A && buffer[pos - 2] == 0x0D) {
						String line = new String(buffer, "UTF-8");
						if (line.trim().equals("")) {
							header = false;
						} else {
							handshakeLines.add(line.trim());
						}

						buffer = new byte[len];
						pos = 0;
					}
				}
			}

			handshake.verifyServerStatusLine(handshakeLines.get(0));
			handshake.verifyServerResponse(serverResponse);

			handshakeLines.remove(0);

			HashMap<String, String> headers = new HashMap<String, String>();
			for (String line : handshakeLines) {
				String[] keyValue = line.split(": ", 2);
				headers.put(keyValue[0], keyValue[1]);
			}
			handshake.verifyServerHandshakeHeaders(headers);

			receiver = new WebSocketReceiver(input, this);
			sender = new WebSocketSender(output, this);
			receiver.start();
			connected = true;
			eventHandler.onOpen();
		} catch (WebSocketException wse) {
			throw wse;
		} catch (IOException ioe) {
			throw new WebSocketException("error while connecting: "
					+ ioe.getMessage(), ioe);
		}
	}

	public synchronized void send(String data) throws WebSocketException {
		if(sender != null && data != null){
			sender.send(data);
		}		
	}

	public void handleReceiverError() {
		try {
			if (connected) {
				close(true);
			}
		} catch (WebSocketException wse) {
			if (Log.isLoggable(TAG, DEBUG))
				Log.d(TAG, "Exception closing web socket", wse);
		}
	}

	public synchronized void close(final boolean isForced) throws WebSocketException {
		if (!connected) {
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					sendCloseHandshake();

					if (receiver.isRunning()) {
						receiver.stopit();
					}

					closeStreams();

					if(!isForced){
						eventHandler.onClose();
					}else{
						eventHandler.onForcedClose();
					}					
				} catch (Exception e) {
					eventHandler.onException(e);
				}
			}
		}).start();
	}

	private synchronized void sendCloseHandshake() throws WebSocketException {
		if(connected){
			try {
				output.write(0xff00);
				output.write("\r\n".getBytes());
			} catch (IOException ioe) {
				throw new WebSocketException("error while sending close handshake",
						ioe);
			}

			connected = false;
		}
	}

	private Socket createSocket() throws WebSocketException {
		String scheme = url.getScheme();
		String host = url.getHost();
		int port = url.getPort();

		Socket socket = null;

		if (scheme != null && scheme.equals("ws")) {
			if (port == -1) {
				port = 80;
			}
			try {
				socket = new Socket(host, port);
				socket.setKeepAlive(true);
				socket.setSoTimeout(0);
			} catch (UnknownHostException uhe) {
				throw new WebSocketException("unknown host: " + host, uhe);
			} catch (IOException ioe) {
				throw new WebSocketException("error while creating socket to "
						+ url, ioe);
			}
		} else if (scheme != null && scheme.equals("wss")) {
			if (port == -1) {
				port = 443;
			}
			try {
				if (trustAllCerts)
					try {
						final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
							public X509Certificate[] getAcceptedIssuers() {
								return new X509Certificate[0];
							}

							public void checkClientTrusted(
									X509Certificate[] chain, String authType)
									throws CertificateException {
							}

							public void checkServerTrusted(
									X509Certificate[] chain, String authType)
									throws CertificateException {
							}
						} };
						SSLContext context = SSLContext.getInstance("TLS");
						context.init(null, trustAllCerts, new SecureRandom());
						socket = context.getSocketFactory().createSocket(host,
								port);
					} catch (GeneralSecurityException e) {
						throw new WebSocketException("Security exception", e);
					}
				else
					socket = SSLSocketFactory.getDefault().createSocket(host,
							port);
			} catch (UnknownHostException uhe) {
				throw new WebSocketException("unknown host: " + host, uhe);
			} catch (IOException ioe) {
				throw new WebSocketException(
						"error while creating secure socket to " + url, ioe);
			}
		} else {
			throw new WebSocketException("unsupported protocol: " + scheme);
		}

		return socket;
	}

	private void closeStreams() throws WebSocketException {
		if (Log.isLoggable(TAG, DEBUG))
			Log.d(TAG, "closeStreams");

		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException ioe) {
			//throw new WebSocketException("error while closing websocket connection: ", ioe);
		}
	}

	public boolean isConnected() {
		return connected;
	}
}
