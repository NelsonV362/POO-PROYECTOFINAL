package controllers;

import models.*;
import views.*;
import java.util.List;

public class HabitacionController {
    private HabitacionView vista;
    private DataManager modelo;
    private ReservaView reservaView;
    
    public HabitacionController(HabitacionView vista, DataManager modelo, ReservaView reservaView) {
        this.vista = vista;
        this.modelo = modelo;
        this.reservaView = reservaView;
        configurarListeners();
        cargarHabitaciones();
    }
    
    private void configurarListeners() {
        vista.getBtnAgregar().addActionListener(e -> agregarHabitacion());
        vista.getBtnEliminar().addActionListener(e -> eliminarHabitacion());
    }
    
    private void cargarHabitaciones() {
        vista.limpiarTabla();
        List<Habitacion> habitaciones = modelo.obtenerHabitaciones();
        for (Habitacion hab : habitaciones) {
            vista.agregarHabitacionATabla(
                hab.getNumero(),
                hab.getTipo().toString(),
                hab.getPrecioPorNoche(),
                hab.isDisponible()
            );
        }
    }
    
    private void agregarHabitacion() {
        try {
            int numero = Integer.parseInt(vista.getNumero());
            String tipo = vista.getTipo();
            double precio = Double.parseDouble(vista.getPrecio());
            
            TipoHabitacion tipoHab = TipoHabitacion.valueOf(tipo);
            Habitacion nuevaHab = new Habitacion(numero, tipoHab, precio);
            
            if (modelo.agregarHabitacion(nuevaHab)) {
                vista.agregarHabitacionATabla(numero, tipo, precio, true);
                vista.limpiarFormulario();
                vista.mostrarMensaje("Habitación agregada exitosamente");
                actualizarHabitacionesEnReservas();
            }
        } catch (NumberFormatException e) {
            vista.mostrarError("Número y precio deben ser valores válidos");
        } catch (IllegalArgumentException e) {
            vista.mostrarError("Tipo de habitación no válido");
        }
    }

    private void actualizarHabitacionesEnReservas() {
        List<Habitacion> habitaciones = modelo.obtenerHabitaciones();
        String[] habitacionesArray = habitaciones.stream()
            .filter(Habitacion::isDisponible)
            .map(h -> "Hab #" + h.getNumero() + " - " + h.getTipo() + " ($" + h.getPrecioPorNoche() + ")")
            .toArray(String[]::new);
        reservaView.cargarHabitaciones(habitacionesArray);
    }

    private void eliminarHabitacion() {
    }
}