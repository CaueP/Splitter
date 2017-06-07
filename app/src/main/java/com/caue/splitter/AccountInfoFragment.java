package com.caue.splitter;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.caue.splitter.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author Caue Polimanti
 * @version 2.0
 * Created on 9/13/2016.
 */
public class AccountInfoFragment extends Fragment{
    @BindView(R.id.user_profile_picture) ImageView userPicture;
    @BindView(R.id.edittext_user_reg_name) EditText name;
    @BindView(R.id.edittext_user_reg_email) EditText email;
//    @BindView(R.id.edittext_user_reg_pass) EditText password;
    @BindView(R.id.edittext_user_reg_phone) EditText phone;
    @BindView(R.id.edittext_user_reg_cpf) EditText cpf;
    @BindView(R.id.edittext_user_reg_dob) EditText dateOfBirth;
    Button btnDeleteAccount;
    Button btnUpdateAccount;

    Usuario usuario = null;
    View rootView = null;
    private static final String ARG_SECTION_NUMBER = "section_number";


    public static AccountInfoFragment newInstance(int sectionNumber, Usuario usuario) {
        AccountInfoFragment fragment = new AccountInfoFragment();
        Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable("UserData",usuario);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountInfoFragment(){
    }

    // set the layout
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        Log.d("AccountInfoFragment", "AccountInfoFragment just entered in its onCreateView");

        // get arguments
        //int option = getArguments().getInt(ARG_SECTION_NUMBER);

        // loads the fragment selected (or the restored one)
        //switch (option){
            //case R.id.account_info_fragment:
                rootView = inflater.inflate(R.layout.fragment_account_info, container, false);
              //  break;
        //}
        ButterKnife.bind(this, rootView);

        // get user data
        usuario = (Usuario) getArguments().getSerializable("UserData");
        Log.d("AccountInfo: ", usuario.getEmail());

        // set user data
        if(usuario != null) {
            name.setText(usuario.getNome());
            email.setText(usuario.getEmail());
            phone.setText(String.valueOf(usuario.getTelefone()));
            cpf.setText(String.valueOf(usuario.getCpf()));
            dateOfBirth.setText(usuario.getDtNascimento());
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Uri userProfilePictureUri = null;
        if(firebaseUser != null)
                userProfilePictureUri = firebaseUser.getPhotoUrl();
        if (userProfilePictureUri != null) {
            Glide.with(this)
                    .load(userProfilePictureUri)
                    .fitCenter()
                    .into(userPicture);
        }

        // create mListener to do the button actions in the activities
        final OnFragmentInteractionListener mListener;
        try{
            mListener = (OnFragmentInteractionListener) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the Fragment" +
                    "forgot to implement OnFragmentInteractionListener");
        }


        // Buttons findView
        btnDeleteAccount = (Button) rootView.findViewById(R.id.delete_account);
        btnUpdateAccount = (Button) rootView.findViewById(R.id.update_account);

        // delete button listener
        btnDeleteAccount.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("AccountInfoFragment", "Delete Button Clicked");
                mListener.OnDeleteButtonCliked();
            }
        });


        // update button listener
        btnUpdateAccount.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("AccountInfoFragment", "Update Button Clicked");

                Usuario updatedUser = new Usuario(usuario);
                updatedUser.setNome(name.getText().toString().trim());
                updatedUser.setCpf(Long.parseLong(cpf.getText().toString()));
                updatedUser.setDtNascimento(dateOfBirth.getText().toString());
                updatedUser.setEmail(email.getText().toString().trim());
                updatedUser.setTelefone(Long.parseLong(phone.getText().toString()));
//                usuario.setSenha(password.getText().toString());
                updatedUser.setSenha("");

                if(updatedUser.equals(usuario)) {
                    Toast.makeText(getContext(), R.string.msg_no_changes_user, Toast.LENGTH_LONG)
                            .show();
                } else {
                    mListener.OnUpdateButtonCliked(updatedUser);
                }
            }
        });
        return rootView;
    }

    // Interactions ocurring on AccountInfoFragment
    public interface OnFragmentInteractionListener{
        void OnDeleteButtonCliked();
        void OnUpdateButtonCliked(Usuario updatedUser);
    }

    // inflate the menu for the fragment and define the actions
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //if(menu.findItem(R.id.action_share) == null)
        inflater.inflate(R.menu.menu_account_info_fragment, menu);    // add action items inside fragment

        MenuItem saveItem = menu.findItem(R.id.action_save);
        Log.d("AccountInfoFragment", "Save Button Clicked");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        getActivity().setTitle(R.string.accountInfoTitle);
    }
}
