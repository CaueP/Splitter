package com.caue.splitter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.helper.Constants;
import com.caue.splitter.model.Checkin;
import com.caue.splitter.model.Usuario;
import com.caue.splitter.model.services.UsuarioClient;
import com.caue.splitter.utils.DatePickerFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MainPageActivity é a activity principal do app. Tem o objetivo de controlar o Menu Drawer e as ações realizadas em todos os fragments
 * @author Caue Polimanti
 * @version 2.0
 * Created on 9/5/2016.
 */
public class MainPageActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
                AccountInfoFragment.OnFragmentInteractionListener,DatePickerFragment.OnDateSetListener{


    @BindView(R.id.main_page_progressBar)
    ProgressBar mainPageProgressBar;

    @BindView(android.R.id.content)
    View mRootView;

    // connectivity manager
    ConnectivityManager cm;
    NetworkInfo netInfo;
    // criação do serviço para realizar operações na conta do usuário
    UsuarioClient service = ServiceGenerator.createService(UsuarioClient.class);

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

    // user data
    FirebaseUser firebaseUser;
    Usuario usuario = null;

    // User Registration request codes
    static final int REGISTRATION_SUCCESSFULL = 1;  // The request code

    //QR Code
    static final int QR_CODE_SCAN = 49374;  // request code for scan qr intent
    private static final String CHECKIN_FRAGMENT_TAG = "checkin_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // se o Usuario nao estiver logado, retorne para a pagina de Login
        if (firebaseUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        carregarUsuario(firebaseUser.getEmail());

        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);

        // getting firebaseUser info
        userProfilePictureUri = firebaseUser.getPhotoUrl();
        userEmail = TextUtils.isEmpty(firebaseUser.getEmail()) ? "No email" : firebaseUser.getEmail();
        userDisplayName = TextUtils.isEmpty(firebaseUser.getDisplayName()) ? "No display name" : firebaseUser.getDisplayName();


        Log.d("MainPageActivity","firebaseUser: " + userDisplayName + " " + userEmail + " " + userProfilePictureUri);


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



        // popular o perfil
        //populateProfile();

         // Carrega o Fragment da Main Page
        if(savedInstanceState == null) {
            // Restore the fragment's instance
            mContent = MainPageFragment.newInstance(R.id.main_page_fragment);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mContent)
                    .commit();
        }
    }

    private void carregarUsuario(String email) {
        Log.d("carregarUsuario", "Carregando usuario");
        // Service para baixar objeto com o usuário
        Call<Usuario> listCall = service.getUsuario(email);

        // callback ao receber a resposta da API
        Callback<Usuario> carregaUsuarioCallback = new Callback<Usuario>() {

            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Log.d("onResponse", "entered in onResponse");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    usuario = response.body();

                    if(usuario == null || !usuario.getContaAtiva()) {
                        Log.d("onResponse", "Usuario inexistente/inativo: " + usuario.getEmail() + " " + usuario.getContaAtiva());
                        Intent intent = new Intent(MainPageActivity.this, UserRegistrationActivity.class);
                        startActivityForResult(intent, REGISTRATION_SUCCESSFULL);
                    }
                    Log.d("retorno usuario", usuario.getEmail());
                    mainPageProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                    if (response.code() == 404){    // usuario nao cadastrado
                        Log.d("onResponse","Usuario nao registrado. Chamando tela de cadastro de usuario");
                        Intent intent = new Intent(MainPageActivity.this, UserRegistrationActivity.class);
                        startActivityForResult(intent, REGISTRATION_SUCCESSFULL);
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("onFailure","Ocorreu um erro ao chamar a API - MainPageActivity");
            }
        };

        // call
        listCall.enqueue(carregaUsuarioCallback);
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

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
            case R.id.account_info:
                //Toast.makeText(MainPageActivity.this, "Item1 clicked", Toast.LENGTH_SHORT).show();
                Bundle data = new Bundle();
                //data.putSerializable("UserData",userDB.getUserData());
                data.putSerializable("UserData",usuario);
                getSupportFragmentManager().beginTransaction()
                       .replace(R.id.content_frame, AccountInfoFragment.newInstance(R.id.account_info_fragment, usuario))
                     .addToBackStack(null)        // add to back stack
                   .commit();
                break;
            case R.id.payments:
                Toast.makeText(MainPageActivity.this, R.string.payments_menu, Toast.LENGTH_SHORT).show();
                //intent = new Intent(this, RecyclerViewActivity_Task3.class);
                //startActivity(intent);
                break;

            case R.id.about:
                Toast.makeText(MainPageActivity.this, R.string.about_splitter, Toast.LENGTH_SHORT).show();
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

    // Retorna os valores da Activity de cadastro de Usuario e os dados do Usuario enviado para cadastro
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REGISTRATION_SUCCESSFULL) {
            Log.d("MainPageActivity","REGISTRATION_SUCCESSFULL");
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d("MainPageActivity","RESULT_OK");
                //Bundle bundle = this.getIntent().getExtras();
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    usuario = (Usuario) bundle.getSerializable("userData");
                    Log.d("onActivityResult",usuario.getEmail());
                    mainPageProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        } else if (requestCode == QR_CODE_SCAN){    // if qr code result
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null){
                if(result.getContents()==null){
                    Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
                }
                else {
                    mainPageProgressBar.setVisibility(View.VISIBLE);
                    String qrCodeResult;
                    qrCodeResult = result.getContents();

                    //Toast.makeText(this, "QR Code Lido:" + qrCodeResult, Toast.LENGTH_SHORT).show();

                    // setting checkin parameters
                    Checkin check = new Checkin();
                    check.setUsuario(usuario);
                    check.getMesa().setQrCode(qrCodeResult);
                    check.getMesa().setNrMesa(Integer.parseInt(qrCodeResult.substring(1,3)));
                    check.getMesa().setCodEstabelecimento(qrCodeResult.substring(3,12));

                    if(usuario != null){
                        Log.d("qrCodeRead"," qrCode" + check.getMesa().getQrCode());
                        Log.d("qrCodeRead", "Email usuario: "+ check.getUsuario().getEmail() +
                                " CodEstabelecimento: " + check.getMesa().getCodEstabelecimento()+
                                " NrMesa:" + check.getMesa().getNrMesa());
                        check.realizarCheckin(this);
                    } else {
                        Toast.makeText(this,R.string.check_internet_connection, Toast.LENGTH_LONG).show();
                    }

                    mainPageProgressBar.setVisibility(View.INVISIBLE);
                }
            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * Método que é chamado quando receber a resposta da realização de checkin
     * @param checkinResponse parâmetro com a resposta de checkin
     */
    public void responseCheckinReceived(Checkin checkinResponse){
        Log.d("responseCheckinReceived", "Status resposta: " + checkinResponse.isSucesso());

        if(checkinResponse.isSucesso()) {
            Intent checkInActivityIntent = new Intent(MainPageActivity.this, CheckedInActivity.class);

            Bundle checkinExtras = new Bundle();
            checkinExtras.putSerializable(Constants.KEY.USER_DATA, usuario);
            checkinExtras.putSerializable(Constants.KEY.CHECKIN_DATA, checkinResponse);
            checkInActivityIntent.putExtras(checkinExtras);
            startActivity(checkInActivityIntent);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.content_frame, CheckedInFragment.newInstance(R.id.checked_in_fragment, usuario, checkinResponse),CHECKIN_FRAGMENT_TAG)
//                    .addToBackStack(null)
//                    .commit();
        } else {
            switch (checkinResponse.getError()) {
                case "MesaOcupada":
                    Toast.makeText(this, "Obtenha o código de acesso com o usuário " + checkinResponse.getMesa().getUsuarioResponsavel(), Toast.LENGTH_LONG).show();
                    break;
                case "ErroDesconhecido":
                    Toast.makeText(this, "Ocorreu um erro com sua solicização. Tente novamente", Toast.LENGTH_LONG).show();
                    break;
                case "MesaNaoEncontrada":
                    Toast.makeText(this, "Mesa inexistente", Toast.LENGTH_LONG).show();
                    break;

            }

        }

    }

    // Interactions ocurring on AccountInfoFragment
    @Override
    public void OnDeleteButtonCliked() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.ask_delete_account_confirmation)
                .setPositiveButton(R.string.asnwer_yes_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .create();

        dialog.show();
    }


    private void deleteAccount() {

        // Service para atualizar o usuário
        Call<ResponseBody> listCall = service.desativarUsuario(usuario.getEmail());

        // callback ao receber a resposta da API
        Callback<ResponseBody> desativarUsuarioCallback = new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("desativarUsuarioCb", "entered in onResponse");
                if (response.isSuccessful()) {
                    Log.d("desativarUsuarioCb", "isSuccessful");
                    Log.d("desativarUsuarioCb", "Response: " + response.body());
                } else {
                    Log.d("desativarUsuarioCb", "isNOTSuccessful (code: " + response.code() + ")");
                    Toast.makeText(MainPageActivity.this, R.string.user_not_deleted, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure","Ocorreu um erro ao chamar a API - MainPageActivity");
                Toast.makeText(MainPageActivity.this, R.string.user_not_deleted, Toast.LENGTH_SHORT).show();
            }
        };

        // call
        listCall.enqueue(desativarUsuarioCallback);

        FirebaseAuth.getInstance()
                .getCurrentUser()
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainPageActivity","deleteAccount Task succeeded.");
                            showSnackbar(R.string.delete_account_succeed);
                            startActivity(LoginActivity.createIntent(MainPageActivity.this));
                            finish();
                        } else {
                            Log.d("MainPageActivity","deleteAccount Task didn't succeed.");
                            showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }

    @Override
    public void OnUpdateButtonCliked(Usuario updatedUser) {
        Log.d("OnUpdateButtobClicked", "User Updated");

        // Service para atualizar o usuário
        Call<Usuario> listCall = service.atualizarUsuario(updatedUser, updatedUser.getEmail());

        // callback ao receber a resposta da API
        Callback<Usuario> atualizarUsuarioCallback = new Callback<Usuario>() {

            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Log.d("atualizarUsuarioCb", "entered in onResponse");
                if (response.isSuccessful()) {
                    Log.d("atualizarUsuarioCb", "isSuccessful");
                    Log.d("atualizarUsuarioCb", "Body: " + response.body());


                    if(usuario == null || !usuario.getContaAtiva()) {
                        Log.d("atualizarUsuarioCb","usuario nao atualizado ou conta inativa");
                    } else {
                        Toast.makeText(MainPageActivity.this, R.string.user_updated, Toast.LENGTH_SHORT).show();
                        usuario = response.body();
                    }
                    Log.d("retorno usuario", usuario.getEmail());
                } else {
                    Log.d("atualizarUsuarioCb", "isNOTSuccessful (code: " + response.code() + ")");
                    Toast.makeText(MainPageActivity.this, R.string.user_not_updated, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("onFailure","Ocorreu um erro ao chamar a API - MainPageActivity");
            }
        };

        // call
        listCall.enqueue(atualizarUsuarioCallback);
    }


    // Chamado ao clicar no campo de data
    public void showDatePicker(View view) {
        // Novo DatePickerDialog utilizando um fragment
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Listener do DatePickerFragment para setar a data
    @Override
    public void OnDateSet(int year, int month, int day) {
        EditText dateOfBirth = (EditText) findViewById(R.id.edittext_user_reg_dob);
        dateOfBirth.setText(day + "/" + month + "/" + year);
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
            if (checkinFrag != null && fragments == 1){  // se o checkin frag estiver na stack e houver apenas 1 fragment na stack, nao retornar
                Log.d("onBackPressed","Checkin fragment está na stack");
                Toast.makeText(this, R.string.accound_should_be_closed, Toast.LENGTH_SHORT).show();
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
}
