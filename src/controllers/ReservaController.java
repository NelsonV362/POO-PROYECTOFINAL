package controllers;

import models.*;
import views.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
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
    
    private void crearReserva() {
        try {
            String codigo = vista.getCodigo();
            String clienteStr = vista.getClienteSeleccionado();
            String habitacionStr = vista.getHabitacionSeleccionada();
            String fechaInicio = vista.getFechaInicio();
            String fechaFin = vista.getFechaFin();
            
            if (codigo.isEmpty() || clienteStr == null || habitacionStr == null || 
                fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                vista.mostrarError("Todos los campos son obligatorios");
                return;
            }
            
            String dniCliente = clienteStr.substring(clienteStr.indexOf("(") + 1, clienteStr.indexOf(")"));
            int numHab = Integer.parseInt(habitacionStr.substring(habitacionStr.indexOf("#") + 1, habitacionStr.indexOf(" - ")));
            LocalDate checkIn = LocalDate.now();
            LocalDate checkOut = LocalDate.now();
            Cliente cliente = modelo.buscarClientePorDni(dniCliente);
            Habitacion habitacion = modelo.buscarHabitacionPorNumero(numHab);
            if (cliente == null || habitacion == null) {
                vista.mostrarError("Cliente o habitación no encontrados");
                return;
            }
     
            Reserva reserva = new Reserva(codigo, cliente, habitacion, checkIn, checkOut);
            if (modelo.agregarReserva(reserva)) {
                vista.mostrarMensaje("Reserva creada exitosamente");
                vista.limpiarFormulario();
                cargarComboboxes(); // Actualizar habitaciones disponibles
            }
        } catch (Exception e) {
            vista.mostrarError("Error al crear reserva: " + e.getMessage());
        }
    }
    
    private void cancelarReserva() {
        // Implementar lógica para cancelar reserva
    }
    
    private void buscarReservas() {
        // Implementar lógica para buscar reservas
    }
}