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

    private boolean validarCamposHabitacion() {
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

    
    private void agregarHabitacion() {
        if (!validarCamposHabitacion()) return;

        try {
            int numero = Integer.parseInt(vista.getNumero().trim());
            String tipo = vista.getTipo();
            double precio = Double.parseDouble(vista.getPrecio().trim());
            
            TipoHabitacion tipoHab = TipoHabitacion.valueOf(tipo);
            Habitacion nuevaHab = new Habitacion(numero, tipoHab, precio);
            
            if (modelo.agregarHabitacion(nuevaHab)) {
                cargarHabitaciones();
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

    private void eliminarHabitacion() {
        int row;
        if ((row = vista.getFilaSeleccionada()) == -1) return;
        String hab = vista.getTablaHabitaciones().getValueAt(row, 0).toString();
        
        if (modelo.eliminarHabitacion(hab)) {
            cargarHabitaciones();
            vista.limpiarFormulario();
            vista.mostrarMensaje("Habitacion Eliminada exitosamente");
            actualizarHabitacionesEnReservas();
        } else {
            vista.mostrarError("Error al eliminar habitacion");
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

}