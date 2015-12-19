package com.vincent.evernotebook;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.type.Note;

public class ReadNoteActivity extends AppCompatActivity {

    private static final String EXTRA_NOTEGUID = "note_guid";

    private EditText mTitleText;
    private EditText mContentText;

    public static void launch(Context ctx, NoteRef noteRef) {
        Intent intent = new Intent(ctx, ReadNoteActivity.class);
        intent.putExtra(EXTRA_NOTEGUID, noteRef.getGuid());
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);

        mTitleText = (EditText) findViewById(R.id.read_note_title);
        mContentText = (EditText) findViewById(R.id.read_note_content);

        Intent intent = getIntent();
        String guid = intent.getStringExtra(EXTRA_NOTEGUID);

        new LoadNoteTask().execute(guid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initUI(Note note) {
        mTitleText.setText(note.getTitle());
        mContentText.setText(note.getContent());

    }


    private class LoadNoteTask extends AsyncTask<String, Void, Note> {

        @Override
        protected Note doInBackground(String... params) {
            String guid = params[0];
            EvernoteApplication app = (EvernoteApplication) getApplication();

            try {
                Note note = app.getEvernoteFacade().getNoteContent(guid);
                return note;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Note note) {
            if (note == null) {
                Toast.makeText(getApplicationContext(), "Loading the note failed.", Toast.LENGTH_LONG).show();
                return;
            }

            initUI(note);
        }
    }

}
