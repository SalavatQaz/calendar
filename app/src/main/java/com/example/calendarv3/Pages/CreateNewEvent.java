package com.example.calendarv3.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.calendarv3.Models.FamilyEvent;
import com.example.calendarv3.Models.MemberEvent;
import com.example.calendarv3.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNewEvent extends AppCompatActivity {


    private TextView dataPicker,timePicker;
    private DatePickerDialog.OnDateSetListener setListener;
    private Button backBtn,endCreateEventBtn;
    private int hour,min;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextInputLayout eventTitle,eventDesc;
    private DatabaseReference reference,ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);

        dataPicker = findViewById(R.id.selectedDay);
        timePicker = findViewById(R.id.selectedTime);
        backBtn = findViewById(R.id.back);
        radioGroup = findViewById(R.id.radioGroup);
        endCreateEventBtn = findViewById(R.id.endCreateEvent);
        eventTitle = findViewById(R.id.eventTitle);
        eventDesc = findViewById(R.id.eventDesc);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dataPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateNewEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               dataPicker.setText(dayOfMonth + "." + (month+=1) + "."+year);

            }
        };
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateNewEvent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                min = minute;
                                String time = hour+":"+min;
                                SimpleDateFormat f24 = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24.parse(time);
                                    timePicker.setText(f24.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },24,0,true);
                timePickerDialog.updateTime(hour,min);
                timePickerDialog.show();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNewEvent.this,MainPage.class));
                finish();
            }
        });

        endCreateEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateTitle() | !validateDecs() | !validateTime() | !validateDate()){
                    return;
                }else {
                    //инфа с выбора типа события
                    int radioID = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioID);
                    //текст с кнопки

                    if(radioButton.getText().toString().equals("Личное событие")) {

                        Intent intentFrom = getIntent();
                        String LoginFromDB = intentFrom.getStringExtra("LoginFromDB");

                        reference = FirebaseDatabase.getInstance().getReference("Members");
                        // проверить существует ли уже такой день

                        reference.child(LoginFromDB).child("Events").child(eventTitle.getEditText().getText().toString())
                                .setValue(new MemberEvent(
                                        LoginFromDB,
                                        dataPicker.getText().toString(),
                                        timePicker.getText().toString(),
                                        eventTitle.getEditText().getText().toString(),
                                        eventDesc.getEditText().getText().toString()));

                        startActivity(new Intent(CreateNewEvent.this, MainPage.class));
                        finish();
                    } else {
                        System.out.println("Семейное событие");
                        Intent intentFrom = getIntent();
                        String familyIDFromDB = intentFrom.getStringExtra("familyIDFromDB");

                        reference = FirebaseDatabase.getInstance().getReference("Families");
                        reference.child(familyIDFromDB).child("Events").child(eventTitle.getEditText().getText().toString())
                                .setValue(new FamilyEvent(familyIDFromDB,
                                        dataPicker.getText().toString(),
                                        timePicker.getText().toString(),
                                        eventTitle.getEditText().getText().toString(),
                                        eventDesc.getEditText().getText().toString()));

                        startActivity(new Intent(CreateNewEvent.this, MainPage.class));
                        finish();
                    }
                }
            }
        });
    }
    private void createMemberEvent(String day){
        String LoginFromDB = getIntent().getStringExtra("LoginFromDB");
        String familyIDFromDB = getIntent().getStringExtra("familyIDFromDB");

        ref = FirebaseDatabase.getInstance().getReference("Members");
        ref.child(LoginFromDB).child("Events");
        Query checkDay = ref.orderByChild("date").equalTo(day);
        checkDay.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    reference = FirebaseDatabase.getInstance().getReference("Members");
                    reference.child(LoginFromDB).child("Events").child(day).child(eventTitle.getEditText().getText().toString())
                            .setValue(new FamilyEvent(familyIDFromDB,
                                    dataPicker.getText().toString(),
                                    timePicker.getText().toString(),
                                    eventTitle.getEditText().getText().toString(),
                                    eventDesc.getEditText().getText().toString()));
                    startActivity(new Intent(CreateNewEvent.this, MainPage.class));
                    finish();
                }
                else{
                    reference = FirebaseDatabase.getInstance().getReference("Members");
                    reference.child(LoginFromDB).child("Events").child(day).child(eventTitle.getEditText().getText().toString())
                            .setValue(new FamilyEvent(familyIDFromDB,
                                    dataPicker.getText().toString(),
                                    timePicker.getText().toString(),
                                    eventTitle.getEditText().getText().toString(),
                                    eventDesc.getEditText().getText().toString()));
                    startActivity(new Intent(CreateNewEvent.this, MainPage.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private boolean validateTitle(){
        if (eventTitle.getEditText().getText().toString().trim().isEmpty()){
            eventTitle.setError("Заполните поле");
            return false;
        }else {
            eventTitle.setError(null);
            eventTitle.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateDecs(){
        if (eventDesc.getEditText().getText().toString().trim().isEmpty()){
            eventDesc.setError("Заполните поле");
            return false;
        }else {
            eventDesc.setError(null);
            eventDesc.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateDate(){
        if (dataPicker.getText().toString().isEmpty()){
            dataPicker.setError("Заполните поле");
            return false;
        }
        else{
            dataPicker.setError(null);
            return true;
        }
    }
    private boolean validateTime(){
        if (timePicker.getText().toString().isEmpty()){
            timePicker.setError("Заполните поле");
            return false;
        }
        else{
            timePicker.setError(null);
            return true;
        }
    }

}