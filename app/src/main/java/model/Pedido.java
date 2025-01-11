package model;

public class Pedido {
    private int idPedido;
    private String cliente;
    private String fecha;
    private double totalPedido;

    // Constructor
    public Pedido(int idPedido, String cliente, String fecha, double totalPedido) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.fecha = fecha;
        this.totalPedido = totalPedido;
    }

    // Getters
    public int getIdPedido() {
        return idPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public double getTotalPedido() {
        return totalPedido;
    }

    // Setters (si es necesario)
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTotalPedido(double totalPedido) {
        this.totalPedido = totalPedido;
    }
}
