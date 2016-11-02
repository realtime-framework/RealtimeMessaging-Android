package ibt.ortc.api;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SecureWebConnections {
	private static boolean alreadyAcceptingSSl = false;
	private static TrustManager[] trustAllCerts;
	private static SSLSocketFactory factory;

    public static SSLSocketFactory getFullTrustSSLFactory() {
		if (!alreadyAcceptingSSl) {
			alreadyAcceptingSSl = true;
			// Create a trust manager that does not validate certificate chains
			trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
				public X509Certificate[] getAcceptedIssuers() {
					//return null;
					return new X509Certificate[ 0 ];
				}

                @Override
				public void checkClientTrusted(
						X509Certificate[] certs, String authType) {
				}

                @Override
				public void checkServerTrusted(
						X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			try {
				SSLContext sc = SSLContext.getInstance("SSL");

				sc.init(new KeyManager[0], trustAllCerts, new SecureRandom( ));
				
                factory = sc.getSocketFactory();
            // CAUSE: Prefer throwing/catching meaningful exceptions instead of Exception
            } catch (NoSuchAlgorithmException e) {
                // CAUSE: Thrown exception is hidden
                //e.printStackTrace();
            } catch (KeyManagementException e) {
                // CAUSE: Thrown exception is hidden
                //e.printStackTrace();
			}
		}
		return factory;
	}

    // CAUSE: Utility class contains only static elements and is still instantiable
    private SecureWebConnections() {
    }
}
