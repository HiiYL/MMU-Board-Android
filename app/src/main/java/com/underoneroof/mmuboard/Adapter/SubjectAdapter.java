package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
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
                        .include("subject")
                        .include("user")
                                .whereEqualTo("user", ParseUser.getCurrentUser())
                        .orderByDescending("status")
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
        titleView.setText(object.getParseObject("subject").getString("title"));
        descriptionView.setText(object.getParseObject("subject").getString("description"));
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
        return v;
    }

}
