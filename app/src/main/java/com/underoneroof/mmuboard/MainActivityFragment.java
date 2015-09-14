package com.underoneroof.mmuboard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.SubjectAdapter;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.SubjectUser;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.Utility.Utility;

import java.text.ParseException;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ListView mListView;
    private ParseQueryAdapter<ParseObject> mSubjectAdapter;
    private FloatingActionButton mCreateSubjectButton;
    private Button joinSubjectsButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean hasLoaded = false;

    public MainActivityFragment() {
    }
    @Override
    public void onResume() {
        loadFromParse();
        if(Utility.isLecturer()) {
            mCreateSubjectButton.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubjectAdapter = new SubjectAdapter(getActivity());
        Log.d("ONCREATE", "ONCREATE CALLED");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("My Subjects");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.subject_listview);
        mCreateSubjectButton = (FloatingActionButton) rootView.findViewById(R.id.create_subject_btn);
        joinSubjectsButton = (Button) rootView.findViewById(R.id.join_subject_btn);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.bulletin_activity_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFromParse();
            }
        });

        mListView.setEmptyView(rootView.findViewById(R.id.empty));
        mListView.setAdapter(mSubjectAdapter);

        joinSubjectsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubjectListFragment subjectListFragment = new SubjectListFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.frame, subjectListFragment);
                fragmentTransaction.commit();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSubjectAdapter.getItem(position).getInt("status") < 2) {
                    Toast.makeText(getActivity(), "You do not have permission to view this topic", Toast.LENGTH_SHORT).show();
                } else {
                    ParseObject subject = mSubjectAdapter.getItem(position).getParseObject("subject");
                    TopicFragment topicFragment = TopicFragment.newInstance(subject.getObjectId(), subject.getString("title"));
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
    public void loadFromParse() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        SubjectUser.getQuery().include("subject.createdBy")
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .findInBackground(new FindCallback<SubjectUser>() {
                    @Override
                    public void done(final List<SubjectUser> objects, com.parse.ParseException e) {
                        if (e == null) {
                            ParseObject.unpinAllInBackground("Subject Adapter", new DeleteCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        ParseObject.pinAllInBackground("Subject Adapter", objects, new SaveCallback() {
                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                mSubjectAdapter.loadObjects();
                                                mSwipeRefreshLayout.setRefreshing(false);
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            Log.e("Subject Adapter", "Find Error");
                        }

                    }
                });
    }
    public void loadAllFromParse() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        ParseQuery<SubjectUser> query = SubjectUser.getQuery();
        query.include("subject.createdBy")
//                .whereEqualTo("user", ParseUser.getCurrentUser())
                .findInBackground(new FindCallback<SubjectUser>() {
                    @Override
                    public void done(final List<SubjectUser> subjectUsers, com.parse.ParseException e) {
                        if (e == null) {
                            ParseObject.pinAllInBackground(subjectUsers,
                                    new SaveCallback() {
                                        @Override
                                        public void done(com.parse.ParseException e) {
                                            if (e == null) {
                                                loadTopics(subjectUsers);
                                            } else {
                                                Log.i("Subject Adapter",
                                                        "Error pinning subjects: "
                                                                + e.getMessage());
                                            }
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
    void loadTopics(final List<SubjectUser> subjectUsers) {
        for (final SubjectUser subjectUser : subjectUsers) {
            Topic.getQuery().whereEqualTo("subject", subjectUser.getParseObject("subject"))
                    .findInBackground(new FindCallback<Topic>() {
                        @Override
                        public void done(final List<Topic> topics, com.parse.ParseException e) {
                            final String subjectObjectId = subjectUser.getParseObject("subject").getObjectId();
                            if (e == null) {
                                ParseObject.unpinAllInBackground("Topic Adapter" + subjectObjectId, new DeleteCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        if (e == null) {
                                            ParseObject.pinAllInBackground("Topic Adapter" + subjectObjectId,topics,
                                                    new SaveCallback() {
                                                        @Override
                                                        public void done(com.parse.ParseException e) {
//                                                            if(subjectUser == subjectUsers.get(subjectUsers.size() - 1)) {
//                                                                mSubjectAdapter.loadObjects();
//                                                                mSwipeRefreshLayout.setRefreshing(false);
//
//                                                            }
                                                            loadPosts(topics, (subjectUser == subjectUsers.get(subjectUsers.size() - 1)));
                                                        }
                                                    });
                                        } else {
                                            Log.e("Topic Adapter" + subjectObjectId,
                                                    "loadFromParse: Error finding pinned todos: "
                                                            + e.getMessage());
                                        }

                                    }
                                });
                            } else {
                                Log.i("Topic Adapter",
                                        "loadFromParse: Error finding pinned todos: "
                                                + e.getMessage());
                            }
                        }
                    });
        }
    }
    void loadPosts(final List<Topic> topics, final boolean is_last) {
        for(final Topic topic : topics) {
            Post.getQuery().whereEqualTo("topic", topic)
                    .findInBackground(new FindCallback<Post>() {
                        @Override
                        public void done(final List<Post> posts, com.parse.ParseException e) {
                            final String topicObjectId = topic.getObjectId();
                            if (e == null) {
                                ParseObject.unpinAllInBackground("PostAdapter" + topicObjectId, new DeleteCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        if (e == null) {
                                            ParseObject.pinAllInBackground("PostAdapter" + topicObjectId, posts,
                                                    new SaveCallback() {
                                                        @Override
                                                        public void done(com.parse.ParseException e) {
                                                            if(is_last && ( topic == topics.get(topics.size() - 1) ) ) {
                                                                Log.d("PARSE LOAD ALL", "SHOULD ONLY BE CALLED ONCE" );
                                                                mSubjectAdapter.loadObjects();
                                                                mSwipeRefreshLayout.setRefreshing(false);
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Log.e("Post Adapter" + topicObjectId, "DELETION FAILED");
                                        }
                                    }
                                });
                            } else {
                                Log.i("Topic Adapter",
                                        "loadFromParse: Error finding pinned todos: "
                                                + e.getMessage());
                            }
                        }
                    });
        }
    }

}
