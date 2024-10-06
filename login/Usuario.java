public class Usuario extends PersonaPlantilla {
    public Usuario(String nombre, String apellido, String correo, String contrasena, String carrera) {
        super(nombre, apellido, correo, contrasena, carrera);
    }

    // Método que representa el menú de Usuario
    public String getTipoMenu() {
        return "Usuario";
    }
}