package models;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
public class Reserva {
    private final String codigo;
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final LocalDate fechaCheckIn;
    private final LocalDate fechaCheckOut;
    private final double precioTotal;
    private boolean activa;
    public Reserva(String codigo, Cliente cliente, Habitacion habitacion, LocalDate fechaCheckIn, LocalDate fechaCheckOut) {
        if (fechaCheckOut.isBefore(fechaCheckIn)) {
            throw new IllegalArgumentException("La fecha de check-out no puede ser nunca una fecha pasada al check-in.");
        }
        this.codigo = Objects.requireNonNull(codigo, "Código no puede ser null");
        this.cliente = Objects.requireNonNull(cliente, "Cliente no puede ser null");
        this.habitacion = Objects.requireNonNull(habitacion, "Habitación no puede ser null");
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.activa = true;
        this.precioTotal = calcularPrecioTotal();
    }

    private double calcularPrecioTotal() {
        long noches = ChronoUnit.DAYS.between(fechaCheckIn, fechaCheckOut);
        return noches * habitacion.getPrecioPorNoche();
    }

    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public Habitacion getHabitacion() { return habitacion; }
    public LocalDate getFechaCheckIn() { return fechaCheckIn; }
    public LocalDate getFechaCheckOut() { return fechaCheckOut; }
    public double getPrecioTotal() { return precioTotal; }
    public boolean isActiva() { return activa; }

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
