package models;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class DataManager {
    private static final String CLIENTES_FILE = "clientes.txt";
    private static final String HABITACIONES_FILE = "habitaciones.txt";
    private static final String RESERVAS_FILE = "reservas.txt";
    public static final String USUARIOS_FILE = "usuarios.txt";

    // -------------------- Utilidades de archivo --------------------

    private <T> void guardarLineas(String archivo, List<String> lineas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            for (String linea : lineas) writer.println(linea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> cargarLineas(String archivo) {
        List<String> datos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) datos.add(linea);
        } catch (IOException ignored) {} // Se crea al guardar si no existe
        return datos;
    }

    // -------------------- Usuarios --------------------

    public Usuario autenticarUsuario(String nombre, String password) {
        for (String linea : cargarLineas(USUARIOS_FILE)) {
            String[] partes = linea.split(",");
            if (partes.length >= 4 && partes[1].equals(nombre) && partes[2].equals(password)) {
                return partes[3].equals("ADMINISTRADOR")
                        ? new Administrador(partes[0], partes[1], partes[2])
                        : new Recepcionista(partes[0], partes[1], partes[2]);
            }
        }
        return null;
    }

    // -------------------- Clientes --------------------

    public boolean agregarCliente(Cliente cliente) {
        List<String> clientes = cargarLineas(CLIENTES_FILE);
        clientes.add(clienteToTxt(cliente));
        guardarLineas(CLIENTES_FILE, clientes);
        return true;
    }

    public List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        for (String linea : cargarLineas(CLIENTES_FILE)) {
            String[] p = linea.split(",");
            if (p.length == 5) {
                clientes.add(new Cliente(p[0], p[1], p[2], p[3], p[4]));
            }
        }
        return clientes;
    }

    private String clienteToTxt(Cliente c) {
        return String.join(",", c.getDni(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getEmail());
    }

    public Cliente buscarClientePorDni(String dni) {
        return obtenerClientes().stream()
                .filter(c -> c.getDni().equals(dni))
                .findFirst().orElse(null);
    }

    // -------------------- Habitaciones --------------------

    public boolean agregarHabitacion(Habitacion h) {
        List<String> habitaciones = cargarLineas(HABITACIONES_FILE);
        habitaciones.add(habitacionToTxt(h));
        guardarLineas(HABITACIONES_FILE, habitaciones);
        return true;
    }

    public List<Habitacion> obtenerHabitaciones() {
        List<Habitacion> habitaciones = new ArrayList<>();
        for (String linea : cargarLineas(HABITACIONES_FILE)) {
            String[] p = linea.split(",");
            if (p.length == 4) {
                int num = Integer.parseInt(p[0]);
                TipoHabitacion tipo = TipoHabitacion.valueOf(p[1]);
                double precio = Double.parseDouble(p[2]);
                boolean disponible = Boolean.parseBoolean(p[3]);
                Habitacion h = new Habitacion(num, tipo, precio);
                h.setDisponible(disponible);
                habitaciones.add(h);
            }
        }
        return habitaciones;
    }

    private String habitacionToTxt(Habitacion h) {
        return String.join(",", String.valueOf(h.getNumero()), h.getTipo().name(),
                String.valueOf(h.getPrecioPorNoche()), String.valueOf(h.isDisponible()));
    }

    public Habitacion buscarHabitacionPorNumero(int numero) {
        return obtenerHabitaciones().stream()
                .filter(h -> h.getNumero() == numero)
                .findFirst().orElse(null);
    }

    // -------------------- Reservas --------------------

    public boolean agregarReserva(Reserva r) {
        List<String> reservas = cargarLineas(RESERVAS_FILE);
        reservas.add(reservaToTxt(r));
        guardarLineas(RESERVAS_FILE, reservas);
        return true;
    }

    public List<Reserva> obtenerReservas() {
        List<Reserva> reservas = new ArrayList<>();
        for (String linea : cargarLineas(RESERVAS_FILE)) {
            Reserva r = txtToReserva(linea);
            if (r != null) reservas.add(r);
        }
        return reservas;
    }

    private String reservaToTxt(Reserva r) {
        return String.join(",", r.getCodigo(), r.getCliente().getDni(),
                String.valueOf(r.getHabitacion().getNumero()),
                String.valueOf(toEpoch(r.getFechaCheckIn())),
                String.valueOf(toEpoch(r.getFechaCheckOut())),
                String.valueOf(r.getPrecioTotal()), String.valueOf(r.isActiva()));
    }

    private Reserva txtToReserva(String linea) {
        String[] p = linea.split(",");
        if (p.length != 7) return null;

        String codigo = p[0];
        Cliente cliente = buscarClientePorDni(p[1]);
        Habitacion habitacion = buscarHabitacionPorNumero(Integer.parseInt(p[2]));

        if (cliente == null || habitacion == null) return null;

        LocalDate checkIn = fromEpoch(Long.parseLong(p[3]));
        LocalDate checkOut = fromEpoch(Long.parseLong(p[4]));
        boolean activa = Boolean.parseBoolean(p[6]);

        Reserva reserva = new Reserva(codigo, cliente, habitacion, checkIn, checkOut);
        if (!activa) reserva.cancelar();
        return reserva;
    }

    private long toEpoch(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    private LocalDate fromEpoch(long epoch) {
        return Instant.ofEpochSecond(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
