package com.example.weatherassignmetn_ubicomp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherassignmetn_ubicomp.forecastPackage.ForecastActicity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView editTextLocation, editTextDays;
    Button buttonLocation, buttonForecast, buttonPreferences;
    Spinner dropdown;
    String[] rainThresholds = new String[] {"None ,Light, Moderate, Heavy"};

    private static String currentLocation;
    private String days;
    private int rain;

    FusedLocationProviderClient fLPClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fLPClient = LocationServices.getFusedLocationProviderClient(this);
        dropdown = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, rainThresholds);
        dropdown.setAdapter(adapter);
        buttonLocation = findViewById(R.id.buttonLocation);
        buttonForecast = findViewById(R.id.buttonForecast);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDays = findViewById(R.id.editTextDays);

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }//close else
            }//close onClick
        });
        buttonForecast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent forecastIntent = new Intent(MainActivity.this, ForecastActicity.class);
                String location = ((TextView)findViewById(R.id.editTextLocation)).getText().toString();
                String days = ((TextView)findViewById(R.id.editTextDays)).getText().toString();
                String rain = dropdown.getSelectedItem().toString();

                forecastIntent.putExtra("location", location);
                forecastIntent.putExtra("days", days);
                forecastIntent.putExtra("rain", rain);

            }//close onClick
        });//close forecastButtonListener
    }//close onCreate

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length > 0 && (grantResults[0]+grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }else{
            Toast.makeText(getApplicationContext(), "Error! Please allow permissions and try again", Toast.LENGTH_SHORT).show();
        }//close if
    }//close onRequestPermissionsResult()
    @SuppressLint("MissingPermission")
    private void getCurrentLocation(){

        fLPClient.getLastLocation().addOnCompleteListener(task -> {

            Location location = task.getResult();
            if(location != null){

                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try{
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(),1
                    );
                    System.out.println(addresses.get(0).getLocality());
                    editTextLocation.setText(addresses.get(0).getLocality());
                }catch(IOException ex){
                    ex.printStackTrace();
                }//close catch
            }else{
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }//close if
        });//close fusedLocationProviderClient()
    }//close getCurrentLocaiton
    public static String returnLocation(){
        return currentLocation;
    }//close returnLocation()
}//close MainActivity