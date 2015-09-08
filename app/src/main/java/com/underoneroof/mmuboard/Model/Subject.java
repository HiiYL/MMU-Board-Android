package com.underoneroof.mmuboard.Model;

import com.orm.SugarRecord;

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
}
