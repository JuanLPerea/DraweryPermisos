package com.example.permisosapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.example.permisosapp.R;

class CuentasViewHolder extends RecyclerView.ViewHolder {

    TextView nombreRow, tipoRow;

    public CuentasViewHolder(@NonNull View itemView) {
        super(itemView);

        nombreRow = itemView.findViewById(R.id.nombre_row);
        tipoRow = itemView.findViewById(R.id.tipo_row);

    }


    public void cargarDatos(String datos){

        String[] splitdatos = datos.split("#");

        nombreRow.setText(splitdatos[0]);
        tipoRow.setText(splitdatos[1]);



    }

}
