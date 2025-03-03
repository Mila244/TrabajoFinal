package com.example.inventario;

public class Producto {
    private String codigo, nombre, fecha, observaciones;
    private int cantidad;
    private String imagen;  // Nueva propiedad

    public Producto(String codigo, String nombre, int cantidad, String fecha, String observaciones, String imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.imagen = imagen;
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
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}