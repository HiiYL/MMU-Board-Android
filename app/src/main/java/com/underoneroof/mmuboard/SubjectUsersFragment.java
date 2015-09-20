package com.underoneroof.mmuboard;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.underoneroof.mmuboard.Adapter.SubjectUsersAdapter;
public class SubjectUsersFragment extends android.support.v4.app.Fragment {
    private static final String ARG_PARAM1 = "index";
    private OnFragmentInteractionListener mListener;
    private String mSubjectObjectId;
    private SubjectUsersAdapter mSubjectUsersAdapter;
    private GridView mListView;

    // TODO: Rename and change types and number of parameters
    public static SubjectUsersFragment newInstance(String mSubjectObjectId) {
        SubjectUsersFragment fragment = new SubjectUsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mSubjectObjectId);
        fragment.setArguments(args);
        return fragment;
    }

    public SubjectUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubjectObjectId = getArguments().getString(ARG_PARAM1);
            mSubjectUsersAdapter = new SubjectUsersAdapter(getActivity(), mSubjectObjectId);
            ParseQuery.getQuery("SubjectUser")
                    .whereEqualTo("user", ParseUser.getCurrentUser())
                    .whereEqualTo("subject", ParseObject.createWithoutData("Subject", mSubjectObjectId))
                    .whereEqualTo("status", 3)
                    .fromLocalDatastore()
                    .getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if(object != null) {
                                mSubjectUsersAdapter.enableSpinner();
                                Log.d("SPINNER", "SPINNER ENABLED");
                            }
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_users, container, false);
        mListView = (GridView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mSubjectUsersAdapter);


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
