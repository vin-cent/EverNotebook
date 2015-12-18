package com.vincent.evernotebook;

import android.app.Application;

import com.evernote.client.android.EvernoteSession;

public class EvernoteApplication extends Application {

    private static final String CONSUMER_KEY = "Your consumer key";
    private static final String CONSUMER_SECRET = "Your consumer secret";

    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;


    @Override
    public void onCreate() {
        super.onCreate();

        initEvernoteAPI();
    }
    
    private void initEvernoteAPI() {
        String consumerSecret = BuildConfig.EVERNOTE_CONSUMER_SECRET;
        String consumerKey = BuildConfig.EVERNOTE_CONSUMER_KEY;

        new EvernoteSession.Builder(this)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .setForceAuthenticationInThirdPartyApp(true)
                .build(consumerKey, consumerSecret)
                .asSingleton();
    }
}
