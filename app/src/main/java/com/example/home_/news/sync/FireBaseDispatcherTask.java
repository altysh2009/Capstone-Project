package com.example.home_.news.sync;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Home- on 20/06/2017.
 */

public class FireBaseDispatcherTask extends JobService {
    private AsyncTask<Void, Void, Void> mfetchdata;
    @Override
    public boolean onStartJob(final JobParameters job) {
        mfetchdata = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                NewTask.task(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mfetchdata != null)
            mfetchdata.cancel(true);
        return false;
    }
}
