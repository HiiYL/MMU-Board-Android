package com.underoneroof.mmuboard.Model;

import com.orm.SugarRecord;

/**
 * Created by Hii on 9/5/15.
 */
public class Post extends SugarRecord<Post> {
    String title;
    String contents;
    long created_at;
    long updated_at;
    Post(String title, String contents) {
        this.title = title;
        this.contents = contents;

    }
}
