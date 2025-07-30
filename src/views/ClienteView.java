package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClienteView extends JPanel {
    private JTextField txtDni, txtNombre, txtApellido, txtTelefono, txtEmail;
    private JButton btnAgregar, btnEliminar;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public ClienteView() {
        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        JLabel[] labels = {
            new JLabel("DNI:"), new JLabel("Nombre:"), new JLabel("Apellido:"),
            new JLabel("Teléfono:"), new JLabel("Email:")
        };
        JTextField[] fields = {
            txtDni = new JTextField(), txtNombre = new JTextField(),
            txtApellido = new JTextField(), txtTelefono = new JTextField(),
            txtEmail = new JTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            formPanel.add(labels[i]);
            formPanel.add(fields[i]);
        }
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAgregar = new JButton("Agregar");
        btnEliminar = new JButton("Eliminar");

        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEliminar);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        modeloTabla = new DefaultTableModel(new Object[]{"DNI", "Nombre", "Apellido", "Teléfono", "Email"}, 0);
        tablaClientes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaClientes);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTablaClientes() {
        return tablaClientes;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public String getDni() {
        return txtDni.getText();
    }

    public String getNombre() {
        return txtNombre.getText();
    }

    public String getApellido() {
        return txtApellido.getText();
    }

    public String getTelefono() {
        return txtTelefono.getText();
    }

    public String getEmail() {
        return txtEmail.getText();
    }

    public void agregarClienteATabla(String dni, String nombre, String apellido, String telefono, String email) {
        modeloTabla.addRow(new Object[]{dni, nombre, apellido, telefono, email});
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void limpiarFormulario() {
        txtDni.setText(""); txtNombre.setText(""); txtApellido.setText("");
        txtTelefono.setText(""); txtEmail.setText("");
    }

    public int getFilaSeleccionada() {
        return tablaClientes.getSelectedRow();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
