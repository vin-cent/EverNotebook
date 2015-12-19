package com.vincent.evernotebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.evernote.client.android.type.NoteRef;

public class ReadNoteActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE_GUID = "note_guid";
    private static final String EXTRA_NOTE_TITLE = "note_title";

    private static final String KEY_NOTE_HTML = "note_html";

    private String mNoteTitle;
    private String mNoteHtml;

    private EditText mTitleText;
    private WebView mContentWebview;
    private ProgressDialog mProgressDialog;

    public static void launch(Context ctx, NoteRef noteRef) {
        Intent intent = new Intent(ctx, ReadNoteActivity.class);
        intent.putExtra(EXTRA_NOTE_GUID, noteRef.getGuid());
        intent.putExtra(EXTRA_NOTE_TITLE, noteRef.getTitle());
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        mTitleText = (EditText) findViewById(R.id.read_note_title);
        mContentWebview = (WebView) findViewById(R.id.read_note_content);

        Intent intent = getIntent();
        String guid = intent.getStringExtra(EXTRA_NOTE_GUID);
        mNoteTitle = intent.getStringExtra(EXTRA_NOTE_TITLE);

        if (savedInstanceState == null ||
                !savedInstanceState.containsKey(KEY_NOTE_HTML) ) {
            mProgressDialog = ProgressDialog.show(this, "Loading", "Wait while loading...");
            new LoadNoteTask().execute(guid);
        } else {
            mNoteHtml = savedInstanceState.getString(KEY_NOTE_HTML);
            initUI(mNoteHtml);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mNoteHtml != null) {
            outState.putString(KEY_NOTE_HTML, mNoteHtml);
        }
    }

    private void initUI(String htmlContent) {
        stopProgressDialog();

        mTitleText.setText(mNoteTitle);
        mNoteHtml = htmlContent;
        String html = "<html><body>" + htmlContent + "</body></html>";
        mContentWebview.loadData(html, "text/html", "UTF-8");
    }

    private void stopProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    private class LoadNoteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String guid = params[0];
            EvernoteApplication app = (EvernoteApplication) getApplication();

            try {
                String html = app.getEvernoteFacade().getNoteContentAsHtml(guid);
                return html;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String htmlContent) {
            if (htmlContent == null) {
                Toast.makeText(getApplicationContext(), "Loading the note failed.", Toast.LENGTH_LONG).show();
                stopProgressDialog();
                finish();
                return;
            }

            initUI(htmlContent);
        }
    }

}
