package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.Model.User;
import com.underoneroof.mmuboard.R;
import com.underoneroof.mmuboard.Utility.Gravatar;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hii on 09/09/2015.
 *
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
        ViewHolder holder = null;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_post, null);
            holder = new ViewHolder();
            holder.contentsView = (TextView) v.findViewById(R.id.contents);
            holder.usernameView = (TextView) v.findViewById(R.id.username);
            holder.likeCountView = (TextView) v.findViewById(R.id.like_count);
            holder.timestamp = (TextView) v.findViewById(R.id.timestamp);
            holder.imageView = (ImageView) v.findViewById(R.id.post_image);
            holder.likeButton = (Button) v.findViewById(R.id.like_btn);
            v.setTag(holder);
        }else {
            holder = (ViewHolder) v.getTag ();
        }

        PrettyTime p = new PrettyTime();
        holder.timestamp.setText(p.format(object.getCreatedAt()));
        final ArrayList<ParseObject> users;
        if(object.get("likedBy") == null) {
            users = new ArrayList<ParseObject>();
        }else {
            users = (ArrayList<ParseObject>) object.get("likedBy");
        }
        final boolean contains_user = (users != null && users.contains(ParseUser.getCurrentUser()));
        if(contains_user) {
            holder.likeButton.setText("UNLIKE");
        }else {
            holder.likeButton.setText("LIKE");
        }
        final ViewHolder finalHolder = holder;
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.likeButton.setEnabled(false);
                if (contains_user) {
                    users.remove(ParseUser.getCurrentUser());
                } else {
                    users.add(ParseUser.getCurrentUser());
                }
                object.put("likedBy", users);
                object.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        finalHolder.likeButton.setEnabled(true);
                    }
                });
                loadObjects();
            }
        });
        long like_count = users.size();
        if(like_count > 0) {
            holder.likeCountView.setVisibility(View.VISIBLE);
        }else {
            holder.likeCountView.setVisibility(View.GONE);
        }
        holder.likeCountView.setText(like_count > 1 ?
                like_count + " users like this post" :
                like_count + " user likes this post");
        ParseFile postImage = object.getParseFile("image");
        if(postImage != null) {
            Uri imageUri = Uri.parse(postImage.getUrl());
            Picasso.with(getContext()).load(imageUri.toString())
                    .placeholder( R.drawable.progress_animation ).fit().centerCrop().into(holder.imageView);
        }



        CircleImageView profileView = (CircleImageView) v.findViewById(R.id.profile_image);
        holder.contentsView.setText(object.getString("contents"));
        holder.usernameView.setText(object.getParseUser("createdBy").getString("name"));
        Picasso.with(parent.getContext())
                    .load(Gravatar.gravatarUrl(object.getParseUser("createdBy")
                            .getEmail())).placeholder( R.drawable.progress_animation )
                    .fit().into(profileView);
        return v;
    }
    static class ViewHolder{
        TextView contentsView;
        TextView usernameView;
        TextView likeCountView;
        TextView timestamp;
        ImageView imageView;
        Button likeButton;
    }
}
