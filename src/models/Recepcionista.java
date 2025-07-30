package models;

public class Recepcionista extends Usuario {
    public Recepcionista(String id, String nombre, String password) {
        super(id, nombre, password);
    }

    public String getTipoUsuario() {
        return "RECEPCIONISTA";
    }
}