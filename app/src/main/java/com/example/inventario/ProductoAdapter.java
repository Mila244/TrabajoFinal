package com.example.inventario;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productoList;
    private Context context;

    public ProductoAdapter(List<Producto> productoList, Context context) {
        this.productoList = productoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);

        holder.txtCodigo.setText("Código: " + producto.getCodigo());
        holder.txtNombre.setText("Nombre: " + producto.getNombre());
        holder.txtCantidad.setText("Cantidad: " + producto.getCantidad());

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarProductoActivity.class);
            intent.putExtra("codigo", producto.getCodigo());

            // Aquí hacemos el casteo para que el contexto sea la actividad correcta
            if (context instanceof ListaProductosActivity) {
                ((ListaProductosActivity) context).startActivityForResult(intent, 1);
            }
        });

        holder.btnEliminar.setOnClickListener(v -> {
            eliminarProducto(producto.getCodigo(), position);
        });
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    private void eliminarProducto(String codigo, int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        db.eliminarProducto(codigo);
        productoList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodigo, txtNombre, txtCantidad;
        Button btnEditar, btnEliminar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}