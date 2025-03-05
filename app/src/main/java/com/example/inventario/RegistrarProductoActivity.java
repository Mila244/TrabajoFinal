package com.example.inventario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistrarProductoActivity extends AppCompatActivity {

    EditText edtCodigo, edtNombre, edtCantidad, edtFecha, edtObservaciones;
    ImageView imgProducto;
    Button btnGuardar, btnTomarFoto, btnSubirFoto;

    DatabaseHelper db;
    private String rutaFoto = "";
    private static final int REQUEST_TOMAR_FOTO = 1;
    private static final int REQUEST_SELECCIONAR_FOTO = 2;
    private static final int REQUEST_PERMISO_CAMARA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        db = new DatabaseHelper(this);

        imgProducto = findViewById(R.id.imgProducto);
        edtCodigo = findViewById(R.id.edtCodigo);
        edtNombre = findViewById(R.id.edtNombre);
        edtCantidad = findViewById(R.id.edtCantidad);
        edtFecha = findViewById(R.id.edtFecha);
        edtObservaciones = findViewById(R.id.edtObservaciones);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnSubirFoto = findViewById(R.id.btnSubirFoto);

        edtFecha.setText(obtenerFechaActual());

        btnTomarFoto.setOnClickListener(v -> verificarPermisoCamara());
        btnSubirFoto.setOnClickListener(v -> seleccionarFotoDesdeGaleria());

        btnGuardar.setOnClickListener(v -> guardarProducto());
    }

    private void guardarProducto() {
        if (camposVacios()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int cantidad = Integer.parseInt(edtCantidad.getText().toString().trim());

            Producto producto = new Producto(
                    edtCodigo.getText().toString().trim(),
                    edtNombre.getText().toString().trim(),
                    cantidad,
                    edtFecha.getText().toString().trim(),
                    edtObservaciones.getText().toString().trim(),
                    rutaFoto
            );

            if (db.insertarProducto(producto)) {
                Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar producto", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "La cantidad debe ser un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarPermisoCamara() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISO_CAMARA);
        } else {
            tomarFoto();
        }
    }

    private void tomarFoto() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File fotoFile = crearArchivoImagen();
            if (fotoFile != null) {
                rutaFoto = fotoFile.getAbsolutePath();
                Uri fotoUri = FileProvider.getUriForFile(this, "com.example.inventario.fileprovider", fotoFile);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, REQUEST_TOMAR_FOTO);
            }
        }
    }

    private File crearArchivoImagen() {
        String nombreArchivo = "IMG_" + System.currentTimeMillis();
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(nombreArchivo, ".jpg", directorio);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void seleccionarFotoDesdeGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECCIONAR_FOTO);
    }

    private boolean camposVacios() {
        return edtCodigo.getText().toString().trim().isEmpty() ||
                edtNombre.getText().toString().trim().isEmpty() ||
                edtCantidad.getText().toString().trim().isEmpty() ||
                edtFecha.getText().toString().trim().isEmpty() ||
                edtObservaciones.getText().toString().trim().isEmpty() ||
                rutaFoto.trim().isEmpty();
    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_TOMAR_FOTO) {
            mostrarImagenRedimensionada(rutaFoto);
        } else if (requestCode == REQUEST_SELECCIONAR_FOTO) {
            Uri imagenSeleccionada = data.getData();
            if (imagenSeleccionada != null) {
                rutaFoto = PathUtil.getPath(this, imagenSeleccionada);
                mostrarImagenRedimensionada(rutaFoto);
            }
        }
    }

    private void mostrarImagenRedimensionada(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            imgProducto.setImageResource(R.drawable.ic_image_not_found);
            return;
        }

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            imgProducto.setImageResource(R.drawable.ic_image_not_found);
            Toast.makeText(this, "La imagen no existe: " + ruta, Toast.LENGTH_SHORT).show();
            rutaFoto = "";  // Limpiamos la ruta
            return;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(ruta, options);

        final int TAMANO_DESEADO = 1024;
        int ancho = options.outWidth;
        int alto = options.outHeight;
        int escala = 1;

        while (ancho / 2 >= TAMANO_DESEADO && alto / 2 >= TAMANO_DESEADO) {
            ancho /= 2;
            alto /= 2;
            escala *= 2;
        }

        options.inSampleSize = escala;
        options.inJustDecodeBounds = false;

        Bitmap imagenRedimensionada = BitmapFactory.decodeFile(ruta, options);
        if (imagenRedimensionada != null) {
            imgProducto.setImageBitmap(imagenRedimensionada);
        } else {
            imgProducto.setImageResource(R.drawable.ic_image_not_found);
            Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISO_CAMARA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tomarFoto();
        }
    }
}