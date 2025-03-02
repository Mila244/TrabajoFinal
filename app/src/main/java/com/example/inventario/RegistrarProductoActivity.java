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
            // Validar campos
            if (camposVacios()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int cantidad = Integer.parseInt(edtCantidad.getText().toString().trim());

                Producto p = new Producto(
                        edtCodigo.getText().toString().trim(),
                        edtNombre.getText().toString().trim(),
                        cantidad,
                        edtFecha.getText().toString().trim(),
                        edtObservaciones.getText().toString().trim()
                );

                if (db.insertarProducto(p)) {
                    Toast.makeText(this, "Producto guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al guardar producto", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "La cantidad debe ser un número válido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean camposVacios() {
        return edtCodigo.getText().toString().trim().isEmpty() ||
                edtNombre.getText().toString().trim().isEmpty() ||
                edtCantidad.getText().toString().trim().isEmpty() ||
                edtFecha.getText().toString().trim().isEmpty() ||
                edtObservaciones.getText().toString().trim().isEmpty();
    }
}