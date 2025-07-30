package controllers;

import models.*;
import views.*;

public class ClienteController {
    private ClienteView vista;
    private DataManager modelo;

    public ClienteController(ClienteView vista, DataManager modelo) {
        this.vista = vista;
        this.modelo = modelo;
        configurarListeners();
        cargarClientes();
    }

    private void configurarListeners() {
        vista.getBtnAgregar().addActionListener(e -> agregarCliente());
        // Agregar otros listeners según sea necesario
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

    private void agregarCliente() {
        String dni = vista.getDni(), nombre = vista.getNombre(),
               apellido = vista.getApellido(), telefono = vista.getTelefono(),
               email = vista.getEmail();

        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            vista.mostrarError("DNI, Nombre y Apellido son obligatorios");
            return;
        }

        Cliente nuevo = new Cliente(dni, nombre, apellido, telefono, email);
        if (modelo.agregarCliente(nuevo)) {
            vista.agregarClienteATabla(dni, nombre, apellido, telefono, email);
            vista.limpiarFormulario();
            vista.mostrarMensaje("Cliente agregado exitosamente");
        } else {
            vista.mostrarError("Error al agregar cliente");
        }
    }

    // Métodos para actualizar y eliminar se implementarán aquí...
}
