package com.underoneroof.mmuboard.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Hii on 8/31/15.
 *
 */
@ParseClassName("Subject")
public class Subject extends ParseObject {

    public static ParseQuery<Subject> getQuery() {
        return ParseQuery.getQuery(Subject.class);
    }
    public void setDraft(boolean draft) {
        put("isDraft",draft);
    }

}
