package com.underoneroof.mmuboard;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.SubjectAdapter;
import com.underoneroof.mmuboard.Adapter.SubjectListAdapter;
import com.underoneroof.mmuboard.Adapter.SubjectUsersAdapter;
import com.underoneroof.mmuboard.Model.SubjectUser;

import java.util.List;

public class SubjectListFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private SubjectListAdapter mSubjectListAdapter;

    public SubjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);
        mListView = (ListView)view.findViewById(R.id.list);
        mSubjectListAdapter = new SubjectListAdapter(getActivity());
        mListView.setAdapter(mSubjectListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "SU Item Clicked", Toast.LENGTH_SHORT).show();

                SubjectUser subjectUser = new SubjectUser();
                subjectUser.put("user", ParseUser.getCurrentUser());
                subjectUser.put("subject",
                        ParseObject.createWithoutData("Subject",
                                mSubjectListAdapter.getItem(position).getObjectId()));
                subjectUser.put("status", 1);
                subjectUser.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        loadFromParse();

                    }
                });
                mSubjectListAdapter.loadObjects();
                mSubjectListAdapter.notifyDataSetChanged();

            }
        });

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    private void loadFromParse() {
        ParseQuery<SubjectUser> query = SubjectUser.getQuery();
//        query.whereEqualTo("author", ParseUser.getCurrentUser());
        query.include("subject").include("users").findInBackground(new FindCallback<SubjectUser>() {
            @Override
            public void done(List<SubjectUser> subjects, com.parse.ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(subjects,
                            new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        mSubjectListAdapter.loadObjects();
                                    } else {
                                        Log.i("Subject Adapter",
                                                "Error pinning subjects: "
                                                        + e.getMessage());
                                    }
                                }

                                public void done(java.text.ParseException e) {

                                }
                            });
                } else {
                    Log.i("TodoListActivity",
                            "loadFromParse: Error finding pinned todos: "
                                    + e.getMessage());
                }
            }
        });
    }

}
