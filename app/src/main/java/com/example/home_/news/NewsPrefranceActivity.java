package com.example.home_.news;


import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.home_.news.data.NewsContract;
import com.example.home_.news.data.NewsPreferencesUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



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

    public static void setAll(Preference sharedPreferences) {
        int x = 0;
        CharSequence[] w = ((MultiSelectListPreference) sharedPreferences).getEntryValues();
        String[] change = new String[w.length];
        for (CharSequence c : ((MultiSelectListPreference) sharedPreferences).getEntryValues()) {
            change[x] = c.toString();
            //Log.d(c.toString(), "  setAll: ");
            x++;
        }
        Set<String> h = new HashSet<>(Arrays.asList(change));

        ((MultiSelectListPreference) sharedPreferences).setValues(h);
    }

    public static Boolean removeAll(Preference sharedPreferences, Set<String> old, Set<String> New) {
        int x = 0;
        final String[] nn = New.toArray(new String[]{});
        final String[] oo = old.toArray(new String[]{});
        String deff = "12";

        //Log.d("in", "removeAll: ");

        //Log.d("equl", "removeAll: ");
        for (String q : oo) {
            // Log.d(q, "removeAll: ");
            if (!Arrays.asList(nn).contains(q)) {

                deff = deff + " " + q;
                //Log.d(deff, "removeAll: ");
            }
        }
        //Log.d(nn.length + " " + oo.length + " ", "removeAll: ");
        if (Arrays.asList(deff.split(" ")).contains("all")) {
            ((MultiSelectListPreference) sharedPreferences).setValues(empty);
            return true;
        } else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null)
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
        MultiSelectListPreference sources_list;
        MultiSelectListPreference catgory_list;
        MultiSelectListPreference lang_list;

        private static void bindPreferenceSummaryToValue(MultiSelectListPreference preference,
                                                         Cursor s) {

            int len = s.getCount();
            if (len > 0) {

                CharSequence[] entries = new CharSequence[len];
                CharSequence[] entryValues = new CharSequence[len];

                // Log.d(len + " ", "bindPreferenceSummaryToValue: ");


                // Populate MultiSelectListPreference with entries for all available accounts

                for (int x = 0; x < len; x++) {
                    if (s.moveToNext())

                        entries[x] = s.getString(s.getColumnIndex(NewsContract.NewsSources.News_Sources_Name));
                    entryValues[x] = s.getString(s.getColumnIndex(NewsContract.NewsSources.News_Sources_Id));

                }

                preference.setEntries(entries);
                preference.setEntryValues(entryValues);
                preference.setDefaultValue(preference.getEntryValues());
                s.close();
            } else {
                preference.setEntries(R.array.empty);
                preference.setEntryValues(R.array.empty);
                preference.setEnabled(false);
                preference.setSummary(R.string.ematy_Value);
                // Toast.makeText(context,context.getString(R.string.ematy_Value),Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefrance);
            context = getActivity().getApplicationContext();

            sources_list = (MultiSelectListPreference) findPreference(getString(R.string.sources_key));
            lang_list = (MultiSelectListPreference) findPreference(getString(R.string.lan_key));
            catgory_list = (MultiSelectListPreference) findPreference(getString(R.string.category_key));
            if (sources_list == null)
                Log.d("object = null ", "onCreate: ");
            getLoaderManager().initLoader(2, null, NewsPrefranceFramgent.this);
        }

        @Override
        public void onViewStateRestored(Bundle savedInstanceState) {
            super.onViewStateRestored(savedInstanceState);
            getLoaderManager().restartLoader(2, null, NewsPrefranceFramgent.this);
        }

        public void setSources(MultiSelectListPreference source) {
            MultiSelectListPreference category = (MultiSelectListPreference) findPreference(getString(R.string.category_key));
            MultiSelectListPreference country = (MultiSelectListPreference) findPreference(getString(R.string.country_key));
            MultiSelectListPreference lan = (MultiSelectListPreference) findPreference(getString(R.string.lan_key));
            if (getSet(lan.getEntryValues()).containsAll(lan.getValues()) &&
                    getSet(country.getEntryValues()).equals(country.getValues()) &&
                    getSet(category.getEntryValues()).equals(category.getValues())) {
                source.setValues(getSet(source.getEntryValues()));
                return;
            }

            String select = NewsContract.NewsSources.Category + " IN "
                    + MainActivity.getStringFromSet(NewsPreferencesUtils.getPreferredCatgory(context)) + " AND " + NewsContract.NewsSources.Lang + " IN "
                    + MainActivity.getStringFromSet(NewsPreferencesUtils.getPreferredLang(context)) + " AND " + NewsContract.NewsSources.Contry + " IN "
                    + MainActivity.getStringFromSet(NewsPreferencesUtils.getPreferredCountry(context));
            //Toast.makeText(context,select,Toast.LENGTH_LONG).show();

            //Log.d(select, "onSharedPreferenceChanged: ");
            Cursor c = context.getContentResolver().query(NewsContract.sources, new String[]{NewsContract.NewsSources.News_Sources_Id}, select, null, null);
            //Log.d(c.getCount()+"", "onSharedPreferenceChanged: ");
            if (c != null && c.getCount() != 0) {
                Log.d(c.getCount() + " in if", "onSharedPreferenceChanged: ");
                String[] sources = new String[c.getCount()];
                int x = 0;
                while (c.moveToNext()) {
                    sources[x] = c.getString(c.getColumnIndex(NewsContract.NewsSources.News_Sources_Id));
                    x++;
                }
                Set<String> h = new HashSet<>(Arrays.asList(sources));
                source.setValues(h);
                c.close();
            } else {
                Toast.makeText(context, getString(R.string.empty_resources_reslut), Toast.LENGTH_SHORT).show();

            }
        }

        public Set<String> getSet(CharSequence[] charSequence) {
            String[] s = new String[charSequence.length];
            for (int i = 0; i < charSequence.length; i++)
                s[i] = charSequence[i].toString();
            return new HashSet<>(Arrays.asList(s));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //  Log.d(key, " onSharedPreferenceChanged: ");

            Preference preference = findPreference(key);
            if (preference instanceof MultiSelectListPreference) {
                //Log.d("true", "onSharedPreferenceChanged: ");
                if (key.equals(getString(R.string.sources_key))) {
//                   Set<String> s = sharedPreferences.getStringSet(getString(R.string.sources_key), empty);
//                   int q= NewsPreferencesUtils.getPreferredSources(context).toArray().length;
//                    Log.d(q+" "+ s.toArray().length, "onSharedPreferenceChanged: ");
//                  final String[] ss = s.toArray(new String[]{});
//                    if(Arrays.asList(ss).contains("all")){
//                        setAll(preference);
//                    }
//                    else removeAll(preference,NewsPreferencesUtils.getPreferredSources(context),s);


                    NewsPreferencesUtils.setSources(context, sharedPreferences.getStringSet(getString(R.string.sources_key), empty));
                } else if (key.equals(getString(R.string.category_key))) {

                    setSources((MultiSelectListPreference) findPreference(getString(R.string.sources_key)));

                    // NewsPreferencesUtils.setPrefCatgory(context, sharedPreferences.getStringSet(getString(R.string.category_key), empty));
                } else if (key.equals(getString(R.string.country_key))) {
                    setSources((MultiSelectListPreference) findPreference(getString(R.string.sources_key)));
                    // NewsPreferencesUtils.setPrefConterys(context, sharedPreferences.getStringSet(getString(R.string.country_key), empty));
                } else if (key.equals(getString(R.string.lan_key))) {
                    setSources((MultiSelectListPreference) findPreference(getString(R.string.sources_key)));
                    //NewsPreferencesUtils.setPrefLang(context, sharedPreferences.getStringSet(getString(R.string.lan_key), empty));
                }
            } else if (preference instanceof ListPreference) {
                if (key.equals(getString(R.string.see_first_key))) {
                    //NewsPreferencesUtils.setSeefirst(context, sharedPreferences.getString(getString(R.string.see_first_key), ""));
                } else if (key.equals(getString(R.string.backed_up_data_key))) {
                    //NewsPreferencesUtils.setBackedData(context, sharedPreferences.getInt(getString(R.string.backed_up_data_key), 7));
                }
            } else if (preference instanceof android.preference.CheckBoxPreference) {
                // NewsPreferencesUtils.setNotifacation(context, sharedPreferences.getBoolean(getString(R.string.backed_up_data_key), false));
            }

        }

        @Override
        public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(context, NewsContract.sources, null, null, null, null);

        }

        @Override
        public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
            bindPreferenceSummaryToValue(sources_list, data);
        }

        @Override
        public void onLoaderReset(android.content.Loader<Cursor> loader) {
            loader.reset();
        }
    }

}
