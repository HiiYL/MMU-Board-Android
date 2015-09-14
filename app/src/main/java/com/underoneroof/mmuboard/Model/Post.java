package com.underoneroof.mmuboard.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Hii on 9/5/15.
 *
 */
@ParseClassName("Post")
public class Post extends ParseObject {
    public static ParseQuery<Post> getQuery() {
        return ParseQuery.getQuery(Post.class);
    }
}
