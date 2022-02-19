package com.example.calendarv3.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.calendarv3.Models.MemberEvent;
import com.example.calendarv3.R;

import java.util.ArrayList;

public class MemberEventAdapter extends ArrayAdapter<MemberEvent> {
    private final Activity activity;
    private final ArrayList<MemberEvent> memberEvents;

    public MemberEventAdapter(Activity activity, ArrayList<MemberEvent> memberEvents) {
        super(activity, R.layout.custom_listview_event, memberEvents);
        this.activity = activity;
        this.memberEvents = memberEvents;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_listview_event, null,true);

        TextView title = (TextView) rowView.findViewById(R.id.titleListView);
        TextView desc = (TextView) rowView.findViewById(R.id.descListView);
        TextView time = (TextView) rowView.findViewById(R.id.timeListView);

        title.setText(memberEvents.get(position).getTitle());
        desc.setText(memberEvents.get(position).getDesc());
        time.setText(memberEvents.get(position).getTime());
        return rowView;
    }
}
