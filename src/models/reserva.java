package models;
import java.util.Date;

public class Reserva {
    private String codigo;
    private Cliente cliente;
    private Habitacion habitacion;
    private Date checkIn;
    private Date checkOut;
    private double precioTotal;
    private boolean activa;

    public Reserva(String codigo, Cliente cliente, Habitacion habitacion, Date checkIn, Date checkOut, boolean activa) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.activa = activa;
        precioTotal = calcularPrecioTotal();
    }

    private double calcularPrecioTotal() {
        long dias = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        return dias * habitacion.getPrecioPorNoche();
    }

   public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public boolean isActiva() {
        return activa;
    }

    public void cancelar() {
        if (activa) {
            activa = false;
            habitacion.setDisponible(true);
        }
    }

    @Override
    public String toString() {
        return String.format("Reserva #%s - %s (%s)", codigo, cliente.getNombre(), habitacion.getNumero());
    }
}
