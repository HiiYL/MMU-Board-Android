package com.underoneroof.mmuboard;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.underoneroof.mmuboard.Adapter.SubjectAdapter;
import com.underoneroof.mmuboard.Model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    TextView tv;
//    private RecyclerView mRecyclerView;
//    private LinearLayoutManager mLayoutManager;
//    private List<Subject> subjectNames;
//    private MyAdapter mAdapter;
    private ListView mListView;
    private SubjectAdapter mSubjectAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) rootView.findViewById(R.id.subject_listview);
        mSubjectAdapter = new SubjectAdapter(getActivity());
        mSubjectAdapter.setData(Subject.listAll(Subject.class));
        mListView.setAdapter(mSubjectAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEST", String.valueOf(position) + " - " + String.valueOf(id));
            }
        });
//        ListView lv = (ListView) rootView.findViewById(R.id.subjects);
        tv = (TextView) rootView.findViewById(R.id.subject_count);
        long number_of_subjects = Subject.count(Subject.class, null, null);
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new LandingAnimator());
//        subjectNames = Subject.listAll(Subject.class);
//        mAdapter = new MyAdapter(subjectNames, getActivity());
//        mRecyclerView.setAdapter(mAdapter);
        String url = "https://mmuboard.herokuapp.com/subjects.json";
//        Subject.deleteAll(Subject.class);

//        JsonArrayRequest jsObjRequest = new JsonArrayRequest
//                (Request.Method.GET, url, (String)null, new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("RECEIVED RESPONSE", "success");
//                        for ( int i = 0 ; i < response.length(); i++) {
//                            try {
//                                JSONObject subjectObject = response.getJSONObject(i);
//                                String name = subjectObject.getString("name");
//                                String description = subjectObject.getString("description");
//                                Long id = subjectObject.getLong("id");
//                                Subject subject = new Subject(id, name,description);
//                                subject.save();
//                                mSubjectAdapter.notifyDataSetChanged();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        tv.setText("Response: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("RECEIVED RESPONSE", error.toString());
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        tv.setText(Long.toString(number_of_subjects));
//        MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
        return rootView;

    }
}
