package models;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataManager {
    public static final String CLIENTES_FILE = "clientes.txt";
    public static final String HABITACIONES_FILE = "habitaciones.txt";
    public static final String RESERVAS_FILE = "reservas.txt";
    public static final String USUARIOS_FILE = "usuarios.txt";

    public List<String> cargarDatos(String archivo) {
        List<String> datos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                datos.add(linea);
            }
        } catch (IOException ignored) {}
        return datos;
    }

    public void guardarDatos(String archivo, List<String> datos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
            for (String linea : datos) {
                writer.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convertirReservasATextoISO() {
        List<String> lineasOriginales = cargarDatos(RESERVAS_FILE);
        List<String> lineasNuevas = new ArrayList<>();

        for (String linea : lineasOriginales) {
            String[] p = linea.split(",");
            if (p.length != 7) continue;

            String codigo = p[0];
            String dniCliente = p[1];
            String numeroHab = p[2];

            try {
                long checkInMillis = Long.parseLong(p[3]);
                long checkOutMillis = Long.parseLong(p[4]);
                LocalDate checkIn = Instant.ofEpochMilli(checkInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOut = Instant.ofEpochMilli(checkOutMillis).atZone(ZoneId.systemDefault()).toLocalDate();

                String precioTotal = p[5];
                String activa = p[6];

                String lineaNueva = String.join(",",
                    codigo,
                    dniCliente,
                    numeroHab,
                    checkIn.toString(),
                    checkOut.toString(),
                    precioTotal,
                    activa
                );

                lineasNuevas.add(lineaNueva);
            } catch (NumberFormatException e) {
                lineasNuevas.add(linea);
            }
        }

        guardarDatos(RESERVAS_FILE, lineasNuevas);
    }

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

    public boolean agregarCliente(Cliente cliente) {
        List<String> clientes = cargarDatos(CLIENTES_FILE);
        clientes.add(clienteToTxt(cliente));
        guardarDatos(CLIENTES_FILE, clientes);
        return true;
    }

    public boolean eliminarCliente(String dni) {
        List<String> clientes = cargarDatos(CLIENTES_FILE);
        boolean eliminado = clientes.removeIf(linea -> {
            String[] partes = linea.split(",");
            return partes.length > 0 && partes[0].equals(dni);
        });
        if (eliminado) {
            guardarDatos(CLIENTES_FILE, clientes);
        }
        return eliminado;
    }

    public Cliente buscarClientePorDni(String dni) {
        for (Cliente c : obtenerClientes()) {
            if (c.getDni().equals(dni)) return c;
        }
        return null;
    }

    private String clienteToTxt(Cliente c) {
        return String.join(",", c.getDni(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getEmail());
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
    
    public boolean agregarHabitacion(Habitacion h) {
        List<String> habitaciones = cargarDatos(HABITACIONES_FILE);
        habitaciones.add(habitacionToTxt(h));
        guardarDatos(HABITACIONES_FILE, habitaciones);
        return true;
    }

    public boolean eliminarHabitacion(String numero) {
        List<String> habitaciones = cargarDatos(HABITACIONES_FILE);
        boolean eliminado = habitaciones.removeIf(linea -> {
            String[] partes = linea.split(",");
            return partes.length > 0 && partes[0].equals(numero);
        });
        if (eliminado) {
            guardarDatos(HABITACIONES_FILE, habitaciones);
        }
        return eliminado;
    }

    public Habitacion buscarHabitacionPorNumero(int numero) {
        for (Habitacion h : obtenerHabitaciones()) {
            if (h.getNumero() == numero) return h;
        }
        return null;
    }    

    private String habitacionToTxt(Habitacion h) {
        return String.join(",",
            String.valueOf(h.getNumero()),
            h.getTipo().name(),
            String.valueOf(h.getPrecioPorNoche()),
            String.valueOf(h.isDisponible())
        );
    }

    
    public List<Reserva> obtenerReservas() {
        List<Reserva> lista = new ArrayList<>();
        for (String linea : cargarDatos(RESERVAS_FILE)) {
            String[] p = linea.split(",");
            if (p.length == 6) {
                String codigo = p[0];
                Cliente cliente = buscarClientePorDni(p[1]);
                Habitacion habitacion = buscarHabitacionPorNumero(Integer.parseInt(p[2]));
                Date checkIn = parseDate(p[3]);
                Date checkOut = parseDate(p[4]);
                boolean activa = Boolean.parseBoolean(p[5]);
                Reserva r = new Reserva(codigo, cliente, habitacion, checkIn, checkOut, activa);
                lista.add(r);
            }
        }
        return lista;
    }

    public boolean agregarReserva(Reserva r) {
        List<String> reservas = cargarDatos(RESERVAS_FILE);
        reservas.add(reservaToTxt(r));
        guardarDatos(RESERVAS_FILE, reservas);
        return true;
    }

    public boolean eliminarReserva(String dni, String nro) {
        List<String> reservas = cargarDatos(RESERVAS_FILE);
        boolean eliminado = false;

        if (dni!=null) {
            eliminado = reservas.removeIf(linea -> {
                String[] partes = linea.split(",");
                return partes.length > 0 && partes[1].equals(dni);
            });
        } else {
            eliminado = reservas.removeIf(linea -> {
                String[] partes = linea.split(",");
                return partes.length > 0 && partes[2].equals(nro);
            });
        }

        if (eliminado) {
            guardarDatos(RESERVAS_FILE, reservas);
        }
        return eliminado;
    }

    public boolean cancelarReserva(String codigo) {
        List<Reserva> reservas = obtenerReservas();
        boolean encontrada = false;
        for (Reserva r : reservas) {
            if (r.getCodigo().equals(codigo) && r.isActiva()) {
                r.cancelar();
                encontrada = true;
                break;
            }
        }
        if (encontrada) {
            List<String> lineas = new ArrayList<>();
            for (Reserva r : reservas) {
                lineas.add(reservaToTxt(r));
            }
            guardarDatos(RESERVAS_FILE, lineas);
        }

        return encontrada;
    }

    public String obtenerCodigoReserva() {
        List<Reserva> reservas = obtenerReservas();
        if (reservas.isEmpty()) return "1";
        int codigo = Integer.parseInt(reservas.getLast().getCodigo())+1;
        return String.valueOf(codigo);
    }

    private String reservaToTxt(Reserva r) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return String.join(",",
                r.getCodigo(),
                r.getCliente().getDni(),
                String.valueOf(r.getHabitacion().getNumero()),
                formato.format(r.getCheckIn()),
                formato.format(r.getCheckOut()),
                String.valueOf(r.isActiva())
        );
    }

    private Date parseDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            System.err.println("Error al parsear la fecha");
            return null;
        }
    }

}
