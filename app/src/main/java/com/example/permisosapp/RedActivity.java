package com.example.permisosapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.example.permisosapp.Utilidades.ComprobarConexion;

public class RedActivity extends AppCompatActivity {

RadioButton hayInternet, sinInternet, conexWifi, conexRed, conexOtro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);

        hayInternet = findViewById(R.id.tienes_internetRB);
        sinInternet = findViewById(R.id.no_tienes_internetRB);
        conexWifi = findViewById(R.id.tiras_wifiRB);
        conexRed = findViewById(R.id.tirasRedRB);
        conexOtro = findViewById(R.id.tiras_otroRB);



        ComprobarConexion.hayConexion3G4G(this);
        ComprobarConexion.hayInternet(this);
        ComprobarConexion.isWifiAvailable(this);




    }

/*
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.tienes_internetRB:
                if (checked) {

                    Log.d("MIAPP" , "TIENES INTERNET");

                }

                    break;
            case R.id.no_tienes_internetRB:
                if (checked) {
                    Log.d("MIAPP" , "NO TIENES INTERNET");
                }

                    break;
            case R.id.tiras_wifiRB:
                if (checked) {
                    Log.d("MIAPP" , "TIRAS DE WIFI");
                }
                break;
            case R.id.tirasRedRB:
                if (checked) {
                    Log.d("MIAPP" , "TIRAS DE RED");
                }
                break;
            case R.id.tiras_otroRB:
                if (checked) {
                    Log.d("MIAPP" , "TIRAS DE OTRO");
                }
                break;
        }
    }
*/
}
