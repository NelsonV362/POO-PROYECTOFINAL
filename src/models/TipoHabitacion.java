package models;

public enum TipoHabitacion {
    INDIVIDUAL("Individual", 1, 50.0),
    DOBLE("Doble", 2, 80.0),
    SUITE("Suite", 4, 150.0);
    
    private String descripcion;
    private int capacidad;
    private double precioBase;
    
    private TipoHabitacion(String descripcion, int capacidad, double precioBase) {
        this.descripcion = descripcion;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
    }

    public String getDescripcion() {
        return descripcion;
    
    }
    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}