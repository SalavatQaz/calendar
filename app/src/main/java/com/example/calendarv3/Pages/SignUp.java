package com.example.calendarv3.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarv3.Models.Family;
import com.example.calendarv3.Models.Member;
import com.example.calendarv3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private ImageView image;
    private TextView logoText,sloganText;
    private TextInputLayout name,lastname,login,pass1,pass2,id_family;
    private Button endRegistrationBtn,alreadyHaveAccount;

    private FirebaseAuth mAuth;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private ScrollView root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        image = (ImageView)  findViewById(R.id.logoImage);

        logoText = (TextView) findViewById(R.id.logoText);
        sloganText = (TextView) findViewById(R.id.sloganText);

        name = (TextInputLayout) findViewById(R.id.name);
        lastname = (TextInputLayout) findViewById(R.id.lastname);
        login = (TextInputLayout) findViewById(R.id.login);

        pass1 = (TextInputLayout) findViewById(R.id.password);
        pass2 = (TextInputLayout) findViewById(R.id.password2);

        id_family = (TextInputLayout) findViewById(R.id.familyId);

        endRegistrationBtn = (Button) findViewById(R.id.endRegistration);
        alreadyHaveAccount = (Button) findViewById(R.id.backToLogin);

        mAuth = FirebaseAuth.getInstance();

        root = (ScrollView) findViewById(R.id.root_signUp);

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              backToLoginPage();
            }
        });

        endRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() | !validateLastname() | !validateLogin() | !validatePass() | !validateFamilyID()){
                    return;
                }else {

                    reference = FirebaseDatabase.getInstance().getReference("Members");
                    reference.child(login.getEditText().getText().toString().trim()).setValue(new Member(
                            name.getEditText().getText().toString().trim(),
                            lastname.getEditText().getText().toString().trim(),
                            login.getEditText().getText().toString().trim(),
                            pass1.getEditText().getText().toString().trim(),
                            id_family.getEditText().getText().toString().trim()
                    ));

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Families");
                    Query checkUser = databaseReference.orderByChild("familyID").equalTo(id_family.getEditText().getText().toString());
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) return;
                            else{
                                reference = FirebaseDatabase.getInstance().getReference("Families");
                                reference.child(id_family.getEditText().getText().toString().trim()).setValue(new Family(
                                        id_family.getEditText().getText().toString()
                                ));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    backToLoginPage();
                }
            }
        });
    }
    private void backToLoginPage(){
        Intent intent = new Intent(SignUp.this, Login.class);

        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View,String>(image,"logo_image");
        pairs[1] = new Pair<View,String>(logoText,"logo_text");
        pairs[2] = new Pair<View,String>(sloganText,"logo_desc");
        pairs[3] = new Pair<View,String>(name,"anim_login");
        pairs[4] = new Pair<View,String>(lastname,"anim_pass");
        pairs[5] = new Pair<View,String>(endRegistrationBtn,"btn_in");
        pairs[6] = new Pair<View,String>(alreadyHaveAccount,"btn_newacc");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
        startActivity(intent,options.toBundle());
        finish();

    }
    private Boolean validateName(){
        String noSpaces = "\\A\\w{4,20}\\z";

        if (name.getEditText().getText().toString().trim().isEmpty()){
            name.setError("Заполните поле");
            return false;
        }
        else if (!name.getEditText().getText().toString().trim().matches(noSpaces)){
            name.setError("Имя не может содержать пробелы и специальные символы");
            return false;
        }else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateLastname(){
        String noSpaces = "\\A\\w{4,20}\\z";
        if (lastname.getEditText().getText().toString().trim().isEmpty()){
            lastname.setError("Заполните поле");
            return false;
        } else if (!lastname.getEditText().getText().toString().trim().matches(noSpaces)){
            lastname.setError("Фамилия не может содержать пробелы и специальные символы");
            return false;
        }
        else {
            lastname.setError(null);
            lastname.setErrorEnabled(false);

            return true;
        }
    }
    private Boolean validateLogin(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members");
        Query checkUser = reference.orderByChild("login").equalTo(login.getEditText().getText().toString());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    login.setError("Пользователь с таким логином уже существует");
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (login.getEditText().getText().toString().trim().isEmpty()){
            login.setError("Заполните поле");
            return false;
        }
        else {
            login.setError(null);
            login.setErrorEnabled(false);
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

        if (pass2.getEditText().getText().toString().trim().isEmpty() | pass1.getEditText().getText().toString().trim().isEmpty()
                |pass1.getEditText().getText().toString().length() < 6 | pass2.getEditText().getText().toString().length() < 6){
            pass2.setError("Заполните поле. Не менее 6 символов");
            pass1.setError("Заполните поле. Не менее 6 символов");
            return false;
        }

        else if (pass1.getEditText().getText().toString().trim().matches(noSpaces)){
            pass1.setError("Слабый пароль");
            return false;
        } else if (pass2.getEditText().getText().toString().trim().matches(noSpaces)){
            pass2.setError("Слабый пароль");
            return false;
        }

        else if (pass1.getEditText().getText().toString().trim().isEmpty()){
            pass1.setError("Заполните поле");
            return false;
        }
        else if (!pass1.getEditText().getText().toString().trim().equals(pass2.getEditText().getText().toString().trim())){
            pass1.setError("Пароли должны совпадать");
            pass2.setError("Пароли должны совпадать");
            return false;
        }
        else {
            pass1.setError(null);
            pass2.setError(null);
            pass1.setErrorEnabled(false);
            pass2.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateFamilyID(){
        String noSpaces = "\\A\\w{4,5}\\z";
        if (id_family.getEditText().getText().toString().trim().isEmpty()){
            id_family.setError("Заполните поле");
            return false;
        }else if (!id_family.getEditText().getText().toString().trim().matches(noSpaces)){
            id_family.setError("ID семьи не может содержать пробелы и специальные символы");
            return false;
        }
        else {
            id_family.setError(null);
            id_family.setErrorEnabled(false);
            return true;
        }
    }

}