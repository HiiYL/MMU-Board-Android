package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.R;

import java.util.List;

/**
 * Created by Hii on 09/09/2015.
 */
public class TopicAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<ParseObject> topics;

    public TopicAdapter(Context context) {
        myInflater = LayoutInflater.from(context);

    }

    public void setData(List<ParseObject> list) {
        this.topics = list;


    }

    @Override
    public int getCount() {
        return topics.size();
    }

    @Override
    public Object getItem(int position) {
        return topics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getObjectId(int position) {
        return topics.get(position).getObjectId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null ) {

            convertView = myInflater.inflate(R.layout.listitem_subject, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.info_text);
            holder.description = (TextView) convertView.findViewById(R.id.description_text);
            holder.username = (TextView) convertView.findViewById(R.id.username);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(topics.get(position).getString("title"));
        holder.description.setText(topics.get(position).getString("description"));
        if(topics.get(position).getParseUser("createdBy") != null) {
            holder.username.setText(topics.get(position).getParseUser("createdBy").getUsername());
        }else {
            Log.d("USERNAME", "IS NULL");
        }



        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView description ;
        TextView username;
    }
}
