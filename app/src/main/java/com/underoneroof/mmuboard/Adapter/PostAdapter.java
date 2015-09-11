package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.R;
import com.underoneroof.mmuboard.Utility.Gravatar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hii on 09/09/2015.
 */
public class PostAdapter extends ParseQueryAdapter<ParseObject> {
    public PostAdapter(Context context, final String topicObjectId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("Post")
                        .fromLocalDatastore()
                        .orderByDescending("createdAt")
                        .include("createdBy")
                                .whereEqualTo("topic", ParseObject.createWithoutData("Topic", topicObjectId));
            }
    });
    }
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_post, null);
        }
        TextView contentsView = (TextView) v.findViewById(R.id.contents);
        TextView usernameView = (TextView) v.findViewById(R.id.username);
        CircleImageView profileView = (CircleImageView) v.findViewById(R.id.profile_image);
        contentsView.setText(object.getString("contents"));
        usernameView.setText(object.getParseUser("createdBy").getString("name"));
        Picasso.with(parent.getContext())
                    .load(Gravatar.gravatarUrl(object.getParseUser("createdBy").getEmail()))
                    .into(profileView);
        return v;
    }
}
