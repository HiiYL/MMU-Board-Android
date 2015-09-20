package com.underoneroof.mmuboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Utility.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreatePostFragment extends android.support.v4.app.Fragment {
    private static final String ARG_TOPIC_OBJECT_ID = "topic_id";

    private String mTopicObjectId;
    public static final int PHOTO_PICKER_ID = 2;
    private Button submitBtn;
    private Button uploadImageBtn;
    private ProgressBar uploadProgressBar;
    private ImageView imagePreviewView;
    private Post post;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PHOTO_PICKER_ID) {
                submitBtn.setEnabled(false);
                uploadImageBtn.setEnabled(false);
                Uri imageUri = data.getData();
                byte[] bbytes = Utility.convertImageToByte(imageUri, getActivity());
                ParseFile parseFile = new ParseFile(Utility.getFileName(imageUri, getActivity()),bbytes);
                post.put("image", parseFile);
                Log.d("Image File Name", Utility.getFileName(imageUri, getActivity()));
                uploadProgressBar.setVisibility(View.VISIBLE);
                imagePreviewView.setImageURI(imageUri);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            uploadProgressBar.setVisibility(View.GONE);
                            uploadImageBtn.setText("Upload Complete");

                            submitBtn.setEnabled(true);
                        }else {
                            Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
                            uploadImageBtn.setEnabled(true);
                            imagePreviewView.setImageURI(null);
                        }
                    }
                }, new ProgressCallback() {
                    public void done(Integer percentDone) {
                        uploadProgressBar.setProgress(percentDone);
                        uploadImageBtn.setText("Uploading - " + percentDone + " %");
                    }
                });

            }
        }
    }

    public static CreatePostFragment newInstance(String param1) {
        CreatePostFragment fragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOPIC_OBJECT_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public CreatePostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTopicObjectId = getArguments().getString(ARG_TOPIC_OBJECT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        submitBtn = (Button) view.findViewById(R.id.btnSubmit);
        uploadImageBtn = (Button) view.findViewById(R.id.btnUpload);
        uploadProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        imagePreviewView = (ImageView) view.findViewById(R.id.imgPreview);

        post = new Post();

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent,
                        "Complete action using"), PHOTO_PICKER_ID);
            }
        });


        final EditText contentsEditText = (EditText) view.findViewById(R.id.contents);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtn.setClickable(false);
                String sContents = contentsEditText.getText().toString();
                if(sContents.matches("")) {
                    Toast.makeText(getActivity(), "Contents can't be empty!", Toast.LENGTH_SHORT).show();
                }else {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.set(Calendar.MONTH,7);
                    Date date = calendar.getTime();

                    post.put("contents", sContents);
                    post.put("createdBy", ParseUser.getCurrentUser());
                    //post.put("createdAt", "2015-08-16T14:46:05.846Z");
                    post.put("topic", ParseObject.createWithoutData("Topic", mTopicObjectId));
                    post.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getActivity(), "Post saved successfully", Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();

                        }
                    });
                }

            }
        });
        return view;
    }
}
