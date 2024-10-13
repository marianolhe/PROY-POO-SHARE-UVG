public class Usuario extends PersonaPlantilla {

    public Usuario(String nombre, String apellido, String correo, String contrasena, String carrera) {
        super(nombre, apellido, correo, contrasena, carrera);
    }

    @Override
    public String getRol() {
        return "Usuario";
    }
}
