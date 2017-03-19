package com.caue.splitter;

import android.app.Activity;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.model.Usuario;
import com.caue.splitter.model.services.UsuarioClient;
import com.caue.splitter.utils.DatePickerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Caue Polimanti
 * @version 1.0
 * created on 9/17/2016
 */
public class UserRegistrationActivity extends AppCompatActivity
    implements DatePickerFragment.OnDateSetListener, Callback<Usuario> {

    @BindView(android.R.id.content) View mRootView;

    @BindView(R.id.edittext_user_reg_name) EditText name;
    @BindView(R.id.edittext_user_reg_email) EditText email;
    @BindView(R.id.edittext_user_reg_pass) EditText password;
    @BindView(R.id.edittext_user_reg_phone) EditText phone;
    @BindView(R.id.edittext_user_reg_cpf) EditText cpf;
    @BindView(R.id.edittext_user_reg_dob) EditText dateOfBirth;

    Usuario usuario;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        ButterKnife.bind(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // se o Usuario nao estiver logado, retorne para a pagina de Login
        if (firebaseUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        // setting information based on firebaseUser
        email.setText(firebaseUser.getEmail());
        name.setText(firebaseUser.getDisplayName());

        // Hide keyboard on dateOfBirth
        //dateOfBirth.setShowSoftInputOnFocus(false);
        //dateOfBirth.setFocusableInTouchMode(false);
        //dateOfBirth.setFocusable(false);

    }

    @OnClick(R.id.save_button)
    public void saveButton() {
        Log.d("UserRegistrationActivit","saveButton Clicked");

        usuario = new Usuario(1,name.getText().toString().trim(),
                password.getText().toString().trim(),
                Long.parseLong(cpf.getText().toString()),
                dateOfBirth.getText().toString(),
                email.getText().toString().trim(),
                Long.parseLong(phone.getText().toString()),
                true
                );

        // create user on the database
        criarUsuario(usuario);
        //userData.createUser(userData.getUserData());

        //Toast.makeText(this, "Usuario criado", Toast.LENGTH_SHORT).show();

    }

    private void criarUsuario(Usuario usuario) {
        // Service para baixar objeto com a lista de imoveis
        UsuarioClient service = ServiceGenerator.createService(UsuarioClient.class);
        Call<Usuario> listCall = service.postUsuario(usuario);

        // call
        listCall.enqueue(this);
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
        dateOfBirth.setText(day + "/" + month + "/" + year);
    }

    @Override
    public void onResponse(Call<Usuario> call, Response<Usuario> response) {

        Log.d("onResponse", "entered in onResponse");
        if (response.isSuccessful()) {
            Log.d("onResponse", "isSuccessful");
            Log.d("onResponse", "Usuario criado: " + response.body());


            if(response.body() == null) {
                Log.d("onResponse", "Usuario não registrado");
            } else {
                usuario = response.body();
            }
            Log.d("retorno usuario", usuario.getEmail());
            Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT).show();

            finishActivity();
        } else {
            Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
            if (response.code() == 404){    // usuario nao cadastrado
                Log.d("onResponse","erro ao registrar usuario");
            }
        }


    }

    @Override
    public void onFailure(Call<Usuario> call, Throwable t) {
        Log.d("onFailure","Ocorreu um erro ao chamar a API - UserRegistrationActivity" + t.getMessage());
        Log.d("onFailure","Erro:" + t.toString());
    }

    // prepara a activiy para ser finalizada e a finaliza
    private void finishActivity() {
        // create return intent for startActivityForResult
        Intent returnIntent = new Intent();
        returnIntent.putExtra("userData", usuario);
        setResult(Activity.RESULT_OK,returnIntent);

        finish();
    }
}
