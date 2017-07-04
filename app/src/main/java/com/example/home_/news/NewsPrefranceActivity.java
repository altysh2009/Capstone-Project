package com.example.home_.news;


import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.home_.news.data.NewsContract;
import com.example.home_.news.data.NewsPreferencesUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by Home- on 02/07/2017.
 */

public class NewsPrefranceActivity extends AppCompatActivity {
    static Set<String> empty = new Set<String>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<String> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(String s) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends String> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    };
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
            if (sharedPreferences instanceof MultiSelectListPreference) {
                if (key.equals(getString(R.string.sources_key))) {
                    NewsPreferencesUtils.setSources(context, sharedPreferences.getStringSet(getString(R.string.sources_key), empty));
                } else if (key.equals(getString(R.string.category_key))) {
                    NewsPreferencesUtils.setPrefCatgory(context, sharedPreferences.getStringSet(getString(R.string.category_key), empty));
                } else if (key.equals(getString(R.string.country_key))) {
                    NewsPreferencesUtils.setPrefConterys(context, sharedPreferences.getStringSet(getString(R.string.country_key), empty));
                } else if (key.equals(getString(R.string.lan_key))) {
                    NewsPreferencesUtils.setPrefLang(context, sharedPreferences.getStringSet(getString(R.string.lan_key), empty));
                }
            } else if (sharedPreferences instanceof ListPreference) {
                if (key.equals(getString(R.string.see_first_key))) {
                    NewsPreferencesUtils.setSeefirst(context, sharedPreferences.getString(getString(R.string.see_first_key), ""));
                } else if (key.equals(getString(R.string.backed_up_data_key))) {
                    NewsPreferencesUtils.setBackedData(context, sharedPreferences.getInt(getString(R.string.backed_up_data_key), 7));
                }
            } else if (sharedPreferences instanceof android.preference.CheckBoxPreference) {
                NewsPreferencesUtils.setNotifacation(context, sharedPreferences.getBoolean(getString(R.string.backed_up_data_key), false));
            }
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
