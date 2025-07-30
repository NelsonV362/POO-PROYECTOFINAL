package models;
import java.util.Date;
import java.util.Objects;
public class Reserva {
    private final String codigo;
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final Date fechaCheckIn;
    private final Date fechaCheckOut;
    private final double precioTotal;
    private boolean activa;

    public Reserva(String codigo, Cliente cliente, Habitacion habitacion, Date fechaCheckIn, Date fechaCheckOut, boolean activa) {
        if (fechaCheckOut.before(fechaCheckIn)) {
            throw new IllegalArgumentException("La fecha de check-out no puede ser anterior a la fecha de check-in.");
        }
        this.codigo = Objects.requireNonNull(codigo, "Código no puede ser null");
        this.cliente = Objects.requireNonNull(cliente, "Cliente no puede ser null");
        this.habitacion = Objects.requireNonNull(habitacion, "Habitación no puede ser null");
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = fechaCheckOut;
        this.activa = activa;
        this.precioTotal = calcularPrecioTotal();
    }

   private double calcularPrecioTotal() {
        long diffEnMillis = fechaCheckOut.getTime() - fechaCheckIn.getTime();
        long noches = diffEnMillis / (1000 * 60 * 60 * 24);
        return noches * habitacion.getPrecioPorNoche();
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

    public Date getFechaCheckIn() {
        return fechaCheckIn;
    }

    public Date getFechaCheckOut() {
        return fechaCheckOut;
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
