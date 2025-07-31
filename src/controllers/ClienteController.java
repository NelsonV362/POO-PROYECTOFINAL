package controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import models.*;
import views.*;

public class ClienteController {
    private ClienteView vista;
    private DataManager modelo;
    private ReservaView reservaView;

    public ClienteController(ClienteView vista, DataManager modelo, ReservaView reservaView) {
        this.vista = vista;
        this.modelo = modelo;
        this.reservaView = reservaView;
        configurarListeners();
        cargarClientes();
    }

    private void configurarListeners() {
        vista.getBtnAgregar().addActionListener(e -> agregarCliente());
        vista.getBtnEliminar().addActionListener(e -> eliminarCliente());
    }

    private void cargarClientes() {
        vista.limpiarTabla();
        for (Cliente c : modelo.obtenerClientes()) {
            vista.agregarClienteATabla(
                c.getDni(), c.getNombre(), c.getApellido(),
                c.getTelefono(), c.getEmail()
            );
        }
    }

    private boolean validarCamposCliente() {
        String dni = vista.getDni().trim();
        String nombre = vista.getNombre().trim();
        String apellido = vista.getApellido().trim();
        String telefono = vista.getTelefono().trim();
        String email = vista.getEmail().trim();

        if (dni.isEmpty() || dni.length()!=10 || !dni.matches("\\d+")) {
            if (dni.isEmpty()) vista.mostrarError("El DNI es obligatorio");
            else vista.mostrarError("El DNI no es valido");
            return false;
        }

        if (modelo.buscarClientePorDni(dni) != null) {
            vista.mostrarError("EL DNI ya fue registrado");
            return false;
        }

        if (nombre.isEmpty()) {
            vista.mostrarError("El nombre es obligatorio");
            return false;
        }

        if (apellido.isEmpty()) {
            vista.mostrarError("El apellido es obligatorio");
            return false;
        }

        if (telefono.isEmpty() || telefono.length()!=10) {
            if (telefono.isEmpty()) vista.mostrarError("El telefono es obligatorio");
            else vista.mostrarError("El telefono no es valido");
            return false;
        }

        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            vista.mostrarError("El email no tiene un formato válido");
            return false;
        }
        
        return true;
    }

    private void agregarCliente() {
        if (!validarCamposCliente()) return;

        String dni = vista.getDni().trim();
        String nombre = vista.getNombre().trim();
        String apellido = vista.getApellido().trim();
        String telefono = vista.getTelefono().trim();
        String email = vista.getEmail().trim();
        Cliente nuevoCliente = new Cliente(dni, nombre, apellido, telefono, email);
        
        if (modelo.agregarCliente(nuevoCliente)) {
            cargarClientes();
            vista.limpiarFormulario();
            vista.mostrarMensaje("Cliente agregado exitosamente");
            actualizarClientesEnReservas();
        } else {
            vista.mostrarError("Error al agregar cliente");
        }
    }

    private void eliminarCliente() {
        int row;
        if ((row = vista.getFilaSeleccionada()) == -1) return;
        String cliente = vista.getTablaClientes().getValueAt(row, 0).toString();
        
        if (modelo.eliminarCliente(cliente)) {
            cargarClientes();
            modelo.eliminarReserva(cliente, null);
            vista.mostrarMensaje("Cliente Eliminado exitosamente");
            actualizarClientesEnReservas();
        } else {
            vista.mostrarError("Error al eliminar cliente");
        }
    }

    private void actualizarClientesEnReservas() {
        List<Cliente> clientes = modelo.obtenerClientes();
        String[] clientesArray = clientes.stream()
            .map(c -> c.getNombre() + " " + c.getApellido() + " (" + c.getDni() + ")")
            .toArray(String[]::new);
        reservaView.cargarClientes(clientesArray);

        reservaView.limpiarTabla();
        for (Reserva reserva : modelo.obtenerReservas()) {
            String estado = reserva.isActiva() ? "Activa" : "Cancelada";
            reservaView.agregarReservaATabla(
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
}
