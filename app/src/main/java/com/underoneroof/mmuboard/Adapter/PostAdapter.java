package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    private List<Post> posts;

    public PostAdapter(Context context) {
        myInflater = LayoutInflater.from(context);

    }

    public void setData(List<Post> list) {
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
        return posts.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        convertView     = myInflater.inflate(R.layout.listitem_post, null);
        holder          = new ViewHolder();
//        holder.title   = (TextView) convertView.findViewById(R.id.info_text);
        holder.contents     = (TextView) convertView.findViewById(R.id.description_text);
        holder.username = (TextView) convertView.findViewById(R.id.username);
        holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);

        convertView.setTag(holder);


//        holder.title.setText(posts.get(position).getTitle());

        holder.contents.setText(posts.get(position).getContents());
        if(posts.get(position).getUser() != null) {
            holder.username.setText(posts.get(position).getUser().username);
            Picasso.with(parent.getContext())
                    .load(Gravatar.gravatarUrl(posts.get(position).getUser().email))
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
