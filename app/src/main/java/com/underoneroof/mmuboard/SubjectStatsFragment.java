package com.underoneroof.mmuboard;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.underoneroof.mmuboard.Model.Topic;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectStatsFragment extends android.support.v4.app.Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SUBJECT_OBJECT_ID = "subject_id";
    private static final String ARG_PARAM2 = "param2";
    private String mSubjectObjectId;
    private String mParam2;

    public static SubjectStatsFragment newInstance(String param1) {
        SubjectStatsFragment fragment = new SubjectStatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT_OBJECT_ID, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubjectStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subject_stats, container, false);
        final TextView topicCountView = (TextView) view.findViewById(R.id.topic_count);
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            mSubjectObjectId = getArguments().getString(ARG_SUBJECT_OBJECT_ID);
            Topic.getQuery().fromLocalDatastore()
                    .whereEqualTo("subject", ParseObject.createWithoutData("Subject", mSubjectObjectId))
                    .whereGreaterThan("createdAt", cal.getTime())
                    .countInBackground(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {
                            topicCountView.setText("Number of Topics : " + count);
                        }
                    });
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        return view;
    }
}
