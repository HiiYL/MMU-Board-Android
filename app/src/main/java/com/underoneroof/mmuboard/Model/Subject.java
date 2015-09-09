package com.underoneroof.mmuboard.Model;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hii on 8/31/15.
 */
public class Subject extends SugarRecord<Subject> {
    String title;
    String description;
    public Subject() {
    }
    public Subject(String title, String description){
        this.title = title;
        this.description = description;
    }
    public Subject(Long id, String title, String description){
        this.title = title;
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        List<SubjectUser> subjectUsers = SubjectUser.find(SubjectUser.class, "subject = ? ", String.valueOf(getId()));
        for(SubjectUser subjectUser : subjectUsers) {
            users.add(subjectUser.user);
        }
        return users;
    }
    public User getCreator() {
        long i = SubjectUser.count(SubjectUser.class, null, null);
        SubjectUser sUser = SubjectUser.findById(SubjectUser.class, Long.valueOf(1));
        List<SubjectUser> subjectUsers = SubjectUser.find(SubjectUser.class, "subject = ? ", String.valueOf(getId()));
        Log.d("SIZE OF SUBJECT USER", sUser.subject.getTitle());
        for(SubjectUser subjectUser : subjectUsers) {
            if(subjectUser.is_creator) {
                return subjectUser.user;
            }
        }
        return null;
    }
}
