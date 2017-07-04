package com.example.home_.news.wedgit;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.home_.news.MainActivity;
import com.example.home_.news.R;
import com.example.home_.news.WebViewActivity;
import com.example.home_.news.data.NewsContract;

/**
 * Implementation of App Widget functionality.
 */
public class NewsWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (NewsContract.update.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            Log.d("update", "onReceive: ");
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_item_widget);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.news_widget);
        Intent g = new Intent(context, MainActivity.class);
        PendingIntent p = PendingIntent.getActivity(context, 0, g, 0);
        views.setOnClickPendingIntent(R.id.widget, p);
        Intent sec = new Intent(context, WebViewActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(sec)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.list_item_widget, pendingIntent);

        setRemoteAdapter(context, views);

        views.setEmptyView(R.id.list_item_widget, R.id.appwidget_error);
        appWidgetManager.updateAppWidget(appWidgetIds[0], views);
    }

    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.list_item_widget,
                new Intent(context, RemoteWidget.class));
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

