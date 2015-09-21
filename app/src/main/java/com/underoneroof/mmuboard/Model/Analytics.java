package com.underoneroof.mmuboard.Model;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by Jack on 9/17/15.
 */
public class Analytics{
   //public static ArrayList<Integer> yVals1 = new ArrayList<Integer>();
   public static ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
    public static ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
    public Object getdata() {
        return yVals2;
    }


//    @Override
//    protected Integer doInBackground(Integer... params) {
//        int i;
//        for (i = 0; i < 12; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            calendar.set(Calendar.MONTH, i);
//            Date date = calendar.getTime();
//
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.set(Calendar.DAY_OF_MONTH, 1);
//            calendar2.set(Calendar.MONTH, i+1);
//            Date date2 = calendar2.getTime();
//
//            //Log.d("date", String.valueOf(date));
//
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
//            query.whereLessThanOrEqualTo("createdAt", date2);
//            query.whereGreaterThanOrEqualTo("createdAt", date);
//
//
//            try {
//                query.count();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            List<ParseObject> results = null;
//            try {
//                results = query.find();
//                yVals2.add(new BarEntry(results.size(), i));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            //Log.d("object: ", String.valueOf(results.size()));
//
//            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Logintrack");
//            query2.whereLessThanOrEqualTo("createdAt", date2);
//            query2.whereGreaterThanOrEqualTo("createdAt", date);
//
//
//            try {
//                query2.count();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            List<ParseObject> results2 = null;
//            try {
//                results2 = query2.find();
//                yVals3.add(new BarEntry(results2.size(), i));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Log.d("object: ", String.valueOf(results2.size()));
//        }
//
//
//
//        return i;
//    }

}
