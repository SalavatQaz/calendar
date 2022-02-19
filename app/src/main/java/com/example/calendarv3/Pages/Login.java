package com.example.calendarv3.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendarv3.Fragments.Account;
import com.example.calendarv3.Models.Member;
import com.example.calendarv3.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class Login extends AppCompatActivity {

    private Button createNewAccountBtn,signInBtn;
    private ImageView image;
    private TextView logoText,sloganText;
    private TextInputLayout login,password;

    private FirebaseAuth mAuth;


    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        root = findViewById(R.id.root_element);

        image = (ImageView) findViewById(R.id.logoImage);
        logoText = (TextView) findViewById(R.id.logoName);
        sloganText = (TextView) findViewById(R.id.signInToContinue);

        login = (TextInputLayout) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.password);

        signInBtn = (Button) findViewById(R.id.signInBtn);
        createNewAccountBtn = (Button) findViewById(R.id.createNewAccountBtn);

        mAuth = FirebaseAuth.getInstance();


        createNewAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logoText,"logo_text");
                pairs[2] = new Pair<View,String>(sloganText,"logo_desc");
                pairs[3] = new Pair<View,String>(login,"anim_login");
                pairs[4] = new Pair<View,String>(password,"anim_pass");
                pairs[5] = new Pair<View,String>(signInBtn,"btn_in");
                pairs[6] = new Pair<View,String>(createNewAccountBtn,"btn_newacc");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateLogin() | !validatePassword()) return;
                else isUser();

//                mAuth.signInWithEmailAndPassword(login.getEditText().getText().toString().trim(),password.getEditText().getText().toString())
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) startActivity(new Intent(Login.this, MainPage.class));
//                                else Toast.makeText(Login.this,"Ошибка автоизация",Toast.LENGTH_LONG).show();
//                            }
//                        });
            }
        });

    }
    private void isUser(){
        final String UserEnteredLogin = login.getEditText().getText().toString().trim();
        final String UserEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members");

        Query checkUser = reference.orderByChild("login").equalTo(UserEnteredLogin);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    login.setError(null);
                    login.setErrorEnabled(false);
                    String passwordFromDB = snapshot.child(UserEnteredLogin).child("password").getValue(String.class);
                    if (passwordFromDB.equals(UserEnteredPassword)){
                        password.setError(null);
                        password.setErrorEnabled(false);
                        String LoginFromDB = snapshot.child(UserEnteredLogin).child("login").getValue(String.class);
                        String nameFromDB = snapshot.child(UserEnteredLogin).child("name").getValue(String.class);
                        String lastnameFromDB = snapshot.child(UserEnteredLogin).child("lastname").getValue(String.class);
                        String familyIDFromDB = snapshot.child(UserEnteredLogin).child("familyID").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), MainPage.class);
                        intent.putExtra("LoginFromDB",LoginFromDB);
                        intent.putExtra("NameFromDB",nameFromDB);
                        intent.putExtra("LastnameFromDB",lastnameFromDB);
                        intent.putExtra("familyIDFromDB",familyIDFromDB);
                        intent.putExtra("PasswordFromDB",UserEnteredPassword);
                        startActivity(intent);

//                        startActivity(new Intent(Login.this,MainPage.class));
                    }
                    else {
                        password.setError("Введен неверный пароль");
                        password.requestFocus();
                    }
                }else{
                    login.setError("Пользователь не найден");
                    login.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private Boolean validateLogin(){
        if (login.getEditText().getText().toString().trim().isEmpty()){
            login.setError("Заполните поле");
            return false;
        }else {
            login.setError(null);
            login.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword(){

        if (password.getEditText().getText().toString().trim().isEmpty() | password.getEditText().getText().toString().trim().length() < 6){
            password.setError("Заполните поле. Не менее 6 символов");
            return false;
        }else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

}