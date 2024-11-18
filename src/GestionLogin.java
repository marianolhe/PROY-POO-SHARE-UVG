import java.io.*;
import javax.swing.JOptionPane;

public class GestionLogin {
    private static final String CSV_FILE = "archivos_csv/perfiles.csv";
    //private static final String[] CARRERAS = {"Ingeniería en Ciencias de la Computación y Tecnologías de la Información"};

    public void registrarUsuario(String nombre, String apellido, String correo, String contrasena, String carrera, String rol) throws IOException {
        String inicialesCarrera = obtenerIniciales(carrera);

        PersonaPlantilla persona;
        if (rol.equals("Usuario")) {
            persona = new Usuario(nombre, apellido, correo, contrasena, inicialesCarrera);
        } else if (rol.equals("Revisor")) {
            persona = new Revisor(nombre, apellido, correo, contrasena, inicialesCarrera);
        } else {
            throw new IllegalArgumentException("Rol inválido");
        }

        guardarUsuario(persona);
    }

    public PersonaPlantilla iniciarSesion(String correo, String contrasena) throws IOException {
        return buscarUsuario(correo, contrasena);
    }

    private static void guardarUsuario(PersonaPlantilla persona) throws IOException {
        File archivo = new File(CSV_FILE);

        if (!archivo.exists()) {
            archivo.getParentFile().mkdirs(); // Crea la carpeta archivos_csv si no existe
            archivo.createNewFile();
        }

        try (FileWriter writer = new FileWriter(archivo, true)) {
            writer.write(persona.getNombre() + "," + persona.getApellido() + "," +
                        persona.getCorreo() + "," + persona.getContrasena() + "," +
                        persona.getCarrera() + "," + persona.getRol() + "\n");
        }
    }

    private PersonaPlantilla buscarUsuario(String correo, String contrasena) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[2].equals(correo) && datos[3].equals(contrasena)) {
                    String nombre = datos[0];
                    String apellido = datos[1];
                    String carrera = datos[4];
                    String rol = datos[5];

                    if (rol.equals("Usuario")) {
                        return new Usuario(nombre, apellido, correo, contrasena, carrera);
                    } else if (rol.equals("Revisor")) {
                        return new Revisor(nombre, apellido, correo, contrasena, carrera);
                    } else if (rol.equals("Administrador")) {
                        return new Administrador(nombre, apellido, correo, contrasena, carrera);
                    }
                }
            }
        }
        return null;
    }

    public String obtenerIniciales(String carrera) {
        String[] palabras = carrera.split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (String palabra : palabras) {
            char primeraLetra = palabra.charAt(0);
            if (Character.isUpperCase(primeraLetra)) {
                iniciales.append(primeraLetra);
            }
        }
        return iniciales.toString();
    }

    public void mostrarMenuPorRol(PersonaPlantilla persona) {
        String[] opciones;
        String titulo;

        if (persona.getRol().equals("Usuario")) {
            opciones = new String[]{"Subir Apunte", "Descargar Apunte", "Cerrar Sesión"};
            titulo = "Menú Estudiante";
        } else if (persona.getRol().equals("Revisor")) {
            opciones = new String[]{"Revisar apuntes", "Cerrar Sesión"};
            titulo = "Menú Revisor";
        } else if (persona.getRol().equals("Administrador")) {
            opciones = new String[]{"Aceptar Revisor", "Gestionar Horas Beca", "Cerrar Sesión"};
            titulo = "Menú Administrador";
        } else {
            JOptionPane.showMessageDialog(null, "Rol no válido.");
            return;
        }

        GestionPDF gestionPDF = new GestionPDF("APUNTES"); // Instancia de GestionPDF

        String opcion;
        do {
            opcion = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione una opción",
                    titulo,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (opcion != null && !opcion.equals("Cerrar Sesión")) {
                if (opcion.equals("Subir Apunte")) {
                    // Lógica para subir apunte
                    gestionPDF.seleccionarYSubirArchivo(persona.getCorreo());
                } else if (opcion.equals("Descargar Apunte")) {
                    // Lógica para descargar apunte
                    JOptionPane.showMessageDialog(null, "Función no implementada");
                } else if (opcion.equals("Revisar apuntes")) {
                    // Lógica para revisar apuntes
                    JOptionPane.showMessageDialog(null, "Función no implementada");
                } else if (opcion.equals("Aceptar Revisor")) {
                    // Lógica para aceptar revisor
                    JOptionPane.showMessageDialog(null, "Función no implementada");
                } else if (opcion.equals("Gestionar Horas Beca")) {
                    // Lógica para gestionar horas beca
                    JOptionPane.showMessageDialog(null, "Función no implementada");
                }
            }
        } while (opcion != null && !opcion.equals("Cerrar Sesión"));
    }
}

