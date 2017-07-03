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


public final class NewsPreferencesUtils {



    public static final String PREF_LAN ="user-lang";
    public static final String PREF_Contreys ="user-country";
    public static final String PREF_Catgory ="user-catgory";
    public static final String PREF_sordBy ="see-first";



    public static void setPrefLang(Context context,String lang) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.lan_key), lang);

        editor.apply();
    }

    public static void setSources(Context context, String lang) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.sources_key), lang);

        editor.apply();
    }
    public static void setPrefConterys(Context context,String contrey) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.country_key), contrey);

        editor.apply();
    }
    public static void setPrefCatgory(Context context,String catgory) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.category_key), catgory);

        editor.apply();
    }
    public static void setSeefirst(Context context,String sord) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(context.getString(R.string.see_first_key), sord);

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


    public static String getPreferredLang(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString(context.getString(R.string.lan_key), "");
    }
    public static String getPreferredCountry(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString(context.getString(R.string.country_key), "");
    }
    public static String getPreferredCatgory(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString(context.getString(R.string.category_key), "");
    }
    public static String getPreferredSeeFirst(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString(context.getString(R.string.see_first_key), "");
    }

    public static String getPreferredSources(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString(context.getString(R.string.sources_key), "");
    }




}