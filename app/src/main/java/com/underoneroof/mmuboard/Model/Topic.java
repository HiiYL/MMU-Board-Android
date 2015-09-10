package com.underoneroof.mmuboard.Model;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Hii on 9/5/15.
 */
@ParseClassName("Topic")
public class Topic extends ParseObject {
    public static ParseQuery<Topic> getQuery() {
        return ParseQuery.getQuery(Topic.class);
    }
//    String title;
//    String description;
//    public long created_at;
//    public long updated_at;
//    Subject subject;
//
//    public User getUser() {
//        return user;
//    }
//
//    User user;
//    public Topic() {
//
//    }
//    public Topic(String title, String description, Subject subject, long created_at, long updated_at) {
//        this.title = title;
//        this.description = description;
//        this.subject = subject;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//    }
//    public Topic(String title, String description, Subject subject,User user, long created_at, long updated_at) {
//        this.title = title;
//        this.description = description;
//        this.subject = subject;
//        this.user = user;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//    }
//    public String getTitle() {
//        return title;
//    }
//    public String getDescription() {
//        return description;
//    }
}
