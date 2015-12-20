package com.vincent.evernotebook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.type.Note;

import static com.vincent.evernotebook.Settings.SortOrder;


public class MainActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback,
                                                NotesListFragment.OnListFragmentInteractionListener,
                                                CreateNoteFragment.CreateNoteFragmentListener {

    private MenuItem mMenuSortTime;
    private MenuItem mMenuSortTitle;
    private NotesListFragment mNoteListFragment;
    private CreateNoteFragment mCreateNoteFragment;
    private FloatingActionButton mFab;

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

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCreatingNote()) {
                    createNote();
                } else {
                    showNoteCreator();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isCreatingNote()) {
            finishNoteCreation();
            return;
        }

        super.onBackPressed();
    }

    private void showNoteCreator() {
        if (mCreateNoteFragment == null) {
            mCreateNoteFragment = CreateNoteFragment.newInstance();
        }

        mFab.setImageResource(R.drawable.ic_done_white);

        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                     android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_fragment_container, mCreateNoteFragment)
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean isCreatingNote() {
        return mCreateNoteFragment != null && mCreateNoteFragment.isVisible();
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void createNote() {
        if (!mCreateNoteFragment.validate()) {
            return;
        }

        String title = mCreateNoteFragment.getNoteTitle();
        String content = mCreateNoteFragment.getNoteContent();

        EvernoteApplication application = (EvernoteApplication) getApplication();
        application.getEvernoteFacade().createNote(title, content, new EvernoteCallback<Note>() {
            @Override
            public void onSuccess(Note result) {
                mNoteListFragment.refreshNotes();
                Toast.makeText(getApplicationContext(), result.getTitle() + " has been created", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Exception exception) {
                Toast.makeText(getApplicationContext(), "Error creating note", Toast.LENGTH_LONG).show();
                exception.printStackTrace();
            }
        });
        finishNoteCreation();
    }

    private void finishNoteCreation() {
        hideSoftInput();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out)
                .remove(mCreateNoteFragment)
                .commit();

        mFab.setImageResource(R.drawable.ic_create_white);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
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

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateNote(String title, String content) {
        Toast.makeText(this, "note " + title + ": " + content, Toast.LENGTH_LONG).show();
    }
}
