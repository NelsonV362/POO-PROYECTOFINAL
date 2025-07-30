package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservaView extends JPanel {
    private JTextField txtCodigo;
    private JComboBox<String> cbClientes;
    private JComboBox<String> cbHabitaciones;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnCrear;
    private JButton btnCancelar;
    private JButton btnBuscar;
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    
    public ReservaView() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nueva Reserva"));
        
        formPanel.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        formPanel.add(txtCodigo);
        
        formPanel.add(new JLabel("Cliente:"));
        cbClientes = new JComboBox<>();
        formPanel.add(cbClientes);
        
        formPanel.add(new JLabel("Habitación:"));
        cbHabitaciones = new JComboBox<>();
        formPanel.add(cbHabitaciones);
        
        formPanel.add(new JLabel("Fecha Inicio (dd/mm/aaaa):"));
        txtFechaInicio = new JTextField();
        formPanel.add(txtFechaInicio);
        
        formPanel.add(new JLabel("Fecha Fin (dd/mm/aaaa):"));
        txtFechaFin = new JTextField();
        formPanel.add(txtFechaFin);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnCrear = new JButton("Crear Reserva");
        btnCancelar = new JButton("Cancelar Reserva");
        btnBuscar = new JButton("Buscar Reservas");
        
        buttonPanel.add(btnCrear);
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnBuscar);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Cliente");
        modeloTabla.addColumn("Habitación");
        modeloTabla.addColumn("Fecha Inicio");
        modeloTabla.addColumn("Fecha Fin");
        modeloTabla.addColumn("Estado");
        
        tablaReservas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaReservas);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public String getCodigo() {
        return txtCodigo.getText();
    }

    public String getClienteSeleccionado() {
        return (String) cbClientes.getSelectedItem();
    }

    public String getHabitacionSeleccionada() {
        return (String) cbHabitaciones.getSelectedItem();
    }

    public String getFechaInicio() {
        return txtFechaInicio.getText();
    }

    public String getFechaFin() {
        return txtFechaFin.getText();
    }

    public JButton getBtnCrear() {
        return btnCrear;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void cargarClientes(String[] clientes) {
        cbClientes.removeAllItems();
        for (String cliente : clientes) {
            cbClientes.addItem(cliente);
        }
    }
    
    public void cargarHabitaciones(String[] habitaciones) {
        cbHabitaciones.removeAllItems();
        for (String habitacion : habitaciones) {
            cbHabitaciones.addItem(habitacion);
        }
    }
    
    public void agregarReservaATabla(String codigo, String cliente, String habitacion, 
                                    String fechaInicio, String fechaFin, String estado) {
        modeloTabla.addRow(new Object[]{codigo, cliente, habitacion, fechaInicio, fechaFin, estado});
    }
    
    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }
    
    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
    }
    
    public int getFilaSeleccionada() {
        return tablaReservas.getSelectedRow();
    }
    
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}