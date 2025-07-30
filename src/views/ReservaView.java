package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservaView extends JPanel {
    private JTextField txtCodigo;
    private JComboBox<String> cbClientes;
    private JComboBox<String> cbHabitaciones;
    private JSpinner checkIn;
    private JSpinner checkOut;
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
        
        formPanel.add(new JLabel("Check In:"));
        checkIn = new JSpinner(new SpinnerDateModel());
        checkIn.setEditor(new JSpinner.DateEditor(checkIn, "dd/MM/yyyy"));
        formPanel.add(checkIn);
        
        formPanel.add(new JLabel("Check Out:"));
        checkOut = new JSpinner(new SpinnerDateModel());
        checkOut.setEditor(new JSpinner.DateEditor(checkOut, "dd/MM/yyyy"));
        formPanel.add(checkOut);
        
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

    public String getCheckIn() {
        return new SimpleDateFormat("dd/MM/yyyy").format(checkIn.getValue());
    }

    public String getCheckOut() {
        return new SimpleDateFormat("dd/MM/yyyy").format(checkOut.getValue());
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
    
    public void agregarReservaATabla(String codigo, String cliente, String habitacion, String fechaInicio, String fechaFin, String estado) {
        System.out.println("\n=== DATOS DE RESERVA ===");
        System.out.println("Código: " + codigo);
        System.out.println("Cliente: " + cliente);
        System.out.println("Habitación: " + habitacion);
        System.out.println("Fecha Inicio: " + fechaInicio);
        System.out.println("Fecha Fin: " + fechaFin);
        System.out.println("Estado: " + estado);
        System.out.println("=======================\n");
        modeloTabla.addRow(new Object[]{codigo, cliente, habitacion, fechaInicio, fechaFin, estado});
    }
    
    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }
    
    public void limpiarFormulario() {
        txtCodigo.setText("");
        checkIn.setValue(new Date());
        checkOut.setValue(new Date());
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