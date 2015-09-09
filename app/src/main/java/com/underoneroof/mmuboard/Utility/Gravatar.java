package com.underoneroof.mmuboard.Utility;

/**
 * Created by Hii on 09/09/2015.
 */
public class Gravatar {

    public static String gravatarUrl(String email) {
        return "http://www.gravatar.com/avatar/" + MD5.md5(email);
    }
}