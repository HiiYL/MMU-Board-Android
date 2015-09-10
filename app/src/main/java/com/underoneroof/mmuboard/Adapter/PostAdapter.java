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

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
//        super.getItemView(object, v, parent);

        // Do additional configuration before returning the View.
        TextView contentsView = (TextView) v.findViewById(R.id.contents);
//        TextView titleView = (TextView) v.findViewById(R.id.info_text);
        TextView usernameView = (TextView) v.findViewById(R.id.username);
        CircleImageView profileView = (CircleImageView) v.findViewById(R.id.profile_image);
//        titleView.setText(object.getString("title"));
        contentsView.setText(object.getString("contents"));
        usernameView.setText(object.getParseUser("createdBy").getUsername());
        Picasso.with(parent.getContext())
                    .load(Gravatar.gravatarUrl(object.getParseUser("createdBy").getEmail()))
                    .into(profileView);
        return v;
    }
//    private LayoutInflater myInflater;
//    private List<ParseObject> posts;
//
//    public PostAdapter(Context context) {
//        myInflater = LayoutInflater.from(context);
//
//    }
//
//    public void setData(List<ParseObject> list) {
//        this.posts = list;
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return posts.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return posts.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    public String getObjectId(int position) {
//        return posts.get(position).getObjectId();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//
//        if (convertView == null) {
//
//            convertView = myInflater.inflate(R.layout.listitem_post, parent, false);
//            holder = new ViewHolder();
////        holder.title   = (TextView) convertView.findViewById(R.id.info_text);
//            holder.contents = (TextView) convertView.findViewById(R.id.description_text);
//            holder.username = (TextView) convertView.findViewById(R.id.username);
//            holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
//
//            convertView.setTag(holder);
//        }else {
//            holder = (ViewHolder) convertView.getTag();
//
//        }
//
//
////        holder.title.setText(posts.get(position).getTitle());
//
//        holder.contents.setText(posts.get(position).getString("contents"));
//        if(posts.get(position).getParseUser("createdBy") != null) {
//            holder.username.setText(posts.get(position).getParseUser("createdBy").getUsername());
//            Picasso.with(parent.getContext())
//                    .load(Gravatar.gravatarUrl(posts.get(position).getParseUser("createdBy").getEmail()))
//                    .into(holder.profile_image);
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
////        TextView title;
//        TextView contents ;
//        TextView username;
//        CircleImageView profile_image;
//    }
}
