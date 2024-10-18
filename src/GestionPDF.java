import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class GestionPDF {
    private String carpetaBase;

    public GestionPDF(String carpetaBase) {
        this.carpetaBase = carpetaBase;
    }

    // Método para leer la carrera desde el archivo CSV de usuarios
    private String obtenerCarreraDesdeCSV(String correoUsuario) {
        String carrera = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("archivos_csv/usuarios.csv"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] columnas = linea.split(",");
                String correo = columnas[0].trim();
                if (correo.equalsIgnoreCase(correoUsuario)) {
                    carrera = columnas[2].trim();  // Supongo que la carrera está en la tercera columna
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV de usuarios: " + e.getMessage());
        }
        return carrera;
    }

    // Método modificado para subir el archivo basado en la entrada del usuario
    public void subirArchivo(String rutaArchivo, String codigoCurso, String correoUsuario) {
        // Obtener la carrera desde el archivo CSV
        String carreraAbreviada = obtenerCarreraDesdeCSV(correoUsuario);
        
        if (carreraAbreviada == null) {
            System.out.println("ERROR: No se encontró la carrera asociada al correo: " + correoUsuario);
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Solicitar el año
        System.out.print("Ingrese el año del curso (por ejemplo, 2024): ");
        String año = scanner.nextLine();

        // Crear la carpeta con el formato carrera-año-codigoCurso
        String nombreCarpeta = carreraAbreviada + "-" + año + "-" + codigoCurso;
        Path rutaDestino = Paths.get(carpetaBase, nombreCarpeta);

        // Verificar si la carpeta existe
        if (Files.exists(rutaDestino)) {
            File archivo = new File(rutaArchivo);

            // Verificar que el archivo exista y sea un archivo PDF
            if (archivo.exists() && archivo.isFile() && rutaArchivo.endsWith(".pdf")) {
                try {
                    // Guardar el PDF en la carpeta de destino
                    Path archivoDestino = rutaDestino.resolve(archivo.getName());
                    Files.copy(archivo.toPath(), archivoDestino);
                    System.out.println("Archivo subido correctamente a la carpeta: " + rutaDestino.toString());

                    // Guardar los datos en el archivo CSV
                    guardarDatosCSV(archivo.getName(), rutaArchivo, codigoCurso, carreraAbreviada, año, "No revisado");

                } catch (IOException e) {
                    System.out.println("Error al copiar el archivo: " + e.getMessage());
                }
            } else {
                System.out.println("ERROR: Asegúrate de que el archivo exista y sea un archivo PDF.");
            }
        } else {
            System.out.println("ERROR: No existe la carpeta: " + nombreCarpeta);
        }
        scanner.close();
    }

    // Método para guardar los datos en un archivo CSV
    private void guardarDatosCSV(String nombreArchivo, String rutaArchivo, String codigoCurso, String carreraAbreviada, String año, String estado) {
        String carpetaCSV = "archivos_csv";  
        Path rutaCSV = Paths.get(carpetaCSV);

        // Crear la carpeta "archivos_csv" si no existe
        try {
            Files.createDirectories(rutaCSV);
        } catch (IOException e) {
            System.out.println("Error al crear la carpeta de CSV: " + e.getMessage());
        }

        File archivoCSV = new File(carpetaCSV, "Apuntes.csv");

        try (FileWriter writer = new FileWriter(archivoCSV, true)) {
            if (!archivoCSV.exists() || archivoCSV.length() == 0) {
                writer.write("Nombre del archivo,Ruta del archivo,Código del curso,Carrera abreviada,Año,Estado\n");
            }

            writer.write(nombreArchivo + "," + rutaArchivo + "," + codigoCurso + "," + carreraAbreviada + "," + año + "," + estado + "\n");
            System.out.println("Datos guardados correctamente en " + archivoCSV.getPath());
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }
}