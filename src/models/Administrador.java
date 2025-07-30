package models;

public class Administrador extends Usuario {
    public Administrador(String id, String nombre, String password) {
        super(id, nombre, password);
    }

    @Override
    public String getTipoUsuario() {
        return "ADMINISTRADOR";
    }
}