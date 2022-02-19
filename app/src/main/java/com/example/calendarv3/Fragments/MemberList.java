package com.example.calendarv3.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.calendarv3.Adapters.MemberAdapter;
import com.example.calendarv3.Adapters.MemberForListView;
import com.example.calendarv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class MemberList extends Fragment {

    public MemberList() {
    }

    public static MemberList newInstance(String param1, String param2) {
        MemberList fragment = new MemberList();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_member_list, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String familyIDFromDB = this.getArguments().getString("familyIDFromDB");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Members");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<MemberForListView> user = getMembersByFamilyID((Map<String,Object>) dataSnapshot.getValue(),familyIDFromDB);
                        MemberAdapter adapter = new MemberAdapter(MemberList.this.getActivity(),user);
                        ListView listView = (ListView) view.findViewById(R.id.memberList);
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }

                });

    }

    private ArrayList getMembersByFamilyID(Map<String,Object> users, String familyID) {

        ArrayList<MemberForListView> members = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            if(singleUser.get("familyID").equals(familyID)){
                members.add((MemberForListView) new MemberForListView(singleUser.get("name").toString(),singleUser.get("lastname").toString()));
            }
        }
        return members;
    }
}