package com.example.home_.news.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class NewDbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "new.db";


    private static final int DATABASE_VERSION = 1;

    public NewDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_NEWS_SOUCES_TABLE =

                "CREATE TABLE " + NewsContract.NewsSources.TABLE_NAME + " (" +


                        NewsContract.NewsSources._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        NewsContract.NewsSources.News_Sources_Name + " STRING NOT NULL, " +

                        NewsContract.NewsSources.News_Sources_Id + " STRING NOT NULL," +

                        NewsContract.NewsSources.Descrption + " STRING , " +
                        NewsContract.NewsSources.Url + " STRING NOT NULL, " +

                        NewsContract.NewsSources.Category + " STRING , " +
                        NewsContract.NewsSources.Contry + " STRING , " +
                        NewsContract.NewsSources.Lang + " STRING , " +

                        NewsContract.NewsSources.Top + " BOOLEAN DEFAULT false , " +
                        NewsContract.NewsSources.Latest + " BOOLEAN DEFAULT false , " +
                        NewsContract.NewsSources.Populer + " BOOLEAN DEFAULT false , " +


                        " UNIQUE (" + NewsContract.NewsSources.News_Sources_Id + ") ON CONFLICT REPLACE);";
        final String SQL_CREATE_NEWS_ARTICLES_TABLE =

                "CREATE TABLE " + NewsContract.NewsArticles.TABLE_NAME + " (" +


                        NewsContract.NewsArticles._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        NewsContract.NewsArticles.Title + " STRING NOT NULL , " +

                        NewsContract.NewsArticles.Image_Url + " STRING  ," +

                        NewsContract.NewsArticles.Descrption + " STRING  , " +
                        NewsContract.NewsArticles.Url + " STRING NOT NULL , " +

                        NewsContract.NewsArticles.Sorded_By + " STRING  , " +
                        NewsContract.NewsArticles.Date + " STRING  , " +

                        NewsContract.NewsArticles.Author + " STRING  , " +
                        NewsContract.NewsArticles.Source_Name + " STRING NOT NULL , " +
                        NewsContract.NewsArticles.Source_Readable_Name + " STRING NOT NULL , " +


                        " UNIQUE (" + NewsContract.NewsArticles.Url + ") ON CONFLICT IGNORE);";


        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_SOUCES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_ARTICLES_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsSources.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsArticles.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}