package com.underoneroof.mmuboard.Model;

import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Hii on 20/09/2015.
 */
public abstract class Model extends ParseObject {
    abstract ParseQuery<Post> getQuery();
}
