package com.example.permisosapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.permisosapp.Adapters.AdapterCuentas;

public class CuentasActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdapterCuentas adapterCuentas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas);

        recyclerView = findViewById(R.id.recycler_cuentas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));



        adapterCuentas = new AdapterCuentas();

        recyclerView.setAdapter(adapterCuentas);






    }
}
