package com.example.home_.news.wedgit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.home_.news.R;
import com.example.home_.news.data.NewsContract;

/**
 * Created by Home- on 01/07/2017.
 */

public class RemoteWidget extends RemoteViewsService {
    Context context;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        context = getApplicationContext();
        final String title;
        return new RemoteViewsFactory() {
            private Cursor cursor = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (cursor != null)
                    cursor.close();
                final long identityToken = Binder.clearCallingIdentity();
                cursor = context.getContentResolver().query(NewsContract.articles, null, null, null, null);

                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return cursor != null ? cursor.getCount() : 0;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.news_widget_item);
                if (cursor.moveToPosition(position)) {
                    views.setTextViewText(R.id.widget_item, cursor.getString(cursor.getColumnIndex(NewsContract.NewsArticles.Title)));
                    Intent temple = new Intent();
                    temple.putExtra("url", cursor.getString(cursor.getColumnIndex(NewsContract.NewsArticles.Url)));
                    views.setOnClickFillInIntent(R.id.item_widget_id, temple);
                    return views;
                }
                return null;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.news_widget_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
