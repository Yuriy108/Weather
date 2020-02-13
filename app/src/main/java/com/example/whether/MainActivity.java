package com.example.whether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView textView;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);

        address="https://api.openweathermap.org/data/2.5/weather?q=%s&APPID=0b9155d166c3a8005569afd826b7a71f";
    }

    public void getweather(View view) {
        cityName=findViewById(R.id.editText);
        String name=cityName.getText().toString().trim();
        String url=String.format(address,name);
        if(cityName!=null){
            DownloadWeather downloadWeather=new DownloadWeather();
            try {
                String a=  downloadWeather.execute(url).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    class DownloadWeather extends AsyncTask<String,Void,String>{
        StringBuilder result= new StringBuilder();
        URL url=null;
        HttpURLConnection httpURLConnection=null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                url=new URL(strings[0]);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String line= bufferedReader.readLine();
                while (line!=null){
                    result.append(line);
                    line=bufferedReader.readLine();

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {


            try {
                JSONObject jsonObject=new JSONObject(s);
                String cityName=jsonObject.getString("name");
                String temp=jsonObject.getJSONObject("main").getString("temp");
                String discr=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
                String resultWeather=String.format("%s\nТемпература: %s\nОписание :%s",cityName,temp,discr);
                Log.i("Result",resultWeather);
                textView.setText(resultWeather);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
