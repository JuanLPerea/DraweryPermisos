package com.example.permisosapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.permisosapp.R;

public class AdapterCuentas extends RecyclerView.Adapter<CuentasViewHolder> {


    @NonNull
    @Override
    public CuentasViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CuentasViewHolder cocheViewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        final View itemView;

        //  if (viewType == HEADER) {
        //      itemView = inflater.inflate(R.layout.header_layout, parent , false);
        // } else {
        itemView = inflater.inflate(R.layout.recycler_row, viewGroup , false);
        // }

        //itemView.setOnClickListener(this);


        cocheViewHolder = new  CuentasViewHolder (itemView);

        return cocheViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CuentasViewHolder holder, int i) {
      //  Coche libro = datos.get(position);

        holder.cargarDatos("Ejemplo#" + ((int) (Math.random() * 100)));
        holder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return 100;
    }
}
