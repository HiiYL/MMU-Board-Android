package com.underoneroof.mmuboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.PostAdapter;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.Utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
public class PostFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mTopicObjectId;
    private String mTopicTitle;
    private String mParam2;
    private long mTopicIndex;
    private PostAdapter mAdapter;
    private FloatingActionButton mCreateSubjectButton;
    private List<ParseObject> posts;
    private ListView mListView;
    private ParseObject mTopic;

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static PostFragment newInstance(String topic_index, String title) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("index", topic_index);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        if(Utility.isConnectedToNetwork(getActivity())) {
            loadFromParse(mTopicObjectId);
        }else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mTopicObjectId = getArguments().getString("index");
            mTopicTitle = getArguments().getString("title");
            mAdapter = new PostAdapter(getActivity(), mTopicObjectId);
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bulletin_activity_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFromParse(mTopicObjectId);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(mTopicTitle);
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);

        mListView.setEmptyView(view.findViewById(R.id.empty));
        mListView.setAdapter(mAdapter);
        mCreateSubjectButton = (FloatingActionButton) view.findViewById(R.id.create_topic_btn);
        Button emptyCreateSubjectButton = (Button) view.findViewById(R.id.empty_create_post_btn);
        View.OnClickListener createSubjectButtonClickListener =  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePostFragment createPostFragment = CreatePostFragment.newInstance(mTopicObjectId);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.frame, createPostFragment);
                fragmentTransaction.addToBackStack("tag").commit();
            }
        };
        mCreateSubjectButton.setOnClickListener(createSubjectButtonClickListener);
        emptyCreateSubjectButton.setOnClickListener(createSubjectButtonClickListener);
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
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post_fragment, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove_topic) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            try {
                                Topic.getQuery().get(mTopicObjectId).deleteEventually(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        getFragmentManager().popBackStack();
                                    }
                                });
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to delete this topic?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    private void loadFromParse(final String topicObjectid) {
        mSwipeRefreshLayout.setRefreshing(true);
        Post.getQuery()
                .include("createdBy")
                .orderByDescending("createdAt")
                .whereEqualTo("topic", ParseObject.createWithoutData("Topic", topicObjectid))
                .findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(final List<Post> posts, com.parse.ParseException e) {
                        if (e == null) {
                            ParseObject.unpinAllInBackground("PostAdapter" + topicObjectid, new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e== null) {
                                        ParseObject.pinAllInBackground("PostAdapter"+ topicObjectid,posts,
                                                new SaveCallback() {
                                                    @Override
                                                    public void done(com.parse.ParseException e) {
                                                        mAdapter.loadObjects();
                                                        mSwipeRefreshLayout.setRefreshing(false);
                                                    }
                                                });
                                    }else {
                                        Log.e("Post Adapter" + topicObjectid, "DELETION FAILED");
                                    }
                                }
                            });
                        } else {
                            Log.i("Post Adapter" + topicObjectid,
                                    "loadFromParse: Error finding pinned todos: "
                                            + e.getMessage());
                        }
                    }
                });
    }


}
