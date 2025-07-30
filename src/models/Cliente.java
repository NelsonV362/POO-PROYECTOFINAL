package models;

public class Cliente {
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    
    public Cliente(String dni, String nombre, String apellido, String telefono, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
    }
    
    public String getDni() {
        return dni;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + dni + ")";
    }
}