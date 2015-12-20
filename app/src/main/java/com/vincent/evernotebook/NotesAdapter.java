package com.vincent.evernotebook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteSearchHelper;
import com.evernote.client.android.type.NoteRef;
import com.vincent.evernotebook.NotesListFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.vincent.evernotebook.Settings.SortOrder;

/**
 * {@link RecyclerView.Adapter} that can display a note and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>
        implements EvernoteCallback<EvernoteSearchHelper.Result> {

    private List<NoteRef> mNotesChronological;
    private List<NoteRef> mNotes;
    private final OnListFragmentInteractionListener mListener;
    private final EvernoteFacade mEvernoteFacade;
    private SortOrder mSortOrder;


    public NotesAdapter(SortOrder sortOrder, EvernoteFacade evernoteFacade, OnListFragmentInteractionListener listener) {
        mEvernoteFacade = evernoteFacade;
        mListener = listener;
        mNotes = Collections.emptyList();
        mSortOrder = sortOrder;
    }

    public List<NoteRef> getNotes() {
        return mNotes;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.mSortOrder = sortOrder;
    }

    public void orderNotes() {
        // sort only when the data was loaded
        if (mNotesChronological == null) {
            return;
        }

        mNotes = new ArrayList<>(mNotesChronological);

        if (mSortOrder == SortOrder.Title) {
            Collections.sort(mNotes, new Comparator<NoteRef>() {
                @Override
                public int compare(NoteRef lhs, NoteRef rhs) {
                    return lhs.getTitle().compareTo(rhs.getTitle());
                }
            });
        }

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NoteRef note = mNotes.get(position);
        holder.mItem = note;
        holder.mTitleView.setText(note.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onNoteClicked(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onSuccess(EvernoteSearchHelper.Result result) {
        mNotesChronological = result.getAllAsNoteRef();
        orderNotes();
    }

    public void setNotes(List<NoteRef> notes) {
        this.mNotes = notes;
    }

    public void requestNotes() {
        mEvernoteFacade.getNotes(this);
    }

    @Override
    public void onException(Exception exception) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        private NoteRef mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.note_title);
            view.setClickable(true);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
