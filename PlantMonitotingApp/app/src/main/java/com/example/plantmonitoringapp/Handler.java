package com.example.plantmonitoringapp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Handler {

    public Handler(){

    }

    public String httpServiceCall(String requestUrl){
        String result = null;
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while((line = reader.readLine())!=null){
                buffer.append(line+"\n");
                //Log.d("Responce : ",">"+line);
            }
            connection.disconnect();
            //Log.d("Responce :",">"+buffer.toString());
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String httpServiceCall(String requestUrl,String type){
        String result = null;
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(type);

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while((line = reader.readLine())!=null){
                buffer.append(line+"\n");
                //Log.d("Responce : ",">"+line);
            }
            connection.disconnect();
            //Log.d("Responce :",">"+buffer.toString());
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
