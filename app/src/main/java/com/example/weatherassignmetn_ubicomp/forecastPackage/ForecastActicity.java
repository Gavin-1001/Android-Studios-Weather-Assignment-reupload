package com.example.weatherassignmetn_ubicomp.forecastPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherassignmetn_ubicomp.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastActicity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_acticity);

        Intent intent = getIntent();
        String location = intent.getExtras().getString("location");
        String days = intent.getExtras().getString("days");
        String rain = intent.getExtras().getString("rain");

        TextView daysForecast = findViewById(R.id.daysForecast);
        daysForecast.setText("Days Forecast" +days);

    }//close onCreate


    public void apiProcessing(final String location, final String days){ //change afterwards //remove final if possible

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //16 day forecast isn't free with openweather, I found weatherbit.io which does have a free 16 day api
                //.url("api.openweathermap.org/data/2.5/forecast/daily?q" + location + "&cnt=" +days+ "&appid=4616b16851daa77e0e064e1b87acd6da").

                .url("https://api.weatherbit.io/v2.0/forecast/daily?=" + location + "=Dublin&days="+days+"&key=ef31a2e610314206a878826f34819369")
                .get()
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{

            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback(){

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String responseData = response.body().toString();
                    if(!responseData.isEmpty()){
                        try{
                            JSONObject jsonOjbect = new JSONObject(responseData);
                            JSONArray jsonarray = jsonOjbect.getJSONArray("data");

                            int numOfDays = Integer.parseInt(days);
                            ArrayList<WeatherActivity> forecastList = new ArrayList<>();
                            List<WeatherActivity> List = new ArrayList<>();

                            for(int i = 0; i < numOfDays; i++){
                                JSONObject object = jsonarray.getJSONObject(i);
                                JSONObject weatherObject = object.getJSONObject("weather");
                                String desc = weatherObject.getString("description");
                                double temperature = object.getDouble("temp");
                                String temp = Math.round(temperature) + "°C";
                                WeatherActivity listWeather = new WeatherActivity(location, desc, temp);
                                List.add(listWeather);
                            }//close for
                        }catch(JSONException e){
                            e.printStackTrace();
                        }//close try/catch
                    }//close if
                }//close onResponse

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }//close onFailure
            });

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }//close apiKey
}//close ForecastActivity