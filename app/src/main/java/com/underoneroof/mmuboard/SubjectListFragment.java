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
import com.parse.GetCallback;
import com.parse.Parse;
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
    public void onResume() {
        super.onResume();
        loadFromParse();
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
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                view.setEnabled(false);
                SubjectUser.getQuery()
                        .whereEqualTo("user", ParseUser.getCurrentUser())
                        .fromLocalDatastore()
                        .whereEqualTo("subject", ParseObject.createWithoutData("Subject",
                                mSubjectListAdapter.getItem(position).getObjectId()))
                        .getFirstInBackground(new GetCallback<SubjectUser>() {
                            @Override
                            public void done(SubjectUser object, ParseException e) {
                                if (e == null) {
                                    if (object.getInt("status") != 1) {
                                        ParseObject subject = object.getParseObject("subject");
                                        TopicFragment topicFragment = TopicFragment.newInstance(subject.getObjectId(),subject.getString("title"));
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                                        fragmentTransaction.replace(R.id.frame, topicFragment);
                                        fragmentTransaction.addToBackStack("tag").commit();
                                    }else {
                                        Toast.makeText(getActivity(), "Your request to join is still pending approval.", Toast.LENGTH_SHORT).show();
                                    }
                                    //object exists
                                } else {
                                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                        SubjectUser subjectUser = new SubjectUser();
                                        subjectUser.put("user", ParseUser.getCurrentUser());
                                        subjectUser.put("subject",
                                                ParseObject.createWithoutData("Subject",
                                                        mSubjectListAdapter.getItem(position).getObjectId()));
                                        if (mSubjectListAdapter.getItem(position).getBoolean("isPrivate")) {
                                            subjectUser.put("status", 1);
                                            Toast.makeText(getActivity(), "Your request to join " +
                                                    mSubjectListAdapter.getItem(position).getString("title") +
                                                    " is pending approval.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            subjectUser.put("status", 2);
                                            Toast.makeText(getActivity(), "Your request to join " +
                                                    mSubjectListAdapter.getItem(position).getString("title") +
                                                    " has been accepted!", Toast.LENGTH_SHORT).show();
                                        }

                                        subjectUser.saveEventually(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                loadFromParse();

                                            }
                                        });
                                        mSubjectListAdapter.loadObjects();
                                        mSubjectListAdapter.notifyDataSetChanged();
                                        //object doesn't exist
                                    } else {
                                        Toast.makeText(getActivity(), "UHOH SOMETHING BAD HAPPENED", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });


            }
        });

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
