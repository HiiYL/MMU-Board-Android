package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.R;

import java.util.Date;

//import com.parse.ParseQueryAdapter;

/**
 * Created by Hii on 9/5/15.
 */
public class SubjectAdapter extends ParseQueryAdapter<ParseObject> {

    public SubjectAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("SubjectUser")
                        .include("subject.createdBy")
                        .whereEqualTo("user", ParseUser.getCurrentUser())
                        .orderByDescending("status")
                        .fromLocalDatastore();
            }
        });
    }
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_subject, null);
        }

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
//        super.getItemView(object, v, parent);

        // Do additional configuration before returning the View.
        TextView descriptionView = (TextView) v.findViewById(R.id.description_text);
        TextView titleView = (TextView) v.findViewById(R.id.info_text);
        TextView usernameView = (TextView) v.findViewById(R.id.username);
        TextView accessView = (TextView) v.findViewById(R.id.access_status);
        final TextView userCountView = (TextView) v.findViewById(R.id.user_count);
        final TextView topicCountView = (TextView) v.findViewById(R.id.topic_count);
        TextView dotView = (TextView) v.findViewById(R.id.dot);
        dotView.setText(Html.fromHtml(" \u25CF "));
        ParseObject subject =  object.getParseObject("subject");
        titleView.setText(subject.getString("title"));
        descriptionView.setText(subject.getString("description"));
        usernameView.setText(subject.getParseUser("createdBy").getString("name"));
        long topic_count = subject.getLong("topic_count");
        long user_count = subject.getLong("user_count");
        topicCountView.setText( topic_count + (topic_count > 1 ? " Topics " : " Topic"));
        userCountView.setText(user_count + (user_count > 1 ? " Users " : " User "));
        switch(object.getInt("status")) {
            case 1:
                accessView.setText("PENDING");
                break;
            case 2:
                accessView.setText("MEMBER");
                break;
            case 3:
                accessView.setText("ADMIN");
                break;
            default:
                accessView.setText("UNKNOWN");
                break;
        }
        return v;
    }

}
