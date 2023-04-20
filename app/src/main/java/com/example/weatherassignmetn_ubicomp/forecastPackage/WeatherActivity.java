package com.example.weatherassignmetn_ubicomp.forecastPackage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.weatherassignmetn_ubicomp.R;

public class WeatherActivity {

    public String location;
    public String desc;
    public String temp;



    public WeatherActivity(String location, String desc, String temp){

        this.location = location;
        this.desc = desc;
        this.temp = temp;
    }//close WeatherActivity




}