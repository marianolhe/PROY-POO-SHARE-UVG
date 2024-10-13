public class Revisor extends PersonaPlantilla {

    public Revisor(String nombre, String apellido, String correo, String contrasena, String carrera) {
        super(nombre, apellido, correo, contrasena, carrera);
    }

    @Override
    public String getRol() {
        return "Revisor";
    }
}
