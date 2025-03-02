package com.example.inventario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}