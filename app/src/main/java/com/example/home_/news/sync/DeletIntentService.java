package com.example.home_.news.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Home- on 30/07/2017.
 */

public class DeletIntentService extends IntentService {
    public DeletIntentService() {
        super("NewsIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DeletIntentService(String name) {
        super("DeletIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("mm", "onHandleIntent: ");
        NewTask.deleteTask(this);
    }
}
