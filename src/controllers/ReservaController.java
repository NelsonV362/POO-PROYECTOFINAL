package controllers;

import models.*;
import views.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaController {
    private ReservaView vista;
    private DataManager modelo;
    private ClienteView clienteView;
    private HabitacionView habitacionView;
    
    public ReservaController(ReservaView vista, DataManager modelo, ClienteView clienteView, HabitacionView habitacionView) {
        this.vista = vista;
        this.modelo = modelo;
        this.clienteView = clienteView;
        this.habitacionView = habitacionView;
        configurarListeners();
        cargarComboboxes();
        cargarReservas();
    }
    
    private void configurarListeners() {
        vista.getBtnCrear().addActionListener(e -> crearReserva());
        vista.getBtnCancelar().addActionListener(e -> cancelarReserva());
        vista.getBtnBuscar().addActionListener(e -> buscarReservas());
    }
    
    private void cargarComboboxes() {
        List<Cliente> clientes = modelo.obtenerClientes();
        String[] clientesArray = clientes.stream().map(c -> c.getNombre() + " " + c.getApellido() + " (" + c.getDni() + ")").toArray(String[]::new);
        vista.cargarClientes(clientesArray);
        
        List<Habitacion> habitaciones = modelo.obtenerHabitaciones();
        String[] habitacionesArray = habitaciones.stream().filter(Habitacion::isDisponible).map(h -> "Hab #" + h.getNumero() + " - " + h.getTipo() + " ($" + h.getPrecioPorNoche() + ")").toArray(String[]::new);
        vista.cargarHabitaciones(habitacionesArray);
    }

    private void cargarReservas() {
        vista.limpiarTabla();
        for (Reserva reserva : modelo.obtenerReservas()) {
            String estado = reserva.isActiva() ? "Activa" : "Cancelada";
            vista.agregarReservaATabla(
                reserva.getCodigo(),
                reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido(),
                "Hab #" + reserva.getHabitacion().getNumero() + " (" + reserva.getHabitacion().getTipo() + ")",
                new SimpleDateFormat("dd/MM/yyyy").format(reserva.getCheckIn()),
                new SimpleDateFormat("dd/MM/yyyy").format(reserva.getCheckOut()),
                String.valueOf(reserva.getPrecioTotal()),
                estado
            );
        }
    }
    

    private boolean validarCamposReserva() {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            formato.setLenient(false);
            Date checkIn = formato.parse(vista.getCheckIn());
            Date checkOut = formato.parse(vista.getCheckOut());
            String f1 = formato.format(checkIn);
            String f2 = formato.format(checkOut);

            if (vista.getCheckIn() == vista.getCheckOut() || !checkIn.before(checkOut)) {
                vista.mostrarError("CheckIn y/o CheckOut no valido(s)");
                return false;
            }

            if (vista.getClienteSeleccionado() == null) {
                vista.mostrarError("El Cliente es obligatorio");
                return false;
            }

            if (vista.getHabitacionSeleccionada() == null) {
                vista.mostrarError("La Habitacion es obligatorio");
                return false;
            }

            String habitacionStr = vista.getHabitacionSeleccionada();
            int numHab = Integer.parseInt(habitacionStr.substring(habitacionStr.indexOf("#") + 1, habitacionStr.indexOf(" - ")));
            Habitacion habitacion = modelo.buscarHabitacionPorNumero(numHab);

            
            List<Reserva> reservas = modelo.obtenerReservas().stream().filter(r -> r.getHabitacion().getNumero() == habitacion.getNumero() && r.isActiva()).collect(Collectors.toList());
            for (Reserva r : reservas) {
                if (!(checkOut.before(r.getCheckIn()) || r.getCheckOut().before(checkIn))) {
                    vista.mostrarError("CheckIn y/o CheckOut se cruzan con otra(s) reservacion(es)");
                    return false;
                }
            }
        } catch (ParseException e) {
            vista.mostrarError("CheckIn y/o CheckOut no valido(s)");
            return false;
        }
        return true;
    }


    private void crearReserva() {
        if (!validarCamposReserva()) return;

        String codigo = modelo.obtenerCodigoReserva();
        String clienteStr = vista.getClienteSeleccionado();
        String dniCliente = clienteStr.substring(clienteStr.indexOf("(") + 1, clienteStr.indexOf(")"));
        Cliente cliente = modelo.buscarClientePorDni(dniCliente);
        String habitacionStr = vista.getHabitacionSeleccionada();
        int numHab = Integer.parseInt(habitacionStr.substring(habitacionStr.indexOf("#") + 1, habitacionStr.indexOf(" - ")));
        Habitacion habitacion = modelo.buscarHabitacionPorNumero(numHab);
        Date checkIn;
        Date checkOut;
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            checkIn = formato.parse(vista.getCheckIn());
            checkOut = formato.parse(vista.getCheckOut());
        } catch (ParseException e) {
            return;
        }
        Reserva nuevaReserva = new Reserva(codigo, cliente, habitacion, checkIn, checkOut, true);
        
        if (modelo.agregarReserva(nuevaReserva)) {
            cargarReservas();
            vista.limpiarFormulario();
            vista.mostrarMensaje("Reserva registrada exitosamente");
        } else {
            vista.mostrarError("Error al registrar reserva");
        }
    }
    
    private void cancelarReserva() {
        int row;
        if ((row = vista.getFilaSeleccionada()) == -1) return;
        String cliente = vista.getTablaReservas().getValueAt(row, 0).toString();
        
        if (modelo.cancelarReserva(cliente)) {
            cargarReservas();
            vista.limpiarFormulario();
            vista.mostrarMensaje("Cliente Eliminado exitosamente");
        } else {
            vista.mostrarError("Error al eliminar cliente");
        }
    }
    
    private void buscarReservas() {}
}