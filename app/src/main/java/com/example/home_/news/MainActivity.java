package com.example.home_.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.home_.news.data.NewsContract;
import com.example.home_.news.data.NewsPreferencesUtils;
import com.example.home_.news.sync.NewsJopInitialize;
import com.example.home_.news.sync.RecyceleAdpterMain;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecyceleAdpterMain.ReciveClick {
    public static final int ITEMS_PER_AD = 5;
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;
    // The Native Express ad unit ID.
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NewsContract.update))
                getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
        }
    };
    String TAG = "MainActivity";
    InterstitialAd mInterstitialAd;
    RecyceleAdpterMain mAdapter;
    RecyclerView mNewsList;
    TextView error;
    Context context;
    List<Object> newData;

    public static String getStringFromSet(Set<String> data) {
        String[] arraySet;
        String string = "";
        if (data != null) {
            arraySet = data.toArray(new String[]{});
            string = Arrays.toString(arraySet).replace("[", "( \'").replace("]", "\' )").replace(",", "\',\'").replace(" ", "");
            return string;
        }
        return string;
    }

    //AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
       /* mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-0243484158988577/9196272994");
         adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(View.INVISIBLE);
        NewsJopInitialize.initialize(getApplicationContext());
        mNewsList = (RecyclerView) findViewById(R.id.recycle_view_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNewsList.setLayoutManager(layoutManager);
        mNewsList.setHasFixedSize(true);
        mAdapter = new RecyceleAdpterMain(this);
        mNewsList.setAdapter(mAdapter);

        createAdd();
        // requestNewInterstitial();

        getSupportLoaderManager().initLoader(1, null, this);
        Log.d(TAG, "onCreate: ");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = NewSpi.getSourcesNames("", "", "").toString();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        if (newData != null) {
            mNewsList.post(new Runnable() {
                @Override
                public void run() {
                    final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                    int w = MainActivity.this.getResources().getDisplayMetrics().widthPixels;
                    Log.d(scale + " " + w + " ", "run: ");
                    // Set the ad size and ad unit ID for each Native Express ad in the items list.
                    for (int i = ITEMS_PER_AD; i <= newData.size(); i += ITEMS_PER_AD) {
                        final NativeExpressAdView adView =
                                (NativeExpressAdView) newData.get(i);
                        final LinearLayout cardView = (LinearLayout) findViewById(R.id.lll);
                        final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                                - cardView.getPaddingRight();
                        AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
                        adView.setAdSize(adSize);
                        adView.setAdUnitId(AD_UNIT_ID);
                    }

                    // Load the first Native Express ad in the items list.
                    loadNativeExpressAd(ITEMS_PER_AD);
                }
            });
        }
    }

    private void createAdd() {
        AdView mAdView = (AdView) findViewById(R.id.ad_view);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.VISIBLE);
    }

    void loadAdd(final String thunb) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial();


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", thunb);
                startActivity(intent);
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", thunb);
                startActivity(intent);


            }
        });

    }

    private void loadNativeExpressAd(final int index) {

        if (index >= newData.size()) {
            return;
        }

        Object item = newData.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
                Log.d(index + ITEMS_PER_AD + " ", "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());
    }

    void loadAdd(final String thunb, int id) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial();


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {

                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", thunb);
                startActivity(intent);
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", thunb);
                startActivity(intent);


            }
        });
    }

    private void requestNewInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd.loadAd(adRequest);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSupportLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), NewsPrefranceActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Set<String> sources = NewsPreferencesUtils.getPreferredSources(context);
        CursorLoader x;

        Set<String> catgorys = NewsPreferencesUtils.getPreferredCatgory(context);
        Set<String> Lan = NewsPreferencesUtils.getPreferredLang(context);
        String source = NewsContract.NewsArticles.Source_Name + " IN " + getStringFromSet(sources);


        if (sources == null)
            x = new CursorLoader(getApplicationContext(), NewsContract.articles, null, null, null, null);
        else
            x = new CursorLoader(getApplicationContext(), NewsContract.articles, null, source, null, null);


        return x;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.getCount() == 0) {
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.INVISIBLE);
            newData = setAddsToList(setDataToList(data));
            mAdapter.setdata(newData);
            setUpAndLoadNativeExpressAds();
        }


    }

    public List<Object> setAddsToList(List<Object> data) {

        for (int i = ITEMS_PER_AD; i <= data.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            data.add(i, adView);
            Log.d(i + " ", "onAdLoaded: ");
        }
        return data;
    }

    public List<Object> setDataToList(Cursor c) {
        List<Object> data = new ArrayList<Object>();

        while (c.moveToNext()) {
            String auther_Name = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Author));

            String description = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Descrption));
            String sourceName = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Source_Name));
            String title = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Title));
            String image = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Image_Url));
            String url = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Url));
            String date = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Date));
            MyObject m = new MyObject(auther_Name, date, description, image, sourceName, title, url);
            data.add(m);
        }
        return data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setdata(null);
        loader.reset();
    }

    @Override
    public void theClickedItem(String thumb) {
        loadAdd(thumb);

    }

    @Override
    public void theClickedItem(String thumb, int id) {
        if (thumb.contains("http")) {
            loadAdd(thumb, id);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(NewsContract.update));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
