package models;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataManager {
    private static final String CLIENTES_FILE = "clientes.txt";
    private static final String HABITACIONES_FILE = "habitaciones.txt";
    private static final String RESERVAS_FILE = "reservas.txt";
    public static final String USUARIOS_FILE = "usuarios.txt";

    // Métodos genéricos de archivo
    private List<String> cargarDatos(String archivo) {
        List<String> datos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                datos.add(linea);
            }
        } catch (IOException ignored) {
            // Archivo no encontrado, se creará al guardar
        }
        return datos;
    }

    private void guardarDatos(String archivo, List<String> datos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            for (String linea : datos) {
                writer.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // USUARIOS
    public Usuario autenticarUsuario(String nombre, String password) {
        for (String linea : cargarDatos(USUARIOS_FILE)) {
            String[] partes = linea.split(",");
            if (partes.length >= 4 && partes[1].equals(nombre) && partes[2].equals(password)) {
                return partes[3].equalsIgnoreCase("ADMINISTRADOR") ?
                        new Administrador(partes[0], partes[1], partes[2]) :
                        new Recepcionista(partes[0], partes[1], partes[2]);
            }
        }
        return null;
    }

    // CLIENTES
    public boolean agregarCliente(Cliente cliente) {
        List<String> clientes = cargarDatos(CLIENTES_FILE);
        clientes.add(clienteToTxt(cliente));
        guardarDatos(CLIENTES_FILE, clientes);
        return true;
    }

    public List<Cliente> obtenerClientes() {
        List<Cliente> lista = new ArrayList<>();
        for (String linea : cargarDatos(CLIENTES_FILE)) {
            String[] p = linea.split(",");
            if (p.length == 5) {
                lista.add(new Cliente(p[0], p[1], p[2], p[3], p[4]));
            }
        }
        return lista;
    }

    private String clienteToTxt(Cliente c) {
        return String.join(",", c.getDni(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getEmail());
    }

    public Cliente buscarClientePorDni(String dni) {
        for (Cliente c : obtenerClientes()) {
            if (c.getDni().equals(dni)) return c;
        }
        return null;
    }

    // HABITACIONES
    public boolean agregarHabitacion(Habitacion h) {
        List<String> habitaciones = cargarDatos(HABITACIONES_FILE);
        habitaciones.add(habitacionToTxt(h));
        guardarDatos(HABITACIONES_FILE, habitaciones);
        return true;
    }

    public List<Habitacion> obtenerHabitaciones() {
        List<Habitacion> lista = new ArrayList<>();
        for (String linea : cargarDatos(HABITACIONES_FILE)) {
            String[] p = linea.split(",");
            if (p.length == 4) {
                int num = Integer.parseInt(p[0]);
                TipoHabitacion tipo = TipoHabitacion.valueOf(p[1]);
                double precio = Double.parseDouble(p[2]);
                boolean disponible = Boolean.parseBoolean(p[3]);
                Habitacion h = new Habitacion(num, tipo, precio);
                h.setDisponible(disponible);
                lista.add(h);
            }
        }
        return lista;
    }

    private String habitacionToTxt(Habitacion h) {
        return String.join(",", String.valueOf(h.getNumero()), h.getTipo().name(),
                String.valueOf(h.getPrecioPorNoche()), String.valueOf(h.isDisponible()));
    }

    public Habitacion buscarHabitacionPorNumero(int numero) {
        for (Habitacion h : obtenerHabitaciones()) {
            if (h.getNumero() == numero) return h;
        }
        return null;
    }

    // RESERVAS
    public boolean agregarReserva(Reserva r) {
        List<String> reservas = cargarDatos(RESERVAS_FILE);
        reservas.add(reservaToTxt(r));
        guardarDatos(RESERVAS_FILE, reservas);
        return true;
    }

    public List<Reserva> obtenerReservas() {
        List<Reserva> lista = new ArrayList<>();
        for (String linea : cargarDatos(RESERVAS_FILE)) {
            Reserva r = txtToReserva(linea);
            if (r != null) lista.add(r);
        }
        return lista;
    }

    private String reservaToTxt(Reserva r) {
        return String.join(",", r.getCodigo(), r.getCliente().getDni(),
                String.valueOf(r.getHabitacion().getNumero()),
                String.valueOf(r.getFechaCheckIn().getTime()),
                String.valueOf(r.getFechaCheckOut().getTime()),
                String.valueOf(r.getPrecioTotal()),
                String.valueOf(r.isActiva()));
    }

    private Reserva txtToReserva(String linea) {
        String[] p = linea.split(",");
        if (p.length != 7) return null;

        String codigo = p[0];
        Cliente cliente = buscarClientePorDni(p[1]);
        Habitacion habitacion = buscarHabitacionPorNumero(Integer.parseInt(p[2]));
        if (cliente == null || habitacion == null) return null;

        Date checkIn = new Date(Long.parseLong(p[3]));
        Date checkOut = new Date(Long.parseLong(p[4]));
        boolean activa = Boolean.parseBoolean(p[6]);

        Reserva reserva = new Reserva(codigo, cliente, habitacion, checkIn, checkOut);
        if (!activa) reserva.cancelar();
        return reserva;
    }
}
