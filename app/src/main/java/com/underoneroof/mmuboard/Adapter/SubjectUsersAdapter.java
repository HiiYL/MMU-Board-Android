package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.R;
import com.underoneroof.mmuboard.Utility.Gravatar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hii on 10/09/2015.
 */
public class SubjectUsersAdapter extends ParseQueryAdapter<ParseObject>{
    boolean mSpinnerEnabled = false;
    public SubjectUsersAdapter(Context context, final String subjectObjectId) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                return ParseQuery.getQuery("SubjectUser")
                        .include("subject")
                        .include("user")
                        .whereEqualTo("subject", ParseObject.createWithoutData("Subject", subjectObjectId));
            }
        });
    }
    public void enableSpinner() {
        mSpinnerEnabled = true;
        loadObjects();
    }
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if (v == null) {
            v = View.inflate(getContext(), R.layout.listitem_subject_users, null);
            holder = new ViewHolder();
            holder.usernameView = (TextView) v.findViewById(R.id.username);
            holder.accessView = (Spinner) v.findViewById(R.id.access_status);
            v.setTag(holder);

        }else {
            holder = (ViewHolder) v.getTag ();
        }



        holder.accessView.setSelection(object.getInt("status") - 1 );
        holder.accessView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if((object.getInt("status") - 1) != position) {
                    object.put("status", position + 1);
                    object.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getContext(), "Permissions saved successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(!mSpinnerEnabled || (object.getInt("status") == 3)) {
            holder.accessView.setEnabled(false);
            holder.accessView.setClickable(false);
        }
        holder.usernameView.setText(object.getParseUser("user").getString("name"));
        CircleImageView profileView = (CircleImageView) v.findViewById(R.id.profile_image);
        Picasso.with(parent.getContext())
                .load(Gravatar.gravatarUrl(object.getParseUser("user").getEmail()))
                .fit().into(profileView);
        return v;
    }
    static class ViewHolder {
        TextView usernameView;
        Spinner accessView;

    }
}
