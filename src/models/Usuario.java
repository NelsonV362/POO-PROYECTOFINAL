package models;

import java.util.Objects;

public abstract class Usuario {
    private final String id;
    private String nombre;
    private String passwordHash;  
    
    public Usuario(String id, String nombre, String password) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede ser nulo o vacío.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }
        
        this.id = id;
        this.nombre = nombre;
        this.passwordHash = hashPassword(password);
    }
    
    public String getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }
        public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }
        
    public abstract String getTipoUsuario();
    
    public boolean autenticar(String password) {
        if (password == null) return false;
        return Objects.equals(passwordHash, hashPassword(password));
    }
    
    private String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }
    
    @Override
    public String toString() {
        return "Usuario{id='" + id + "', nombre='" + nombre + "', tipo='" + getTipoUsuario() + "'}";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return id.equals(usuario.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
