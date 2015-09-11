package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.R;

/**
 * Created by Hii on 09/09/2015.
 */
public class TopicAdapter extends ParseQueryAdapter<ParseObject> {
    public TopicAdapter(Context context, final String subjectObjectId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("Topic")
                        .include("createdBy")
                        .whereEqualTo("subject", ParseObject.createWithoutData("Subject", subjectObjectId))
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

        ParseQuery.getQuery("Post")
                .fromLocalDatastore()
                .whereEqualTo("topic", Topic.createWithoutData("Topic", object.getObjectId()))
                .fromLocalDatastore()
                .countInBackground(new CountCallback() {
                    @Override
                    public void done(int count, ParseException e) {
                        topicCountView.setText(count + (count > 1 ? " Posts " : " Post "));
                    }
                });
        titleView.setText(object.getString("title"));
        descriptionView.setText(object.getString("description"));
        usernameView.setText(object.getParseUser("createdBy").getString("name"));
        return v;
    }
}
