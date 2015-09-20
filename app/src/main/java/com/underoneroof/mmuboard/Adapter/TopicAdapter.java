package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.underoneroof.mmuboard.R;

import org.ocpsoft.prettytime.PrettyTime;

/**
 * Created by Hii on 09/09/2015.
 *
 */
public class TopicAdapter extends ParseQueryAdapter<ParseObject> {
    PrettyTime p = new PrettyTime();
    public TopicAdapter(Context context, final String subjectObjectId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("Topic")
                        .include("createdBy")
                        .whereEqualTo("subject", ParseObject.createWithoutData("Subject", subjectObjectId))
                        .orderByDescending("createdAt")
                        .fromLocalDatastore();
            }
        });
    }
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_topic, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) v.findViewById(R.id.info_text);
            holder.usernameView = (TextView) v.findViewById(R.id.username);
            holder.postCountView = (TextView) v.findViewById(R.id.post_count);
            v.setTag(holder);
        }else {
            holder = (ViewHolder) v.getTag ();
        }
        long post_count = object.getLong("post_count");
        holder.postCountView.setText(post_count + (post_count > 1 ? " Posts " : " Post ") +
                Html.fromHtml(" \u25CF ") + p.format(object.getCreatedAt()) );

        holder.titleView.setText(object.getString("title"));
        holder.usernameView.setText(object.getParseUser("createdBy").getString("name"));
        return v;
    }
    static class ViewHolder {
        TextView titleView ;
        TextView usernameView;
        TextView postCountView;

    }
}
