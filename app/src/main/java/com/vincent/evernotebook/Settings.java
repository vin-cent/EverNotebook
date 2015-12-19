package com.vincent.evernotebook;


import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    public static final String KEY_SORT_ORDER = "sort";
    private static Settings instance;
    private final SharedPreferences mPrefs;

    private Settings(Context context) {
        mPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public static Settings getInstance(Context ctx) {
        if (instance != null) {
            return instance;
        }

        // lazy initialization
        synchronized (Settings.class) {
            // could be set by a different thread
            if (instance != null) {
                return instance;
            }

            instance = new Settings(ctx.getApplicationContext());
        }

        return instance;
    }

    public SortOrder loadSortOrder() {
        String order = mPrefs.getString(KEY_SORT_ORDER, SortOrder.Chronological.name());
        return SortOrder.valueOf(order);
    }

    public void saveSortOrder(SortOrder order) {
        mPrefs.edit().putString(KEY_SORT_ORDER, order.name())
                .commit();
    }

    public enum SortOrder {
        Chronological, Title
    }

}
