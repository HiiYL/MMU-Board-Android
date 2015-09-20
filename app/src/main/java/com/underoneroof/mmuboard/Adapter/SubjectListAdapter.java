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
import com.underoneroof.mmuboard.Common.AccessLevel;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.R;

/**
 * Created by Hii on 10/09/2015.
 *
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
        ViewHolder holder = null;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_subject_list, null);
            holder = new ViewHolder();
            holder.subjectView = (TextView) v.findViewById(R.id.subject_name);
            holder.accessBtn = (TextView) v.findViewById(R.id.access_status);
            holder.userCountView = (TextView) v.findViewById(R.id.user_count);
            holder.topicCountView = (TextView) v.findViewById(R.id.topic_count);
            holder.dotView = (TextView) v.findViewById(R.id.dot);
            v.setTag(holder);

        }else {
            holder = (ViewHolder) v.getTag ();
        }
        holder.dotView.setText(Html.fromHtml(" \u25CF "));
        long topic_count = subject.getLong("topic_count");
        long user_count = subject.getLong("user_count");
        holder.topicCountView.setText( topic_count + (topic_count > 1 ? " Topics " : " Topic"));
        holder.userCountView.setText(user_count + (user_count > 1 ? " Users " : " User "));
        holder.subjectView.setText(subject.getString("title"));
        final ViewHolder finalHolder = holder;
        ParseQuery.getQuery("SubjectUser")
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .whereEqualTo("subject", Subject.createWithoutData("Subject",subject.getObjectId()))
                .fromLocalDatastore()
                .getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            finalHolder.accessBtn.setText(AccessLevel.values()[object.getInt("status")].name());
                        } else {
                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                                finalHolder.accessBtn.setText("NOT A MEMBER");
                            } else {
                                finalHolder.accessBtn.setText("SOMETHING WENT WRONG");
                            }
                        }
                    }
                });

        return v;
    }
    static class ViewHolder {
        TextView subjectView;
        TextView accessBtn;
        TextView userCountView;
        TextView topicCountView;
        TextView dotView;
    }
}
