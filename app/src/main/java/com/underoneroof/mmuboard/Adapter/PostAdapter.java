package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.R;
import com.underoneroof.mmuboard.Utility.Gravatar;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hii on 09/09/2015.
 */
public class PostAdapter extends ParseQueryAdapter<ParseObject> {
    public PostAdapter(Context context, final String topicObjectId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("Post")
                        .orderByDescending("createdAt")
                        .include("createdBy")
                        .whereEqualTo("topic", ParseObject.createWithoutData("Topic", topicObjectId))
                        .fromLocalDatastore();
            }
    });
    }
    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_post, null);
        }
        TextView contentsView = (TextView) v.findViewById(R.id.contents);
        TextView usernameView = (TextView) v.findViewById(R.id.username);
        TextView likeCountView = (TextView) v.findViewById(R.id.like_count);
        TextView timestamp = (TextView) v.findViewById(R.id.timestamp);
        ImageView imageView = (ImageView) v.findViewById(R.id.post_image);

        PrettyTime p = new PrettyTime();

        timestamp.setText(p.format(object.getCreatedAt()));
        final Button likeButton = (Button) v.findViewById(R.id.like_btn);
        final ArrayList<ParseObject> users;
        if(object.get("likedBy") == null) {
            users = new ArrayList<ParseObject>();
        }else {
            users = (ArrayList<ParseObject>) object.get("likedBy");
        }
        final boolean contains_user = (users != null && users.contains(ParseUser.getCurrentUser()));
        if(contains_user) {
            likeButton.setText("UNLIKE");
        }else {
            likeButton.setText("LIKE");
        }
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButton.setEnabled(false);
                if (contains_user) {
                    users.remove(ParseUser.getCurrentUser());
                } else {
                    users.add(ParseUser.getCurrentUser());
                }
                object.put("likedBy", users);
                object.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        likeButton.setEnabled(true);
                    }
                });
                loadObjects();
            }
        });
        long like_count = users.size();
        if(like_count > 0) {
            likeCountView.setVisibility(View.VISIBLE);
        }
        likeCountView.setText(like_count > 1 ?
                like_count + " users like this post" :
                like_count + " user likes this post");
        ParseFile postImage = object.getParseFile("image");
        if(postImage != null) {
            Uri imageUri = Uri.parse(postImage.getUrl());
            Log.d("NOT NULL", "NOT NULL" + imageUri);
            Picasso.with(getContext()).load(imageUri.toString()).into(imageView);
        }



        CircleImageView profileView = (CircleImageView) v.findViewById(R.id.profile_image);
        contentsView.setText(object.getString("contents"));
        usernameView.setText(object.getParseUser("createdBy").getString("name"));
        Picasso.with(parent.getContext())
                    .load(Gravatar.gravatarUrl(object.getParseUser("createdBy").getEmail()))
                    .into(profileView);
        return v;
    }
}
