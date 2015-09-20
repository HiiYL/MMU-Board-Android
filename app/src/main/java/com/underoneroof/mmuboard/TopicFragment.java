package com.underoneroof.mmuboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.TopicAdapter;
import com.underoneroof.mmuboard.Interface.FragmentParseLocalInterface;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.Utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends android.support.v4.app.Fragment implements FragmentParseLocalInterface {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SUBJECT_ACCESS = "subject_access";

    private String mSubjectObjectId;
    private String mSubjectName;
    private boolean mPushEnabled = false;
    private int mSubjectAccessLevel;

    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;

    private TopicAdapter mAdapter;
    private FloatingActionButton mCreateSubjectButton;

    private List<ParseObject> topics;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private MenuItem mPushEnabledMenuItem;
    public static TopicFragment newInstance(String index, String mSubjectName, int subjectAccessLevel) {
        TopicFragment f = new TopicFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("index", index);
        args.putString("subject_name", mSubjectName);
        args.putInt(ARG_SUBJECT_ACCESS, subjectAccessLevel);
        f.setArguments(args);
        return f;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TopicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mSubjectObjectId = getArguments().getString("index");
            mSubjectName = getArguments().getString("subject_name");
            mSubjectAccessLevel = getArguments().getInt(ARG_SUBJECT_ACCESS);
            mAdapter = new TopicAdapter(getActivity(), mSubjectObjectId);
        }
    }
    @Override
    public void onResume() {
        if(Utility.isConnectedToNetwork(getActivity())) {
            loadFromParse();
        }else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(mSubjectName);

        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(R.id.empty));
        Button emptyCreateTopicButton = (Button) view.findViewById(R.id.empty_create_topic_btn);
        mCreateSubjectButton = (FloatingActionButton) view.findViewById(R.id.create_topic_btn);
        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bulletin_activity_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFromParse();
            }
        });

        View.OnClickListener createSubjectButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTopicFragment createTopicFragment = CreateTopicFragment.newInstance(mSubjectObjectId, mSubjectName);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, createTopicFragment);
                fragmentTransaction.addToBackStack( "tag" ).commit();

            }
        };

        mCreateSubjectButton.setOnClickListener(createSubjectButtonListener);
        emptyCreateTopicButton.setOnClickListener(createSubjectButtonListener);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject topic = mAdapter.getItem(position);
                PostFragment postFragment = PostFragment.newInstance(topic.getObjectId(), topic.getString("title"),mSubjectAccessLevel);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, postFragment);
                fragmentTransaction.addToBackStack( "tag" ).commit();
            }
        });

        return view;
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
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }
    @Override
    public void loadFromParse() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(mAdapter.isEmpty()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        Topic.getQuery()
                .include("createdBy")
                .whereEqualTo("subject", ParseObject.createWithoutData("Subject", mSubjectObjectId))
        .findInBackground(new FindCallback<Topic>() {
            @Override
            public void done(final List<Topic> topics, com.parse.ParseException e) {
                if (e == null) {
                    ParseObject.unpinAllInBackground("Topic Adapter" + mSubjectObjectId, new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("Topic Adapter" + mSubjectObjectId, topics,
                                        new SaveCallback() {
                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                mAdapter.loadObjects();
                                                mSwipeRefreshLayout.setRefreshing(false);
                                            }
                                        });
                            } else {
                                Log.e("Topic Adapter" + mSubjectObjectId,
                                        "loadFromParse: Error finding pinned todos: "
                                                + e.getMessage());
                            }

                        }
                    });
                } else {
                    Log.e("Topic Adapter" + mSubjectObjectId, "DELETE FAILED");
                }

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_subject_users, menu);
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.action_subscribe);
            if (item != null) {
                mPushEnabledMenuItem = item;
                ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                ArrayList<ParseObject> subjects = (ArrayList<ParseObject>) parseInstallation.get("subjects");
                if(subjects != null && subjects.contains(ParseObject.createWithoutData("Subject", mSubjectObjectId))) {
                    item.setIcon(R.drawable.ic_notifications_active_white_24dp);
                    mPushEnabled = true;
                }
            }
            else {
                Log.e("MENUITEM", " MENU ITEM IS EMPTY");

            }
        }else {
            Log.e("MENU", "MENU IS EMPTY");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_add_user) {
//            return true;
//        }
        if (id == R.id.action_subscribe) {
            ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
            if(mPushEnabled) {
                ArrayList<ParseObject> subject = new ArrayList<ParseObject>();
                subject.add(ParseObject.createWithoutData("Subject", mSubjectObjectId));
                parseInstallation.removeAll("subjects", subject);
                parseInstallation.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mPushEnabled = false;
                        if (mPushEnabledMenuItem != null) {
                            mPushEnabledMenuItem.setIcon(R.drawable.ic_notifications_none_white_24dp);

                        } else {
                            Log.e("PUSH ENABLED", "PUSH ENABLED MENU ITEM IS NULL");
                        }
                    }
                });
            }else {
                parseInstallation.add("subjects", ParseObject.createWithoutData("Subject", mSubjectObjectId));
                parseInstallation.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mPushEnabled = true;
                        if (mPushEnabledMenuItem != null) {
                            mPushEnabledMenuItem.setIcon(R.drawable.ic_notifications_active_white_24dp);
                        } else {
                            Log.e("PUSH ENABLED", "PUSH ENABLED MENU ITEM IS NULL");
                        }
                    }
                });

            }
        }
        if (id == R.id.action_view_users) {
            SubjectUsersFragment subjectUsersFragment = SubjectUsersFragment.newInstance(mSubjectObjectId);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.frame, subjectUsersFragment);
            fragmentTransaction.addToBackStack( "tag" ).commit();
        }

        return super.onOptionsItemSelected(item);
    }

}
