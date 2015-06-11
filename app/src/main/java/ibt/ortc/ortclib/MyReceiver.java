package ibt.ortc.ortclib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.Map;
import ibt.ortc.plugins.IbtRealtimeSJ.OrtcMessage;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";
    private JSONParser parser = new JSONParser();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "BOOT");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            try {
                createNotification(context, extras);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void createNotification(Context context, Bundle extras) throws ParseException {
        if(extras != null){
            if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
                String strPayload = extras.getString("P");
                String message = extras.getString("M");
                String channel = extras.getString("C");
                Map<String, Object> payload = null;

                if (strPayload != null) {
                    payload = (Map<String, Object>) parser.parse(strPayload);
                }

                String fstPass = JSONValue.toJSONString(message);
                fstPass = fstPass.substring(1, fstPass.length() - 1);
                String sndPass = JSONValue.toJSONString(fstPass);
                sndPass = sndPass.substring(1, sndPass.length() - 1);
                String messageForOrtc = String.format("a[\"{\\\"ch\\\":\\\"%s\\\",\\\"m\\\":\\\"%s\\\"}\"]", channel, sndPass);

                try {
                    OrtcMessage ortcMessage = OrtcMessage.parseMessage(messageForOrtc);

                    if (MainActivity.isInForeground() == false)
                    {
                        displayNotification(context, ortcMessage.getMessageChannel(), ortcMessage.getMessage(), payload);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    private void displayNotification(Context context, String channel, String message, Map<String, Object> payload) {
        Log.i(TAG, "on Notifier");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String appName = getAppName(context);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("channel", channel);
        notificationIntent.putExtra("message", message);

        if (payload != null){
            JSONObject json = new JSONObject(payload);
            notificationIntent.putExtra("payload", json.toString());
        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context).setContentTitle(appName).setContentText(message).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent).setAutoCancel(true).build();
        notificationManager.notify(9999, notification);
    }

    private String getAppName(Context context)
    {
        CharSequence appName =
                context
                        .getPackageManager()
                        .getApplicationLabel(context.getApplicationInfo());

        return (String)appName;
    }


}
