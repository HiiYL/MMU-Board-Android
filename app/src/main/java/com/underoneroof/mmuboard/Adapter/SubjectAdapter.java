package com.underoneroof.mmuboard.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Hii on 9/5/15.
 */
public class SubjectAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<Subject> subjects;

    public SubjectAdapter(Context context) {
        myInflater = LayoutInflater.from(context);

    }

    public void setData(List<Subject> list) {
        this.subjects = list;


    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return subjects.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        convertView     = myInflater.inflate(R.layout.listitem_subject, null);
        holder          = new ViewHolder();
        holder.title   = (TextView) convertView.findViewById(R.id.info_text);
        holder.description     = (TextView) convertView.findViewById(R.id.description_text);
        holder.username = (TextView) convertView.findViewById(R.id.username);

        convertView.setTag(holder);


        holder.title.setText(subjects.get(position).getTitle());
        holder.description.setText(subjects.get(position).getDescription());
        if(subjects.get(position).getCreator() != null) {
            holder.username.setText(subjects.get(position).getCreator().username);
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
