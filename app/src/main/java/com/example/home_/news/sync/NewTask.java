package com.example.home_.news.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.home_.news.NewSpi;
import com.example.home_.news.data.NewsContract;
import com.example.home_.news.data.NewsPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NewTask {
    synchronized public static void task(Context context) {
        Intent dataUpdatingdIntent = new Intent(NewsContract.updating);
        context.sendBroadcast(dataUpdatingdIntent);
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(NewsContract.sources, null, null, null, null, null);
        if (c == null || c.getCount() == 0)
            getSources(context);
        getArticals(context);

        Intent dataUpdatedIntent = new Intent(NewsContract.update);
        context.sendBroadcast(dataUpdatedIntent);
        NotifcationNews.showNotifecation(context);
        if (c != null)
            c.close();

    }

    synchronized private static void getSources(Context context) {

        URL uri = NewSpi.getSourcesNames("", "", "");
        try {
            String respond = NewSpi.getResponseFromHttpUrl(uri);

            ContentValues[] contentValues = NewSpi.readSourcesNames(new JSONObject(respond));
            if (contentValues != null && contentValues.length != 0) {
                int x = 0;
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.bulkInsert(NewsContract.sources, contentValues);
                if (NewsPreferencesUtils.getPreferredSources(context) == null || NewsPreferencesUtils.getPreferredSources(context).size() == 0) {
                    Cursor c = context.getContentResolver().query(NewsContract.sources, null, null, null, null, null);
                    if (c != null) {
                        String[] strings = new String[c.getCount()];
                        while (c.moveToNext()) {
                            strings[x] = c.getString(c.getColumnIndex(NewsContract.NewsSources.News_Sources_Id));
                            x++;
                        }
                        Set<String> qq = new HashSet<String>(Arrays.asList(strings));
                        NewsPreferencesUtils.setSources(context, qq);
                        c.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    synchronized private static void getArticals(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(NewsContract.sources, null, null, null, null, null);
        String[] sources;
        if (c == null || c.getCount() == 0) {
            assert c != null;
            c.close();
            getSources(context);
        } else {
            sources = new String[c.getCount()];
            if (c.moveToNext())
                for (int i = 0; i < c.getCount(); i++) {

                    sources[i] = c.getString(c.getColumnIndex(NewsContract.NewsSources.News_Sources_Id));
                    c.moveToNext();
                }

            URL[] uri = NewSpi.getMultiSources(sources);
            for (URL url : uri) {
                try {

                    String respond = NewSpi.getResponseFromHttpUrl(url);

                    ContentValues[] contentValues = NewSpi.readSourcesRespond(respond, context);
                    if (contentValues != null && contentValues.length != 0) {

                        contentResolver.bulkInsert(NewsContract.articles, contentValues);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException a) {
                    a.printStackTrace();
                }

            }
        }
    }

}
