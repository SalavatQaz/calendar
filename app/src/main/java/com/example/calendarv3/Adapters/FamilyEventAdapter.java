package com.example.calendarv3.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendarv3.Models.FamilyEvent;
import com.example.calendarv3.Models.MemberEvent;
import com.example.calendarv3.R;

import java.util.ArrayList;

public class FamilyEventAdapter extends ArrayAdapter<FamilyEvent> {
    private final Activity activity;
    private final ArrayList<FamilyEvent> familyEvents;

    public FamilyEventAdapter(Activity activity, ArrayList<FamilyEvent> familyEvents) {
        super(activity, R.layout.custom_listview_event, familyEvents);
        this.activity = activity;
        this.familyEvents = familyEvents;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_listview_event, null,true);

        TextView title = (TextView) rowView.findViewById(R.id.titleListView);
        TextView desc = (TextView) rowView.findViewById(R.id.descListView);
        TextView time = (TextView) rowView.findViewById(R.id.timeListView);

        title.setText(familyEvents.get(position).getTitle());
        desc.setText(familyEvents.get(position).getDesc());
        time.setText(familyEvents.get(position).getTime());
        return rowView;
    }
}
