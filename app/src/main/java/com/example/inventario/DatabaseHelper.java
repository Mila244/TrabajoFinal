package com.example.inventario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventario.db";
    private static final int DATABASE_VERSION = 2;  // Subimos la versión para el cambio de estructura

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE productos (" +
                "codigo TEXT PRIMARY KEY, " +
                "nombre TEXT, " +
                "cantidad INTEGER, " +
                "fecha TEXT, " +
                "observaciones TEXT, " +
                "imagen TEXT)";  // ✅ Añadimos la columna imagen
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Si la tabla ya existe y es versión antigua, añadimos la columna imagen
            db.execSQL("ALTER TABLE productos ADD COLUMN imagen TEXT");
        }
    }

    // ✅ Método para insertar producto con imagen
    public List<Producto> obtenerProductos() {
        List<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM productos", null);

        while (cursor.moveToNext()) {
            Producto p = new Producto(
                    cursor.getString(0), // codigo
                    cursor.getString(1), // nombre
                    cursor.getInt(2),    // cantidad
                    cursor.getString(3), // fecha
                    cursor.getString(4), // observaciones
                    cursor.getString(5)  // imagen (aquí está el fix)
            );
            lista.add(p);
        }
        cursor.close();
        return lista;
    }

    // ✅ Método para obtener un producto por código
    public boolean insertarProducto(Producto producto, String rutaImagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("codigo", producto.getCodigo());
        valores.put("nombre", producto.getNombre());
        valores.put("cantidad", producto.getCantidad());
        valores.put("fecha", producto.getFecha());
        valores.put("observaciones", producto.getObservaciones());
        valores.put("imagen", rutaImagen != null ? rutaImagen : "");  // Guarda la ruta de la imagen

        long resultado = db.insert("productos", null, valores);
        return resultado != -1;
    }

    // ✅ Método para eliminar producto
    public void eliminarProducto(String codigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("productos", "codigo=?", new String[]{codigo});
    }

    // ✅ Método para actualizar producto con imagen
    public boolean actualizarProducto(Producto producto, String rutaImagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", producto.getNombre());
        valores.put("cantidad", producto.getCantidad());
        valores.put("fecha", producto.getFecha());
        valores.put("observaciones", producto.getObservaciones());
        valores.put("imagen", rutaImagen);  // Actualiza la ruta de imagen

        int filasAfectadas = db.update("productos", valores, "codigo=?", new String[]{producto.getCodigo()});
        return filasAfectadas > 0;
    }
    public Producto obtenerProductoPorCodigo(String codigo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM productos WHERE codigo = ?", new String[]{codigo});

        if (cursor.moveToFirst()) {
            Producto producto = new Producto(
                    cursor.getString(0), // codigo
                    cursor.getString(1), // nombre
                    cursor.getInt(2),    // cantidad
                    cursor.getString(3), // fecha
                    cursor.getString(4), // observaciones
                    cursor.getString(5)  // imagen
            );
            cursor.close();
            return producto;
        } else {
            cursor.close();
            return null;  // Producto no encontrado
        }
    }
}