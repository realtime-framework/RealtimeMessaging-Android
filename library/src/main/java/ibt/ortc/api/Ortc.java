/**
 * @fileoverview This file contains the class to create ortc factories
 * @author ORTC team members (ortc@ibt.pt) 
 */
package ibt.ortc.api;

import ibt.ortc.extensibility.OnMessage;
import ibt.ortc.extensibility.OnMessageWithPayload;
import ibt.ortc.extensibility.OrtcFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Class containing the methods to create Ortc Client factories and use the Ortc
 * Rest services <br>
 * <b>How to use in android:<b>
 * 
 * <pre>
 * try {
 * 	Ortc ortc = new Ortc();
 * 
 * 	OrtcFactory factory;
 * 
 * 	factory = ortc.loadOrtcFactory(&quot;IbtRealtimeSJ&quot;);
 * 
 * 	client = factory.createClient();
 * 
 * 	HashMap&lt;String, ChannelPermissions&gt; permissions = new HashMap&lt;String, ChannelPermissions&gt;();
 * 	permissions.put(&quot;channel1:*&quot;, ChannelPermissions.Write);
 * 	permissions.put(&quot;channel1&quot;, ChannelPermissions.Write);
 * 
 * 	if (!Ortc.saveAuthentication(
 * 			&quot;http://ortc-developers.realtime.co/server/2.1/&quot;, true,
 * 			&quot;SessionId&quot;, false, &quot;APPKEY&quot;, 1800, &quot;PVTKEY&quot;, permissions)) {
 * 		throw new Exception(&quot;Was not possible to authenticate&quot;);
 * 	}
 * 
 * 	client.setClusterUrl(defaultServerUrl);
 * 	client.setConnectionMetadata(&quot;DroidApp&quot;);
 * 
 * 	client.OnConnected = new OnConnected() {
 * 		&#064;Override
 * 		public void run(final OrtcClient sender) {
 * 			runOnUiThread(new Runnable() {
 * 				&#064;Override
 * 				public void run() {
 * 					TextView t = ((TextView) findViewById(R.id.TextViewTitle));
 * 					t.setText(&quot;Client connected to: &quot;
 * 							+ ((OrtcClient) sender).getUrl());
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.OnDisconnected = new OnDisconnected() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender) {
 * 			runOnUiThread(new Runnable() {
 * 				&#064;Override
 * 				public void run() {
 * 					TextView t = ((TextView) findViewById(R.id.TextViewTitle));
 * 					t.setText(&quot;Client disconnected&quot;);
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.OnSubscribed = new OnSubscribed() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender, String channel) {
 * 			final String subscribedChannel = channel;
 * 			runOnUiThread(new Runnable() {
 * 				&#064;Override
 * 				public void run() {
 * 					TextView textViewLog = (TextView) findViewById(R.id.TextViewLog);
 * 					textViewLog.append(String.format(&quot;Channel subscribed %s\n&quot;,
 * 							subscribedChannel));
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.OnUnsubscribed = new OnUnsubscribed() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender, String channel) {
 * 			final String subscribedChannel = channel;
 * 			runOnUiThread(new Runnable() {
 * 				&#064;Override
 * 				public void run() {
 * 					TextView textViewLog = (TextView) findViewById(R.id.TextViewLog);
 * 					textViewLog.append(String.format(
 * 							&quot;Channel unsubscribed %s\n&quot;, subscribedChannel));
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.OnException = new OnException() {
 * 		&#064;Override
 * 		public void run(OrtcClient send, Exception ex) {
 * 			final Exception exception = ex;
 * 			runOnUiThread(new Runnable() {
 * 				&#064;Override
 * 				public void run() {
 * 					TextView textViewLog = (TextView) findViewById(R.id.TextViewLog);
 * 					textViewLog.append(String.format(&quot;Ortc Error: %s\n&quot;,
 * 							exception.getMessage()));
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.OnReconnected = new OnReconnected() {
 * 		&#064;Override
 * 		public void run(final OrtcClient sender) {
 * 			runOnUiThread(new Runnable() {
 * 				&#064;Override
 * 				public void run() {
 * 					reconnectingTries = 0;
 * 					TextView textViewLog = (TextView) findViewById(R.id.TextViewTitle);
 * 					textViewLog.setText(&quot;Client reconnected to: &quot;
 * 							+ ((OrtcClient) sender).getUrl());
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.OnReconnecting = new OnReconnecting() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender) {
 * 			runOnUiThread(new Runnable() {
 * 				&#064;Override
 * 				public void run() {
 * 					reconnectingTries++;
 * 					TextView textViewLog = (TextView) findViewById(R.id.TextViewTitle);
 * 					textViewLog.setText(String.format(&quot;Client reconnecting %s&quot;,
 * 							reconnectingTries));
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.connect(defaultApplicationKey, defaultAuthenticationToken);
 * 
 * } catch (Exception e) {
 * 	System.out.println(&quot;ORTC ERROR: &quot; + e.toString());
 * }
 * </pre>
 * 
 * <br>
 * <b>How to use in java:<b>
 * 
 * <pre>
 * try {
 * 	boolean isBalancer = true;
 * 
 * 	Ortc api = new Ortc();
 * 
 * 	OrtcFactory factory = api.loadOrtcFactory(&quot;IbtRealtimeSJ&quot;);
 * 
 * 	final OrtcClient client = factory.createClient();
 * 
 * 	if (isBalancer) {
 * 		client.setClusterUrl(serverUrl);
 * 	} else {
 * 		client.setUrl(serverUrl);
 * 	}
 * 
 * 	System.out.println(String.format(&quot;Connecting to server %s&quot;, serverUrl));
 * 
 * 	client.OnConnected = new OnConnected() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender) {
 * 			System.out
 * 					.println(String.format(&quot;Connected to %s&quot;, client.getUrl()));
 * 			System.out.println(String.format(&quot;Session ID: %s\n&quot;,
 * 					((OrtcClient) sender).getSessionId()));
 * 
 * 			client.subscribe(&quot;channel1&quot;, true, new OnMessage() {
 * 				&#064;Override
 * 				public void run(Object sender, String channel, String message) {
 * 					System.out.println(String.format(
 * 							&quot;Message received on channel %s: '%s'&quot;, channel,
 * 							message));
 * 
 * 					((OrtcClient) sender).send(channel, &quot;Echo &quot; + message);
 * 				}
 * 			});
 * 		}
 * 	};
 * 
 * 	client.OnException = new OnException() {
 * 		&#064;Override
 * 		public void run(OrtcClient send, Exception ex) {
 * 			System.out.println(String.format(&quot;Error: '%s'&quot;, ex.toString()));
 * 		}
 * 	};
 * 
 * 	client.OnDisconnected = new OnDisconnected() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender) {
 * 			System.out.println(&quot;Disconnected&quot;);
 * 		}
 * 	};
 * 
 * 	client.OnReconnected = new OnReconnected() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender) {
 * 			System.out.println(String.format(&quot;Reconnected to %s&quot;,
 * 					client.getUrl()));
 * 		}
 * 	};
 * 
 * 	client.OnReconnecting = new OnReconnecting() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender) {
 * 			System.out.println(String.format(&quot;Reconnecting to %s&quot;,
 * 					client.getUrl()));
 * 		}
 * 	};
 * 
 * 	client.OnSubscribed = new OnSubscribed() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender, String channel) {
 * 			System.out.println(String.format(&quot;Subscribed to channel %s&quot;,
 * 					channel));
 * 		}
 * 	};
 * 
 * 	client.OnUnsubscribed = new OnUnsubscribed() {
 * 		&#064;Override
 * 		public void run(OrtcClient sender, String channel) {
 * 			System.out.println(String.format(&quot;Unsubscribed from channel %s&quot;,
 * 					channel));
 * 		}
 * 	};
 * 
 * 	System.out.println(&quot;Connecting...&quot;);
 * 	client.connect(&quot;APPLICATION_KEY&quot;, &quot;AUTHENTICATION_TOKEN&quot;);
 * 
 * } catch (Exception e) {
 * 	System.out.println(&quot;ORTC ERROR: &quot; + e.toString());
 * }
 * </pre>
 * How to use ORTC with GCM (Google Cloud Messaging)

 * In order to use GCM you have to fulfilled the following steps:
 * 1. Create a reference to Google Play Services library. See http://developer.android.com/google/play-services/setup.html for more informations.
 *  2. Add following tags to your application's manifest file:
 *  <uses-permission android:name="android.permission.GET_ACCOUNTS" />
 *  <uses-permission android:name="android.permission.WAKE_LOCK" />
 *  <permission android:name="YOUR_PACKAGE_NAME.permission.C2D_MESSAGE" android:protectionLevel="signature" />
 *  <uses-permission android:name="YOUR_PACKAGE_NAME.permission.C2D_MESSAGE" />
 *  <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
 *  Remember to change YOUR_PACKAGE_NAME for the package name of your application (ex: com.example.your.application).
 *
 *  3. Add following tags to your application's manifest file, as a child of the <application> element:
 *  <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
 *  <receiver
 *  android:name="ibt.ortc.extensibility.GcmOrtcBroadcastReceiver"
 *  android:permission="com.google.android.c2dm.permission.SEND" >
 *  <intent-filter>
 *  <action android:name="com.google.android.c2dm.intent.RECEIVE" />
 *  <category android:name="YOUR_PACKAGE_NAME" />
 *  </intent-filter>
 *  </receiver>
 *  <service android:name="ibt.ortc.extensibility.GcmOrtcIntentService" />
 *  Remember to change YOUR_PACKAGE_NAME for the package name of your application (ex: com.example.your.application).
 *
 *  4. Before perform the method 'connect' on the OrtcClient instance, you have to provide the application context (with method 'setApplicationContext') and Google project Id (with method 'setGoogleProjectId'). See the code snippet below:
 *  OrtcClient client = (new Ortc()).loadOrtcFactory("IbtRealtimeSJ").createClient();
 *  client.setApplicationContext(getApplicationContext());
 *  client.setGoogleProjectId("0123456789");
 *  client.setClusterUrl("http://ortc-developers.realtime.co/server/2.1/");
 *  client.connect("your_application_key", "your_authentication_token");
 *  If you do not provide the application context or Google project Id, the ORTC API will not register the device during connect process and will not use Google Cloud Messaging.
 *
 *
 *  Disclaimer:
 *  1. GCM require the Google Play Services to be installed and enabled at client device.
 *  2. When you perform for the very first time the method 'connect', ORTC API will try to register your application with GCM service to obtain the registration id. It is an asynchronous process and it can take a while. So when you perform the method 'subscribeWithNotifications' right after the method 'connect', you may get an ORTC exception "The application is not registered with GCM yet!". After the registration, the id is stored in the application's shared preferences, so it will be available immediately.
 * 
 * @version 2.1.0 27 Mar 2013
 * @author IBT
 * 
 */
public class Ortc {
	
	private static OnMessageWithPayload onPushNotification;

	public Ortc() {
	}

	/**
	 * Creates an instance of a factory of the specified Ortc plugin type
	 * 
	 * @param ortcType
	 *            The Ortc plugin type
	 * @return Instance of Ortc factory
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public OrtcFactory loadOrtcFactory(String ortcType)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		OrtcFactory result = null;

		// Gets the plugin class definition
		Class<?> factoryClass = this
				.getClass()
				.getClassLoader()
				.loadClass(
						String.format("ibt.ortc.plugins.%s.%sFactory",
								ortcType, ortcType));

		if (factoryClass != null) {
			// Creates an instance of the plugin class
			result = OrtcFactory.class.cast(factoryClass.newInstance());
		}

		return result;
	}

	/**
	 * Saves the authentication token channels permissions in the ORTC server.
	 * 
	 * <pre>
	 * HashMap&lt;String, ChannelPermissions&gt; permissions = new HashMap&lt;String, ChannelPermissions&gt;();
	 * permissions.put(&quot;channel1:*&quot;, ChannelPermissions.Write);
	 * permissions.put(&quot;channel1&quot;, ChannelPermissions.Write);
	 * 
	 * if (!Ortc.saveAuthentication(&quot;http://ortc-developers.realtime.co/server/2.1/&quot;,
	 * 		true, &quot;SessionId&quot;, false, &quot;APPKEY&quot;, 1800, &quot;PVTKEY&quot;, permissions)) {
	 * 	throw new Exception(&quot;Was not possible to authenticate&quot;);
	 * }
	 * </pre>
	 * 
	 * @param url
	 *            Ortc Server Url
	 * @param isCluster
	 *            Indicates whether the ORTC server is in a cluster.
	 * @param authenticationToken
	 *            Authentication Token which is generated by the application
	 *            server, for instance a unique session ID.
	 * @param authenticationTokenIsPrivate
	 *            Indicates whether the authentication token is private (true)
	 *            or not (false)
	 * @param applicationKey
	 *            Application Key that was provided to you together with the
	 *            ORTC service purchasing.
	 * @param timeToLive
	 *            The authentication token time to live, in other words, the
	 *            allowed activity time (in seconds).
	 * @param privateKey
	 *            The private key provided to you together with the ORTC service
	 *            purchasing.
	 * @param permissions
	 *            The channels and their permissions (w: write/read or r: read
	 *            or p: presence, case sensitive).
	 * @return True if the authentication was successful or false if it was not.
	 * @throws ibt.ortc.api.OrtcAuthenticationNotAuthorizedException
	 */
	public static boolean saveAuthentication(String url, boolean isCluster,
			String authenticationToken, boolean authenticationTokenIsPrivate,
			String applicationKey, int timeToLive, String privateKey,
			Map<String, ChannelPermissions> permissions) throws IOException,
			InvalidBalancerServerException,
			OrtcAuthenticationNotAuthorizedException {
		String connectionUrl = url;
		if (isCluster) {
			connectionUrl = Balancer.getServerFromBalancer(url, applicationKey);
		}

		boolean isAuthenticated = false;

		try {
			URL authenticationUrl = new URL(String.format("%s/authenticate",
					connectionUrl));

			Map<String, LinkedList<ChannelPermissions>> permissionsMap = new HashMap<String, LinkedList<ChannelPermissions>>();

			Set<String> channels = permissions.keySet();
			for (String channelName : channels) {
				LinkedList<ChannelPermissions> channelPermissionList = new LinkedList<ChannelPermissions>();
				channelPermissionList.add(permissions.get(channelName));
				permissionsMap.put(channelName, channelPermissionList);
			}

			isAuthenticated = Authentication.saveAuthentication(
					authenticationUrl, authenticationToken,
					authenticationTokenIsPrivate, applicationKey, timeToLive,
					privateKey, permissionsMap);
		} catch (Exception e) {
			throw new OrtcAuthenticationNotAuthorizedException(e.getMessage());
		}

		return isAuthenticated;
	}

	/**
	 * Saves the authentication token channels permissions in the ORTC server.
	 * 
	 * <pre>
	 * HashMap&lt;String, LinkedList&lt;ChannelPermissions&gt;&gt; permissions = new HashMap&lt;String, LinkedList&lt;ChannelPermissions&gt;&gt;();
	 * 
	 * LinkedList&lt;ChannelPermissions&gt; channelPermissions = new LinkedList&lt;ChannelPermissions&gt;();
	 * channelPermissions.add(ChannelPermissions.Write);
	 * channelPermissions.add(ChannelPermissions.Presence);
	 * 
	 * permissions.put(&quot;channel&quot;, channelPermissions);
	 * 
	 * if (!Ortc.saveAuthentication(&quot;http://ortc-developers.realtime.co/server/2.1/&quot;,
	 * 		true, &quot;SessionId&quot;, false, &quot;APPKEY&quot;, 1800, &quot;PVTKEY&quot;, permissions)) {
	 * 	throw new Exception(&quot;Was not possible to authenticate&quot;);
	 * }
	 * </pre>
	 * 
	 * @param url
	 *            Ortc Server Url
	 * @param isCluster
	 *            Indicates whether the ORTC server is in a cluster.
	 * @param authenticationToken
	 *            Authentication Token which is generated by the application
	 *            server, for instance a unique session ID.
	 * @param authenticationTokenIsPrivate
	 *            Indicates whether the authentication token is private (true)
	 *            or not (false)
	 * @param applicationKey
	 *            Application Key that was provided to you together with the
	 *            ORTC service purchasing.
	 * @param timeToLive
	 *            The authentication token time to live, in other words, the
	 *            allowed activity time (in seconds).
	 * @param privateKey
	 *            The private key provided to you together with the ORTC service
	 *            purchasing.
	 * @param permissions
	 *            HashMap&lt;String,LinkedList&lt;String,ChannelPermissions&gt;&
	 *            gt; permissions& The channels and their permissions (w:
	 *            write/read or r: read or p: presence, case sensitive).
	 * @return True if the authentication was successful or false if it was not.
	 * @throws ibt.ortc.api.OrtcAuthenticationNotAuthorizedException
	 */
	public static boolean saveAuthentication(String url, boolean isCluster,
			String authenticationToken, boolean authenticationTokenIsPrivate,
			String applicationKey, int timeToLive, String privateKey,
			HashMap<String, LinkedList<ChannelPermissions>> permissions)
			throws IOException, InvalidBalancerServerException,
			OrtcAuthenticationNotAuthorizedException {
		String connectionUrl = url;
		if (isCluster) {
			connectionUrl = Balancer.getServerFromBalancer(url, applicationKey);
		}

		boolean isAuthenticated = false;

		try {
			URL authenticationUrl = new URL(String.format("%s/authenticate",
					connectionUrl));

			isAuthenticated = Authentication.saveAuthentication(
					authenticationUrl, authenticationToken,
					authenticationTokenIsPrivate, applicationKey, timeToLive,
					privateKey, permissions);
		} catch (Exception e) {
			throw new OrtcAuthenticationNotAuthorizedException(e.getMessage());
		}

		return isAuthenticated;
	}

	/**
	 * Asynchronously saves the authentication token channels permissions in the ORTC server.
	 * 
	 * <pre>
	 * HashMap&lt;String, ChannelPermissions&gt; permissions = new HashMap&lt;String, ChannelPermissions&gt;();
	 * permissions.put(&quot;channel1:*&quot;, ChannelPermissions.Write);
	 * permissions.put(&quot;channel1&quot;, ChannelPermissions.Write);
	 * 
	 * if (!Ortc.saveAuthentication(&quot;http://ortc-developers.realtime.co/server/2.1/&quot;,
	 * 		true, &quot;SessionId&quot;, false, &quot;APPKEY&quot;, 1800, &quot;PVTKEY&quot;, permissions)) {
	 * 	throw new Exception(&quot;Was not possible to authenticate&quot;);
	 * }
	 * </pre>
	 * 
	 * @param url
	 *            Ortc Server Url
	 * @param isCluster
	 *            Indicates whether the ORTC server is in a cluster.
	 * @param authenticationToken
	 *            Authentication Token which is generated by the application
	 *            server, for instance a unique session ID.
	 * @param authenticationTokenIsPrivate
	 *            Indicates whether the authentication token is private (true)
	 *            or not (false)
	 * @param applicationKey
	 *            Application Key that was provided to you together with the
	 *            ORTC service purchasing.
	 * @param timeToLive
	 *            The authentication token time to live, in other words, the
	 *            allowed activity time (in seconds).
	 * @param privateKey
	 *            The private key provided to you together with the ORTC service
	 *            purchasing.
	 * @param permissions
	 *            The channels and their permissions (w: write/read or r: read
	 *            or p: presence, case sensitive).
	 * @param onCompleted
	 *            The callback that is executed after the save authentication is completed
	 * @throws ibt.ortc.api.OrtcAuthenticationNotAuthorizedException
	 */
	public static void saveAuthentication(String url, boolean isCluster,
			final String authenticationToken, final boolean authenticationTokenIsPrivate,
			final String applicationKey, final int timeToLive, final String privateKey,
			final Map<String, ChannelPermissions> permissions,
			final OnRestWebserviceResponse onCompleted) throws IOException,
			InvalidBalancerServerException,
			OrtcAuthenticationNotAuthorizedException {
		
		HashMap<String, LinkedList<ChannelPermissions>> permissionsMap = new HashMap<String, LinkedList<ChannelPermissions>>();

		Set<String> channels = permissions.keySet();
		for (String channelName : channels) {
			LinkedList<ChannelPermissions> channelPermissionList = new LinkedList<ChannelPermissions>();
			channelPermissionList.add(permissions.get(channelName));
			permissionsMap.put(channelName, channelPermissionList);
		}
		
		saveAuthentication(url, isCluster, authenticationToken, authenticationTokenIsPrivate, applicationKey, timeToLive, privateKey, permissionsMap, onCompleted);
	}
	
	/**
	 * Saves the authentication token channels permissions in the ORTC server.
	 * 
	 * <pre>
	 * HashMap&lt;String, LinkedList&lt;ChannelPermissions&gt;&gt; permissions = new HashMap&lt;String, LinkedList&lt;ChannelPermissions&gt;&gt;();
	 * 
	 * LinkedList&lt;ChannelPermissions&gt; channelPermissions = new LinkedList&lt;ChannelPermissions&gt;();
	 * channelPermissions.add(ChannelPermissions.Write);
	 * channelPermissions.add(ChannelPermissions.Presence);
	 * 
	 * permissions.put(&quot;channel&quot;, channelPermissions);
	 * 
	 * if (!Ortc.saveAuthentication(&quot;http://ortc-developers.realtime.co/server/2.1/&quot;,
	 * 		true, &quot;SessionId&quot;, false, &quot;APPKEY&quot;, 1800, &quot;PVTKEY&quot;, permissions)) {
	 * 	throw new Exception(&quot;Was not possible to authenticate&quot;);
	 * }
	 * </pre>
	 * 
	 * @param url
	 *            Ortc Server Url
	 * @param isCluster
	 *            Indicates whether the ORTC server is in a cluster.
	 * @param authenticationToken
	 *            Authentication Token which is generated by the application
	 *            server, for instance a unique session ID.
	 * @param authenticationTokenIsPrivate
	 *            Indicates whether the authentication token is private (true)
	 *            or not (false)
	 * @param applicationKey
	 *            Application Key that was provided to you together with the
	 *            ORTC service purchasing.
	 * @param timeToLive
	 *            The authentication token time to live, in other words, the
	 *            allowed activity time (in seconds).
	 * @param privateKey
	 *            The private key provided to you together with the ORTC service
	 *            purchasing.
	 * @param permissions
	 *            HashMap&lt;String,LinkedList&lt;String,ChannelPermissions&gt;&
	 *            gt; permissions& The channels and their permissions (w:
	 *            write/read or r: read or p: presence, case sensitive).
	 * @param onCompleted
	 *            The callback that is executed after the save authentication is completed
	 * @throws ibt.ortc.api.OrtcAuthenticationNotAuthorizedException
	 */
	public static void saveAuthentication(String url, boolean isCluster,
			final String authenticationToken, final boolean authenticationTokenIsPrivate,
			final String applicationKey, final int timeToLive, final String privateKey,
			final HashMap<String, LinkedList<ChannelPermissions>> permissions,
			final OnRestWebserviceResponse onCompleted) throws IOException,
			InvalidBalancerServerException,
			OrtcAuthenticationNotAuthorizedException {
		String connectionUrl = url;
		if (isCluster) {
			Balancer.getServerFromBalancerAsync(url, applicationKey, new OnRestWebserviceResponse() {
				@Override
				public void run(Exception error, String response) {
					if(error != null){
						onCompleted.run(error, null);
					}else{
						saveAuthenticationAsync(response, authenticationToken, authenticationTokenIsPrivate, applicationKey, timeToLive, privateKey, permissions, onCompleted);
					}
				}
			});
		}else{
			saveAuthenticationAsync(connectionUrl, authenticationToken, authenticationTokenIsPrivate, applicationKey, timeToLive, privateKey, permissions, onCompleted);
		}		
	}

	private static void saveAuthenticationAsync(String url, 
			String authenticationToken, boolean authenticationTokenIsPrivate,
			String applicationKey, int timeToLive, String privateKey,
			HashMap<String, LinkedList<ChannelPermissions>> permissions,
			OnRestWebserviceResponse onCompleted){
		try {
			URL authenticationUrl = new URL(String.format("%s/authenticate",
					url));

			Authentication.saveAuthenticationAsync(authenticationUrl,
					authenticationToken, authenticationTokenIsPrivate,
					applicationKey, timeToLive, privateKey, permissions,
					onCompleted);
		} catch (Exception e) {
			onCompleted.run(e, null);
		}
	}
	
	/**
	 * Gets the subscriptions in the specified channel and if active the first
	 * 100 unique metadata.
	 * 
	 * <pre>
	 * Ortc.presence(&quot;http://ortc-developers.realtime.co/server/2.1/&quot;, true,
	 * 		&quot;APPLICATION_KEY&quot;, &quot;AUTHENTICATION_TOKEN&quot;, &quot;CHANNEL&quot;, new onPresence() {
	 * 
	 * 			public void run(Exception error, Presence presence) {
	 * 				if (error != null) {
	 * 					System.out.println(error.getMessage());
	 * 				} else {
	 * 					System.out.println(&quot;Subscriptions - &quot;
	 * 							+ presence.getSubscriptions());
	 * 
	 * 					Iterator&lt;?&gt; metadataIterator = presence.getMetadata()
	 * 							.entrySet().iterator();
	 * 					while (metadataIterator.hasNext()) {
	 * 						Map.Entry&lt;String, Long&gt; entry = (Map.Entry&lt;String, Long&gt;) metadataIterator
	 * 								.next();
	 * 						System.out.println(entry.getKey() + &quot; - &quot;
	 * 								+ entry.getValue());
	 * 					}
	 * 				}
	 * 			}
	 * 		});
	 * </pre>
	 * 
	 * @param url
	 *            Server containing the presence service.
	 * @param isCluster
	 *            Specifies if url is cluster.
	 * @param applicationKey
	 *            Application key with access to presence service.
	 * @param authenticationToken
	 *            Authentication token with access to presence service.
	 * @param channel
	 *            Channel with presence data active.
	 * @param callback
	 *            Callback with error and result.
	 */
	public static void presence(String url, Boolean isCluster,
			String applicationKey, String authenticationToken, String channel,
			OnPresence callback) {
		Presence.getPresence(url, isCluster, applicationKey,
				authenticationToken, channel, callback);
	}

	/**
	 * Enables presence for the specified channel with first 100 unique metadata
	 * if metadata is set to true.
	 * 
	 * <pre>
	 * Ortc.enablePresence(&quot;http://ortc-developers.realtime.co/server/2.1/&quot;, true,
	 * 		&quot;APPLICATION_KEY&quot;, &quot;PRIVATE_KEY&quot;, &quot;CHANNEL&quot;, true,
	 * 		new onEnablePresence() {
	 * 
	 * 			public void run(Exception error, String result) {
	 * 				if (error != null) {
	 * 					System.out.println(error.getMessage());
	 * 				} else {
	 * 					System.out.println(result);
	 * 
	 * 				}
	 * 			}
	 * 		});
	 * </pre>
	 * 
	 * @param url
	 *            Server containing the presence service.
	 * @param isCluster
	 *            Specifies if url is cluster.
	 * @param applicationKey
	 *            Application key with access to presence service.
	 * @param privateKey
	 *            The private key provided when the ORTC service is purchased.
	 * @param channel
	 *            Channel with presence data active.
	 * @param metadata
	 *            Defines if to collect first 100 unique metadata.
	 * @param callback
	 *            Callback with error and result.
	 */
	public static void enablePresence(String url, Boolean isCluster,
			String applicationKey, String privateKey, String channel,
			Boolean metadata, OnEnablePresence callback) {
		Presence.enablePresence(url, isCluster, applicationKey, privateKey,
				channel, metadata, callback);
	}

	/**
	 * Disables presence for the specified channel.
	 * 
	 * <pre>
	 * Ortc.disablePresence(&quot;http://ortc-developers.realtime.co/server/2.1/&quot;, true,
	 * 		&quot;APPLICATION_KEY&quot;, &quot;PRIVATE_KEY&quot;, &quot;CHANNEL&quot;, new onDisablePresence() {
	 * 
	 * 			public void run(Exception error, String result) {
	 * 				if (error != null) {
	 * 					System.out.println(error.getMessage());
	 * 				} else {
	 * 					System.out.println(result);
	 * 
	 * 				}
	 * 			}
	 * 		});
	 * </pre>
	 * 
	 * @param url
	 *            Server containing the presence service.
	 * @param isCluster
	 *            Specifies if url is cluster.
	 * @param applicationKey
	 *            Application key with access to presence service.
	 * @param privateKey
	 *            The private key provided when the ORTC service is purchased.
	 * @param channel
	 *            Channel to disable presence
	 * @param callback
	 *            Callback with error and result.
	 */
	public static void disablePresence(String url, Boolean isCluster,
			String applicationKey, String privateKey, String channel,
			OnDisablePresence callback) {
		Presence.disablePresence(url, isCluster, applicationKey, privateKey,
				channel, callback);
	}

	/**
	 * Returns the on message event to be executed when a push notification is received and there is a Realtime Client that is not connected
	 * @return onPushNotification
	 */
	public static OnMessageWithPayload getOnPushNotification() {
		return onPushNotification;
	}


	/**
	 * Sets the on message event to be executed when a push notification is received and there is a Realtime Client that is not connected
	 * @param onPushNotification
	 * 			Executed when a notification is received and there is a Realtime Client that is not connected 
	 */
	public static void setOnPushNotification(OnMessageWithPayload onPushNotification) {
		Ortc.onPushNotification = onPushNotification;
	}


}
