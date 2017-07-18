package com.example.home_.news.sync;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.example.home_.news.MainActivity;
import com.example.home_.news.R;

/**
 * Created by Home- on 12/07/2017.
 */

public class NotifcationNews {
    public static void showNotifecation(Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.xlarge)
                .setLargeIcon(BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.vlarge))
                .setContentTitle("new news")
                .setContentText("new newss")
                .setAutoCancel(true);
        Intent detailIntentForToday = new Intent(context, MainActivity.class);
        //detailIntentForToday.setData(todaysWeatherUri);

//          COMPLETED (4) Use TaskStackBuilder to create the proper PendingINtent
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
        PendingIntent resultPendingIntent = taskStackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//          COMPLETED (5) Set the content Intent of the NotificationBuilder
        notificationBuilder.setContentIntent(resultPendingIntent);

//          COMPLETED (6) Get a reference to the NotificationManager
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

//          COMPLETED (7) Notify the user with the ID WEATHER_NOTIFICATION_ID
            /* WEATHER_NOTIFICATION_ID allows you to update or cancel the notification later on */
        notificationManager.notify(3001, notificationBuilder.build());
    }
}
