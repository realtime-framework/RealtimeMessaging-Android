/**
 * 
 */
package ibt.ortc.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author ORTC team members (ortc@ibt.pt)
 * 
 */
public class Authentication {
	// CAUSE: Prefer throwing/catching meaningful exceptions instead of
	// Exception
	protected static boolean saveAuthentication(URL url,
			String authenticationToken, boolean authenticationTokenIsPrivate,
			String applicationKey, int timeToLive, String privateKey,
			Map<String, LinkedList<ChannelPermissions>> permissions)
			throws IOException {
		String postBody = String.format(
				"AT=%s&AK=%s&PK=%s&TTL=%s&TP=%s&PVT=%s", authenticationToken,
				applicationKey, privateKey, timeToLive, permissions.size(),
				(authenticationTokenIsPrivate ? "1" : "0"));

		// CAUSE: Inefficient use of keySet iterator instead of entrySet
		// iterator
		for (Map.Entry<String, LinkedList<ChannelPermissions>> channelNamePerms : permissions
				.entrySet()) {
			LinkedList<ChannelPermissions> channelPermissions = channelNamePerms
					.getValue();
			// CAUSE: Method concatenates strings using + in a loop
			// TODO: specify a correct capacity
			StringBuilder channelPermissionText = new StringBuilder(16);
			for (ChannelPermissions channelPermission : channelPermissions) {
				channelPermissionText.append(channelPermission.getPermission());
			}

			String channelPermission = String.format("&%s=%s",
					channelNamePerms.getKey(), channelPermissionText);
			postBody = String.format("%s%s", postBody, channelPermission);
		}

		// CAUSE: Unused assignment
		boolean isAuthenticated;
		isAuthenticated = "https".equals(url.getProtocol()) ? secureSaveAuthentication(
				url, postBody) : unsecureSaveAuthentication(url, postBody);

		return isAuthenticated;
	}

	protected static void saveAuthenticationAsync(URL url,
			String authenticationToken, boolean authenticationTokenIsPrivate,
			String applicationKey, int timeToLive, String privateKey,
			Map<String, LinkedList<ChannelPermissions>> permissions,
			OnRestWebserviceResponse onCompleted) throws IOException {
		String postBody = String.format(
				"AT=%s&AK=%s&PK=%s&TTL=%s&TP=%s&PVT=%s", authenticationToken,
				applicationKey, privateKey, timeToLive, permissions.size(),
				(authenticationTokenIsPrivate ? "1" : "0"));

		// CAUSE: Inefficient use of keySet iterator instead of entrySet
		// iterator
		for (Map.Entry<String, LinkedList<ChannelPermissions>> channelNamePerms : permissions
				.entrySet()) {
			LinkedList<ChannelPermissions> channelPermissions = channelNamePerms
					.getValue();
			// CAUSE: Method concatenates strings using + in a loop
			// TODO: specify a correct capacity
			StringBuilder channelPermissionText = new StringBuilder(16);
			for (ChannelPermissions channelPermission : channelPermissions) {
				channelPermissionText.append(channelPermission.getPermission());
			}

			String channelPermission = String.format("&%s=%s",
					channelNamePerms.getKey(), channelPermissionText);
			postBody = String.format("%s%s", postBody, channelPermission);
		}

		// CAUSE: Unused assignment
		RestWebservice.postAsync(url, postBody, onCompleted);
	}

	// CAUSE: Prefer throwing/catching meaningful exceptions instead of
	// Exception
	protected static boolean unsecureSaveAuthentication(URL url, String postBody)
			throws IOException {
		HttpURLConnection connection = null;
		boolean result = false;

		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			OutputStreamWriter wr = null;
			try {
				// CAUSE: Reliance on default encoding
				wr = new OutputStreamWriter(connection.getOutputStream(),
						"UTF-8");

				wr.write(postBody);

				wr.flush();
				// CAUSE: Method may fail to close stream on exception
			} finally {
				if (wr != null) {
					wr.close();
				}
			}

			BufferedReader rd = null;
			try {
				// CAUSE: Reliance on default encoding
				rd = new BufferedReader(new InputStreamReader(
						connection.getInputStream(), "UTF-8"));
				// CAUSE: Method may fail to close stream on exception
			} finally {
				if (rd != null) {
					rd.close();
				}
			}

			result = connection.getResponseCode() == 201;
			// CAUSE: Method may fail to close connection on exception
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return result;
	}

	// CAUSE: Prefer throwing/catching meaningful exceptions instead of
	// Exception
	private static boolean secureSaveAuthentication(URL url, String postBody)
			throws IOException {
		SSLSocketFactory sslsocketfactory = SecureWebConnections
				.getFullTrustSSLFactory();

		int port = url.getPort() == -1 ? 443 : url.getPort();

		SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(
				url.getHost(), port);
		String[] protocols = { "TLSv1" };
		sslsocket.setEnabledProtocols(protocols);

		OutputStream outputstream = sslsocket.getOutputStream();
		// CAUSE: Reliance on default encoding
		OutputStreamWriter outputstreamwriter = new OutputStreamWriter(
				outputstream, "UTF-8");
		BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

		// CAUSE: Reliance on default encoding
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				sslsocket.getInputStream(), "UTF-8"));

		String request = String.format("POST %s HTTP/1.1\r\n", url.getPath())
				+ "User-Agent: OrtcJavaApi\r\n" + "Connection: keep-alive\r\n"
				+ "Content-Length: " + postBody.length() + "\r\n"
				+ "Content-Type: application/x-www-form-urlencoded\r\n"
				+ "\r\n" + postBody + "\r\n\r\n";

		bufferedwriter.write(request);
		bufferedwriter.flush();

		String reply = stdIn.readLine();
		boolean isAuthenticated = false;
		// CAUSE: Assignment expressions nested inside other expressions
		while (!isAuthenticated && reply != null) {
			isAuthenticated = reply.contains("HTTP/1.1 201");
			reply = stdIn.readLine();
		}

		outputstream.close();
		stdIn.close();
		sslsocket.close();

		return isAuthenticated;
	}

	// CAUSE: Utility class contains only static elements and is still
	// instantiable
	private Authentication() {
	}
}
