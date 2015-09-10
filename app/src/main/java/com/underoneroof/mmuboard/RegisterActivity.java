package com.underoneroof.mmuboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.underoneroof.mmuboard.Model.User;

public class RegisterActivity extends AppCompatActivity {
    Button regBtn, loginBtn;
    EditText email,name,username, password;
    RadioButton lecturerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regBtn = (Button) findViewById(R.id.register_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        lecturerBtn = (RadioButton) findViewById(R.id.lecturer_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                User user = new User(name.getText().toString(), username.getText().toString(),
//                        password.getText().toString(), email.getText().toString(), lecturerBtn.isChecked() ? 1 : 0 );
//                user.save();
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.put("name", name.getText().toString());
// other fields can be set just like with ParseObject

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("YAY", "YAY");
                            finish();
                            // Hooray! Let them use the app now.
                        } else {
                            Log.d("NAY", "NAY");
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
