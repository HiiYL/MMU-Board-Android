package com.underoneroof.mmuboard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.SubjectAdapter;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.SubjectUser;

import java.text.ParseException;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    TextView tv;
//    private RecyclerView mRecyclerView;
//    private LinearLayoutManager mLayoutManager;
    private List<Subject> subjects;
//    private MyAdapter mAdapter;
    private ListView mListView;
    private ParseQueryAdapter<ParseObject> mSubjectAdapter;
    private FloatingActionButton mCreateSubjectButton;

    public MainActivityFragment() {
    }
    @Override
    public void onResume() {
        loadFromParse();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("My Subjects");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.subject_listview);
        mCreateSubjectButton = (FloatingActionButton) rootView.findViewById(R.id.create_subject_btn);
        mSubjectAdapter = new SubjectAdapter(getActivity());
        mListView.setAdapter(mSubjectAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mSubjectAdapter.getItem(position).getInt("status") == 0) {
                }else {
                    TopicFragment topicFragment = TopicFragment.newInstance(mSubjectAdapter.getItem(position).getParseObject("subject").getObjectId());
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

                    fragmentTransaction.replace(R.id.frame, topicFragment);
                    fragmentTransaction.addToBackStack("tag").commit();
                }

            }
        });

        mCreateSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateSubjectActivity.class);
                startActivity(intent);
            }
        });
        return rootView;

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
                                        mSubjectAdapter.loadObjects();
                                    } else {
                                        Log.i("Subject Adapter",
                                                "Error pinning subjects: "
                                                        + e.getMessage());
                                    }
                                }

                                public void done(ParseException e) {

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
    private void syncTodosToParse() {
            if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                ParseQuery<Subject> query = Subject.getQuery();
                query.fromPin(MainActivity.SUBJECT_GROUP_NAME);
                query.whereEqualTo("isDraft", true);
                query.findInBackground(new FindCallback<Subject>() {
                    @Override
                    public void done(List<Subject> objects, com.parse.ParseException e) {
                        for (final Subject subject : subjects) {
                            // Set is draft flag to false before
                            // syncing to Parse
                            subject.setDraft(false);
                            subject.saveEventually(new SaveCallback() {

                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        // Let adapter know to update view
                                        mSubjectAdapter
                                                .notifyDataSetChanged();
                                    } else {
                                        // Reset the is draft flag locally to true
                                        subject.setDraft(true);
                                    }
                                }
                            });
                        }
                    }
                });
                // If we have a network connection and a current
                // logged in user, sync the todos
            } else {
                // If we have a network connection but no logged in user, direct
                // the person to log in or sign up.
                Toast.makeText(
                        getActivity(),
                        "You appear to not be logged in! Log in to continue",
                        Toast.LENGTH_LONG).show();
            }
    }

}
