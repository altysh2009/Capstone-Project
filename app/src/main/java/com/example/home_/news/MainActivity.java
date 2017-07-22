package com.example.home_.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecyceleAdpterMain.ReciveClick {
    public static final int ITEMS_PER_AD = 5;
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;
    // The Native Express ad unit ID.
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";
    private static Bundle mBundleRecyclerViewState;
    final int maxSize = 80;
    private final String LIST_STATE_KEY = "listPostion";
    private final String LIST_DATA_KEY = "List Data";
    private final String SEARCH_KEY_WORD = "keyword";
    int listIndex = 0;
    String TAG = "MainActivity";
    InterstitialAd mInterstitialAd;
    InterstitialAd mInterstitialAd2;
    RecyceleAdpterMain mAdapter;
    RecyclerView mNewsList;
    TextView error;
    Context context;
    ProgressBar searchPrgress;
    ProgressBar loading;
    List<Object> newData;
    ImageButton cancelBottom;
    List<Object> limitData;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    int pos = 0;
    private Boolean search_state = false;
    private Boolean loadingState = false;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(NewsContract.update)) {
                Log.d("update", "onReceive: ");
                loadingState = false;
                //loading.setVisibility(View.GONE);
                getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
            }
//            else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
//                Log.d("internertcheck", "onReceive: ");
            else if (s.equals("android.intent.action.MANAGE_NETWORK_USAGE"))
                Log.d("android.intent", "onReceive: ");
            else if (s.equals("android.intent.action.EXTRA_NETWORK_TYPE"))
                Log.d("EXTRA_NETWORK_TYPE", "onReceive: ");
            else if (s.equals("android.net.conn.CONNECTIVITY_CHANGE"))
                Log.d("CONNECTIVITY_CHANGE", "onReceive: ");
            else if (s.equals(NewsContract.updating)) {
                loadingState = true;
                // loading.setVisibility(View.VISIBLE);
                Log.d(TAG, "onReceive: ");
            }

        }
    };
    private EditText search;

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

    public static String getStringFromArray(String[] data) {
        String[] arraySet;
        String string = "";
        if (data != null) {
            arraySet = data;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.ttoolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        search = (EditText) findViewById(R.id.search_main);
        searchPrgress = (ProgressBar) findViewById(R.id.progressBar_search);
        cancelBottom = (ImageButton) findViewById(R.id.cancel_serach_bottom);
        error = (TextView) findViewById(R.id.error_text);
        error.setVisibility(View.INVISIBLE);
        NewsJopInitialize.initialize(getApplicationContext());
        mNewsList = (RecyclerView) findViewById(R.id.recycle_view_main);
        loading = (ProgressBar) findViewById(R.id.loading);
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial();
        mInterstitialAd2 = new InterstitialAd(context);
        mInterstitialAd2.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial2();
        mNewsList.setLayoutManager(layoutManager);
        mNewsList.setHasFixedSize(true);
        mAdapter = new RecyceleAdpterMain(this);
        mNewsList.setAdapter(mAdapter);

        mNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean endend = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.d(TAG, "onScrollStateChanged: ");
                endend = newState == RecyclerView.SCROLL_STATE_IDLE;
                if (endend)
                    onScrolled(recyclerView, 0, 0);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                //Log.d(TAG+" "+visibleItemCount+" "+pastVisiblesItems, " onScrolled: ");
                if (visibleItemCount + pastVisiblesItems >= totalItemCount && endend) {
                    setMore();

                } else if (pastVisiblesItems == 0) {

                    // if(listIndex>20)
                    if (endend) {
                        //Log.d(TAG+" "+ listIndex, "onScrolled: ");
                        loadFrist();
                    }

                }

            }
        });
        cancelBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                getSupportLoaderManager().destroyLoader(1);
                getSupportLoaderManager().initLoader(1, null, MainActivity.this);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    serachData(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 2) {
                    cancelBottom.setVisibility(View.GONE);
                    getSupportLoaderManager().destroyLoader(1);
                    getSupportLoaderManager().initLoader(1, null, MainActivity.this);
                }
            }
        });

        createAdd();
        // requestNewInterstitial();
        Log.d(TAG, "onCreate: ");
        getSupportLoaderManager().initLoader(1, null, this);
        //Log.d(TAG, "onCreate: ");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsJopInitialize.startImmediateSync(context);
            }
        });

    }

    public void loadFrist() {
        Log.d(TAG + " " + limitData.size(), "loadFrist: ");
        if (listIndex >= maxSize + 20) {
            Log.d(TAG + " " + listIndex + " " + maxSize + 20, " loadFrist: ");
            for (int x = listIndex - maxSize; x >= listIndex - maxSize - 20; x--) {
                Log.d(TAG + " " + x, " loadFrist: ");
                limitData.add(0, newData.get(x));
            }
            for (int i = 0; i < 20; i++)
                limitData.remove(limitData.size() - 1);
            mNewsList.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setdata(limitData);
                }

            });
            listIndex -= 20;
        }

    }

    public void setMore() {
        int listIneex = listIndex;
        Log.d(TAG + " " + limitData.size(), "setMore: ");
        if (listIneex >= maxSize) {
            Log.d(TAG + "ss", "setMore: ");
            for (int i = 0; i < 20; i++)
                limitData.remove(0);


            listIneex = 80;
        }

        if (listIneex != 0)
            if (listIndex < newData.size()) {
                int jump = 20;
                if (newData.size() < listIndex + jump)
                    jump = newData.size() - listIneex + 1;
                for (int i = listIndex; i < listIndex + jump; i++)
                    limitData.add(newData.get(i));
                listIndex += jump;
                Log.d(TAG + " " + listIndex + " " + listIneex, " setMore: ");
                mNewsList.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setdata(limitData);
                    }
                });

            }
    }

    private void serachData(String key) {
        Bundle b = new Bundle();
        b.putString(SEARCH_KEY_WORD, key.toString());
        searchPrgress.setVisibility(View.VISIBLE);
        search_state = true;
        getSupportLoaderManager().destroyLoader(1);
        getSupportLoaderManager().initLoader(1, b, MainActivity.this);
    }

    private void setUpAndLoadNativeExpressAds() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
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
                        //final LinearLayout cardView = (LinearLayout) findViewById(R.id.lll);
                        final int adWidth = width - 16
                                - 16;
                        AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
                        if (adView.getAdSize() == null) {
                            adView.setAdSize(adSize);
                            adView.setAdUnitId(AD_UNIT_ID);
                        }
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
        if (mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        else {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtra("url", thunb);
            startActivity(intent);
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            requestNewInterstitial();
        }



        mInterstitialAd.setAdListener(new AdListener() {


            @Override
            public void onAdClosed() {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", thunb);
                startActivity(intent);
                mInterstitialAd = new InterstitialAd(context);
                mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                requestNewInterstitial();

            }
        });

    }

    void loadAdd(final String thunb, int id) {
        if (mInterstitialAd2.isLoaded())
            mInterstitialAd2.show();
        else {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            intent.putExtra("url", thunb);
            startActivity(intent);
            mInterstitialAd2 = new InterstitialAd(context);
            mInterstitialAd2.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            requestNewInterstitial2();
        }


        mInterstitialAd2.setAdListener(new AdListener() {


            @Override
            public void onAdClosed() {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", thunb);
                mInterstitialAd2 = new InterstitialAd(context);
                mInterstitialAd2.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                requestNewInterstitial2();
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

    private void requestNewInterstitial2() {

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAd2.loadAd(adRequest);

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

                // Log.d(index + ITEMS_PER_AD + " ", "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list." + errorCode);

                loadNativeExpressAd(index + ITEMS_PER_AD);

            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());
    }





    @Override
    protected void onRestart() {
        super.onRestart();

        // getSupportLoaderManager().restartLoader(1, null, this);
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
        loading.setVisibility(View.VISIBLE);


        String source = NewsContract.NewsArticles.Source_Name + " IN " + getStringFromSet(sources);
        if (args == null) {


            if (sources == null)
                x = new CursorLoader(getApplicationContext(), NewsContract.articles, null, null, null, null);
            else
                x = new CursorLoader(getApplicationContext(), NewsContract.articles, null, source, null, null);


            return x;
        } else {
            String word = args.getString(SEARCH_KEY_WORD);


            if (sources == null)
                x = new CursorLoader(getApplicationContext(), NewsContract.articles, null, word + " IN " + NewsContract.NewsArticles.Source_Readable_Name + " GLOB " + "( \'" + word + "*" + "\' )"
                        + " OR " + NewsContract.NewsArticles.Title + " like   " + "( \'" + "%" + word + "%" + "\' )", null, null);

            else
                x = new CursorLoader(getApplicationContext(), NewsContract.articles, null, source + " AND " + NewsContract.NewsArticles.Source_Readable_Name + " GLOB " + "( \'" + word + "*" + "\' )"
                        + " OR " + NewsContract.NewsArticles.Title + " like   " + "( \'" + "%" + word + "%" + "\' )", null, null);

            return x;

        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (searchPrgress.getVisibility() == View.VISIBLE) {
            searchPrgress.setVisibility(View.GONE);
            cancelBottom.setVisibility(View.VISIBLE);

        } else {
            searchPrgress.setVisibility(View.GONE);
            cancelBottom.setVisibility(View.GONE);
        }

        if (data.getCount() == 0) {
            if (search_state) {
                error.setText(R.string.news_search_empty);
                search_state = false;
            }
            Log.d("data is empty", "onLoadFinished: ");
            if (!loadingState)
                error.setVisibility(View.VISIBLE);
        } else {
            if (!loadingState)
                error.setVisibility(View.INVISIBLE);
            // newData = setAddsToList(setDataToList(data));
            newData = setDataToList(data);
            limitData = new ArrayList<Object>();
            Set<String> ind = NewsPreferencesUtils.getPos(context);

            try {

                if (ind == null) {

                    marge();
                } else {

                    String[] ii = ind.toArray(new String[]{});
                    //Log.d(TAG + ii[0], "onLoadFinished: ");

                    listIndex = Integer.parseInt(ii[0]);

                    pos = Integer.parseInt(ii[1]);
                    if (listIndex > 20)
                    //Log.d(TAG + listIndex + " " + pos, " onLoadFinished: ");
                    {
                        if (listIndex > 80) {
                            for (int i = listIndex - 81; i < listIndex; i++)
                                limitData.add(newData.get(i));
                            //Log.d(TAG + " " + limitData.size(), "onLoadFinished: ");
                            mAdapter.setdata(limitData);
                        } else {
                            for (int i = 0; i < listIndex; i++)
                                limitData.add(newData.get(i));
                            //Log.d(TAG + " " + limitData.size(), "onLoadFinished: ");
                            mAdapter.setdata(limitData);
                        }
                    } else {
                        marge();
                    }
                }
            } catch (Exception e) {

                marge();
            }

            // setUpAndLoadNativeExpressAds();

        }
        Log.d(TAG + " " + pos, " onLoadFinished: ");
        if (pos > 0)
            layoutManager.scrollToPosition(pos);
        if (!loadingState)
            loading.setVisibility(View.GONE);


    }

    public void marge() {
        for (int i = 0; i < 20; i++)
            limitData.add(newData.get(i));
        listIndex = 20;
        mAdapter.setdata(limitData);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
        mAdapter.setdata(null);

        loader.reset();
    }

    @Override
    public void theClickedItem(String thumb) {
        String[] inde = new String[]{listIndex + "", layoutManager.findFirstVisibleItemPosition() + ""};
        NewsPreferencesUtils.setPos(context, new HashSet<String>(Arrays.asList(inde)));
        loadAdd(thumb);
    }

    @Override
    public void theClickedItem(String thumb, int id) {
        if (thumb.contains("http")) {
            String[] inde = new String[]{listIndex + "", layoutManager.findFirstVisibleItemPosition() + ""};
            NewsPreferencesUtils.setPos(context, new HashSet<String>(Arrays.asList(inde)));
            loadAdd(thumb, id);

        }
    }

    public List<Object> setAddsToList(List<Object> data) {

        for (int i = ITEMS_PER_AD; i <= data.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            data.add(i, adView);
            //Log.d(i + " ", "onAdLoaded: ");
        }
        return data;
    }

    public List<Object> setDataToList(Cursor c) {
        List<Object> data = new ArrayList<Object>();


        while (c.moveToNext()) {
            String auther_Name = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Author));

            String description = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Descrption));
            String sourceName = c.getString(c.getColumnIndex(NewsContract.NewsArticles.Source_Readable_Name));
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
    protected void onResume() {
        super.onResume();
        IntentFilter i = new IntentFilter();
        i.addAction(NewsContract.update);
        i.addAction(NewsContract.updating);
        i.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        i.addAction("android.intent.action.MANAGE_NETWORK_USAGE");
        i.addAction("android.intent.action.EXTRA_NETWORK_TYPE");
        i.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, i);
//        String s = search.getText().toString();
//        if (s.length() >2)
//            serachData(s);
//        else
//        getSupportLoaderManager().restartLoader(1,null,this);
//        if (mBundleRecyclerViewState != null) {
//            Parcelable listState = mBundleRecyclerViewState.getParcelable(LIST_STATE_KEY);
//            mNewsList.getLayoutManager().onRestoreInstanceState(listState);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
//        mBundleRecyclerViewState = new Bundle();
//        Parcelable listState = mNewsList.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        Parcelable listState = mNewsList.getLayoutManager().onSaveInstanceState();
//        mNewsList.getLayoutManager().
//        // putting recyclerview position
//        outState.putParcelable(LIST_STATE_KEY, listState);
//        // putting recyclerview items
        int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        if (pos == -1)
            pos = layoutManager.findFirstVisibleItemPosition();


        outState.putInt(LIST_STATE_KEY, pos);
//        Log.d(layoutManager.findFirstVisibleItemPosition()+TAG, "onSaveInstanceState: ");

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //       Log.d(savedInstanceState.getInt(LIST_STATE_KEY)+TAG, "onRestoreInstanceState: ");
        pos = savedInstanceState.getInt(LIST_STATE_KEY);
        //mNewsList.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(LIST_STATE_KEY));
    }
}
