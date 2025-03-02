package com.example.inventario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditarProductoActivity extends AppCompatActivity {

    EditText edtCodigo, edtNombre, edtCantidad, edtFecha, edtObservaciones;
    Button btnActualizar;
    DatabaseHelper db;
    String codigoProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        db = new DatabaseHelper(this);

        edtCodigo = findViewById(R.id.edtCodigo);
        edtNombre = findViewById(R.id.edtNombre);
        edtCantidad = findViewById(R.id.edtCantidad);
        edtFecha = findViewById(R.id.edtFecha);
        edtObservaciones = findViewById(R.id.edtObservaciones);
        btnActualizar = findViewById(R.id.btnActualizar);

        codigoProducto = getIntent().getStringExtra("codigo");

        cargarDatosProducto(codigoProducto);

        btnActualizar.setOnClickListener(v -> {
            actualizarProducto();
        });
    }

    private void cargarDatosProducto(String codigo) {
        Producto producto = db.obtenerProductoPorCodigo(codigo);
        if (producto != null) {
            edtCodigo.setText(producto.getCodigo());
            edtNombre.setText(producto.getNombre());
            edtCantidad.setText(String.valueOf(producto.getCantidad()));
            edtFecha.setText(producto.getFecha());
            edtObservaciones.setText(producto.getObservaciones());

            edtCodigo.setEnabled(false);
        } else {
            Toast.makeText(this, "Error al cargar el producto", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void actualizarProducto() {
        Producto producto = new Producto(
                codigoProducto,
                edtNombre.getText().toString(),
                Integer.parseInt(edtCantidad.getText().toString()),
                edtFecha.getText().toString(),
                edtObservaciones.getText().toString()
        );

        if (db.actualizarProducto(producto)) {
            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);  // Notificar al volver
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }
}