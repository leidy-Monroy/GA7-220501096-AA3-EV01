package model;

import android.content.ContentValues;

public class Product {
    private int idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioProducto;
    private int stockProducto;

    // Constructor
    public Product(int idProducto, String nombreProducto, String descripcionProducto, double precioProducto, int stockProducto) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioProducto = precioProducto;
        this.stockProducto = stockProducto;
    }

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public int getStockProducto() {
        return stockProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public void setStockProducto(int stockProducto) {
        this.stockProducto = stockProducto;
    }

    // Método para convertir el objeto a ContentValues (para insertar o actualizar en SQLite)
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("nombreProducto", nombreProducto);
        values.put("descripcionProducto", descripcionProducto);
        values.put("precioProducto", precioProducto);
        values.put("stockProducto", stockProducto);
        return values;
    }
}
