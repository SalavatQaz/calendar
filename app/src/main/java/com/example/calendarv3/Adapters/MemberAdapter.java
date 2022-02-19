package com.example.calendarv3.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.calendarv3.R;
import java.util.ArrayList;

public class MemberAdapter  extends ArrayAdapter<MemberForListView> {
    private final Activity activity;
    private final ArrayList<MemberForListView> members;

    public MemberAdapter(Activity activity, ArrayList<MemberForListView> members) {
        super(activity, R.layout.custom_listview_members, members);
        this.activity = activity;
        this.members = members;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_listview_members, null,true);

        TextView name = (TextView) rowView.findViewById(R.id.nameListView);
        TextView lastname = (TextView) rowView.findViewById(R.id.lastnameListView);

        name.setText(members.get(position).getName());
        lastname.setText(members.get(position).getLastname());

        return rowView;
    }
}
