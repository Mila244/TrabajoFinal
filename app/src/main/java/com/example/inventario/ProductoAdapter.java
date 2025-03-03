package com.example.inventario;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private final List<Producto> productoList;
    private final Context context;

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

        // Cargar imagen desde archivo (BitmapFactory)
        if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
            File imgFile = new File(producto.getImagen());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imgProducto.setImageBitmap(bitmap);
            } else {
                holder.imgProducto.setImageResource(R.drawable.ic_image_not_found);
            }
        } else {
            holder.imgProducto.setImageResource(R.drawable.ic_image_not_found);
        }

        // Botón Editar
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarProductoActivity.class);
            intent.putExtra("codigo", producto.getCodigo());
            ((ListaProductosActivity) context).startActivityForResult(intent, 1);
        });

        // Botón Eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            eliminarProducto(producto.getCodigo(), position);
        });
    }
    private void eliminarProducto(String codigo, int position) {
        DatabaseHelper db = new DatabaseHelper(context);
        db.eliminarProducto(codigo);
        productoList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodigo, txtNombre, txtCantidad;
        ImageView imgProducto;
        Button btnEditar, btnEliminar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCodigo = itemView.findViewById(R.id.txtCodigo);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}