package com.underoneroof.mmuboard;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.underoneroof.mmuboard.Adapter.SubjectUsersAdapter;
import com.underoneroof.mmuboard.Model.Subject;
public class SubjectUsersFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "index";
    private static final String ARG_PARAM2 = "param2";
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_users, container, false);
        mListView = (GridView) view.findViewById(android.R.id.list);
        mSubjectUsersAdapter = new SubjectUsersAdapter(getActivity(), mSubjectObjectId);
        mListView.setAdapter(mSubjectUsersAdapter);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
