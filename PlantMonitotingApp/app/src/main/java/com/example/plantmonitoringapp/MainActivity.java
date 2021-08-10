package com.example.plantmonitoringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ArrayList<parameters> Weather = new ArrayList<>();
    ArrayList<String> H= new ArrayList<>();
    ArrayList<String> V= new ArrayList<>();
    String motorStatus ;
    ListView listView;
    Dialog Motor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Motor = new Dialog(this);
        new FetchData().execute();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new FetchData().execute();
            }
        },0,100000);

        listView = (ListView)findViewById(R.id.MainView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==3)
                {
                    showMotor(view);
                }
            }
        });
    }



    public void setList()
    {
        CustomAdapter customAdapter = new CustomAdapter(this,H,V);
        listView.setAdapter(customAdapter);
    }

    @SuppressLint("ResourceAsColor")
    public void showMotor(View v){
        Motor.setContentView(R.layout.motoronoff);

        Button OnOff = (Button)Motor.findViewById(R.id.MotorButton);
        TextView text = (TextView)Motor.findViewById(R.id.MotorStatus);
        ImageView image = (ImageView) Motor.findViewById(R.id.MotorImage);

        image.setImageResource(R.drawable.plant);

        text.setText("Status : "+motorStatus);
        if(motorStatus.equals("on"))
        {
            OnOff.setBackgroundColor(R.color.design_default_color_on_primary);
            OnOff.setText("OFF");
        }
        else {
            OnOff.setBackgroundColor(R.color.design_default_color_on_secondary);
            OnOff.setText("ON");
        }

        Motor.show();

        OnOff.setOnClickListener(new View.OnClickListener() {
            private Object Void;

            @Override
            public void onClick(View v) {
                new OnOffMotor().execute();
                new FetchData().execute();
                Toast.makeText(getApplicationContext(),"Loading Data",Toast.LENGTH_LONG);
                Motor.dismiss();
            }
        });
    }

    private class OnOffMotor extends AsyncTask<Void,Void,Void>{

        @Override
        public java.lang.Void doInBackground(java.lang.Void... voids) {
            Handler handler = new Handler();
            String query = "";
            if(motorStatus.equals("on")) {
                query = handler.httpServiceCall("https://plantmonitoringsystem.000webhostapp.com/api/update_motor.php?id=1&status=off");
            }
            else {
                query = handler.httpServiceCall("https://plantmonitoringsystem.000webhostapp.com/api/update_motor.php?id=1&status=on");
            }
            try {
                JSONObject jsonObject = new JSONObject(query);
                int success = jsonObject.getInt("success");
                if(success==1){
                    if(motorStatus.equals("on")){
                        motorStatus = "off";
                    }
                    else{
                        motorStatus = "on";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSON Parssing Error", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(java.lang.Void unused) {
            super.onPostExecute(unused);


        }

    }

    private class FetchData extends AsyncTask<Void,Void,Void> {

        @Override
        protected java.lang.Void doInBackground(java.lang.Void... voids) {
            Handler handler =new Handler();

            String jsonString = handler.httpServiceCall("https://plantmonitoringsystem.000webhostapp.com/api/read.php");

            try {
                Weather = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray name = jsonObject.getJSONArray("weather");
                for(int i=0;i<name.length();i++){
                    JSONObject jsonObject1 = name.getJSONObject(i);
                    String id = jsonObject1.getString("id");
                    String temp = jsonObject1.getString("temp");
                    String hum = jsonObject1.getString("hum");
                    String moist = jsonObject1.getString("moist");
                    Weather.add(new parameters(id,temp,hum,moist));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "JSON Parssing Error", Toast.LENGTH_LONG).show();
            }


            String motorString = handler.httpServiceCall("https://plantmonitoringsystem.000webhostapp.com/api/read_all.php?id=1");

            try {
                JSONObject jsonObject = new JSONObject(motorString);
                JSONArray m = jsonObject.getJSONArray("led");
                for(int i=0;i<m.length();i++){
                    JSONObject jsonObject1 = m.getJSONObject(i);
                    motorStatus = jsonObject1.getString("status");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(java.lang.Void unused) {
            super.onPostExecute(unused);
            H = new ArrayList<>();
            H.add("Tempearture");
            H.add("Humidity");
            H.add("Moisture");
            H.add("Motor");
            V = new ArrayList<>();
            V.add(Weather.get(Weather.size()-1).temp);
            V.add(Weather.get(Weather.size()-1).hum);
            V.add(Weather.get(Weather.size()-1).moist);
            V.add(motorStatus);

            setList();
        }
    }

}