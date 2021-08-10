package com.example.plantmonitoringapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {

    ArrayList<String> Headers;
    ArrayList<String> Values;
    Context context ;


    public CustomAdapter(@NonNull MainActivity context, ArrayList<String>Headers, ArrayList<String>Values) {
        super(context, R.layout.item,Headers);

        this.Headers = Headers;
        this.Values = Values;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        TextView Name = (TextView)v.findViewById(R.id.ItemText);
        ImageView image = (ImageView)v.findViewById(R.id.ItemImage);
        TextView Value = (TextView)v.findViewById(R.id.ItemValue);
        if(position==0)
        {
            image.setImageResource(R.drawable.tepearture);
        }
        else if(position==1){
            image.setImageResource(R.drawable.humid);
        }
        else if(position==2) {
            image.setImageResource(R.drawable.moist1);
        }
        else {
            image.setImageResource(R.drawable.plant);
        }
        Name.setText(Headers.get(position).toString());
        Value.setText(Values.get(position).toString());

        return v;
    }
}
