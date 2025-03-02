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
    private static final int DATABASE_VERSION = 1;

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
                "observaciones TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS productos");
        onCreate(db);
    }

    public boolean insertarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("codigo", producto.getCodigo());
        valores.put("nombre", producto.getNombre());
        valores.put("cantidad", producto.getCantidad());
        valores.put("fecha", producto.getFecha());
        valores.put("observaciones", producto.getObservaciones());

        long resultado = db.insert("productos", null, valores);
        Log.d("DatabaseHelper", "Producto insertado: " + resultado);
        return resultado != -1;
    }

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
                    cursor.getString(4)  // observaciones
            );
            lista.add(p);
        }
        cursor.close();
        return lista;
    }

    public Producto obtenerProductoPorCodigo(String codigo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM productos WHERE codigo=?", new String[]{codigo});

        if (cursor.moveToFirst()) {
            Producto producto = new Producto(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            cursor.close();
            return producto;
        } else {
            cursor.close();
            return null;
        }
    }
    public void eliminarProducto(String codigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("productos", "codigo=?", new String[]{codigo});
    }

    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", producto.getNombre());
        valores.put("cantidad", producto.getCantidad());
        valores.put("fecha", producto.getFecha());
        valores.put("observaciones", producto.getObservaciones());

        int filasAfectadas = db.update("productos", valores, "codigo=?", new String[]{producto.getCodigo()});
        return filasAfectadas > 0;
    }
}