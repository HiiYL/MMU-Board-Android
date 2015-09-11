package com.underoneroof.mmuboard;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.PostAdapter;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Model.Topic;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mTopicObjectId;
    private String mParam2;
    private long mTopicIndex;
    private PostAdapter mAdapter;
    private FloatingActionButton mCreateSubjectButton;
    private List<ParseObject> posts;
    private ListView mListView;
    private ParseObject mTopic;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static PostFragment newInstance(String topic_index) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("index", topic_index);
        fragment.setArguments(args);
        return fragment;
    }

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTopicObjectId = getArguments().getString("index");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mAdapter = new PostAdapter(getActivity(), mTopicObjectId);
        mListView.setAdapter(mAdapter);
        mCreateSubjectButton = (FloatingActionButton) view.findViewById(R.id.create_topic_btn);
        mCreateSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("Create A Post")
                        .content("Contents")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .input("Enter the contents of your post", null , new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                final Post post = new Post();
                                post.put("title", String.valueOf(input).split("\\r?\\n")[0]);
                                post.put("contents", String.valueOf(input));
                                post.put("topic", ParseObject.createWithoutData("Topic", mTopicObjectId));
                                post.put("createdBy", ParseUser.getCurrentUser());
                                post.saveEventually(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParseQuery<ParseObject> topic_query = ParseQuery.getQuery("Post");
                                        topic_query.whereEqualTo("topic", mTopic);
                                        topic_query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> list, ParseException e) {
                                                ParseObject.pinAllInBackground(list);
                                                if (e == null) {
                                                    loadFromParse();
                                                } else {
                                                    Log.d("subject", "Error: " + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                });
                                mAdapter.loadObjects();
                                mAdapter.notifyDataSetChanged();
                            }
                        }).show();

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    private void loadFromParse() {
        ParseQuery<Post> query = Post.getQuery();
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, com.parse.ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(posts,
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

}
