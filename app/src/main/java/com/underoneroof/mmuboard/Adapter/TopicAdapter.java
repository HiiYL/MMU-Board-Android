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
 *
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
            v = View.inflate(getContext(), R.layout.listitem_topic, null);
        }

        // Do additional configuration before returning the View.
        TextView titleView = (TextView) v.findViewById(R.id.info_text);
        TextView usernameView = (TextView) v.findViewById(R.id.username);
        final TextView postCountView = (TextView) v.findViewById(R.id.post_count);

        long post_count = object.getLong("post_count");

        postCountView.setText(post_count + (post_count > 1 ? " Posts " : " Post "));

        titleView.setText(object.getString("title"));
        usernameView.setText(object.getParseUser("createdBy").getString("name"));
        return v;
    }
}
