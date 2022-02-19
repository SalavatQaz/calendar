package com.example.calendarv3.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.calendarv3.Fragments.Account;
import com.example.calendarv3.Fragments.FamilyCalendar;
import com.example.calendarv3.Fragments.MemberCalendar;
import com.example.calendarv3.Fragments.MemberList;
import com.example.calendarv3.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainPage extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FloatingActionButton addNewEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        addNewEventBtn = findViewById(R.id.addNewEventBtn);


        if(savedInstanceState == null) {
            MemberCalendar memberCalendar = new MemberCalendar();
            memberCalendar.setArguments(sendData());
            getSupportFragmentManager().beginTransaction().replace(R.id.content,memberCalendar).commit();
//            getSupportFragmentManager().
//                    beginTransaction().replace(R.id.content,new MemberCalendar()).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragmentSelected = null;
                switch (item.getItemId())
                {
                    case R.id.personCalendar:
                        fragmentSelected = new MemberCalendar();
                        break;
                    case R.id.familyCalendar:
                        fragmentSelected = new FamilyCalendar();
                        break;
                    case R.id.account:
                        fragmentSelected = new Account();
                        break;
                    case R.id.member:
                        fragmentSelected = new MemberList();
                }
                fragmentSelected.setArguments(sendData());
                getSupportFragmentManager().beginTransaction().replace(R.id.content,fragmentSelected).commit();
                return true;
            }
        });
        addNewEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //получение  familyId из Login
                Intent intentFrom = getIntent();
                String familyIDFromDB = intentFrom.getStringExtra("familyIDFromDB");
                String LoginFromDB = intentFrom.getStringExtra("LoginFromDB");

                Intent intent = new Intent(getApplicationContext(), CreateNewEvent.class);
                intent.putExtra("familyIDFromDB",familyIDFromDB);
                intent.putExtra("LoginFromDB",LoginFromDB);

                startActivity(intent);
            }
        });


    }
    private Bundle sendData() {
        Intent intent = getIntent();
        String LoginFromDB = intent.getStringExtra("LoginFromDB");
        String NameFromDB = intent.getStringExtra("NameFromDB");
        String LastnameFromDB = intent.getStringExtra("LastnameFromDB");
        String familyIDFromDB = intent.getStringExtra("familyIDFromDB");

        Bundle bundle = new Bundle();
        bundle.putString("LoginFromDB",LoginFromDB);
        bundle.putString("NameFromDB",NameFromDB);
        bundle.putString("LastnameFromDB",LastnameFromDB);
        bundle.putString("familyIDFromDB",familyIDFromDB);

        return bundle;
    }

}