package com.example.permisosapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FotoActivity extends AppCompatActivity {

    private static final String[] PERMISOS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String PREFIJO_FOTOS = "CURSO_PIC";
    private static final String SUFIJO_FOTOS = ".jpg";
    private String ruta_foto;     // Nombre del fichero creado para la foto


    private static final int CODIGO_PETICION_SELECCIONAR_FOTO = 100;
    private static final int CODIGO_PETICION_HACER_FOTO = 200;
    private static final int CODIGO_PETICION_PERMISOS = 150;
    private ImageView imagen;
    private Uri fotoURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        imagen = findViewById(R.id.fotoIV);


        // Crear un menú contextual
        registerForContextMenu(imagen);
        //    imagen.setOnCreateContextMenuListener(this);

        // imagen.setScaleType(ImageView.ScaleType.FIT_XY);

        // PEDIR PERMISOS PARA LA FOTO
        ActivityCompat.requestPermissions(this, PERMISOS, CODIGO_PETICION_PERMISOS);


        // Comprobar si tenemos foto guardada
        SharedPreferences ficherosp = getSharedPreferences("ARCHIVOFOTO", MODE_PRIVATE);
        String ruta_foto_guardada = ficherosp.getString("RUTAFOTO", null);
        Log.d("MIAPP", "HAY FOTO EN SHARED: " + ruta_foto_guardada);

        if (ruta_foto_guardada != null) {
            fotoURI = Uri.parse(ruta_foto_guardada);
            cargarImagenInicio();
        }


        imagen.setImageBitmap(getRoundedCornerBitmap(imagen, 200));

    }


    // Se invoca para dibujar nuestro menú contextual
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_borrarfoto, menu);
        menu.setHeaderTitle("Selecciona Opción");

        Log.d("MIAPP", "Menú foto");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        Log.d("MIAPP", "Seleccionado item " + item.getItemId());


        // Dialogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage("¿Desea eliminar la foto?")
                .setTitle("Confirme por favor");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Si quiere borrar la foto del Image View y del dispositivo
                imagen.setImageBitmap(null);
                fotoURI = null;
                guardarFotoSharedPreferences();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelar (No hay que hacer nada, se cierra todo)
                Log.d("MIAPP", "El usuario Canceló el borrado");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


        return super.onContextItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("MIAPP", "Me ha concedido todos los permisos");

        } else {
            Log.d("MIAPP", "No ha concedido los permisos");
            Toast.makeText(this, "No puedes seguir sin conceder permisos!!", Toast.LENGTH_LONG).show();
            this.finish();
        }


    }

    private void desactivarModoEstricto() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                //se usa la reflexión para desactivar el modo estricto
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void tomarFoto(View view) {
        Log.d("MIAPP", "Quiere hacer una foto");

        Intent intent_foto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.fotoURI = crearFicheroImagen();
        intent_foto.putExtra(MediaStore.EXTRA_OUTPUT, this.fotoURI);
        desactivarModoEstricto();
        startActivityForResult(intent_foto, CODIGO_PETICION_HACER_FOTO);


    }

    private Uri crearFicheroImagen() {

        File file = null;

        Uri uri_destino = null;

        String momento_actual = null;

        momento_actual = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String nombre_fichero = PREFIJO_FOTOS + momento_actual + SUFIJO_FOTOS;

        ruta_foto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/" + nombre_fichero;


        Log.d("MIAPP", "RUTA Foto: " + ruta_foto);

        file = new File(ruta_foto);


        try {
            if (file.createNewFile()) {
                Log.d("MIAPP", "FICHERO CREADO");
            } else {
                Log.d("MIAPP", "FICHERO NO CREADO");
            }
        } catch (IOException e) {
            //  e.printStackTrace();
            Log.e("MIAPP", "Error al crear el fichero ", e);
        }


        uri_destino = Uri.fromFile(file);
        Log.d("MIAPP", "URI = " + uri_destino.toString());

        return uri_destino;

    }


    public void seleccionarFoto(View view) {
        Log.d("MIAPP", "Quiere seleccionar una foto");


        Intent intent_pide_foto = new Intent();
        intent_pide_foto.setAction(Intent.ACTION_PICK);
        intent_pide_foto.setType("image/*");      // Tipo MIME para fotos
        startActivityForResult(intent_pide_foto, CODIGO_PETICION_SELECCIONAR_FOTO);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_PETICION_SELECCIONAR_FOTO) {
            setearImagenDesdeArchivo(resultCode, data);
        } else if (requestCode == CODIGO_PETICION_HACER_FOTO) {
            setearImagenDesdeCamara(resultCode, data);

        }

    }

    private void setearImagenDesdeCamara(int resultado, Intent intent) {
        switch (resultado) {
            case RESULT_OK:
                Log.d("MIAPP", "Tiene la foto bien");
                this.imagen.setImageURI(this.fotoURI);
                this.imagen.setScaleType(ImageView.ScaleType.FIT_XY);
                // Actualizamos para que en la carpeta Pictures salga la foto que acabamos de hacer
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fotoURI));
                guardarFotoSharedPreferences();
                break;
            case RESULT_CANCELED:
                Log.d("MIAPP", "Ha cancelado la foto");
                break;
        }
    }


    private void setearImagenDesdeArchivo(int resultado, Intent data) {

        switch (resultado) {

            case RESULT_OK:

                Log.d("MIAPP", "La foto ha sido seleccionada");

                this.fotoURI = data.getData();
                this.imagen.setImageURI(fotoURI);
                guardarFotoSharedPreferences();

                break;

            case RESULT_CANCELED:

                Log.d("MIAPP", "El usuario canceló la operación");

                break;


        }

    }

    private void cargarImagenInicio() {

        Log.d("MIAPP", "FOTO CARGADA DE SHARED PREFERENCES");

        imagen.setImageURI(fotoURI);


    }


    private void guardarFotoSharedPreferences() {

        Log.d("MIAPP", "FOTO GUARDADA EN SHARED PREFERENCES" + fotoURI);

        SharedPreferences ficherosp = getSharedPreferences("ARCHIVOFOTO", MODE_PRIVATE);
        SharedPreferences.Editor editor = ficherosp.edit();
        editor.putString("RUTAFOTO", fotoURI + "");
        editor.commit();

    }


    public static Bitmap getRoundedCornerBitmap(ImageView vista, int pixels) {


        Bitmap output = null;

        // Convertimos la vista a Bitmap
        BitmapDrawable drawable = (BitmapDrawable) vista.getDrawable();

        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();

            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();

            final Rect rect = new Rect(0, 0, bitmap.getHeight(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        }


        return output;
    }


}
