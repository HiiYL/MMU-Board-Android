package com.underoneroof.mmuboard.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Hii on 9/5/15.
 */
@ParseClassName("Post")
public class Post extends ParseObject {
    public static ParseQuery<Post> getQuery() {
        return ParseQuery.getQuery(Post.class);
    }
//    String title;
//    String contents;
//    Topic topic;
//
//    public User getUser() {
//        return user;
//    }
//
//    User user;
//    long created_at;
//    long updated_at;
//    public Post() {
//    }
//    public Post(String title, String contents) {
//        this.title = title;
//        this.contents = contents;
//    }
//    public Post(String title, String description, Topic topic, long created_at, long updated_at) {
//        this.title = title;
//        this.contents = description;
//        this.topic = topic;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//    }
//    public Post(String title, String description, Topic topic, User user, long created_at, long updated_at) {
//        this.title = title;
//        this.contents = description;
//        this.topic = topic;
//        this.user = user;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getContents() {
//        return contents;
//    }
}
