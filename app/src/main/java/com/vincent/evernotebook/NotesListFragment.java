package com.vincent.evernotebook;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evernote.client.android.type.NoteRef;
import com.evernote.edam.type.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NotesListFragment extends Fragment {

    private static final int COLUMN_COUNT = 1;
    public static final String KEY_NOTES = "notes";
    private OnListFragmentInteractionListener mListener;

    private EvernoteFacade mEvernoteFacade;
    private NotesAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotesListFragment() {
    }

    @SuppressWarnings("unused")
    public static NotesListFragment newInstance() {
        NotesListFragment fragment = new NotesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EvernoteApplication app = (EvernoteApplication) getContext().getApplicationContext();
        mEvernoteFacade = app.getEvernoteFacade();
        Settings.SortOrder order = Settings.getInstance(getContext()).loadSortOrder();
        mAdapter = new NotesAdapter(order, mEvernoteFacade, mListener);

        boolean requestNotes = true;

        if (savedInstanceState != null) {
            List<NoteRef> notes = (ArrayList) savedInstanceState.getParcelableArrayList(KEY_NOTES);

            if (notes != null) {
                mAdapter.setNotes(notes);
                requestNotes = false;
            }
        }

        if (requestNotes) {
            mAdapter.requestNotes();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<NoteRef> notes = mAdapter.getNotes();

        if (notes != null && !notes.isEmpty()) {
            outState.putParcelableArrayList(KEY_NOTES, (ArrayList<NoteRef>) notes);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            if (COLUMN_COUNT <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, COLUMN_COUNT));
            }


            NotesAdapter adapter = getNotesAdapter();
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public NotesAdapter getNotesAdapter() {
        return mAdapter;
    }

    public void updateSorting(Settings.SortOrder order) {
        mAdapter.setSortOrder(order);
        mAdapter.orderNotes();
    }

    public void refreshNotes() {
        mAdapter.requestNotes();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onNoteClicked(NoteRef note);
    }
}
