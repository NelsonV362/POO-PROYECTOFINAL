package models;

public abstract class Usuario {
    private String id;
    private String nombre;
    private String password;
    
    public Usuario(String id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }
    
    // Getters y setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPassword() { return password; }
    
    public abstract String getTipoUsuario();
    
    public boolean autenticar(String password) {
        return this.password.equals(password);
    }
}