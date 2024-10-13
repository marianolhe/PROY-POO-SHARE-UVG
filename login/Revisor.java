public class Revisor extends PersonaPlantilla {
    public Revisor(String nombre, String apellido, String correo, String contrasena, String carrera) {
        super(nombre, apellido, correo, contrasena, carrera);
    }

    // Método para obtener el tipo de menú asociado
    public String getTipoMenu() {
        return "Revisor";
    }
}