import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    // Método para listar los archivos en la carpeta de una carrera y curso específico
    public List<String> listarArchivos(String carreraAbreviada, String codigoCurso) {
        String nombreCarpeta = carreraAbreviada + "-" + codigoCurso;
        Path rutaCarpeta = Paths.get(carpetaBase, nombreCarpeta);
        List<String> archivos = new ArrayList<>();

        if (Files.exists(rutaCarpeta) && Files.isDirectory(rutaCarpeta)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(rutaCarpeta, "*.pdf")) {
                for (Path archivo : stream) {
                    archivos.add(archivo.getFileName().toString());
                }
            } catch (IOException e) {
                System.out.println("Error al listar archivos: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró la carpeta: " + rutaCarpeta.toString());
        }

        return archivos;
    }

    // Método para aprobar o denegar un archivo PDF y actualizar el CSV
    public void aprobarOdenegarArchivo(String nombreArchivo, String carreraAbreviada, String codigoCurso, String accion) {
        File archivoCSV = new File("Datos CSV/Apuntes.csv");
        List<String> lineasCSV = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] columnas = linea.split(",");

                // Si el nombre del archivo coincide y pertenece al curso y carrera indicados
                if (columnas[0].equals(nombreArchivo) && columnas[2].equals(codigoCurso) && columnas[3].equals(carreraAbreviada)) {
                    // Actualizar el estado del archivo
                    columnas[4] = accion.equalsIgnoreCase("aprobar") ? "Aprobado" : "Denegado";
                    linea = String.join(",", columnas);
                }

                lineasCSV.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }

        // Reescribir el archivo CSV con las modificaciones
        try (FileWriter writer = new FileWriter(archivoCSV, false)) {
            for (String linea : lineasCSV) {
                writer.write(linea + "\n");
            }
            System.out.println("Estado del archivo actualizado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }

    // Método para correr un mini menú de prueba
    public void menu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la carrera abreviada (ejemplo: ICC): ");
        String carrera = scanner.nextLine();

        System.out.print("Ingrese el código del curso: ");
        String codigoCurso = scanner.nextLine();

        // Listar los archivos disponibles en la carpeta especificada
        List<String> archivos = listarArchivos(carrera, codigoCurso);
        if (archivos.isEmpty()) {
            System.out.println("No hay archivos para revisar en esta carpeta.");
            return;
        }

        System.out.println("Archivos disponibles:");
        for (int i = 0; i < archivos.size(); i++) {
            System.out.println((i + 1) + ". " + archivos.get(i));
        }

        System.out.print("Seleccione el número del archivo que desea revisar: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer

        if (opcion < 1 || opcion > archivos.size()) {
            System.out.println("Opción inválida.");
            return;
        }

        String archivoSeleccionado = archivos.get(opcion - 1);
        System.out.println("Ha seleccionado el archivo: " + archivoSeleccionado);

        System.out.print("¿Desea aprobar o denegar el archivo? (aprobar/denegar): ");
        String accion = scanner.nextLine();

        if (!accion.equalsIgnoreCase("aprobar") && !accion.equalsIgnoreCase("denegar")) {
            System.out.println("Acción inválida.");
            return;
        }

        // Actualizar el estado del archivo en el CSV
        aprobarOdenegarArchivo(archivoSeleccionado, carrera, codigoCurso, accion);
    }
}
