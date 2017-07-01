package com.example.home_.news;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.home_.news.data.NewsContract;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import cz.msebera.android.httpclient.Header;


public class  NewSpi {
    private final static String apiKey = "29a83320cbc446dda4dfc8b200dd077a";
    private final static String sources = "https://newsapi.org/v1/articles";
    private final static String artciles = "https://newsapi.org/v1/sources";

    private final static String SOURCE_PRAM = "source";
    private final static String API_KEY_PRAM = "apiKey";
    private final static String SORD_IT_BY_PRAM = "sortBy";
    private final static String top = "top";
    private final static String latest = "latest";
    private final static String popular = "popular";

    private final static String CATGORY_PRAM = "category";
    private final static String LANG_PRAM = "language";
    private final static String COUNTRY_PRAM = "country";
    private final static String STATUS = "status";
    private final static String SOURCE = "source";
    private final static String ARTICLES = "articles";
    private final static String AUTHOR = "author";
    private final static String DESCR = "description";
    private final static String TITLE = "title";
    private final static String URL_AR = "url";
    private final static String URL_TO_IMAGE = "urlToImage";
    private final static String PUBLISHED_AT = "publishedAt";
    private final static String SOURCES = "sources";
    private final static String SOURCES_ID = "id";
    private final static String SOURCES_NAME = "name";
    private final static String SOURCES_CATGORY = CATGORY_PRAM;
    private final static String SOURCES_LANG = LANG_PRAM;
    private final static String SOURCES_CONTRY = COUNTRY_PRAM;
    private final static String OK = "ok";
    private final static String ERROR = "error";
    private final static String CODE = "code";
    private final static String MESSAGE = "message";
    private final static String SORD_BYS_AVAILABLE = "sortBysAvailable";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static URL getSourceNews(String source)
    {
        Uri bulid = Uri.parse(sources).buildUpon().appendQueryParameter(SOURCE_PRAM,source)
                .appendQueryParameter(API_KEY_PRAM,apiKey)
                .build();
        try {
            return new URL(bulid.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getSourcesNames(String lan, String con, String catgory)
    {
        Uri.Builder bulid = Uri.parse(artciles).buildUpon();
        if (!lan.equals(""))
        bulid.appendQueryParameter(LANG_PRAM,lan);
        if (!con.equals(""))
               bulid.appendQueryParameter(COUNTRY_PRAM,con);
        if (!catgory.equals(""))
                bulid.appendQueryParameter(CATGORY_PRAM,catgory);
        try {
            return new URL(bulid.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL[] getMultiSources(String[] sources)
    {
        Log.d("Multi", "getMultiSources: ");
        URL[] urls = new URL[sources.length];
        int index = 0;
        for (String i:sources)
        {
            urls[index] = getSourceNews(sources[index]);
            index++;
        }
        return urls;
    }

    public static ContentValues[] readSourcesRespond(String data) throws JSONException {
        Log.d("Artclas", "readSourcesRespond: ");
        JSONObject reader = new JSONObject(data);


        String statue = reader.getString(STATUS);
        if (statue.equals(ERROR))
        {
            return null;
        }
        String source = reader.getString(SOURCE);
        JSONArray jsonArray = reader.getJSONArray(ARTICLES);
        ContentValues[] allInput = new ContentValues[jsonArray.length()];
        for (int s = 0;s<jsonArray.length();s++)
        {
           ContentValues contentValues = new ContentValues();
            JSONObject object = jsonArray.getJSONObject(s);
            contentValues.put(NewsContract.NewsArticles.Author, object.getString(AUTHOR));
            contentValues.put(NewsContract.NewsArticles.Source_Name, source);
            contentValues.put(NewsContract.NewsArticles.Title, object.getString(TITLE));
            contentValues.put(NewsContract.NewsArticles.Descrption, object.getString(DESCR));
            contentValues.put(NewsContract.NewsArticles.Url, object.getString(URL_AR));
            contentValues.put(NewsContract.NewsArticles.Image_Url, object.getString(URL_TO_IMAGE));
            contentValues.put(NewsContract.NewsArticles.Date, object.getString(PUBLISHED_AT));
            allInput[s] = contentValues;
        }

        return allInput;
    }

    public static ContentValues[] readSourcesNames(JSONObject reader) throws JSONException {
        Log.d("sources", "readSourcesNames: ");
        String statue = reader.getString(STATUS);
        if (statue.equals(ERROR))
        {
            return null;
        }

        JSONArray jsonArray = reader.getJSONArray(SOURCES);
        ContentValues[] allInput = new ContentValues[jsonArray.length()];
        for (int s = 0; s < jsonArray.length(); s++)
        {
            ContentValues contentValues = new ContentValues();
            JSONObject object = jsonArray.getJSONObject(s);

            contentValues.put(NewsContract.NewsSources.News_Sources_Id, object.getString(SOURCES_ID));
            contentValues.put(NewsContract.NewsSources.News_Sources_Name, object.getString(SOURCES_NAME));
            contentValues.put(NewsContract.NewsSources.Category, object.getString(SOURCES_CATGORY));
            contentValues.put(NewsContract.NewsSources.Lang, object.getString(SOURCES_LANG));
            contentValues.put(NewsContract.NewsSources.Contry, object.getString(SOURCES_CONTRY));
            contentValues.put(NewsContract.NewsSources.Url, object.getString(URL_AR));
            JSONArray array = object.getJSONArray(SORD_BYS_AVAILABLE);
            for (int a = 0; a < array.length(); a++) {
                if (array.getString(a).equals("top"))
                    contentValues.put(NewsContract.NewsSources.Top, Boolean.TRUE);
                else if (array.getString(a).equals("latest"))
                    contentValues.put(NewsContract.NewsSources.Latest, Boolean.TRUE);
                else if (array.getString(a).equals("popular"))
                    contentValues.put(NewsContract.NewsSources.Populer, Boolean.TRUE);
            }
            allInput[s]= contentValues;


        }

        return allInput;
    }

    public static void httpGet(Context c, String url) {
        final JSONObject[] jsonObject = {null};
        client.get(c, url, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


            }
        });


    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Log.d("sync", "getResponseFromHttpUrl: ");
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {

            urlConnection.disconnect();
        }
    }

    private JSONObject give(JSONObject j) {
        return j;
    }
}
