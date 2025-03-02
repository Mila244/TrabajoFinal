package com.example.inventario;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity; // ✅ Import necesario
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
        List<Producto> productos = db.obtenerProductos();

        // 💥 Log para saber si está vacía la lista
        Log.d("ListaProductos", "Productos encontrados: " + productos.size());

        adapter = new ProductoAdapter(productos, this);
        recyclerView.setAdapter(adapter);
    }
}