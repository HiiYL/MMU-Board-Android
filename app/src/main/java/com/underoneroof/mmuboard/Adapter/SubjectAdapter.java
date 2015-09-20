package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.underoneroof.mmuboard.R;

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
        ViewHolder holder = null;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_subject, null);
            holder = new ViewHolder();
            holder.descriptionView = (TextView) v.findViewById(R.id.description_text);
            holder.titleView = (TextView) v.findViewById(R.id.info_text);
            holder.usernameView = (TextView) v.findViewById(R.id.username);
            holder.accessView = (TextView) v.findViewById(R.id.access_status);
            holder.userCountView = (TextView) v.findViewById(R.id.user_count);
            holder.topicCountView = (TextView) v.findViewById(R.id.topic_count);
            holder.dotView = (TextView) v.findViewById(R.id.dot);
            v.setTag(holder);
        }else {
            holder = (ViewHolder) v.getTag ();
        }




        holder.dotView.setText(Html.fromHtml(" \u25CF "));
        ParseObject subject =  object.getParseObject("subject");
        holder.titleView.setText(subject.getString("title"));
        holder.descriptionView.setText(subject.getString("description"));
        holder.usernameView.setText(subject.getParseUser("createdBy").getString("name"));
        long topic_count = subject.getLong("topic_count");
        long user_count = subject.getLong("user_count");
        holder.topicCountView.setText( topic_count + (topic_count > 1 ? " Topics " : " Topic"));
        holder.userCountView.setText(user_count + (user_count > 1 ? " Users " : " User "));
        switch(object.getInt("status")) {
            case 1:
                holder.accessView.setText("PENDING");
                break;
            case 2:
                holder.accessView.setText("MEMBER");
                break;
            case 3:
                holder.accessView.setText("ADMIN");
                break;
            default:
                holder.accessView.setText("UNKNOWN");
                break;
        }
        return v;
    }
    static class ViewHolder {
        TextView descriptionView;
        TextView titleView;
        TextView usernameView;
        TextView accessView;
        TextView userCountView;
        TextView topicCountView;
        TextView dotView;
    }


}
