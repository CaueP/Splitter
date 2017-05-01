package com.caue.splitter;

import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Produto;
import com.caue.splitter.model.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity exibida quando o usuário realizar check-in em um estabelecimento
 * @author Caue Polimanti
 * @version 1.0
 * Created on 4/30/2017.
 */
public class CheckedInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    // custom toolbars and navigation drawer
    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    // Log tags
    private static final String ACTIVITY_TAG = "CheckedInActivity";

    @BindView(R.id.progressBar_loading)
    ProgressBar progressBarLoading;

    @BindView(android.R.id.content)
    View mRootView;

    Fragment mContent;      // Fragment object that will receive the current Fragment on the screen
    // in order to be restored when the activity is destroyed, after
    // rotating the screen

    // Dados da activity anterior
    Usuario user = null;
    Checkin checkin = null;

    // Cardapio do estabelecimento
    ArrayList<Produto> cardapio = null;

    // Fragment IDs
    private static final String CHECKIN_FRAGMENT_TAG = "checkin_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkedin);
        ButterKnife.bind(this);

        // Inicializando a Toolbar and a configurando como a ActionBar
        toolbar = (Toolbar) findViewById(R.id.checkedin_page_toolbar);
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

        // calling sync state is necessary or else hamburger icon won't show up
        actionBarDrawerToggle.syncState();


        // getting data from intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            user = (Usuario) bundle.getSerializable(Constants.KEY.USER_DATA);
            checkin = (Checkin) bundle.getSerializable(Constants.KEY.CHECKIN_DATA);
            Log.d(ACTIVITY_TAG, checkin.toString());
        } else {
            Log.d(ACTIVITY_TAG, "Bundle NOT FOUND");
        }

        // Loads CheckedInFragment
        if(savedInstanceState == null && user != null && checkin != null) {
            mContent = CheckedInFragment.newInstance(R.id.checked_in_fragment, user, checkin);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent,CHECKIN_FRAGMENT_TAG)
                .commit();
        }

        Produto produto = new Produto();
        produto.obterCardapio(checkin.getMesa().getCodEstabelecimento(), this);
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

//        ImageView mUserProfilePicture = (ImageView) findViewById(R.id.user_profile_picture);
//        TextView mUserDisplayName = (TextView) this.findViewById(R.id.user_display_name);
//        TextView mUserEmail = (TextView) findViewById(R.id.user_email);

        // can be changed to restaurant's picture
//        if (userProfilePictureUri != null) {
//            Glide.with(this)
//                    .load(userProfilePictureUri)
//                    .fitCenter()
//                    .into(mUserProfilePicture);
//        }

//        mUserDisplayName.setText(userDisplayName);
//        mUserEmail.setText(userEmail);

        return super.onPrepareOptionsMenu(menu);
    }

    // inflate the actionBar
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionview_checkedin, menu);
        return true;
    }

    // defines the actions when a menu item is clicked
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.menu:
                Toast.makeText(CheckedInActivity.this, R.string.menu, Toast.LENGTH_SHORT).show();
//                Bundle data = new Bundle();
//                data.putSerializable("UserData",usuario);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, AccountInfoFragment.newInstance(R.id.account_info_fragment, usuario))
//                        .addToBackStack(null)        // add to back stack
//                        .commit();
                break;
            case R.id.orders:
                Toast.makeText(CheckedInActivity.this, R.string.orders_menu, Toast.LENGTH_SHORT).show();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, NewOrderFragment.newInstance(R.id.new_order_fragment))
//                        .addToBackStack(null)
//                        .commit();
                break;
            case R.id.close_bill:
                Toast.makeText(CheckedInActivity.this, R.string.close_bill, Toast.LENGTH_SHORT).show();
                //intent = new Intent(this, RecyclerViewActivity_Task3.class);
                //startActivity(intent);
                break;
            case R.id.about:
                Toast.makeText(CheckedInActivity.this, R.string.about_splitter, Toast.LENGTH_SHORT).show();
                //getSupportFragmentManager().beginTransaction()
                //       .replace(R.id.container, AboutMeFragment.newInstance(R.id.about_me_fragment))
                //     .addToBackStack(null)
                //   .commit();
                break;
            default:
                Toast.makeText(CheckedInActivity.this, "Option not found", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Overriding onBackPressed para alterar funcionalidade ao pressionar o botão para voltar
     */
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            Fragment checkinFrag = getSupportFragmentManager().findFragmentByTag(CHECKIN_FRAGMENT_TAG);

            Log.d("onBackPressed","Quantidade de fragments na stack: " + fragments);
            if (checkinFrag != null && fragments == 0){  // se o checkin frag estiver na stack e houver apenas 1 fragment na stack, nao retornar
                Log.d("onBackPressed","Checkin fragment está na stack");
                //Toast.makeText(this, R.string.accound_should_be_closed, Toast.LENGTH_SHORT).show();
                showSnackbar(R.string.accound_should_be_closed);
                //finish();
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    /*
    Callbacks
     */

    /**
     * Callback de resposta para a consulta do cardapio
     * @param cardapioRecebido Cardapio recebido da consulta
     */
    public void responseCardapioReceived(ArrayList<Produto> cardapioRecebido) {
        Log.d(ACTIVITY_TAG, "Cardapio disponivel para consulta");
        cardapio = cardapioRecebido;
    }
}
