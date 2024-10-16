public class Revisor extends PersonaPlantilla {
    public Revisor(String nombre, String apellido, String correo, String contrasena, String carrera) {
        super(nombre, apellido, correo, contrasena, carrera);
    }

    // Método para obtener el tipo de menú asociado
    @Override
    public String getRol() {
        return "Revisor";
    }

    public void aprobarDocumento() {
        System.out.println("Documento aprobado.");
    }
    
}