package com.example.inventario;

public class Producto {
    private String codigo, nombre, fecha, observaciones;
    private int cantidad;

    public Producto(String codigo, String nombre, int cantidad, String fecha, String observaciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }
}