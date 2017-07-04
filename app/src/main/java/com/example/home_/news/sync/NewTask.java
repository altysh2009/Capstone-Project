package com.example.home_.news.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.home_.news.NewSpi;
import com.example.home_.news.data.NewsContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class NewTask {
    synchronized public static void task(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(NewsContract.sources, null, null, null, null, null);
        if (c == null || c.getCount() == 0)
            getSources(context);
        getArticals(context);

        Intent dataUpdatedIntent = new Intent(NewsContract.update);
        context.sendBroadcast(dataUpdatedIntent);
        if (c != null)
            c.close();

    }

    synchronized private static void getSources(Context context) {
        URL uri = NewSpi.getSourcesNames("", "", "");
        try {
            String respond = NewSpi.getResponseFromHttpUrl(uri);

            ContentValues[] contentValues = NewSpi.readSourcesNames(new JSONObject(respond));
            if (contentValues != null && contentValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.bulkInsert(NewsContract.sources, contentValues);
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

                    ContentValues[] contentValues = NewSpi.readSourcesRespond(respond);
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
