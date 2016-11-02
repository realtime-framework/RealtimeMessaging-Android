package ibt.ortc.extensibility;

import ibt.ortc.api.Ortc;
import ibt.ortc.extensibility.exception.OrtcGcmException;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

class GcmRegistration {
	static GoogleCloudMessaging gcm;
	static final String PROPERTY_REG_ID = "registration_id";
	static final String TAG = "GCM ORTC";
	static final String PROPERTY_APP_VERSION = "appVersion";
//	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	protected static void getRegistrationId(OrtcClient ortcClient){
		if (checkPlayServices(ortcClient)) {
            gcm = GoogleCloudMessaging.getInstance(ortcClient.appContext);

            if (ortcClient.registrationId.isEmpty()){
                String regid = getRegistrationId(ortcClient.appContext);
                ortcClient.registrationId = regid;
                if (regid.isEmpty()) {
                    registerInBackground(ortcClient);
                }
            }

        } else {
        	ortcClient.raiseOrtcEvent(EventEnum.OnException, ortcClient, new OrtcGcmException("No valid Google Play Services APK found."));
        }
	}
	
	/**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices(OrtcClient ortcClient) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ortcClient.appContext);
        if (resultCode != ConnectionResult.SUCCESS) {
        	/*
        	if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) ortcClient.appContext, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            	ortcClient.raiseOrtcEvent(EventEnum.OnException, ortcClient, new OrtcGcmException("The device is not supported!"));                
            }*/
            return false;
        }
        return true;
    }
    
    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }

        if (OrtcClient.getOnRegistrationId() != null){
            OrtcClient.getOnRegistrationId().run(registrationId);
        }

        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
    
    private static void registerInBackground(final OrtcClient ortcClient) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(ortcClient.appContext);
                    }
                    String regid = gcm.register(ortcClient.googleProjectId);
                    ortcClient.registrationId = regid;
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    //sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    if (OrtcClient.getOnRegistrationId() != null){
                        OrtcClient.getOnRegistrationId().run(regid);
                    }

                    storeRegistrationId(ortcClient.appContext, regid);
                } catch (IOException ex) {
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
				return null;
            }
        }.execute(null, null, null);
    }
 
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
