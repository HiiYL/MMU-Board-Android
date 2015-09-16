package com.underoneroof.mmuboard.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Hii on 09/09/2015.
 *
 */
@ParseClassName("User")
public class User extends ParseObject {
    public static ParseQuery<User> getQuery() {
        return ParseQuery.getQuery(User.class);
    }
}
