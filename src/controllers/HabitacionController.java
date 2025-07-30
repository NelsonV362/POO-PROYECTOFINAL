package controllers;

import models.*;
import views.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HabitacionController {
    private HabitacionView vista;
    private DataManager modelo;
    
    public HabitacionController(HabitacionView vista, DataManager modelo) {
        this.vista = vista;
        this.modelo = modelo;
        configurarListeners();
        cargarHabitaciones();
    }
    
    private void configurarListeners() {
        vista.getBtnAgregar().addActionListener(e -> agregarHabitacion());
        vista.getBtnActualizar().addActionListener(e -> actualizarHabitacion());
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
            }
        } catch (NumberFormatException e) {
            vista.mostrarError("Número y precio deben ser valores válidos");
        } catch (IllegalArgumentException e) {
            vista.mostrarError("Tipo de habitación no válido");
        }
    }
    
    private void actualizarHabitacion() {
        // Implementar lógica similar a ClienteController
    }
    
    private void eliminarHabitacion() {
        // Implementar lógica similar a ClienteController
    }
}