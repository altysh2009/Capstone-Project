package com.example.home_.news.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class NewsContract {
    static final String Authority = "com.example.home_.news.data";


    private static final Uri DATA_TABLE = Uri.parse("content://" + Authority);
    public static final Uri sources = DATA_TABLE.buildUpon().appendPath(NewsSources.TABLE_NAME).build();
    public static final Uri articles = DATA_TABLE.buildUpon().appendPath(NewsArticles.TABLE_NAME).build();
    public static final String update = DATA_TABLE.buildUpon().appendPath("update").build().toString();



    public static final class NewsSources implements BaseColumns {
        public static final String TABLE_NAME = "news_sources";
        public static final String News_Sources_Id = "news_Sources_id";
        public static final String News_Sources_Name = "news_Sources_name";
        public static final String Descrption = "descrption";
        public static final String Url = "url";
        public static final String Category = "category";
        public static final String Contry = "contry";
        public static final String Top = "top";
        public static final String Latest = "latest";
        public static final String Populer = "pupuler";
        public static final String Lang = "lang";




    }
    public static final class NewsArticles implements BaseColumns {
        public static final String TABLE_NAME = "news_articles";
        public static final String Author = "author";
        public static final String Title = "title";
        public static final String Descrption = "descrption";
        public static final String Url = "url";
        public static final String Image_Url = "image";
        public static final String Date = "date";

        public static final String Sorded_By = "sorded_by";
        public static final String Source_Name = "source_name";

    }
}