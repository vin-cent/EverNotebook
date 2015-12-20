package com.vincent.evernotebook;


import android.content.Context;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteClientFactory;
import com.evernote.client.android.asyncclient.EvernoteHtmlHelper;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

public class EvernoteFacade {

    private static final String CONSUMER_KEY = "Your consumer key";
    private static final String CONSUMER_SECRET = "Your consumer secret";

    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.SANDBOX;
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = false;

    private EvernoteSession mSession;


    public EvernoteFacade(Context applicationContext) {
        String consumerSecret = BuildConfig.EVERNOTE_CONSUMER_SECRET;
        String consumerKey = BuildConfig.EVERNOTE_CONSUMER_KEY;

        mSession = new EvernoteSession.Builder(applicationContext)
                .setEvernoteService(EVERNOTE_SERVICE)
                .setSupportAppLinkedNotebooks(SUPPORT_APP_LINKED_NOTEBOOKS)
                .setForceAuthenticationInThirdPartyApp(true)
                .build(consumerKey, consumerSecret);
        mSession.asSingleton();
    }

    public void getNotes(EvernoteCallback<EvernoteSearchHelper.Result> callback) {
        EvernoteSearchHelper.Search search = new EvernoteSearchHelper.Search();
        EvernoteSearchHelper searchHelper = mSession.getEvernoteClientFactory().getEvernoteSearchHelper();
        searchHelper.executeAsync(search, callback);
    }

    public void createNote(String title, String content, EvernoteCallback<Note> callback) {
        if (!EvernoteSession.getInstance().isLoggedIn()) {
            return;
        }

        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();

        Note note = new Note();
        note.setTitle(title);
        note.setContent(EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX);

        noteStoreClient.createNoteAsync(note, callback);
    }

    public Note getNoteContent(NoteRef noteRef) throws Exception {
        return noteRef.loadNote(true, false, false, false);
    }

    public String getNoteContentAsHtml(String guid) throws Exception {
        EvernoteClientFactory clientFactory = EvernoteSession.getInstance().getEvernoteClientFactory();
        EvernoteHtmlHelper htmlHelper = clientFactory.getHtmlHelperDefault();
        Response response = htmlHelper.downloadNote(guid);
        return htmlHelper.parseBody(response);
    }

    public Note getNoteContent(String guid) throws Exception {
        EvernoteNoteStoreClient noteStoreClient = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        return noteStoreClient.getNote(guid, true, false, false, false);
    }

}
