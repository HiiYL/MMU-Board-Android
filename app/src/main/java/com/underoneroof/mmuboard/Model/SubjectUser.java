package com.underoneroof.mmuboard.Model;

import com.orm.SugarRecord;

/**
 * Created by Hii on 09/09/2015.
 */
public class SubjectUser extends SugarRecord<SubjectUser> {
    public User user;
    public Subject subject;
    boolean approved = false;
    boolean is_creator = false;
    boolean is_admin = false;
    public SubjectUser() {

    }
    public SubjectUser(User user, Subject subject,boolean approved, boolean is_creator, boolean is_admin) {
        this.user = user;
        this.approved = approved;
        this.subject = subject;
        this.is_creator = is_creator;
        this.is_admin = is_admin;
    }


}
