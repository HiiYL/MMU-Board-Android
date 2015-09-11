package com.underoneroof.mmuboard.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Hii on 09/09/2015.
 */
@ParseClassName("SubjectUser")
public class SubjectUser extends ParseObject {
    public User user;
    public Subject subject;
    int status;
    public SubjectUser() {

    }
    public SubjectUser(User user, Subject subject,int status) {
        this.user = user;
        this.subject = subject;
        this.status = status;
    }


    public static ParseQuery<SubjectUser> getQuery() {
        return ParseQuery.getQuery(SubjectUser.class);
    }
}
