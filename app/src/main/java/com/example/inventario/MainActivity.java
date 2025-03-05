package com.example.inventario;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar, btnVerProductos, btnExportarExcel;
    private static final int REQUEST_CREATE_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVerProductos = findViewById(R.id.btnVerProductos);
        btnExportarExcel = findViewById(R.id.btnExportarExcel);

        btnRegistrar.setOnClickListener(v ->
                startActivity(new Intent(this, RegistrarProductoActivity.class)));

        btnVerProductos.setOnClickListener(v ->
                startActivity(new Intent(this, ListaProductosActivity.class)));

        btnExportarExcel.setOnClickListener(v -> iniciarProcesoDeGuardarArchivo());
    }

    private void iniciarProcesoDeGuardarArchivo() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "productos_inventario.csv");
        startActivityForResult(intent, REQUEST_CREATE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            exportarProductosAUri(uri);
        }
    }

    private void exportarProductosAUri(Uri uri) {
        DatabaseHelper db = new DatabaseHelper(this);
        List<Producto> productos = db.obtenerProductos();

        if (productos.isEmpty()) {
            Toast.makeText(this, "No hay productos para exportar", Toast.LENGTH_SHORT).show();
            return;
        }

        try (OutputStream outputStream = getContentResolver().openOutputStream(uri);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {

            writer.append("Código,Nombre,Cantidad,Fecha,Observaciones\n");

            for (Producto producto : productos) {
                writer.append(producto.getCodigo()).append(",")
                        .append(producto.getNombre()).append(",")
                        .append(String.valueOf(producto.getCantidad())).append(",")
                        .append(producto.getFecha()).append(",")
                        .append(producto.getObservaciones()).append("\n");
            }

            writer.flush();
            Toast.makeText(this, "Archivo guardado exitosamente", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}