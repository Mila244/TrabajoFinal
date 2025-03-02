package com.example.inventario;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class RegistrarProductoActivity extends AppCompatActivity {
    EditText edtCodigo, edtNombre, edtCantidad, edtFecha, edtObservaciones;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        db = new DatabaseHelper(this);
        edtCodigo = findViewById(R.id.edtCodigo);
        edtNombre = findViewById(R.id.edtNombre);
        edtCantidad = findViewById(R.id.edtCantidad);
        edtFecha = findViewById(R.id.edtFecha);
        edtObservaciones = findViewById(R.id.edtObservaciones);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> {
            Producto p = new Producto(
                    edtCodigo.getText().toString(),
                    edtNombre.getText().toString(),
                    Integer.parseInt(edtCantidad.getText().toString()),
                    edtFecha.getText().toString(),
                    edtObservaciones.getText().toString()
            );
            if (db.insertarProducto(p)) {
                Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}