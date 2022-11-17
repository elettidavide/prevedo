package com.example.prevedo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ActivitiesAdapter extends ArrayAdapter<String>{

    private final Context context;
    private ArrayList<String> activitiesArrayList;
    private Activity parentActivity;

    public ActivitiesAdapter(Context context, ArrayList<String> activitiesArrayList,
                             Activity parentActivity) {
        super(context, R.layout.activities_adapter, activitiesArrayList);
        this.context = context;
        this.activitiesArrayList = activitiesArrayList;
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return activitiesArrayList.size();
    }//il metodo di sotto lo itera tante volte in base alla dimensione dell'arraylist


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.activities_adapter, parent, false);

        TextView activityName =  convertView
                .findViewById(R.id.activityNameTxt);
        activityName.setText(activitiesArrayList.get(position));

        return convertView;
    }
}

