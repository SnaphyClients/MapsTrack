package com.snaphy.mapstrack;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by Ravi-Gupta on 3/9/2016.
 */
public class GcmIntentService extends IntentService  {

    public int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private static String verificationCode;

    public static String getStatus() {
        return status;
    }

    private static String status;
    public Matcher m;
    int mNotificationId1 = 001;
    int mNotificationId2 = 002;


    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String message = (String)extras.getString("message");
                Log.i(Constants.TAG, "Message is: " + message);
                Gson gson = new Gson();

                if (message == "5001") {

                    //New order has arrived..
                    //Send push message..
                    sendNotification(message , Constants.TAG);

                }
                else{
                    sendNotification(message , Constants.TAG);
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }




    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        int color = 0xff14649f;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("mapstrack")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mapstrack_logo))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(msg)
                        .setColor(color);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        Random random = new Random();
        int randomNumber = random.nextInt(9999 - 1000) + 1000;
        mNotificationManager.notify(randomNumber, mBuilder.build());
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String subject) {
        int color = 0xff14649f;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        /*Intent notificationIntent;
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);*/

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mapstrack_logo))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(subject)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg)
                        .setColor(color);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        Random random = new Random();
        int randomNumber = random.nextInt(9999 - 1000) + 1000;
        mNotificationManager.notify(randomNumber, mBuilder.build());

    }

}
