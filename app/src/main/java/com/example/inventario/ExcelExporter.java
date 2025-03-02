package com.example.inventario;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static void exportar(Context context, List<Producto> productos) {
        try {
            // Crear el libro Excel
            Workbook workbook = new HSSFWorkbook();  // HSSFWorkbook es para archivos .xls (formato Excel 97-2003)
            Sheet sheet = workbook.createSheet("Inventario");

            // Crear fila de cabeceras
            Row headerRow = sheet.createRow(0);
            String[] columnas = {"Código", "Nombre", "Cantidad", "Fecha", "Observaciones"};

            for (int i = 0; i < columnas.length; i++) {
                headerRow.createCell(i).setCellValue(columnas[i]);
            }

            // Llenar con los datos de los productos
            int rowIndex = 1;
            for (Producto producto : productos) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(producto.getCodigo());
                row.createCell(1).setCellValue(producto.getNombre());
                row.createCell(2).setCellValue(producto.getCantidad());
                row.createCell(3).setCellValue(producto.getFecha());
                row.createCell(4).setCellValue(producto.getObservaciones());
            }

            // Crear archivo en la carpeta de documentos internos de la app
            File directorio = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }

            File file = new File(directorio, "inventario.xls");

            // Guardar el archivo
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();

            // Notificar al usuario
            Toast.makeText(context, "Archivo Excel guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al exportar Excel: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}