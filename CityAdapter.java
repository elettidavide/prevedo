package com.example.prevedo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CityAdapter extends ArrayAdapter<String>{

    private final Context context;
    private ArrayList<String> cityArrayList;
    private Activity parentActivity;

    public CityAdapter(Context context, ArrayList<String> cityArrayList,
                       Activity parentActivity) {
        super(context, R.layout.city_adapter, cityArrayList);
        this.context = context;
        this.cityArrayList = cityArrayList;
        this.parentActivity = parentActivity;

    }

    @Override
    public int getCount() {
        return cityArrayList.size();
    } //il metodo di sotto lo itera tante volte in base alla dimensione dell'arraylist

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.city_adapter, parent, false);

        TextView cityName =  convertView
                .findViewById(R.id.cityNameTxt);
        cityName.setText(cityArrayList.get(position));

        return convertView;
    }


}

