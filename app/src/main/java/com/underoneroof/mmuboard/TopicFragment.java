package com.underoneroof.mmuboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.SubjectAdapter;
import com.underoneroof.mmuboard.Adapter.TopicAdapter;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.SubjectUser;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.Utility.Utility;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TopicFragment extends android.support.v4.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mSubjectObjectId;
    private String mSubjectName;
    private ParseObject mSubject;
    private String mParam2;
//    private long mSubjectIndex;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private TopicAdapter mAdapter;
    private FloatingActionButton mCreateSubjectButton;

    private List<ParseObject> topics;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //    private List<Topic> topics;
    public static TopicFragment newInstance(String index, String mSubjectName) {
        TopicFragment f = new TopicFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("index", index);
        args.putString("subject_name", mSubjectName);
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
            mAdapter = new TopicAdapter(getActivity(), mSubjectObjectId);
        }
    }
    @Override
    public void onResume() {
        if(Utility.isConnectedToNetwork(getActivity())) {
            loadFromParse(mSubjectObjectId);
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
                loadFromParse(mSubjectObjectId);
            }
        });

        View.OnClickListener createSubjectButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("Create A Topic")
                        .content("Title")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Enter the title of your topic", null , new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                final Topic topic = new Topic();
                                topic.put("title", String.valueOf(input).split("\\r?\\n")[0]);
                                topic.put("description", String.valueOf(input));
                                topic.put("subject", ParseObject.createWithoutData("Subject", mSubjectObjectId));
                                topic.put("createdBy", ParseUser.getCurrentUser());
                                topic.saveEventually(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            loadFromParse(mSubjectObjectId);
                                        } else {
                                            Log.d("COUNT OF ADAPTER", "EROR?");
                                        }
                                    }
                                });
                                mAdapter.loadObjects();
                                mAdapter.notifyDataSetChanged();
                            }
                        }).show();

            }
        };

        mCreateSubjectButton.setOnClickListener(createSubjectButtonListener);
        emptyCreateTopicButton.setOnClickListener(createSubjectButtonListener);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject topic = mAdapter.getItem(position);
                PostFragment postFragment = PostFragment.newInstance(topic.getObjectId(), topic.getString("title"));
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
        public void onFragmentInteraction(String id);
    }
    private void loadFromParse(final String subjectObjectId) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        Topic.getQuery()
                .include("createdBy")
                .whereEqualTo("subject", ParseObject.createWithoutData("Subject", subjectObjectId))
        .findInBackground(new FindCallback<Topic>() {
            @Override
            public void done(final List<Topic> topics, com.parse.ParseException e) {
                if (e == null) {
                    ParseObject.unpinAllInBackground("Topic Adapter" + subjectObjectId, new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("Topic Adapter" + subjectObjectId, topics,
                                        new SaveCallback() {
                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                mAdapter.loadObjects();
                                                mSwipeRefreshLayout.setRefreshing(false);
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
                    Log.e("Topic Adapter" + subjectObjectId, "DELETE FAILED");
                }

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_subject_users, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_user) {
            return true;
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
