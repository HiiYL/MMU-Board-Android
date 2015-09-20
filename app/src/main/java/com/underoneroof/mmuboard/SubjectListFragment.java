package com.underoneroof.mmuboard;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Adapter.SubjectListAdapter;
import com.underoneroof.mmuboard.Model.Subject;
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
        loadFromParse();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("All Subjects");
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
                                        TopicFragment topicFragment = TopicFragment.newInstance(subject.getObjectId(), subject.getString("title"), object.getInt("status"));
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                                        fragmentTransaction.replace(R.id.frame, topicFragment);
                                        fragmentTransaction.addToBackStack("tag").commit();
                                    } else {
                                        Toast.makeText(getActivity(), "Your request to join is still pending approval.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                        final ParseObject subject = mSubjectListAdapter.getItem(position);
                                        final SubjectUser subjectUser = new SubjectUser();
                                        subjectUser.put("user", ParseUser.getCurrentUser());
                                        subjectUser.put("subject",
                                                ParseObject.createWithoutData("Subject",
                                                        subject.getObjectId()));
                                        if (subject.getBoolean("isPrivate")) {
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
                                                if (subjectUser.getInt("status") != 1) {
//                                                    view.setClickable(true);
                                                    loadFromParse();
                                                    TopicFragment topicFragment = TopicFragment.newInstance(subject.getObjectId(), subject.getString("title"), subjectUser.getInt("status"));
                                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                                                    fragmentTransaction.replace(R.id.frame, topicFragment);
                                                    fragmentTransaction.addToBackStack("tag").commit();

                                                }
                                            }
                                        });
                                        mSubjectListAdapter.loadObjects();
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

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        Activity a;
//
//        if (context instanceof Activity){
//            a=(Activity) context;
//        }
//
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private void loadSubjectUsers() {
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        });
        SubjectUser.getQuery().include("subject.createdBy")
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .findInBackground(new FindCallback<SubjectUser>() {
                    @Override
                    public void done(final List<SubjectUser> objects, com.parse.ParseException e) {
                        if (e == null) {
                            ParseObject.unpinAllInBackground("SubjectAdapter", new DeleteCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        ParseObject.pinAllInBackground("Subject Adapter", objects, new SaveCallback() {
                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                mSubjectListAdapter.loadObjects();
                                                mSubjectListAdapter.notifyDataSetChanged();
//                                                mSwipeRefreshLayout.setRefreshing(false);
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
    private void loadFromParse() {
        Subject.getQuery().findInBackground(new FindCallback<Subject>() {
            @Override
            public void done(final List<Subject> subjects, com.parse.ParseException e) {
                if (e == null) {
                    ParseObject.unpinAllInBackground("SubjectList", new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseObject.pinAllInBackground("SubjectList", subjects,
                                        new SaveCallback() {
                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                if (e == null) {
                                                    loadSubjectUsers();
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
                }else {
                    Log.e("SUBJECTLIST", e.toString());
                }


            }
        });
    }

}
