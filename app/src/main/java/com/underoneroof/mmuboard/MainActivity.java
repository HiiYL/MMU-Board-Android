package com.underoneroof.mmuboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.squareup.picasso.Picasso;
import com.underoneroof.mmuboard.Model.Analytics;
import com.underoneroof.mmuboard.Utility.Gravatar;
import com.underoneroof.mmuboard.Utility.Utility;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements TopicFragment.OnFragmentInteractionListener,
        PostFragment.OnFragmentInteractionListener, SubjectUsersFragment.OnFragmentInteractionListener,
        SubjectListFragment.OnFragmentInteractionListener{
    public static final String SUBJECT_GROUP_NAME = "SUBJECT";
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView email, username;
    private MainActivityFragment mainActivityFragment;
    private int LOGIN_ACTIVITY_RESULT_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_ACTIVITY_RESULT_CODE) {
            ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
            parseInstallation.remove("user");
            parseInstallation.add("user", ParseUser.getCurrentUser());
            parseInstallation.saveInBackground();
            Log.d("MyApp", "onActivityResult in MainActivity is called");
            email = (TextView) findViewById(R.id.email);
            username = (TextView) findViewById(R.id.username);
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                email.setText(currentUser.getEmail());
                username.setText(currentUser.getUsername());
                if (currentUser.getEmail() != null) {
                    Picasso.with(MainActivity.this)
                            .load(Gravatar.gravatarUrl(currentUser.getEmail()))
                            .into((CircleImageView) findViewById(R.id.profile_image));
                }
            }
            if (mainActivityFragment != null) {
                mainActivityFragment.loadAllFromParse();
            } else {
                Log.e("MAIN ACTIVITY FRAGMENT", "mainActivityFragment IS NULL");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ParseUser.getCurrentUser() == null) {
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            startActivityForResult(builder.build(), LOGIN_ACTIVITY_RESULT_CODE);
        }
        setContentView(R.layout.activity_main);
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        email = (TextView) findViewById(R.id.email);
        username = (TextView) findViewById(R.id.username);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            email.setText(currentUser.getEmail());
            username.setText(currentUser.getUsername());
            if(currentUser.getEmail() != null ) {
                Picasso.with(MainActivity.this)
                        .load(Gravatar.gravatarUrl(currentUser.getEmail()))
                        .into((CircleImageView) findViewById(R.id.profile_image));
            }
        }

        if(Utility.isLecturer()) {
            navigationView.getMenu().findItem(R.id.analytics).setVisible(true);
        }

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.my_subjects:
                        MainActivityFragment mainActivityFragment = new MainActivityFragment();
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                                .replace(R.id.frame, mainActivityFragment)
                                .commit();
                        return true;

                    // For rest of the options we just show a toast on click
                    case R.id.sent_mail: {
                        ParseUser.logOut();
                        Intent new_intent = getIntent();
                        finish();
                        startActivity(new_intent);
                        return true;
                    }
                    case R.id.drafts:
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        SubjectListFragment subjectListFragment = new SubjectListFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.frame, subjectListFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.analytics:{
                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                       /* AnalyticsFragment analyticsFragment = new AnalyticsFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                        fragmentTransaction1.replace(R.id.frame, analyticsFragment);
                        fragmentTransaction1.commit();*/

                        AnalyticsFragment analyticsFragment = new AnalyticsFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                        fragmentTransaction2.replace(R.id.frame, analyticsFragment);
                        fragmentTransaction2.commit();


                        return true;
                    }


                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


        mainActivityFragment = new MainActivityFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.frame, mainActivityFragment);
        fragmentTransaction.commit();
    }




    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}
