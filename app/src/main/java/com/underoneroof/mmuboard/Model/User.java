package com.underoneroof.mmuboard.Model;

import com.orm.SugarRecord;

/**
 * Created by Hii on 09/09/2015.
 */
public class User extends SugarRecord<User> {
    public String name;
    public String username;
    public String password;
    public String email;
    public int user_type;
    long updated_at;
    long created_at;
    public User() {

    }
    public User(String name, String username, String password, String email, int user_type) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.user_type = user_type;
    }
}
