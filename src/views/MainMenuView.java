package views;

import javax.swing.*;

public class MainMenuView extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel panelClientes;
    private JPanel panelHabitaciones;
    private JPanel panelReservas;
    
    public MainMenuView(String tipoUsuario) {
        setTitle("Sistema de Gestión Hotelera - " + tipoUsuario);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        tabbedPane = new JTabbedPane();
        panelClientes = new JPanel();
        panelClientes.add(new JLabel("Gestión de Clientes"));
        tabbedPane.addTab("Clientes", panelClientes);
        
        panelHabitaciones = new JPanel();
        panelHabitaciones.add(new JLabel("Gestión de Habitaciones"));
        tabbedPane.addTab("Habitaciones", panelHabitaciones);
        
        panelReservas = new JPanel();
        panelReservas.add(new JLabel("Gestión de Reservas"));
        tabbedPane.addTab("Reservas", panelReservas);
        
        if ("RECEPCIONISTA".equals(tipoUsuario)) tabbedPane.setEnabledAt(1, false);
        add(tabbedPane);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JPanel getPanelClientes() {
        return panelClientes;
    }

    public JPanel getPanelHabitaciones() {
        return panelHabitaciones;
    }

    public JPanel getPanelReservas() {
        return panelReservas;
    }

}