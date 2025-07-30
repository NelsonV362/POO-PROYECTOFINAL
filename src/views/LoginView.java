package views;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    public LoginView() {
        setTitle("Login - Sistema Hotel");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Sistema de Gestión Hotelera", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        formPanel.add(txtUsuario);
        
        formPanel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);
        
        formPanel.add(new JLabel());
        btnLogin = new JButton("Ingresar");
        formPanel.add(btnLogin);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    public JButton getBtnLogin() { 
        return btnLogin;
    }

    public String getUsuario() {
        return txtUsuario.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de autenticación", JOptionPane.ERROR_MESSAGE);
    }
    
    public void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
    }
}