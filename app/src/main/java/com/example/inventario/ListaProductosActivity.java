package com.example.inventario;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListaProductosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductoAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);

        recyclerView = findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        cargarProductos();
    }

    public void cargarProductos() {
        List<Producto> productos = db.obtenerProductos();
        adapter = new ProductoAdapter(productos, this);
        recyclerView.setAdapter(adapter);
    }

    // Lo importante: al volver de EditarProductoActivity, recargamos la lista
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarProductos();
        }
    }
}