import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionPDF {
    private String carpetaBase;

    public GestionPDF(String carpetaBase) {
        this.carpetaBase = carpetaBase;
    }

    public void subirArchivo(String rutaArchivo, String codigoCurso, String correoUsuario, String anio) {
        File archivo = new File(rutaArchivo);

        if (archivo.exists() && archivo.isFile() && rutaArchivo.endsWith(".pdf")) {
            try {
                // Obtener la carrera desde el CSV de usuarios
                String carreraAbreviada = obtenerCarreraDesdeCSV(correoUsuario);

                if (carreraAbreviada == null) {
                    System.out.println("No se pudo encontrar la carrera asociada a este usuario.");
                    return;
                }

                // Concatenar carrera-abreviada, año y código del curso
                String nombreCarpeta = carreraAbreviada + "-" + anio + "-" + codigoCurso;
                Path rutaDestino = Paths.get(carpetaBase, nombreCarpeta);

                // Verificar si la carpeta existe
                if (Files.exists(rutaDestino) && Files.isDirectory(rutaDestino)) {
                    // Guardar el PDF en la carpeta de destino
                    Path archivoDestino = rutaDestino.resolve(archivo.getName());
                    Files.copy(archivo.toPath(), archivoDestino);

                    System.out.println("Archivo subido correctamente a la carpeta: " + rutaDestino.toString());

                    // Guardar los datos en el archivo CSV
                    guardarDatosCSV(archivo.getName(), rutaArchivo, codigoCurso, carreraAbreviada, "No revisado");
                } else {
                    System.out.println("ERROR: No existe una carpeta para este curso en el directorio APUNTES.");
                }
            } catch (IOException e) {
                System.out.println("Error al copiar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("ERROR: Asegúrate de que el archivo exista y sea un archivo PDF.");
        }
    }

    // Método para obtener la carrera desde el CSV de usuarios
    private String obtenerCarreraDesdeCSV(String correoUsuario) {
        String carpetaCSV = "Base";  // Carpeta donde se encuentra el archivo CSV
        File archivoCSV = new File(carpetaCSV, "usuarios.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(correoUsuario)) { // Asumiendo que el correo es la primera columna
                    return datos[4]; // La carrera está en la quinta columna (índice 4)
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return null; // Si no se encuentra el correo
    }

    // Método para guardar los datos en un archivo CSV con títulos de columna en la carpeta "archivos_csv"
    private void guardarDatosCSV(String nombreArchivo, String rutaArchivo, String codigoCurso, String carreraAbreviada, String estado) {
        // Definir la carpeta "archivos_csv" en la raíz del repositorio
        String carpetaCSV = "archivos_csv";  // Ruta relativa dentro del repositorio
        Path rutaCSV = Paths.get(carpetaCSV);

        // Crear la carpeta "archivos_csv" si no existe
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
    //Método para revisar archivos
    public void revisarArchivo(String carreraAbreviada, String codigoCurso) {
        Scanner scanner = new Scanner(System.in);
        List<String> archivos = listarArchivos(carreraAbreviada, codigoCurso);

        if (archivos.isEmpty()) {
            System.out.println("No hay archivos para revisar.");
        }

        System.out.println("Archivos disponibles para revisar:");
        for (int i = 0; i < archivos.size(); i++) {
            System.out.println((i + 1) + ". " + archivos.get(i));
        }

        System.out.print("Seleccione el número del archivo que desea revisar: ");
        int seleccion = scanner.nextInt();
        scanner.nextLine();

        if (seleccion < 1 || seleccion > archivos.size()) {
            System.out.println("Selección inválida.");
        }

        String archivoSeleccionado = archivos.get(seleccion - 1);
        System.out.println("Revisando archivo: " + archivoSeleccionado);

        System.out.print("¿Desea aprobar o rechazar el archivo? (aprobar/rechazar): ");
        String decision = scanner.nextLine();

        String nuevoEstado = "";
        if (decision.equalsIgnoreCase("aprobar")) {
            nuevoEstado = "Aprobado";
        } else if (decision.equalsIgnoreCase("rechazar")) {
            nuevoEstado = "Rechazado";
        } else {
            System.out.println("Decisión inválida.");
        }

        actualizarEstadoCSV(archivoSeleccionado, nuevoEstado);
        scanner.close();
    }
    //Método para actualizar el estado del csv para indicar si fue aprobado o denegado
    private void actualizarEstadoCSV(String nombreArchivo, String nuevoEstado) {
        String carpetaCSV = "archivos_csv";
        File archivoCSV = new File(carpetaCSV, "Apuntes.csv");
        List<String> lineasActualizadas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(nombreArchivo)) {
                    datos[4] = nuevoEstado;
                    linea = String.join(",", datos);
                }
                lineasActualizadas.add(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCSV))) {
            for (String linea : lineasActualizadas) {
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("Estado actualizado a: " + nuevoEstado);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }

}
    