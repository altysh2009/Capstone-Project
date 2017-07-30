package com.example.home_.news;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;



public class WebViewActivity extends AppCompatActivity {
    String url = "http://www.example.com";
    WebView myWebView;
    Toolbar toolbar;
    Context cc;

    public void shareTwitter(String url) {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text(url);

        builder.show();
    }

    public void shareFacebook(String url) {


        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(url))
                .build();
        ShareDialog shareDialog = new ShareDialog(WebViewActivity.this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);


    }

    public void shareGoogle(String url) {
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")

                .setContentUrl(Uri.parse(url))
                .getIntent();
        try {
            startActivityForResult(shareIntent, 0);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(WebViewActivity.this, getString(R.string.toast_gmail), Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        cc = getApplicationContext();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        url = getIntent().getExtras().getString("url");
        toolbar = (Toolbar) findViewById(R.id.toolbar_web);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(url);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(WebViewActivity.this).title(getString(R.string.menu_descrption)).sheet(R.menu.buttom_menu).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.share_face:
                                shareFacebook(url);

                                break;
                            case R.id.share_gmail:
                                shareGoogle(url);

                                break;
                            case R.id.share_twitter:
                                shareTwitter(url);

                                break;

                        }
                    }
                }).show();
            }
        });
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        final Activity activity = this;
        setSupportActionBar(toolbar);
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%

                progressBar.setProgress(progress);
            }
        });
        myWebView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        myWebView.loadUrl(url);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}
