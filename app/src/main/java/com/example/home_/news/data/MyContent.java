package com.example.home_.news.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.home_.news.MainActivity;

/**
 * Created by Home- on 05/03/2017.
 */

public class MyContent extends ContentProvider {
    final int articales = 100;
    final int newSources = 200;
    final int revies = 300;
    final int video = 400;
    NewDbHelper dbHelper;
    UriMatcher matcher = createMatcher();
    SQLiteDatabase liteDatabase;
    private UriMatcher createMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NewsContract.Authority, NewsContract.NewsArticles.TABLE_NAME, articales);
        uriMatcher.addURI(NewsContract.Authority, NewsContract.NewsSources.TABLE_NAME, newSources);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new NewDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        int match = matcher.match(uri);
        Cursor c = null;
        switch (match) {
            case articales:
                if (sortOrder != null) {

                    Cursor ss = sqLiteDatabase.query(NewsContract.NewsSources.TABLE_NAME, new String[]{NewsContract.NewsSources.News_Sources_Name}, sortOrder + " =?", new String[]{"true"}, null, null, null);
                    String[] sources = new String[ss.getCount()];
                    if (ss.getCount() != 0) {
                        int i = 0;
                        while (ss.moveToNext()) {
                            sources[i] = ss.getString(ss.getColumnIndex(NewsContract.NewsSources.News_Sources_Id));
                            i++;
                        }
                        ss.close();
                        String select = NewsContract.NewsArticles.Source_Name + " IN " + MainActivity.getStringFromArray(sources) + " and ";

                        c = sqLiteDatabase.query(NewsContract.NewsArticles.TABLE_NAME, projection, select, selectionArgs, null, null, sortOrder);

                    }
                    c = sqLiteDatabase.query(NewsContract.NewsArticles.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                } else
                    c = sqLiteDatabase.query(NewsContract.NewsArticles.TABLE_NAME, projection, selection, selectionArgs, null, null, "strftime('%yyyy-%mm-%dd %HH:%MM', " + NewsContract.NewsArticles.Date + ")");
                break;
            case newSources:
                c = sqLiteDatabase.query(NewsContract.NewsSources.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                Log.d(c.getColumnCount() + "", "query: ");
                break;

            default:
                break;
        }


        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int macher = matcher.match(uri);
        long retur;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        switch (macher) {

            case articales:


                retur = sqLiteDatabase.insert(NewsContract.NewsArticles.TABLE_NAME, null, values);


                return ContentUris.withAppendedId(uri, retur);

            case newSources:

                retur = sqLiteDatabase.insert(NewsContract.NewsSources.TABLE_NAME, null, values);

                return ContentUris.withAppendedId(uri, retur);




            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int ma = matcher.match(uri);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long retur;
        switch (ma) {

            case articales:


                retur = sqLiteDatabase.update(NewsContract.NewsArticles.TABLE_NAME, values, selection, selectionArgs);

                if (retur > 0)
                    return 1;
                else throw new android.database.SQLException("error101");
            case newSources:

                retur = sqLiteDatabase.update(NewsContract.NewsSources.TABLE_NAME, values, selection, selectionArgs);
                if (retur > 0)
                    return 1;
                else throw new android.database.SQLException("error101");


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = matcher.match(uri);
        String table = "";
        switch (match) {
            case articales:
                table = NewsContract.NewsArticles.TABLE_NAME;
                break;
            case newSources:
                table = NewsContract.NewsSources.TABLE_NAME;
                break;

            default:
                table = "error";
        }
        if (table.equals("error"))
            return 0;
        sqLiteDatabase.beginTransaction();
        try {
            for (ContentValues i : values) {
                sqLiteDatabase.insert(table, null, i);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return super.bulkInsert(uri, values);
    }
}
