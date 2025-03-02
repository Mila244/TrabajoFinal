package com.example.inventario;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar, btnVerProductos, btnExportarExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVerProductos = findViewById(R.id.btnVerProductos);
        btnExportarExcel = findViewById(R.id.btnExportarExcel);

        btnRegistrar.setOnClickListener(v ->
                startActivity(new Intent(this, RegistrarProductoActivity.class))
        );

        btnVerProductos.setOnClickListener(v ->
                startActivity(new Intent(this, ListaProductosActivity.class))
        );

        btnExportarExcel.setOnClickListener(v -> {
            try {
                DatabaseHelper db = new DatabaseHelper(this);
                List<Producto> productos = db.obtenerProductos();

                ExcelExporter.exportar(this, productos);
                Toast.makeText(this, "Archivo Excel exportado correctamente", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {  // Captura cualquier otro error inesperado
                e.printStackTrace();
                Toast.makeText(this, "Error inesperado: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}