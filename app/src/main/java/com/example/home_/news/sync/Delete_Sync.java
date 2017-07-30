package com.example.home_.news.sync;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Home- on 30/07/2017.
 */

public class Delete_Sync extends JobService {
    private AsyncTask<Void, Void, Void> mfetchdata;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mfetchdata = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                NewTask.deleteTask(getApplicationContext());
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
