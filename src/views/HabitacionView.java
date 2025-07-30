package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HabitacionView extends JPanel {
    private final JTextField txtNumero = new JTextField();
    private final JComboBox<String> cbTipo = new JComboBox<>(new String[]{"INDIVIDUAL", "DOBLE", "SUITE"});
    private final JTextField txtPrecio = new JTextField();
    private final JButton btnAgregar = new JButton("Agregar");
    private final JButton btnEliminar = new JButton("Eliminar");
    private final JTable tablaHabitaciones;
    private final DefaultTableModel modeloTabla;

    public HabitacionView() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Habitación"));

        formPanel.add(new JLabel("Número:"));
        formPanel.add(txtNumero);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(cbTipo);
        formPanel.add(new JLabel("Precio por noche:"));
        formPanel.add(txtPrecio);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEliminar);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        modeloTabla = new DefaultTableModel(new Object[]{"Número", "Tipo", "Precio", "Disponible"}, 0);
        tablaHabitaciones = new JTable(modeloTabla);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(tablaHabitaciones), BorderLayout.CENTER);
    }

    public JTable getTablaHabitaciones() {
        return tablaHabitaciones;
    }

    public String getNumero() {
        return txtNumero.getText();
    }

    public String getTipo() {
        return (String) cbTipo.getSelectedItem();
    }

    public String getPrecio() {
        return txtPrecio.getText();
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void agregarHabitacionATabla(int numero, String tipo, double precio, boolean disponible) {
        modeloTabla.addRow(new Object[]{numero, tipo, precio, disponible ? "Sí" : "No"});
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public void limpiarFormulario() {
        txtNumero.setText("");
        cbTipo.setSelectedIndex(0);
        txtPrecio.setText("");
    }

    public int getFilaSeleccionada() {
        return tablaHabitaciones.getSelectedRow();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
