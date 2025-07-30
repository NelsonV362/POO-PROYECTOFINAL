package models;

public class Habitacion {
    private int numero;
    private TipoHabitacion tipo;
    private double precioPorNoche;
    private boolean disponible;
    
    public Habitacion(int numero, TipoHabitacion tipo, double precioPorNoche) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
        disponible = true;
    }
    
    public int getNumero() {
        return numero;
    }

    public TipoHabitacion getTipo() {
        return tipo;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    @Override
    public String toString() {
        return "Habitación #" + numero + " - " + tipo + " (" + (disponible ? "Disponible" : "Ocupada") + ")";
    }
}