package com.underoneroof.mmuboard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class AnalyticsFragment extends android.support.v4.app.Fragment implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView mTotal_user;
    private TextView mTotal_post;
    private TextView mTitle;
    private BarChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private Typeface mTf;



    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AnalyticsFragment newInstance(int sectionNumber) {
        AnalyticsFragment fragment = new AnalyticsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public AnalyticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analytics, container, false);
        mTotal_user = (TextView) rootView.findViewById(R.id.total_user);
        mTotal_post = (TextView) rootView.findViewById(R.id.total_post);
        mTitle = (TextView) rootView.findViewById(R.id.title);


        mSeekBarX = (SeekBar) rootView.findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) rootView.findViewById(R.id.seekBar2);

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);

        //mChart.setDrawBarShadow(false);
        //mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        //mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        //ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, true);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, true);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        setPostData();




        Button btnLogin= (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginData();
                mTitle.setText("User Login Analytics");
            }
        });

        Button btnPost = (Button) rootView.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPostData();
                mTitle.setText("Post Analytics");
            }
        });


        mSeekBarY.setOnSeekBarChangeListener(this);
        mSeekBarX.setOnSeekBarChangeListener(this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    mTotal_user.setText(Integer.toString(count));
                } else {
                    mTotal_user.setText("Error");
                }
            }
        });

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Post");
        query1.countInBackground(new CountCallback() {
            public void done(int count, ParseException e) {
                if (e == null) {
                    mTotal_post.setText(Integer.toString(count));
                } else {
                    mTotal_post.setText("Error");
                }
            }
        });



        return rootView;
    }

    private void setPostData() {
        final int [] counter = new int[12];
        ParseQuery.getQuery("Post").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject object : objects) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(object.getCreatedAt());
                    int month = cal.get(Calendar.MONTH);
                    counter[month] = counter[month] + 1;
                }
                ArrayList<String> xVals = new ArrayList<String>();
                xVals.addAll(Arrays.asList(mMonths).subList(0, 12));

                ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
                for(int i = 0 ; i < 12; i++ ) {
                    yVals2.add(new BarEntry(counter[i], i));
                }

                BarDataSet set1 = new BarDataSet(yVals2, "DataSet");
                set1.setBarSpacePercent(35f);

                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(xVals, dataSets);
                // data.setValueFormatter(new MyValueFormatter());
                data.setValueTextSize(10);
                data.setValueTypeface(mTf);

                mChart.setData(data);
                mChart.invalidate();
            }
        });
    }
    private void setLoginData() {
        ParseQuery.getQuery("Logintrack").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                final int[] counter = new int[12];
                for (ParseObject object : objects) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(object.getCreatedAt());
                    int month = cal.get(Calendar.MONTH);
                    counter[month] = counter[month] + 1;
                }
                ArrayList<String> xVals = new ArrayList<String>();
                xVals.addAll(Arrays.asList(mMonths).subList(0, 12));

                ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
                for (int i = 0; i < 12; i++) {
                    yVals2.add(new BarEntry(counter[i], i));
                }

                BarDataSet set1 = new BarDataSet(yVals2, "DataSet");
                set1.setBarSpacePercent(35f);

                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(xVals, dataSets);
                // data.setValueFormatter(new MyValueFormatter());
                data.setValueTextSize(10);
                data.setValueTypeface(mTf);

                mChart.setData(data);
                mChart.invalidate();
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}