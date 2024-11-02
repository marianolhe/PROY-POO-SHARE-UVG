import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionHorasBeca {

    // Clase interna para representar a un Estudiante
    public static class Estudiante {
        private String nombre;
        private String id;
        private int horasBeca;

        // Constructor
        public Estudiante(String nombre, String id, int horasBeca) {
            this.nombre = nombre;
            this.id = id;
            this.horasBeca = horasBeca;
        }

        // Getters y Setters
        public String getNombre() {
            return nombre;
        }

        public String getId() {
            return id;
        }

        public int getHorasBeca() {
            return horasBeca;
        }

        public void setHorasBeca(int horasBeca) {
            this.horasBeca = horasBeca;
        }

        // Método para agregar horas de beca
        public void agregarHorasBeca(int horas) {
            if (horas >= 0) {
                this.horasBeca += horas;
            } else {
                System.out.println("No se pueden agregar horas negativas.");
            }
        }

        @Override
        public String toString() {
            return nombre + "," + id + "," + horasBeca;
        }
    }

    // Clase para la gestión de estudiantes
    public static class GestionEstudiantes {
        private List<Estudiante> estudiantes;

        private final String archivoCSV = "archivos_csv/estudiantes.csv"; 

        // Constructor
        public GestionEstudiantes() {
            this.estudiantes = new ArrayList<>();
            cargarEstudiantes();
        }

        // Método para cargar estudiantes desde el archivo CSV
        public void cargarEstudiantes() {
            // Código de depuración para verificar el directorio actual y la ruta del archivo
            System.out.println("Directorio actual: " + new File(".").getAbsolutePath());
            System.out.println("Intentando abrir archivo en la ruta: " + archivoCSV);

            try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    String nombre = datos[0];
                    String id = datos[1];
                    int horasBeca = Integer.parseInt(datos[2]);
                    estudiantes.add(new Estudiante(nombre, id, horasBeca));
                }
                System.out.println("Archivo leído exitosamente.");
            } catch (FileNotFoundException e) {
                System.out.println("Error: El archivo no se encontró en la ruta especificada.");
            } catch (IOException e) {
                System.out.println("Error al leer el archivo CSV: " + e.getMessage());
            }
        }

        // Método para guardar estudiantes en el archivo CSV
        public void guardarEstudiantes() {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivoCSV))) {
                for (Estudiante estudiante : estudiantes) {
                    pw.println(estudiante.toString());
                }
                System.out.println("Datos guardados correctamente en el archivo.");
            } catch (IOException e) {
                System.out.println("Error al guardar en el archivo CSV: " + e.getMessage());
            }
        }

        // Método para buscar estudiante por ID
        public Estudiante buscarEstudiantePorID(String id) {
            for (Estudiante estudiante : estudiantes) {
                if (estudiante.getId().equals(id)) {
                    return estudiante;
                }
            }
            return null;
        }

        // Método para agregar horas a un estudiante específico
        public void agregarHorasEstudiante(String id, int horas) {
            Estudiante estudiante = buscarEstudiantePorID(id);
            if (estudiante != null) {
                estudiante.agregarHorasBeca(horas);
                guardarEstudiantes();  // Actualizar el archivo CSV con los nuevos datos
                System.out.println("Horas de beca actualizadas para " + estudiante.getNombre());
            } else {
                System.out.println("Estudiante no encontrado.");
            }
        }
    }

   