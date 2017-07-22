/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.home_.news.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.home_.news.R;

import java.util.Set;


public final class NewsPreferencesUtils {



    public static final String PREF_LAN ="user-lang";
    public static final String PREF_Contreys ="user-country";
    public static final String PREF_Catgory ="user-catgory";
    public static final String PREF_sordBy ="see-first";


    public static void setPrefLang(Context context, Set<String> lang) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();


        editor.putStringSet(context.getString(R.string.lan_key), lang);

        editor.apply();
    }

    public static void setSources(Context context, Set<String> lang) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        //Set<String> set = new HashSet<String>(Arrays.asList(lang.split(" ")));
        editor.putStringSet(context.getString(R.string.sources_key), lang);
        editor.apply();
    }

    public static void setPrefConterys(Context context, Set<String> contrey) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        //Set<String> set = new HashSet<String>(Arrays.asList(contrey.split(" ")));
        editor.putStringSet(context.getString(R.string.country_key), contrey);
        editor.apply();
    }

    public static void setPrefCatgory(Context context, Set<String> catgory) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        //Set<String> set = new HashSet<String>(Arrays.asList(catgory.split(" ")));
        editor.putStringSet(context.getString(R.string.category_key), catgory);

        editor.apply();
    }
    public static void setSeefirst(Context context,String sord) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.see_first_key), sord);

        editor.apply();
    }

    public static void setBackedData(Context context, int durtation) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(context.getString(R.string.see_first_key), durtation);

        editor.apply();
    }

    public static void setNotifacation(Context context, Boolean durtation) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(context.getString(R.string.notifications_key), durtation);

        editor.apply();
    }


//    public static void resetLocationCoordinates(Context context) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sp.edit();
//
//        editor.remove(PREF_COORD_LAT);
//        editor.remove(PREF_COORD_LONG);
//        editor.apply();
//    }


    public static Set<String> getPreferredLang(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getStringSet(context.getString(R.string.lan_key), null);
    }

    public static Set<String> getPreferredCountry(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getStringSet(context.getString(R.string.country_key), null);
    }

    public static Set<String> getPreferredCatgory(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getStringSet(context.getString(R.string.category_key), null);
    }
    public static String getPreferredSeeFirst(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString(context.getString(R.string.see_first_key), "");
    }

    public static Set<String> getPreferredSources(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> ss = sp.getStringSet(context.getString(R.string.sources_key), null);
        //Log.d(ss.size()+"", "getPreferredSources: ");

        return ss;
    }

    public static String getSeefirst(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getString(R.string.see_first_key), null);
    }

    public static int getBackedData(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(context.getString(R.string.sources_key), 7);
    }

    public static Boolean getNotifacation(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(context.getString(R.string.sources_key), false);
    }

    public static void saveLastId(Context context, long Id) {
        if (Id != -1) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();

            editor.putLong("Last Id", Id);

            editor.apply();
        } else if (getLastId(context) > 0) {

        } else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sp.edit();

            editor.putLong("Last Id", 0);

            editor.apply();
        }
    }

    public static long getLastId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong("Last Id", 0);
    }

    public static void setPos(Context context, Set<String> pos) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putStringSet("listIndex", pos);


        editor.apply();
    }

    public static Set<String> getPos(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getStringSet("listIndex", null);
    }




}