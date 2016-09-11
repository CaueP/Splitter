package com.caue.splitter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.OnClick;

/**
 * Created by Caue on 9/5/2016.
 */
public class MainPageActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{


    //@BindView(R.id.user_profile_picture)
    //ImageView mUserProfilePicture;

    //@BindView(R.id.user_email)
    //TextView mUserEmail;

    //@BindView(R.id.user_display_name)
    //TextView mUserDisplayName;


    //@BindView(android.R.id.content)
    View mRootView;

    //@BindView(R.id.user_enabled_providers)
    //TextView mEnabledProviders;

    Fragment mContent;      // Fragment object that will receive the current Fragment on the screen
    // in order to be restored when the activity is destroyed, after
    // rotating the screen

    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    // User info for navigation drawer header
    Uri userProfilePictureUri;
    String userEmail;
    String userDisplayName;

    //mUserProfilePicture = (ImageView) findViewById(R.id.user_profile_picture);

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // se o usuario nao estiver logado, retorne para a pagina de Login
        if (user == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_main_page);


        // Using findview
        //mUserEmail = (TextView) this.findViewById(R.id.user_email);
        //mUserDisplayName = (TextView) this.findViewById(R.id.user_display_name);
        //mUserProfilePicture = (ImageView) this.findViewById(R.id.user_profile_picture);

        // getting user info
        userProfilePictureUri = user.getPhotoUrl();
        userEmail = TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail();
        userDisplayName = TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName();

        Log.d("MainPageActivity","User Info: " + userDisplayName + " " + userEmail + " " + userProfilePictureUri);


        // Inicializando a Toolbar and a configurando como a ActionBar
        toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);

        // Inicializando a NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);   // setando o listener do fragment

        // Inicializando Drawer Layout
        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.openDrawer,R.string.closeDrawer) {
                    @Override
                    public void onDrawerClosed(View drawerView){
                        super.onDrawerClosed(drawerView);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                    }
                };

        // Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // calling sync state is necessary or else your hamburger icon won't show up
        actionBarDrawerToggle.syncState();

        //ButterKnife.bind(this);

        // popular o perfil
        //populateProfile();

        /* // Carrega o Fragment
        if(savedInstanceState == null) {
            // Restore the fragment's instance
            mContent = MainPageFragment.newInstance(R.id.main_page_fragment);


            //
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mContent)
                    .commit();
        }*/
    }




    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = drawerLayout.isDrawerOpen(drawerLayout);
        //menu.findItem(R.id.about).setVisible(!drawerOpen);

        //mUserProfilePicture = (ImageView) this.findViewById(R.id.user_profile_picture);
        //mUserEmail = (TextView) this.findViewById(R.id.user_email);

        ImageView mUserProfilePicture = (ImageView) findViewById(R.id.user_profile_picture);
        TextView mUserDisplayName = (TextView) this.findViewById(R.id.user_display_name);
        TextView mUserEmail = (TextView) findViewById(R.id.user_email);

        if (userProfilePictureUri != null) {
            Glide.with(this)
                    .load(userProfilePictureUri)
                    .fitCenter()
                    .into(mUserProfilePicture);
        }

        mUserDisplayName.setText(userDisplayName);
        mUserEmail.setText(userEmail);

        return super.onPrepareOptionsMenu(menu);
    }

    // inflate the actionBar
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionview, menu);
        return true;
    }


    // defines the actions when a menu item is clicked
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.item1:
                Toast.makeText(MainPageActivity.this, "Item1 clicked", Toast.LENGTH_SHORT).show();
                //intent = new Intent(this, RecyclerViewActivity.class);
                //startActivity(intent);
                break;
            case R.id.item2:
                Toast.makeText(MainPageActivity.this, "Item2 clicked", Toast.LENGTH_SHORT).show();
                //intent = new Intent(this, RecyclerViewActivity_Task3.class);
                //startActivity(intent);
                break;

            case R.id.about:
                //getSupportFragmentManager().beginTransaction()
                 //       .replace(R.id.container, AboutMeFragment.newInstance(R.id.about_me_fragment))
                   //     .addToBackStack(null)
                     //   .commit();
                break;

            case R.id.sign_out:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(LoginActivity.createIntent(MainPageActivity.this));
                                    finish();
                                } else {
                                    showSnackbar(R.string.sign_out_failed);
                                }
                            }
                        });
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MainPageActivity.class);
        return in;
    }
}
