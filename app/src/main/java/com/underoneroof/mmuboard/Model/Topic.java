package com.underoneroof.mmuboard.Model;

import com.orm.SugarRecord;

/**
 * Created by Hii on 9/5/15.
 */
public class Topic extends SugarRecord<Topic> {
    String title;
    String description;
    public long created_at;
    public long updated_at;
    Subject subject;
    public Topic(String title, String description, long created_at, long updated_at) {
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
