package com.underoneroof.mmuboard.Model;

import com.orm.SugarRecord;

/**
 * Created by Hii on 9/5/15.
 */
public class Post extends SugarRecord<Post> {
    String title;

    public String getTitle() {
        return title;
    }

    public String getContents() {

        return contents;
    }

    String contents;
    Topic topic;
    long created_at;
    long updated_at;
    Post() {

    }
    Post(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
    public Post(String title, String description, Topic topic, long created_at, long updated_at) {
        this.title = title;
        this.contents = description;
        this.topic = topic;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
