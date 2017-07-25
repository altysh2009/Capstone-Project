package com.example.home_.news;

import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by Home- on 24/07/2017.
 */

public class MyAdd {
    private NativeExpressAdView adView;
    private boolean isReady = false;
    private boolean isLoaded = false;

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public NativeExpressAdView getAdView() {
        return adView;
    }

    public void setAdView(NativeExpressAdView adView) {
        this.adView = adView;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }
}
