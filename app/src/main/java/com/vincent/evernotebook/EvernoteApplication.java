package com.vincent.evernotebook;

import android.app.Application;

import com.evernote.client.android.EvernoteSession;

public class EvernoteApplication extends Application {

    private EvernoteFacade evernoteFacade;

    @Override
    public void onCreate() {
        super.onCreate();

        evernoteFacade = new EvernoteFacade(this);
    }

    public EvernoteFacade getEvernoteFacade() {
        return evernoteFacade;
    }
}
