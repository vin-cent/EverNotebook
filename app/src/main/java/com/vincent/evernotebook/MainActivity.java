package com.vincent.evernotebook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.evernote.client.android.type.NoteRef;

public class MainActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback,
                                                NotesListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ensureLoggedIn()) {
            EvernoteSession.getInstance().authenticate(this);
        } else {
            initUI();
        }
    }

    @Override
    public void onLoginFinished(boolean successful) {
        if (successful) {
            initUI();
        } else {
            finish();
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onNoteClicked(NoteRef note) {
        Toast.makeText(this, "Note clicked: " + note.getTitle(), Toast.LENGTH_LONG).show();
        ReadNoteActivity.launch(this, note);
    }

    private boolean ensureLoggedIn() {
        boolean loggedIn = EvernoteSession.getInstance().isLoggedIn();

        if (!loggedIn) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show();
        }

        return loggedIn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
