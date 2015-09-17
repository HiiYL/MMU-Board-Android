package com.underoneroof.mmuboard.Model;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jack on 9/17/15.
 */
public class Analytics extends AsyncTask<Integer,Integer,Integer> {
   //public static ArrayList<Integer> yVals1 = new ArrayList<Integer>();
   public static ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
    public static int i;
    public Object getdata() {
        return yVals2;
    }


    @Override
    protected Integer doInBackground(Integer... params) {
        for ( i = 0; i < 12; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, i);
            Date date = calendar.getTime();

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.DAY_OF_MONTH, 1);
            calendar2.set(Calendar.MONTH, i+1);
            Date date2 = calendar2.getTime();

            Log.d("date", String.valueOf(date));

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.whereLessThanOrEqualTo("createdAt", date2);
            query.whereGreaterThanOrEqualTo("createdAt", date);


            try {
                query.count();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<ParseObject> results = null;
            try {
                results = query.find();
                yVals2.add(new BarEntry(results.size(), i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("object: ", String.valueOf(results.size()));
        }
        return i;
    }

    @Override
    protected void onPostExecute(Integer result) {
        // execution of result of Long time consuming operation
        Log.d("hello ", String.valueOf(result));
        i=result;
    }
    @Override
    protected void onProgressUpdate(Integer... result) {
        i=result[0];
    }
}
