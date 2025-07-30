package controllers;

import models.*;
import views.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
                formatFecha(reserva.getFechaCheckIn()),
                formatFecha(reserva.getFechaCheckOut()),
                estado
            );
        }
    }

    private String formatFecha(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }
    

    private boolean validarCamposReserva() {
        String num = vista.getNumero().trim();
        String precio = vista.getPrecio().trim();

        if (num.isEmpty() || !num.matches("[1-9]\\d?")) {
            if (num.isEmpty()) vista.mostrarError("El Nro. es obligatorio");
            else vista.mostrarError("El Nro. no es valido (1 - 99)");
            return false;
        }

        if (modelo.buscarHabitacionPorNumero(Integer.parseInt(num)) != null) {
            vista.mostrarError("El Nro. ya sea registrado");
            return false;
        }

        if (precio.isEmpty() || !precio.matches("([1-9]\\d*(\\.\\d+)?|0\\.(0*[1-9]\\d*))")) {
            if (precio.isEmpty()) vista.mostrarError("El Precio es obligatorio");
            else vista.mostrarError("El Precio no es valido");
            return false;
        }
        
        return true;
    }


    private void crearReserva() {
        try {
            String codigo = vista.getCodigo();
            String clienteStr = vista.getClienteSeleccionado();
            String habitacionStr = vista.getHabitacionSeleccionada();
            String fechaInicio = vista.getCheckIn();
            String fechaFin = vista.getCheckOut();
            
            if (codigo.isEmpty() || clienteStr == null || habitacionStr == null || 
                fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                vista.mostrarError("Todos los campos son obligatorios");
                return;
            }
            
            String dniCliente = clienteStr.substring(clienteStr.indexOf("(") + 1, clienteStr.indexOf(")"));
            int numHab = Integer.parseInt(habitacionStr.substring(habitacionStr.indexOf("#") + 1, habitacionStr.indexOf(" - ")));
            Date checkIn = new Date();
            Date checkOut = new Date();
            Cliente cliente = modelo.buscarClientePorDni(dniCliente);
            Habitacion habitacion = modelo.buscarHabitacionPorNumero(numHab);
            if (cliente == null || habitacion == null) {
                vista.mostrarError("Cliente o habitación no encontrados");
                return;
            }
     
            Reserva reserva = new Reserva(codigo, cliente, habitacion, checkIn, checkOut);
            if (modelo.agregarReserva(reserva)) {
                vista.agregarReservaATabla(codigo, clienteStr, habitacionStr, fechaInicio, fechaFin, dniCliente);
                vista.mostrarMensaje("Reserva creada exitosamente");
                vista.limpiarFormulario();
                cargarComboboxes();
            }
        } catch (Exception e) {
            vista.mostrarError("Error al crear reserva: " + e.getMessage());
        }
    }
    
    private void cancelarReserva() {}
    
    private void buscarReservas() {}
}