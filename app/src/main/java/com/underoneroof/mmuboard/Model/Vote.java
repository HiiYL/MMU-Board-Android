package com.underoneroof.mmuboard.Model;

import com.orm.SugarRecord;

/**
 * Created by Hii on 09/09/2015.
 */
public class Vote extends SugarRecord<Vote> {
    boolean like;
    Post post;
    User user;
    public Vote() {

    }
    public Vote(boolean like, Post post, User user) {
        this.like = like;
        this.post = post;
        this.user = user;
    }
}
