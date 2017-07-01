package com.example.home_.news.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class NewsIntentService extends IntentService {


    public NewsIntentService() {
        super("NewsIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NewTask.task(this);
    }
}
