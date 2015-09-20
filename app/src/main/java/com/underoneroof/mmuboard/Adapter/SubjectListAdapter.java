package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.R;

/**
 * Created by Hii on 10/09/2015.
 */
public class SubjectListAdapter extends ParseQueryAdapter<ParseObject> {
    public SubjectListAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("Subject").fromLocalDatastore();
            }
        });
    }
    @Override
    public View getItemView(ParseObject subject, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_subject_list, null);
        }


        TextView subjectView = (TextView) v.findViewById(R.id.subject_name);
        final TextView accessBtn = (TextView) v.findViewById(R.id.access_status);
        final TextView userCountView = (TextView) v.findViewById(R.id.user_count);
        final TextView topicCountView = (TextView) v.findViewById(R.id.topic_count);
        TextView dotView = (TextView) v.findViewById(R.id.dot);
        dotView.setText(Html.fromHtml(" \u25CF "));
        long topic_count = subject.getLong("topic_count");
        long user_count = subject.getLong("user_count");
        topicCountView.setText( topic_count + (topic_count > 1 ? " Topics " : " Topic"));
        userCountView.setText(user_count + (user_count > 1 ? " Users " : " User "));
        subjectView.setText(subject.getString("title"));
        ParseQuery.getQuery("SubjectUser")
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .whereEqualTo("subject", Subject.createWithoutData("Subject",subject.getObjectId()))
                .fromLocalDatastore()
                .getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            switch (object.getInt("status")) {
                                case 0:
                                    accessBtn.setText("UH-OH SOMETHING WENT WRONG");
                                    break;
                                case 1:
                                    accessBtn.setText("PENDING");
                                    break;
                                case 2:
                                    accessBtn.setText("MEMBER");
                                    break;
                                case 3:
                                    accessBtn.setText("ADMIN");
                                    break;
                                default:
                                    accessBtn.setText(object.getInt("status"));
                                    break;
                            }
                        } else {
                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                accessBtn.setText("NOT A MEMBER");
                            } else {
                                accessBtn.setText("SOMETHING WENT WRONG");
                            }
                        }
                    }
                });

        return v;
    }
}
