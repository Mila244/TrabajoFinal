package com.example.inventario;

import android.content.Context;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    public static void exportarExcel(Context context, List<Producto> productos) {
        // Carpeta privada dentro de Android/data/com.example.inventario/files/
        File carpeta = new File(context.getExternalFilesDir(null), "InventarioApp");

        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                Toast.makeText(context, "Error al crear carpeta", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        File archivo = new File(carpeta, "productos_inventario.csv");

        try (FileWriter writer = new FileWriter(archivo)) {
            // Escribir encabezados
            writer.append("Código,Nombre,Cantidad,Fecha,Observaciones\n");

            // Escribir productos
            for (Producto producto : productos) {
                writer.append(producto.getCodigo()).append(",")
                        .append(producto.getNombre()).append(",")
                        .append(String.valueOf(producto.getCantidad())).append(",")
                        .append(producto.getFecha()).append(",")
                        .append(producto.getObservaciones()).append("\n");
            }

            writer.flush();
            Toast.makeText(context, "Archivo exportado en: " + archivo.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error al exportar: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}