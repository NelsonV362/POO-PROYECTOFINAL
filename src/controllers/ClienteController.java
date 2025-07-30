package controllers;

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

        if (dni.isEmpty()) {
            vista.mostrarError("El DNI es obligatorio");
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

        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            vista.mostrarError("El email no tiene un formato válido");
            return false;
        }
        return true;
    }

    private void agregarCliente() {
        if (!validarCamposCliente()) {
            return;
        }

        String dni = vista.getDni().trim();
        String nombre = vista.getNombre().trim();
        String apellido = vista.getApellido().trim();
        String telefono = vista.getTelefono().trim();
        String email = vista.getEmail().trim();

        Cliente nuevoCliente = new Cliente(dni, nombre, apellido, telefono, email);
        
        if (modelo.agregarCliente(nuevoCliente)) {
            vista.agregarClienteATabla(dni, nombre, apellido, telefono, email);
            vista.limpiarFormulario();
            vista.mostrarMensaje("Cliente agregado exitosamente");
            actualizarClientesEnReservas();
        } else {
            vista.mostrarError("Error al agregar cliente. El DNI ya existe");
        }
    }

    private void actualizarClientesEnReservas() {
        List<Cliente> clientes = modelo.obtenerClientes();
        String[] clientesArray = clientes.stream()
            .map(c -> c.getNombre() + " " + c.getApellido() + " (" + c.getDni() + ")")
            .toArray(String[]::new);
        reservaView.cargarClientes(clientesArray);
    }
}
