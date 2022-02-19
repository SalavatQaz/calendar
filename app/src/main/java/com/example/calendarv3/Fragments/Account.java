package com.example.calendarv3.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.calendarv3.Pages.Login;
import com.example.calendarv3.Pages.MainPage;
import com.example.calendarv3.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Account extends Fragment {


    TextView name,lastname,family_id;
    TextInputLayout editName,editLastname,editLogin,editPassword;
    Button editBtn, logOutBtn;

    public Account() {
    }

    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment__account, container, false);
        name = (TextView) rootView.findViewById(R.id.name);
        lastname = (TextView) rootView.findViewById(R.id.lastname);
        family_id = (TextView) rootView.findViewById(R.id.family_id);

        String memberLogin =  this.getArguments().getString("LoginFromDB");
        editName = (TextInputLayout) rootView.findViewById(R.id.editName);
        editLastname = (TextInputLayout) rootView.findViewById(R.id.editLastname);
        editLogin = (TextInputLayout) rootView.findViewById(R.id.editLogin);
        editPassword = (TextInputLayout) rootView.findViewById(R.id.editPassword);

        editBtn = (Button) rootView.findViewById(R.id.editBtn);
        logOutBtn = (Button) rootView.findViewById(R.id.logOutBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() | !validateLastname() | !validateLogin() | !validatePass()){
                    return;
                }else {
                    // найти запись совпадающую с логином
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members");
                    // изменить данные (все поля заполненные)
                    reference.child(memberLogin).child("name").setValue(editName.getEditText().getText().toString().trim());
                    reference.child(memberLogin).child("lastname").setValue(editLastname.getEditText().getText().toString().trim());
                    reference.child(memberLogin).child("login").setValue(editLogin.getEditText().getText().toString().trim());
                    reference.child(memberLogin).child("password").setValue(editPassword.getEditText().getText().toString().trim());
                    // сохранить новую запись
                    clearFields();
                }
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        showAllMemberInformation();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void showAllMemberInformation() {

        String NameFromDB = this.getArguments().getString("NameFromDB").toString();
        String LastnameFromDB = this.getArguments().getString("LastnameFromDB").toString();
        String familyIDFromDB = this.getArguments().getString("familyIDFromDB").toString();

        name.setText(NameFromDB);
        lastname.setText(LastnameFromDB);
        family_id.setText(familyIDFromDB);

    }
    private Boolean validateName(){
        String noSpaces = "\\A\\w{4,20}\\z";

        if (editName.getEditText().getText().toString().trim().isEmpty()){
            editName.setError("Заполните поле");
            return false;
        }
        else if (!editName.getEditText().getText().toString().trim().matches(noSpaces)){
            editName.setError("Имя не может содержать пробелы и специальные символы");
            return false;
        }else {
            editName.setError(null);
            editName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateLastname(){
        String noSpaces = "\\A\\w{4,20}\\z";
        if (editLastname.getEditText().getText().toString().trim().isEmpty()){
            editLastname.setError("Заполните поле");
            return false;
        } else if (!editLastname.getEditText().getText().toString().trim().matches(noSpaces)){
            editLastname.setError("Фамилия не может содержать пробелы и специальные символы");
            return false;
        }
        else {
            editLastname.setError(null);
            editLastname.setErrorEnabled(false);

            return true;
        }
    }
    private Boolean validateLogin(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members");
        Query checkUser = reference.orderByChild("login").equalTo(editLogin.getEditText().getText().toString());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editLogin.setError("Пользователь с таким логином уже существует");
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (editLogin.getEditText().getText().toString().trim().isEmpty()){
            editLogin.setError("Заполните поле");
            return false;
        }
        else {
            editLogin.setError(null);
            editLogin.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePass(){
        String noSpaces = "^" +
                "(?=.*[a-zA-z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\s+$)" +
                ".{4,}" +
                "$";

        if (editPassword.getEditText().getText().toString().trim().isEmpty()
                |editPassword.getEditText().getText().toString().length() < 6){
            editPassword.setError("Заполните поле. Не менее 6 символов");
            return false;
        }

        else if (editPassword.getEditText().getText().toString().trim().matches(noSpaces)){
            editPassword.setError("Слабый пароль");
            return false;
        }
        else {
            editPassword.setError(null);
            editPassword.setErrorEnabled(false);
            return true;
        }
    }
    private void clearFields(){
        editName.getEditText().setText("");
        editLastname.getEditText().setText("");
        editLogin.getEditText().setText("");
        editPassword.getEditText().setText("");
    }
}