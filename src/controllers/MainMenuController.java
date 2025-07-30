package controllers;

import models.*;
import views.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuController {
    private MainMenuView vista;
    private DataManager modelo;
    private Usuario usuario;
    
    public MainMenuController(MainMenuView vista, DataManager modelo, Usuario usuario) {
        this.vista = vista;
        this.modelo = modelo;
        this.usuario = usuario;

        ClienteView clienteView = new ClienteView();
        HabitacionView habitacionView = new HabitacionView();
        ReservaView reservaView = new ReservaView();

        vista.getPanelClientes().add(clienteView);
        vista.getPanelHabitaciones().add(habitacionView);
        vista.getPanelReservas().add(reservaView);
        
        new ClienteController(clienteView, modelo);
        new HabitacionController(habitacionView, modelo);
        new ReservaController(reservaView, modelo, clienteView, habitacionView);
        configurarPermisos();
    }
    
    private void configurarPermisos() {
        if ("RECEPCIONISTA".equals(usuario.getTipoUsuario())) vista.getTabbedPane().setEnabledAt(1, false);
    }
}