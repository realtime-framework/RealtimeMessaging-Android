package ibt.ortc.ortclib;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ibt.ortc.extensibility.OrtcClient;

/**
 * Created by jcaixinha on 05/06/15.
 *
 * Use this Class to communicate with your backend REST API to store the GCM registration Ids
 * This is useful to avoid duplicated push notifications when the app is installed multiple times (namely during development)
 */
public class RegistrationIdRemoteStore {
    // Enter your backend REST API URL
    private final static String backendURL = "http://localhost";

    public static void getRegistrationIdFromBackend(final Context context, final OrtcClient ortcClient){
        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {

                HttpResponse response = null;
                CharSequence appName =
                        context
                                .getPackageManager()
                                .getApplicationLabel(context.getApplicationInfo());
                String app_id = (String) appName;
                app_id = app_id + Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                String urlString = RegistrationIdRemoteStore.backendURL +"/RegistrationIdStore?ACTION=getRegistration&app_id="+app_id;
                urlString = urlString.replace(' ', '+');
                String resultToDisplay = "";

                InputStream in = null;

                // HTTP Get
                try {

                    URL url = new URL(urlString);

                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();

                    in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line);
                    }

                    resultToDisplay = total.toString();
                } catch (Exception e) {

                    System.out.println(e.getMessage());

                    return e.getMessage();

                }

                return resultToDisplay;
            }

            protected void onPostExecute(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    ortcClient.setRegistrationId(json.getString("reg_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }.execute();
    }

    public static void setRegistrationIdToBackend(final Context context, final String reg_id){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                CharSequence appName =
                        context
                                .getPackageManager()
                                .getApplicationLabel(context.getApplicationInfo());
                String app_id = (String) appName;
                app_id = app_id + Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                String resultToDisplay = "";

                InputStream in = null;

                // HTTP Get
                try {
                    String urlString = RegistrationIdRemoteStore.backendURL +"/RegistrationIdStore?ACTION=setRegistration&app_id="+app_id+"&reg_id="+reg_id;
                    urlString = urlString.replace(' ', '+');
                    URL url = new URL(urlString);

                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();

                    in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line);
                    }

                    resultToDisplay = total.toString();
                } catch (Exception e) {

                    System.out.println(e.getMessage());

                    return e.getMessage();

                }

                return resultToDisplay;

            }
        }.execute();
    }
}
