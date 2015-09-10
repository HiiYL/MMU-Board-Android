package com.underoneroof.mmuboard.Model;

/**
 * Created by Hii on 09/09/2015.
 */
//public class Session {
//    static User user;
//    public static boolean userLoggedIn(Context context) {
//        return null != getUser(context);
//    }
//    public static User getUser(Context context) {
//        if(user == null) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            if(prefs.contains("user_id")) {
//                user = User.findById(User.class, prefs.getLong("user_id", 0));
//            }
//        }
//        return user;
//    }
//    public static void logOut(Context context) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.remove("user_id");
//        editor.apply();
//    }
//}
