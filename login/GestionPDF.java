import java.io.*;
import java.nio.file.*;

public class GestionPDF {
    // Carpeta base donde se guardarán los archivos PDF organizados
    private String carpetaBase;

    public GestionPDF(String carpetaBase) {
        this.carpetaBase = carpetaBase;
    }

    // Método para subir y guardar el archivo en la estructura de carpetas generada
    public void subirArchivo(String rutaArchivo, String codigoCurso, String carreraAbreviada) {
        File archivo = new File(rutaArchivo);

        // Verificar que el archivo exista y sea un archivo PDF
        if (archivo.exists() && archivo.isFile() && rutaArchivo.endsWith(".pdf")) {
            try {
                // Crear la ruta de destino con el formato: CarreraAbreviada-CodigoCurso
                String nombreCarpeta = carreraAbreviada + "-" + codigoCurso;
                Path rutaDestino = Paths.get(carpetaBase, nombreCarpeta);

                // Crear las carpetas si no existen
                Files.createDirectories(rutaDestino);

                // Guardar el PDF en la carpeta de destino
                Path archivoDestino = rutaDestino.resolve(archivo.getName());
                Files.copy(archivo.toPath(), archivoDestino);

                System.out.println("Archivo subido correctamente a la carpeta: " + rutaDestino.toString());

                // Guardar los datos en el archivo CSV dentro de la carpeta "Datos CSV" en el directorio raíz
                guardarDatosCSV(archivo.getName(), rutaArchivo, codigoCurso, carreraAbreviada, "No revisado");

            } catch (IOException e) {
                System.out.println("Error al copiar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("ERROR: Asegúrate de que el archivo exista y sea un archivo PDF.");
        }
    }

    // Método para guardar los datos en un archivo CSV con títulos de columna en la carpeta "Datos CSV"
    private void guardarDatosCSV(String nombreArchivo, String rutaArchivo, String codigoCurso, String carreraAbreviada, String estado) {
        // Definir la carpeta "Datos CSV" en la raíz del repositorio
        String carpetaCSV = "Datos CSV";  // Ruta relativa dentro del repositorio
        Path rutaCSV = Paths.get(carpetaCSV);

        // Crear la carpeta "Datos CSV" si no existe
        try {
            Files.createDirectories(rutaCSV);
        } catch (IOException e) {
            System.out.println("Error al crear la carpeta de CSV: " + e.getMessage());
        }

        // Definir la ruta completa del archivo CSV
        File archivoCSV = new File(carpetaCSV, "Apuntes.csv");

        try (FileWriter writer = new FileWriter(archivoCSV, true)) {
            // Si el archivo no existe o está vacío, escribir los títulos de las columnas
            if (!archivoCSV.exists() || archivoCSV.length() == 0) {
                writer.write("Nombre del archivo,Ruta del archivo,Código del curso,Carrera abreviada,Estado\n");
            }

            // Escribir los datos del archivo, incluyendo el estado "No revisado"
            writer.write(nombreArchivo + "," + rutaArchivo + "," + codigoCurso + "," + carreraAbreviada + "," + estado + "\n");
            System.out.println("Datos guardados correctamente en " + archivoCSV.getPath());
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }
}
