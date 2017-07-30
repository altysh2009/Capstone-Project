package com.example.home_.news;

import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home_.news.data.NewsContract;
import com.example.home_.news.data.NewsPreferencesUtils;
import com.example.home_.news.sync.NewsJopInitialize;
import com.example.home_.news.sync.RecyceleAdpterMain;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.plus.PlusShare;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

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

    final int PLAY_SERVICES_RESOLUTION_REQUEST = 8000;
    final int maxSize = 50;
    private final String LIST_STATE_KEY = "listPostion";

    private final String SEARCH_KEY_WORD = "keyword";
    int listIndex = 0;
    String TAG = "MainActivity";
    InterstitialAd mInterstitialAd;
    InterstitialAd mInterstitialAd2;
    RecyceleAdpterMain mAdapter;
    RecyclerView mNewsList;
    TextView error;
    Context context;
    int[] in;
    ProgressBar searchPrgress;
    ProgressBar loading;
    List<Object> newData;
    ImageButton cancelBottom;
    List<Object> limitData;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    int pos = 0;
    ArrayList<String> addIndex = new ArrayList<>();
    PackageInfo info;
    ObjectAnimator fadeIn;
    private FloatingActionButton fab;
    private Boolean search_state = false;
    private Boolean loadingState = false;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            switch (s) {
                case NewsContract.update:
                    loadingState = false;
                    getSupportLoaderManager().restartLoader(1, null, MainActivity.this);
                    break;
                case "android.intent.action.MANAGE_NETWORK_USAGE":
                    Log.d("android.intent", "onReceive: ");
                    break;
                case "android.intent.action.EXTRA_NETWORK_TYPE":
                    Log.d("EXTRA_NETWORK_TYPE", "onReceive: ");
                    break;
                case "android.net.conn.CONNECTIVITY_CHANGE":
                    Log.d("CONNECTIVITY_CHANGE", "onReceive: ");
                    break;
                case NewsContract.updating:
                    loadingState = true;
                    loading.setVisibility(View.VISIBLE);
                    error.setVisibility(View.INVISIBLE);

                    break;
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
        /*PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.home_.news", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/
        context = getApplicationContext();
        checkPlayServices();


        Twitter.initialize(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.ttoolbar);
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
        mAdapter.setHasStableIds(true);
        mNewsList.setAdapter(mAdapter);

        mNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean endend = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.d(TAG, "onScrollStateChanged: ");

                mAdapter.setScrolling(false);
                endend = newState == RecyclerView.SCROLL_STATE_IDLE;
                if (endend) {
                    in = getlistin();


                    onScrolled(recyclerView, -1, -1);
                }

            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.d(TAG, "onScrolled: ");
                if (in != null && in.length > 0 && dx == -1 && dy == -1) {
                    int visibleItemCount = in[0];
                    int pastVisiblesItems = in[2];
                    int totalItemCount = in[1];
                    if (pastVisiblesItems > 0)
                        fab.setVisibility(View.VISIBLE);
                    else if (listIndex >= 20 && listIndex < 40 && pastVisiblesItems == 0 && visibleItemCount < 2)
                        fab.setVisibility(View.GONE);
                    //Log.d(TAG + " " + visibleItemCount + " " + pastVisiblesItems + " " + totalItemCount, " onScrolled: ");
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
            }

        });
        fadeIn = ObjectAnimator.ofFloat(mNewsList, "alpha", .3f, 1f);
        fadeIn.setDuration(2000);

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
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limitData = new ArrayList<>();
                marge();
                layoutManager.scrollToPosition(0);
                NewsPreferencesUtils.setPos(context, new HashSet<>(Arrays.asList(new String[]{"20", "0"})));

            }
        });

        createAdd();
        // requestNewInterstitial();
        // Log.d(TAG, "onCreate: ");
        getSupportLoaderManager().initLoader(1, null, this);
        //Log.d(TAG, "onCreate: ");


    }


    public void loadFrist() {
        //Log.d(TAG + " " + limitData.size(), "loadFrist: ");
        int jump = 20;
        if (listIndex > maxSize && listIndex < maxSize + jump)
            jump = listIndex - maxSize;
        if (listIndex > maxSize) {

            //Log.d(TAG + " " + listIndex + " " + maxSize + 20, " loadFrist: ");
            for (int x = listIndex - maxSize; x >= listIndex - maxSize - jump; x--) {
                //      Log.d(TAG + " " + x, " loadFrist: ");
                limitData.add(0, newData.get(x));
            }
            limitData = setAddsToList(limitData, 0, jump);
            if (limitData.size() >= maxSize + jump) {
                int delet = limitData.size() - maxSize + jump / 2;
                //Log.d(TAG + " " + delet, "getAddsIndex: ");

                for (int i = 0; i < jump + delet; i++)
                    limitData.remove(limitData.size() - 1);
            }
            addIndex = getAddsIndex(limitData);
            final int finalJump = jump;
            mNewsList.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setdata(limitData, addIndex);
                    layoutManager.scrollToPosition(finalJump);
                    setUpAndLoadNativeExpressAds(ITEMS_PER_AD, finalJump / ITEMS_PER_AD);
                }

            });
            listIndex -= jump;
        }

    }

    public void setMore() {
        int listIneex = listIndex;
        if (listIneex >= newData.size())
            return;
        // Log.d(TAG + " " + limitData.size(), "setMore: ");


        if (listIneex != 0)
            if (listIndex < newData.size()) {
                int jump = 20;
                if (newData.size() < listIndex + jump)
                    jump = newData.size() - listIneex;
                for (int i = listIndex; i < listIndex + jump; i++)
                    limitData.add(newData.get(i));
                final int start = addIndex.size();
                limitData = setAddsToList(limitData, limitData.size() - jump + ITEMS_PER_AD);

                if (limitData.size() >= maxSize + jump) {
                    int delet = limitData.size() - maxSize + jump / 2;
                    // Log.d(TAG + " " + delet, "getAddsIndex: " + newData.size() + " " + limitData.size());

                    for (int i = 0; i < jump + delet; i++)
                        limitData.remove(0);


                }
                addIndex = getAddsIndex(limitData);
                listIndex += jump;

                //Log.d(TAG + " " + listIndex + " " + listIneex, " setMore: ");
                final int finalJump = jump;
                mNewsList.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setdata(limitData, addIndex);
                        int pos = limitData.size() - finalJump;

                        //  Log.d(TAG, "run2: "+pos+ " "+finalJump+" "+limitData.size());
                        layoutManager.scrollToPosition(pos);
                        setUpAndLoadNativeExpressAds(start);

                    }
                });

            }
    }

    private void serachData(String key) {
        Bundle b = new Bundle();
        b.putString(SEARCH_KEY_WORD, key);
        searchPrgress.setVisibility(View.VISIBLE);
        search_state = true;
        getSupportLoaderManager().destroyLoader(1);
        getSupportLoaderManager().initLoader(1, b, MainActivity.this);
    }

    private void setUpAndLoadNativeExpressAds() {

        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        if (limitData != null) {
            mNewsList.post(new Runnable() {
                @Override
                public void run() {


                    final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                    //int w = MainActivity.this.getResources().getDisplayMetrics().widthPixels;
                    AppBarLayout c = (AppBarLayout) findViewById(R.id.width);
                    final int adWidth = c.getWidth();
                    Log.d(TAG, "run: " + adWidth);
                    // Log.d(scale + " " + w + " ", "run: ");
                    // Set the ad size and ad unit ID for each Native Express ad in the items list.
                    for (String ind : addIndex) {
                        MyAdd m = (MyAdd) limitData.get(Integer.parseInt(ind));
                        if (!m.isReady()) {

                        final NativeExpressAdView adView =
                                m.getAdView();

                            adView.setVideoOptions(new VideoOptions.Builder()
                                    .setStartMuted(true)
                                    .build());
                            //mVideoController = adView.getVideoController();
                        //final LinearLayout cardView = (LinearLayout) findViewById(R.id.lll);

                        AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);

                            adView.setAdSize(adSize);
                            adView.setAdUnitId(AD_UNIT_ID);
                            m.setReady(true);

                    }
                    }
                    // Load the first Native Express ad in the items list.

                    loadNativeExpressAd(0);

                }
            });
        }
    }

    private void setUpAndLoadNativeExpressAds(final int index) {
        if (index - 1 < 0)
            return;

        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        if (limitData != null) {
            mNewsList.post(new Runnable() {
                @Override
                public void run() {
                    final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                    //int w = MainActivity.this.getResources().getDisplayMetrics().widthPixels;
                    AppBarLayout c = (AppBarLayout) findViewById(R.id.width);
                    final int adWidth = c.getWidth() - c.getPaddingLeft()
                            - c.getPaddingRight();
                    // Log.d(scale + " " + w + " "+" "+width, "run: ");
                    // Set the ad size and ad unit ID for each Native Express ad in the items list.
                    for (int i = index - 1; i < addIndex.size(); i++) {
                        String ind = addIndex.get(i);
                        MyAdd m = (MyAdd) limitData.get(Integer.parseInt(ind));
                        if (!m.isReady()) {
                            final NativeExpressAdView adView = m.getAdView();
                            //final LinearLayout cardView = (LinearLayout) findViewById(R.id.lll);

                            AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);

                            adView.setAdSize(adSize);

                            adView.setAdUnitId(AD_UNIT_ID);
                            m.setReady(true);

                        }
                    }


                    // Load the first Native Express ad in the items list.

                    loadNativeExpressAd(index);

                }
            });
        }
    }

    private void setUpAndLoadNativeExpressAds(final int startIndex, final int finshIndex) {


        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        if (limitData != null) {
            mNewsList.post(new Runnable() {
                @Override
                public void run() {
                    final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                    // int w = MainActivity.this.getResources().getDisplayMetrics().widthPixels;
                    // Log.d(scale + " " + w + " ", "run: ");
                    // Set the ad size and ad unit ID for each Native Express ad in the items list.
//                    if(finshIndex >addIndex.size())
                    AppBarLayout c = (AppBarLayout) findViewById(R.id.width);
                    final int adWidth = c.getWidth() - c.getPaddingLeft()
                            - c.getPaddingRight();
//
                    for (int i = startIndex; i < finshIndex; i++) {
                        String ind = addIndex.get(i);
                        MyAdd m = (MyAdd) limitData.get(Integer.parseInt(ind));
                        if (m.isReady()) {
                            final NativeExpressAdView adView = m.getAdView();
                            //final LinearLayout cardView = (LinearLayout) findViewById(R.id.lll);


                            AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);

                            adView.setAdSize(adSize);
                            adView.setAdUnitId(AD_UNIT_ID);
                            m.setReady(true);
                        }
                    }


                    // Load the first Native Express ad in the items list.

                    loadNativeExpressAd(startIndex, finshIndex);

                }
            });
        }
    }

    private void loadNativeExpressAd(final int indexx) {
        if (indexx >= addIndex.size())
            return;
        int index = Integer.parseInt(addIndex.get(indexx));

        if (index >= limitData.size()) {
            return;
        }

        // Log.d(TAG+ " "+ index, "loadNativeExpressAd: ");


        Object item = limitData.get(index);

        if (!(item instanceof MyAdd)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }
        MyAdd m = (MyAdd) item;
        if (m.isReady() && !m.isLoaded()) {

            final NativeExpressAdView adView = m.getAdView();

            // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
            // to finish loading before loading the next ad in the items list.
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    // The previous Native Express ad loaded successfully, call this method again to
                    // load the next ad in the items list.

                    loadNativeExpressAd(indexx + 1);


                    // Log.d(index + ITEMS_PER_AD + " ", "onAdLoaded: ");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // The previous Native Express ad failed to load. Call this method again to load
                    // the next ad in the items list.
                    Log.e("ads", "The previous Native Express ad failed to load. Attempting to"
                            + " load the next Native Express ad in the items list." + errorCode);

                    loadNativeExpressAd(indexx + 1);
                    adView.setVisibility(View.GONE);

                }
            });

            // Load the Native Express ad.
            adView.loadAd(new AdRequest.Builder().addTestDevice("ca-app-pub-3940256099942544/6300978111").addTestDevice("6EC15F3EEB74C3D7EBD0EF69AD20CB5A").addTestDevice("5FEBBFA12C1FD5271E42652F4EF7BD25").build());
            m.setLoaded(true);
        }
    }

    private void loadNativeExpressAd(final int startIndex, final int finshIndex) {
        if (startIndex >= finshIndex)
            return;

        Object item = limitData.get(startIndex);
        if (!(item instanceof MyAdd)) {
            throw new ClassCastException("Expected item at index " + startIndex + " to be a Native"
                    + " Express ad.");
        }
        MyAdd m = (MyAdd) item;
        if (m.isReady() && !m.isLoaded()) {
            final NativeExpressAdView adView = m.getAdView();

            // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
            // to finish loading before loading the next ad in the items list.
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    // The previous Native Express ad loaded successfully, call this method again to
                    // load the next ad in the items list.

                    loadNativeExpressAd(startIndex + 1, finshIndex);

                    // Log.d(index + ITEMS_PER_AD + " ", "onAdLoaded: ");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // The previous Native Express ad failed to load. Call this method again to load
                    // the next ad in the items list.
                    Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                            + " load the next Native Express ad in the items list." + errorCode);

                    loadNativeExpressAd(startIndex + 1, finshIndex);

                }
            });

            // Load the Native Express ad.
            adView.loadAd(new AdRequest.Builder().addTestDevice("ca-app-pub-3940256099942544/6300978111")
                    .addTestDevice("6EC15F3EEB74C3D7EBD0EF69AD20CB5A")
                    .addTestDevice("5FEBBFA12C1FD5271E42652F4EF7BD25").build());
        }
        m.setLoaded(true);
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

    @Override
    public void share(String thumb, int type) {
        Log.d(TAG, "share: ");
        switch (type) {
            case R.id.share_twitter:
                Log.d(TAG, "share: twitter");
                shareTwitter(thumb);
                break;
            case R.id.share_gmail:


                break;
            case R.id.share_face:
                Log.d(TAG, "share: face");
                shareFacebook(thumb);
                break;
        }
    }

    @Override
    public void share(String thumb, int type, String title) {
        if (type == R.id.share_gmail) {
            shareGoogle(thumb, title);
        }
    }

    public void shareTwitter(String url) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text(url);

        builder.show();
    }

    public void shareFacebook(String url) {


        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(url))
                .build();
        ShareDialog shareDialog = new ShareDialog(MainActivity.this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);


    }

    public void shareGoogle(String url, String string) {
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(string)
                .setContentUrl(Uri.parse(url))
                .getIntent();
        try {
            startActivityForResult(shareIntent, 0);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, getString(R.string.toast_gmail), Toast.LENGTH_SHORT).show();
        }

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

    private int[] getlistin() {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
        return new int[]{visibleItemCount, totalItemCount, pastVisiblesItems};
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
            } else error.setText(R.string.empty_recycle);
            // Log.d("data is empty", "onLoadFinished: ");
            if (!loadingState)
                error.setVisibility(View.VISIBLE);
            if (loadingState) {
                error.setVisibility(View.INVISIBLE);
                loading.setProgress(View.VISIBLE);
                return;
            }
        } else {
            if (!loadingState)
                error.setVisibility(View.INVISIBLE);
            // newData = setAddsToList(setDataToList(data));
            newData = setDataToList(data);
            limitData = new ArrayList<>();
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
                            limitData = setAddsToList(limitData);
                            addIndex = getAddsIndex(limitData);
                            mAdapter.setdata(limitData, addIndex);
                            fadeIn.start();
                            in = getlistin();
                            setUpAndLoadNativeExpressAds();
                        } else {
                            for (int i = 0; i < listIndex; i++) {
                                limitData.add(newData.get(i));
                            }
                            //Log.d(TAG + " " + limitData.size(), "onLoadFinished: ");
                            limitData = setAddsToList(limitData);
                            addIndex = getAddsIndex(limitData);
                            mAdapter.setdata(limitData, addIndex);
                            fadeIn.start();
                            setUpAndLoadNativeExpressAds();
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
        //Log.d(TAG + " " + pos, " onLoadFinished: ");
        if (pos > 0)
            layoutManager.scrollToPosition(pos);
        if (!loadingState)
            loading.setVisibility(View.GONE);


    }


    public void marge() {
        int jup = 20;
        if (newData.size() < jup)
            jup = newData.size();
        for (int i = 0; i < jup; i++)
            limitData.add(newData.get(i));
        limitData = setAddsToList(limitData);
        addIndex = getAddsIndex(limitData);
        listIndex = jup;
        mAdapter.setdata(limitData, addIndex);
        fadeIn.start();
        setUpAndLoadNativeExpressAds();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Log.d(TAG, "onLoaderReset: ");
        mAdapter.setdata(null, null);

        loader.reset();
    }

    @Override
    public void theClickedItem(String thumb) {
        String[] inde = new String[]{listIndex + "", layoutManager.findFirstVisibleItemPosition() + ""};
        NewsPreferencesUtils.setPos(context, new HashSet<>(Arrays.asList(inde)));
        loadAdd(thumb);
    }

    @Override
    public void theClickedItem(String thumb, int id) {
        if (thumb.contains("http")) {
            String[] inde = new String[]{listIndex + "", layoutManager.findFirstVisibleItemPosition() + ""};
            NewsPreferencesUtils.setPos(context, new HashSet<>(Arrays.asList(inde)));
            loadAdd(thumb, id);

        }
    }


    public List<Object> setAddsToList(List<Object> data) {

        for (int i = ITEMS_PER_AD; i <= data.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            MyAdd my = new MyAdd();
            my.setAdView(adView);
            data.add(i, my);

            //Log.d(i + " ", "onAdLoaded: ");
        }
        return data;
    }

    public List<Object> setAddsToList(List<Object> data, int index) {

        for (int i = index; i <= limitData.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            MyAdd my = new MyAdd();
            my.setAdView(adView);
            data.add(i, my);
            //Log.d(i + " ", "onAdLoaded: ");
        }
        return data;
    }

    public List<Object> setAddsToList(List<Object> data, int fristIndex, int lastIndex) {

        for (int i = ITEMS_PER_AD; i <= lastIndex; i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            MyAdd my = new MyAdd();
            my.setAdView(adView);
            data.add(i, my);
            //Log.d(i + " ", "onAdLoaded: ");
        }
        return data;
    }

    public ArrayList<String> getAddsIndex(List<Object> data) {
        ArrayList<String> index = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) instanceof MyAdd)
                index.add(i + "");
        }
        //Log.d(TAG, "getAddsIndex: " + index);
        return index;
    }

    public List<Object> setDataToList(Cursor c) {
        List<Object> data = new ArrayList<>();


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

    protected void checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        try {
                            Log.d(TAG, "onClick: go to play store");
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.android.play.games")));
                        } catch (Exception anfe) {
                            Log.d(TAG, "onClick: go to website");
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.play.games")));
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        finish();
                    }
                });


// 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();

                if (dialog != null) {
                    dialog.show();


                }
            }


        }

    }
}
