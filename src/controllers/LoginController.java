package controllers;

import java.util.ArrayList;
import java.util.List;

import models.*;
import views.*;

public class LoginController {
    private LoginView vista;
    private DataManager modelo;
    
    public LoginController(LoginView vista, DataManager modelo) {
        this.vista = vista;
        this.modelo = modelo;
        configurarListeners();
        crearUsuarioAdminPorDefecto();
    }
    
    private void crearUsuarioAdminPorDefecto() {
        if (modelo.autenticarUsuario("admin", "admin123") == null) {
            Administrador admin = new Administrador("1", "admin", "12345");
            List<String> usuarios = new ArrayList<>();
            usuarios.add("1,admin,12345,ADMINISTRADOR");
            modelo.guardarDatos(DataManager.USUARIOS_FILE, usuarios);
        }
    }
    
    private void configurarListeners() {
        vista.getBtnLogin().addActionListener(e -> {
            String usuario = vista.getUsuario();
            String password = vista.getPassword();
            
            if (usuario.isEmpty() || password.isEmpty()) {
                vista.mostrarError("Usuario y contraseña son obligatorios");
                return;
            }
            
            Usuario user = modelo.autenticarUsuario(usuario, password);
            if (user != null) {
                abrirMenuPrincipal(user);
                vista.dispose();
            } else {
                vista.mostrarError("Credenciales incorrectas");
                vista.limpiarCampos();
            }
        });
    }
    
    private void abrirMenuPrincipal(Usuario usuario) {
        MainMenuView menuView = new MainMenuView(usuario.getTipoUsuario());
        MainMenuController menuController = new MainMenuController(menuView, modelo, usuario);
        menuView.setVisible(true);
    }
}