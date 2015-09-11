package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.R;
import com.underoneroof.mmuboard.Utility.Gravatar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hii on 10/09/2015.
 */
public class SubjectUsersAdapter extends ParseQueryAdapter<ParseObject>{
    public SubjectUsersAdapter(Context context, final String subjectObjectId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("SubjectUser")
                        .include("subject")
                        .include("user")
                        .whereEqualTo("subject", ParseObject.createWithoutData("Subject", subjectObjectId));
//                        .fromLocalDatastore();
            }
        });
    }
    public String getSubjectObjectId(int position) {
        return getItem(position).getObjectId();
    }
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_subject_users, null);
        }

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
//        super.getItemView(object, v, parent);

        // Do additional configuration before returning the View.
        TextView usernameView = (TextView) v.findViewById(R.id.username);
        TextView accessView = (TextView) v.findViewById(R.id.access_status);
        usernameView.setText(object.getParseUser("user").getUsername());
        switch(object.getInt("status")) {
            case 0:
                accessView.setText("PENDING");
                break;
            case 1:
                accessView.setText("MEMBER");
                break;
            case 2:
                accessView.setText("ADMIN");
                break;
            default:
                accessView.setText("UNKNOWN");
                break;
        }
        CircleImageView profileView = (CircleImageView) v.findViewById(R.id.profile_image);
//        titleView.setText(object.getString("title"));
        Picasso.with(parent.getContext())
                .load(Gravatar.gravatarUrl(object.getParseUser("user").getEmail()))
                .into(profileView);
        return v;
    }
}
