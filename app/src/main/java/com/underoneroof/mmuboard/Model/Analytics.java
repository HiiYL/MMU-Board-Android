package com.underoneroof.mmuboard.Model;

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

/**
 * Created by Jack on 9/17/15.
 */
public class Analytics {
   public static ArrayList<Integer> yVals1 = new ArrayList<Integer>();

    public Object getdata() {
        return yVals1;
    }

    public static void setMonthlyData() {

        for (int i = 0; i < 12; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, i);
            Date date = calendar.getTime();


            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.whereGreaterThanOrEqualTo("createdAt", date);
            final int finalI = i;

            query.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (e == null) {
                       yVals1.add(count); //add value to first bar to last bar
                    }
                }
            });
       }
    }

}
