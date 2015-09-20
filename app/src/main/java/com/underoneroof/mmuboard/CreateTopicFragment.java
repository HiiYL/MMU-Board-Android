package com.underoneroof.mmuboard;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.Utility.Utility;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTopicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTopicFragment extends android.support.v4.app.Fragment {
    private static final String ARG_SUBJECT_OBJECT_ID = "subject_object_id";
    private static final int TOPIC_PHOTO_PICKER_ID = 3;
    private static final String ARG_SUBJECT_NAME = "subject_name";

    private String mSubjectObjectId;
    private Button submitBtn;
    private Button uploadImageBtn;
    private ProgressBar uploadProgressBar;
    private ImageView imagePreviewView;
    private Post post;
    private Topic topic;
    private String mSubjectName;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TOPIC_PHOTO_PICKER_ID) {
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
    public static CreateTopicFragment newInstance(String param1, String params2) {
        CreateTopicFragment fragment = new CreateTopicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBJECT_OBJECT_ID, param1);
        args.putString(ARG_SUBJECT_NAME, params2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateTopicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubjectObjectId = getArguments().getString(ARG_SUBJECT_OBJECT_ID);
            mSubjectName = getArguments().getString(ARG_SUBJECT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_topic, container, false);
        submitBtn = (Button) view.findViewById(R.id.btnSubmit);
        uploadImageBtn = (Button) view.findViewById(R.id.btnUpload);
        uploadProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        imagePreviewView = (ImageView) view.findViewById(R.id.imgPreview);
        final EditText contentsEditText = (EditText) view.findViewById(R.id.contents);
        final EditText topicTitleEditText = (EditText) view.findViewById(R.id.topic_title);

        topic = new Topic(mSubjectObjectId);
        post = new Post();

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent();
                getIntent.setType("image/*");
                getIntent.setAction(Intent.ACTION_GET_CONTENT);
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");


                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                try {
                    startActivityForResult(pickIntent, TOPIC_PHOTO_PICKER_ID);
                }catch(Exception e) {
                    startActivityForResult(getIntent, TOPIC_PHOTO_PICKER_ID);
                }
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sContents = contentsEditText.getText().toString();
                String sTitle = topicTitleEditText.getText().toString();

                if(sTitle.matches("")) {
                    Toast.makeText(getActivity(), "Title can't be empty", Toast.LENGTH_SHORT).show();
                }
                if(sContents.matches("")) {
                    Toast.makeText(getActivity(), "Contents can't be empty!", Toast.LENGTH_SHORT).show();
                }else {
                    topic.put("title", sTitle);
                    topic.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            post.put("contents", sContents);
                            post.put("createdBy", ParseUser.getCurrentUser());
                            post.put("topic", ParseObject.createWithoutData("Topic", topic.getObjectId()));

                            post.saveEventually(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    ParseQuery pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereEqualTo("subjects",
                                            ParseInstallation.createWithoutData("Subject", mSubjectObjectId))
                                    .whereNotEqualTo("user", ParseUser.getCurrentUser());
                                    ParsePush push = new ParsePush();
                                    push.setQuery(pushQuery); // Set our Installation query
                                    push.setMessage(ParseUser.getCurrentUser().getUsername()
                                            + " submitted a topic to #" + mSubjectName);
                                    push.sendInBackground();
                                    Toast.makeText(getActivity(), "Post saved successfully", Toast.LENGTH_SHORT).show();
                                    getFragmentManager().popBackStack();
                                }
                            });
                        }
                    });

                }

            }
        });
        return view;
    }


}
