package com.vincent.evernotebook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNoteFragmentListener} interface
 * to handle interaction events.
 * Use the {@link CreateNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNoteFragment extends Fragment {


    private CreateNoteFragmentListener mListener;

    private EditText mTitleEditText;
    private EditText mContentEditText;


    public CreateNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNoteFragment newInstance() {
        CreateNoteFragment fragment = new CreateNoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_note, container, false);

        mTitleEditText = (EditText) view.findViewById(R.id.create_note_title);
        mContentEditText = (EditText) view.findViewById(R.id.create_note_content);

        return view;
    }

    public String getNoteTitle() {
        return mTitleEditText.getText().toString();
    }

    public String getNoteContent() {
        return mContentEditText.getText().toString();
    }

    public boolean validate() {
        if (getNoteTitle().isEmpty()) {
            String msg = getString(R.string.create_note_title_missing);
            mTitleEditText.setError(msg);
            mTitleEditText.requestFocus();
            return false;
        }

        if (getNoteContent().isEmpty()) {
            String msg = getString(R.string.create_note_content_missing);
            mContentEditText.setError(msg);
            mContentEditText.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateNoteFragmentListener) {
            mListener = (CreateNoteFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreateNoteFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface CreateNoteFragmentListener {

    }
}
