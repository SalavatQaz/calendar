package com.example.calendarv3.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.calendarv3.Adapters.MemberEventAdapter;
import com.example.calendarv3.Adapters.MemberForListView;
import com.example.calendarv3.Models.MemberEvent;
import com.example.calendarv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class MemberCalendar extends Fragment {
    private CalendarView calendarView;


    public MemberCalendar() {
    }


    public static MemberCalendar newInstance(String param1, String param2) {
        MemberCalendar fragment = new MemberCalendar();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_person_calendar, container, false);
        calendarView = (CalendarView) rootView.findViewById(R.id.calendarOnPersonPage);

        String LoginFromDB = this.getArguments().getString("LoginFromDB");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //получаем логин пользователя

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Members").child(LoginFromDB).child("Events");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            ArrayList events = getEventByDay((Map<String,Object>) snapshot.getValue(),dayOfMonth+"."+(1+month)+"."+year);
                            MemberEventAdapter adapter = new MemberEventAdapter(MemberCalendar.this.getActivity(),events);
                            ListView listView = (ListView) rootView.findViewById(R.id.memberEventList);
                            listView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //вывод события по дням
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        CalendarView calendarView =(CalendarView) view.findViewById(R.id.calendarOnPersonPage);
//        String LoginFromDB = this.getArguments().getString("LoginFromDB");
//
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                //получаем логин пользователя
//
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members");
//                reference.child(LoginFromDB).child("Events");
//
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        ArrayList events = getEventByDay((Map<String,Object>) snapshot.getValue(),dayOfMonth+"."+(1+month)+"."+year);
//                        MemberEventAdapter adapter = new MemberEventAdapter(MemberCalendar.this.getActivity(),events);
//                        ListView listView = (ListView) view.findViewById(R.id.memberEventList);
//                        listView.setAdapter(adapter);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//                //вывод события по дням
//            }
//        });

    }
    private ArrayList getEventByDay(Map<String,Object> eventsMap, String day) {
        ArrayList<MemberEvent> events = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : eventsMap.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
//            System.out.println(singleUser);
            //Get phone field and append to list

            if(singleUser.get("date").equals(day)){
                events.add((MemberEvent) new MemberEvent(singleUser.get("memberID").toString(),
                        day,
                        singleUser.get("time").toString(),
                        singleUser.get("title").toString(),
                        singleUser.get("desc").toString()));
            }


        }
        return events;
    }

    //когда фрагмент становится видимым
    @Override
    public void onResume() {
        super.onResume();
    }
}