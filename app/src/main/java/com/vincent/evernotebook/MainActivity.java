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

import static com.vincent.evernotebook.Settings.SortOrder;


public class MainActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback,
                                                NotesListFragment.OnListFragmentInteractionListener {

    private MenuItem mMenuSortTime;
    private MenuItem mMenuSortTitle;
    private NotesListFragment mNoteListFragment;

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
            Toast.makeText(this, "Logging in failed. Check your Internet connection.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_main);

        mNoteListFragment = (NotesListFragment) getSupportFragmentManager().findFragmentById(R.id.main_notes_fragment);

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
        return EvernoteSession.getInstance().isLoggedIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mMenuSortTime = menu.findItem(R.id.menu_sort_by_time);
        mMenuSortTitle = menu.findItem(R.id.menu_sort_by_title);

        SortOrder sortOrder = Settings.getInstance(this).loadSortOrder();
        switch (sortOrder) {
            default:
            case Chronological:
                mMenuSortTime.setChecked(true);
                break;
            case Title:
                mMenuSortTitle.setChecked(true);
        }
        return true;
    }

    private void changeSortOrder(SortOrder sortOrder) {
        mNoteListFragment.updateSorting(sortOrder);
        Settings.getInstance(this).saveSortOrder(sortOrder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean check = !item.isChecked();

        //noinspection SimplifiableIfStatement
        if (item == mMenuSortTime) {
            item.setChecked(check);
            changeSortOrder(SortOrder.Chronological);
            return true;
        }

        if (item == mMenuSortTitle) {
            item.setChecked(check);
            changeSortOrder(SortOrder.Title);
            return true;
        }

        if (id == R.id.menu_sort_by_title) {

        }

        return super.onOptionsItemSelected(item);
    }

}
