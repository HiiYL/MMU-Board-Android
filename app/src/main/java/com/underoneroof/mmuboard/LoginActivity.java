package com.underoneroof.mmuboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.underoneroof.mmuboard.Model.Session;
import com.underoneroof.mmuboard.Model.User;

import java.text.ParseException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button regBtn, loginBtn;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        regBtn = (Button) findViewById(R.id.register_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class );
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<User> user = User.find(User.class, "email = ? and password = ? ",
//                        email.getText().toString(), password.getText().toString());
//                if(user.size() > 0) {
                    ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, com.parse.ParseException e) {
                            if (parseUser != null) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                // Hooray! The user is logged in.
                            } else {
                                Log.d("UHOH", "UHOH");
                                // Signup failed. Look at the ParseException to see what happened.
                            }
                        }
                    });
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putLong("user_id", user.get(0).getId());
//                    editor.apply();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(LoginActivity.this, "Nobody is here", Toast.LENGTH_SHORT).show();
//                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
