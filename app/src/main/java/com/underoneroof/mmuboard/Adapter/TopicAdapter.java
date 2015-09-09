package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.R;

import java.util.List;

/**
 * Created by Hii on 09/09/2015.
 */
public class TopicAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<Topic> topics;

    public TopicAdapter(Context context) {
        myInflater = LayoutInflater.from(context);

    }

    public void setData(List<Topic> list) {
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
        return topics.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        convertView     = myInflater.inflate(R.layout.listitem_subject, null);
        holder          = new ViewHolder();
        holder.title   = (TextView) convertView.findViewById(R.id.info_text);
        holder.description     = (TextView) convertView.findViewById(R.id.description_text);

        convertView.setTag(holder);


        holder.title.setText(topics.get(position).getTitle());
        holder.description.setText(topics.get(position).getDescription());



        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView description ;
    }
}
