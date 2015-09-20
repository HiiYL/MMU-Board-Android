package com.underoneroof.mmuboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.SubjectUser;

public class CreateSubjectActivity extends AppCompatActivity {
    private Button createSubjectBtn;
    private TextView subjectNameTextView, subjectDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
        createSubjectBtn = (Button) findViewById(R.id.create_subject_btn);
        subjectNameTextView = (TextView) findViewById(R.id.subject_name);
        subjectDescriptionTextView = (TextView) findViewById(R.id.subject_description);
        final CheckBox isPrivateCheckbox = (CheckBox) findViewById(R.id.is_private_checkbox);
        createSubjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject subject = new Subject();
                subject.put("title", subjectNameTextView.getText().toString());
                subject.put("description", subjectDescriptionTextView.getText().toString());
                subject.put("createdBy", ParseUser.getCurrentUser());
                subject.put("isPrivate", isPrivateCheckbox.isChecked());
                subject.saveInBackground();
                SubjectUser subjectUser = new SubjectUser();
                subjectUser.put("user", ParseUser.getCurrentUser());
                subjectUser.put("subject", subject);
                subjectUser.put("status", 3);
                subjectUser.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(CreateSubjectActivity.this, "Subject created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_subject, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
