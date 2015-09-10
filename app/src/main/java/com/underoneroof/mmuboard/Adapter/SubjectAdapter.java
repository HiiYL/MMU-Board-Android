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

import com.parse.Parse;
import com.parse.ParseObject;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Hii on 9/5/15.
 */
public class SubjectAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<ParseObject> subjects;

    public SubjectAdapter(Context context) {
        myInflater = LayoutInflater.from(context);

    }

    public void setData(List<ParseObject> list) {
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
        return 0;
    }
    public String getObjectId(int position) {
        return subjects.get(position).getObjectId();
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


        holder.title.setText(subjects.get(position).getString("title"));
        holder.description.setText(subjects.get(position).getString("description"));
        if(subjects.get(position).getParseUser("createdBy") != null) {
            holder.username.setText(subjects.get(position).getParseUser("createdBy").getUsername());
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
