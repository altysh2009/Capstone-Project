package com.example.home_.news;


import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.home_.news.data.NewsContract;


/**
 * Created by Home- on 02/07/2017.
 */

public class NewsPrefranceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new NewsPrefranceFramgent()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class NewsPrefranceFramgent extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, android.app.LoaderManager.LoaderCallbacks<Cursor> {
        Context context;
        MultiSelectListPreference multiSelectListPreference;

        private static void bindPreferenceSummaryToValue(MultiSelectListPreference preference,
                                                         Cursor s) {

            int len = s.getCount();
            CharSequence[] entries = new CharSequence[len];
            CharSequence[] entryValues = new CharSequence[len];


            // Populate MultiSelectListPreference with entries for all available accounts

            for (int x = 0; x < len; x++) {
                if (s.moveToNext())

                    entries[x] = s.getString(s.getColumnIndex(NewsContract.NewsSources.News_Sources_Name));
                entryValues[x] = s.getString(s.getColumnIndex(NewsContract.NewsSources.News_Sources_Id));

            }
            preference.setEntries(entries);
            preference.setEntryValues(entryValues);
            s.close();

        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefrance);
            context = getActivity().getApplicationContext();

            multiSelectListPreference = (MultiSelectListPreference) findPreference(getString(R.string.multi_select_menu_key));
            getLoaderManager().initLoader(2, null, NewsPrefranceFramgent.this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }

        @Override
        public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(context, NewsContract.sources, null, null, null, null);

        }

        @Override
        public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
            bindPreferenceSummaryToValue(multiSelectListPreference, data);
        }

        @Override
        public void onLoaderReset(android.content.Loader<Cursor> loader) {
            loader.reset();
        }
    }
}
