package com.example.calendarv3.Fragments;

import static com.example.calendarv3.Models.FamilyEvent.COMPARE_BY_COUNT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.calendarv3.Adapters.FamilyEventAdapter;
import com.example.calendarv3.Adapters.MemberEventAdapter;
import com.example.calendarv3.Models.FamilyEvent;
import com.example.calendarv3.Models.MemberEvent;
import com.example.calendarv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class FamilyCalendar extends Fragment {
    private CalendarView calendarView;
    private ListView listView;
    private String familyIDFromDB;
    public FamilyCalendar() {
    }

    public static FamilyCalendar newInstance(String param1, String param2) {
        FamilyCalendar fragment = new FamilyCalendar();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_family_calendar, container, false);
        listView = (ListView) rootView.findViewById(R.id.familyEventList);
        calendarView = (CalendarView) rootView.findViewById(R.id.calendarOnFamilyPage);
        familyIDFromDB = this.getArguments().getString("familyIDFromDB");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Families").child(familyIDFromDB).child("Events");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            ArrayList events = getEventByDay((Map<String,Object>) snapshot.getValue(),dayOfMonth+"."+(1+month)+"."+year);
                            FamilyEventAdapter adapter = new FamilyEventAdapter(FamilyCalendar.this.getActivity(),events);
                            listView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return rootView;
    }



    private ArrayList getEventByDay(Map<String,Object> eventsMap, String day) {
        ArrayList<FamilyEvent> events = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : eventsMap.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
//            System.out.println(singleUser);
            //Get phone field and append to list

            if(singleUser.get("date").equals(day)){
                events.add((FamilyEvent) new FamilyEvent(singleUser.get("familyID").toString(),
                        day,
                        singleUser.get("time").toString(),
                        singleUser.get("title").toString(),
                        singleUser.get("desc").toString()));
            }
        }
        Collections.sort(events,COMPARE_BY_COUNT);
        return events;
    }
}