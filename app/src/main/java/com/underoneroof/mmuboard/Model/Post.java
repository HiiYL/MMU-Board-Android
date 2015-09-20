package com.underoneroof.mmuboard.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Hii on 9/5/15.
 *
 */
@ParseClassName("Post")
public class Post extends ParseObject {
    public static ParseQuery<Post> getQuery() {
        return ParseQuery.getQuery(Post.class);
    }
    public Post() {

    }
    public void setContents(String contents) {
        put("contents", contents);
    }
    public void setImage(ParseFile image) {
        put("image", image);
    }
    public void setCreatedBy(ParseUser user) {
        put("createdBy", user);
    }
    public void setTopic(ParseObject topic) {
        put("topic", topic);
    }

}
