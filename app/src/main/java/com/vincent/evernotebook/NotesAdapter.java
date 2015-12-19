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

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a note and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>
        implements EvernoteCallback<EvernoteSearchHelper.Result> {

    private List<NoteRef> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final EvernoteFacade mEvernoteFacade;

    public NotesAdapter(EvernoteFacade evernoteFacade, OnListFragmentInteractionListener listener) {
        mEvernoteFacade = evernoteFacade;
        mListener = listener;
        mValues = Collections.emptyList();
        evernoteFacade.getNotes(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NoteRef note = mValues.get(position);
        holder.mItem = note;
        holder.mIdView.setText(note.getGuid());
        holder.mContentView.setText(note.getTitle());

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
        return mValues.size();
    }

    @Override
    public void onSuccess(EvernoteSearchHelper.Result result) {
        mValues = result.getAllAsNoteRef();
        notifyDataSetChanged();
    }

    @Override
    public void onException(Exception exception) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        private NoteRef mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
