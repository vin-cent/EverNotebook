package com.vincent.evernotebook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    private static final String KEY_TITLE = "edit-title";
    private static final String KEY_CONTENT = "edit-content";

    private CreateNoteFragmentListener mListener;

    private EditText mTitleEditText;
    private EditText mContentEditText;
    private TabLayout mTabs;
    private ViewPager mViewPager;
    private View editContentView;
    private View drawContentView;

    private String mContentText = "";

    public CreateNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateNoteFragment.
     */
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
        mViewPager = (ViewPager) view.findViewById(R.id.create_note_pager);
        initViewPager(mViewPager);

        mTabs = (TabLayout) view.findViewById(R.id.create_note_tabs);
        mTabs.setupWithViewPager(mViewPager);
        if (savedInstanceState != null) {
            mTitleEditText.setText(savedInstanceState.getString(KEY_TITLE));
            mContentText = savedInstanceState.getString(KEY_CONTENT);
        }

        return view;
    }

    private void initViewPager(ViewPager mViewPager) {
        mViewPager.setAdapter(new ViewPagerAdapter());
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_TITLE, getNoteTitle());
        outState.putString(KEY_CONTENT, getNoteContent());
    }

    public String getNoteTitle() {
        return mTitleEditText.getText().toString();
    }

    public String getNoteContent() {
        if (mContentEditText != null) {
            return mContentEditText.getText().toString();
        } else {
            return "";
        }
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


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                editContentView = LayoutInflater.from(getContext()).inflate(R.layout.create_note_text, container, false);
                mContentEditText = (EditText) editContentView.findViewById(R.id.create_note_content);
                mContentEditText.setText(mContentText);
                container.addView(editContentView);
                return editContentView;
            } else {
                drawContentView = LayoutInflater.from(getContext()).inflate(R.layout.create_note_canvas, container, false);
                container.addView(drawContentView);
                return drawContentView;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Text";
            } else {
                return "OCR";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
