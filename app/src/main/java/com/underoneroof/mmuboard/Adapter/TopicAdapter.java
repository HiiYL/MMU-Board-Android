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
                        .fromLocalDatastore()
                        .include("createdBy")
                        .whereEqualTo("subject", ParseObject.createWithoutData("Subject", subjectObjectId));
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
        usernameView.setText(object.getParseUser("createdBy").getUsername());
        return v;
    }

//    private LayoutInflater myInflater;
//    private List<ParseObject> topics;
//
//    public TopicAdapter(Context context) {
//        myInflater = LayoutInflater.from(context);
//
//    }
//
//    public void setData(List<ParseObject> list) {
//        this.topics = list;
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return topics.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return topics.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    public String getObjectId(int position) {
//        return topics.get(position).getObjectId();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//
//        if(convertView == null ) {
//
//            convertView = myInflater.inflate(R.layout.listitem_subject, parent, false);
//            holder = new ViewHolder();
//            holder.title = (TextView) convertView.findViewById(R.id.info_text);
//            holder.description = (TextView) convertView.findViewById(R.id.description_text);
//            holder.username = (TextView) convertView.findViewById(R.id.username);
//
//            convertView.setTag(holder);
//        }else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.title.setText(topics.get(position).getString("title"));
//        holder.description.setText(topics.get(position).getString("description"));
//        if(topics.get(position).getParseUser("createdBy") != null) {
//            holder.username.setText(topics.get(position).getParseUser("createdBy").getUsername());
//        }else {
//            Log.d("USERNAME", "IS NULL");
//        }
//
//
//
//        return convertView;
//    }
//
//    static class ViewHolder {
//        TextView title;
//        TextView description ;
//        TextView username;
//    }
}
