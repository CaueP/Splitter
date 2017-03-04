package com.caue.splitter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import com.caue.splitter.data.UserDataJson;
import com.caue.splitter.utils.DatePickerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Caue on 9/17/2016.
 */
public class UserRegistrationActivity extends AppCompatActivity
    implements DatePickerFragment.OnDateSetListener{

    @BindView(android.R.id.content) View mRootView;

    @BindView(R.id.edittext_user_reg_name) EditText name;
    @BindView(R.id.edittext_user_reg_email) EditText email;
    @BindView(R.id.edittext_user_reg_pass) EditText password;
    @BindView(R.id.edittext_user_reg_phone) EditText phone;
    @BindView(R.id.edittext_user_reg_cpf) EditText cpf;
    @BindView(R.id.edittext_user_reg_dob) EditText dateOfBirth;

    // utilizado pelo antigo DatePicker (substituido pelo DatePickerFragment)
    /*int year,month,day;
    static final int DIALOG_ID=0;*/

    UserDataJson userData;
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


        userData = new UserDataJson(1,name.getText().toString(),
                Long.parseLong(cpf.getText().toString()),
                dateOfBirth.getText().toString(),
                email.getText().toString(),
                Long.parseLong(phone.getText().toString()),
                password.getText().toString());


        // create return intent for startActivityForResult
        Intent returnIntent = new Intent();

        Bundle data = new Bundle();
        // serialize data
        data.putSerializable("UserData",userData.getUserData());
        returnIntent.putExtras(data);

        //returnIntent.putExtra("userData",userData);
        setResult(Activity.RESULT_OK,returnIntent);

        // create user on the database
        userData.createUser(userData.getUserData());

        finish();
    }


    // parse to Int to check if the user typed an int
    public int parseToInt(String maybeInt, int defaultValue){
        if (maybeInt == null) return defaultValue;
        maybeInt = maybeInt.trim();
        if (maybeInt.isEmpty()) return defaultValue;
            return Integer.parseInt(maybeInt);
    }


    // Chamado ao clicar no campo de data
    public void showDatePicker(View view) {
        // Antigo DatePickerDialog
        //showDialog(DIALOG_ID);

        // Novo DatePickerDialog utilizando um fragment
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Listener do DatePickerFragment para setar a data
    @Override
    public void OnDateSet(int year, int month, int day) {
        dateOfBirth.setText(day + "/" + month + "/" + year);
    }

/* // Antigo DatePickerDialog
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year, month, day);

        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            year = y;
            month = m+1;
            day = d;
            dateOfBirth.setText(day + "/" + month + "/" + year);
        }
    };
    */
}
