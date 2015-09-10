package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.SubjectUser;
import com.underoneroof.mmuboard.R;
import com.underoneroof.mmuboard.Utility.Gravatar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hii on 10/09/2015.
 */
public class SubjectListAdapter extends ParseQueryAdapter<ParseObject> {
    public SubjectListAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("Subject");
            }
        });
    }
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_subject_list, null);
        }

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
//        super.getItemView(object, v, parent);

        // Do additional configuration before returning the View.
        TextView subjectView = (TextView) v.findViewById(R.id.subject_name);
        final Button accessBtn = (Button) v.findViewById(R.id.access_status);
//        titleView.setText(object.getString("title"));
        subjectView.setText(object.getString("title"));
        ParseQuery.getQuery("SubjectUser")
                .whereEqualTo("user", ParseUser.getCurrentUser())
                .whereEqualTo("subject", Subject.createWithoutData("Subject",object.getObjectId()))
                .fromLocalDatastore()
                .orderByDescending("status")
                .getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            if (object != null) {
                                switch (object.getInt("status")) {
                                    case 0:
                                        accessBtn.setText("NOT A MEMBER");
                                        break;
                                    case 1:
                                        accessBtn.setText("PENDING");
                                        break;
                                    case 2:
                                        accessBtn.setText("MEMBER");
                                        break;
                                    case 3:
                                        accessBtn.setText("ADMIN");
                                        break;
                                    default:
                                        accessBtn.setText("UNKNOWN");
                                        break;
                                }
                            }
                        }
                    }
                });

        return v;
    }
}
