package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.R;
import com.underoneroof.mmuboard.Utility.Gravatar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hii on 09/09/2015.
 */
public class PostAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<ParseObject> posts;

    public PostAdapter(Context context) {
        myInflater = LayoutInflater.from(context);

    }

    public void setData(List<ParseObject> list) {
        this.posts = list;


    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getObjectId(int position) {
        return posts.get(position).getObjectId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = myInflater.inflate(R.layout.listitem_post, parent, false);
            holder = new ViewHolder();
//        holder.title   = (TextView) convertView.findViewById(R.id.info_text);
            holder.contents = (TextView) convertView.findViewById(R.id.description_text);
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();

        }


//        holder.title.setText(posts.get(position).getTitle());

        holder.contents.setText(posts.get(position).getString("contents"));
        if(posts.get(position).getParseUser("createdBy") != null) {
            holder.username.setText(posts.get(position).getParseUser("createdBy").getUsername());
            Picasso.with(parent.getContext())
                    .load(Gravatar.gravatarUrl(posts.get(position).getParseUser("createdBy").getEmail()))
                    .into(holder.profile_image);
        }else {
            Log.d("USERNAME", "IS NULL");
        }



        return convertView;
    }

    static class ViewHolder {
//        TextView title;
        TextView contents ;
        TextView username;
        CircleImageView profile_image;
    }
}
