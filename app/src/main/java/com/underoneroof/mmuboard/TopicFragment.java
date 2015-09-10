package com.underoneroof.mmuboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.TopicAdapter;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.Topic;

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
//    private List<Topic> topics;

    // TODO: Rename and change types of parameters
    public static TopicFragment newInstance(String param1, String param2) {
        TopicFragment fragment = new TopicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static TopicFragment newInstance(long index) {
        TopicFragment f = new TopicFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putLong("index", index);
        f.setArguments(args);
        return f;
    }
    public static TopicFragment newInstance(String index) {
        TopicFragment f = new TopicFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("index", index);
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
        }


    }
    @Override
    public void onResume() {
//        loadFromParse();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_topic, container, false);


//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subject");
//        query.whereEqualTo("objectId", mSubjectObjectId);
////        query.fromLocalDatastore();
//        query.getInBackground(mSubjectObjectId, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject parseObject, ParseException e) {
//                if( e == null ) {
//                    mSubject = parseObject;
//                    getActivity().setTitle(mSubject.getString("title"));
//                    ParseQuery<ParseObject> topic_query = ParseQuery.getQuery("Topic");
//                    topic_query.whereEqualTo("subject", mSubject);
//                    topic_query.findInBackground(new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> list, ParseException e) {
//                            if (e == null) {
////                                ParseObject.pinAllInBackground(list);
//                                Log.d("SIZE OF TOPICS", String.valueOf(list.size()));
//                                mAdapter = new TopicAdapter(getActivity());
//                                topics = list;
//                                mAdapter.setData(topics);
//                                mListView.setAdapter(mAdapter);
//
//                            } else {
//                                Log.d("subject", "Error: " + e.getMessage());
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//        ParseQueryAdapter.QueryFactory<Topic> factory = new ParseQueryAdapter.QueryFactory<Topic>() {
//            public ParseQuery<Topic> create() {
//                ParseQuery<Topic> query = Topic.getQuery();
//                query.orderByDescending("createdAt");
//                query.fromLocalDatastore();
//                return query;
//            }
//        };

        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mCreateSubjectButton = (FloatingActionButton) view.findViewById(R.id.create_topic_btn);
        mAdapter = new TopicAdapter(getActivity(), mSubjectObjectId);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
//        topics = Topic.find(Topic.class, "subject = ? ", String.valueOf(mSubjectIndex));
        // TODO: Change Adapter to display your content

        mCreateSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("Create A Topic")
                        .content("Title")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Enter the title of your topic", null , new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
//                                Topic topic = new Topic(String.valueOf(input),
//                                        "Testing",
//                                        Subject.findById(Subject.class, mSubjectIndex),
//                                        Session.getUser(getActivity()),
//                                        Calendar.getInstance().get(Calendar.SECOND),
//                                        Calendar.getInstance().get(Calendar.SECOND));
//                                topic.save();
//                                topics.add(topic);
//                                mAdapter.notifyDataSetChanged();
                                final Topic topic = new Topic();
                                topic.put("title", String.valueOf(input).split("\\r?\\n")[0]);
                                topic.put("description", String.valueOf(input));
                                topic.put("subject", ParseObject.createWithoutData("Subject", mSubjectObjectId));
                                topic.put("createdBy", ParseUser.getCurrentUser());
                                topic.saveEventually(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            loadFromParse();
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
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostFragment postFragment = PostFragment.newInstance(mAdapter.getItem(position).getObjectId());
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
    private void loadFromParse() {
        ParseQuery<Topic> query = Topic.getQuery();
        query.findInBackground(new FindCallback<Topic>() {
            @Override
            public void done(List<Topic> topics, com.parse.ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(topics,
                            new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    mAdapter.loadObjects();
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
