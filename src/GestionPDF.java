import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

public class GestionPDF {
    private String carpetaBase;
    private static final String ARCHIVO_PERFILES = "perfiles.csv"; // Nombre del archivo CSV de usuarios
    private static final String CARPETA_ARCHIVOS_CSV = "archivos_csv"; // Carpeta donde se guardará el CSV de apuntes
    private static final String ARCHIVO_APUNTES = "Apuntes.csv"; // Nombre del archivo CSV de apuntes
    private Scanner scanner; 

    public GestionPDF(String carpetaBase) {
        this.carpetaBase = Paths.get("APUNTES").toString();
        this.scanner = new Scanner(System.in); // Inicialización del Scanner
    }

    public void seleccionarYSubirArchivo(String correoUsuario) {
        // Crear un JFrame oculto
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true); // Asegura que el JFrame esté siempre al frente
        frame.setVisible(false); // Hacerlo invisible

        // Crear un JFileChooser para seleccionar el archivo PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Apunte");

        // Establecer el directorio inicial al escritorio
        String desktopPath = System.getProperty("user.home") + "/Desktop"; // Ruta al escritorio
        fileChooser.setCurrentDirectory(new File(desktopPath)); // Cambiar al escritorio

        // Filtrar solo archivos PDF
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

        // Abrir el diálogo y verificar si el usuario seleccionó un archivo
        int resultado = fileChooser.showOpenDialog(frame); // Usar el JFrame oculto como padre
        String rutaArchivo = "";
        if (resultado == JFileChooser.APPROVE_OPTION) {
            rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath(); // Obtener la ruta del archivo seleccionado
        } else {
            System.out.println("No se seleccionó ningún archivo (._.).");
            return; // Salir si no se seleccionó archivo
        }

        System.out.print("Para poder subir el archivo al curso correspondiente, necesitamos algunos datos ^o^ ...");
        // Solicitar el código del curso
        System.out.print("Código del curso (ej. CC2019): ");
        String codigoCurso = scanner.nextLine();

        // Solicitar el año
        System.out.print("Año del curso  (ej. 3): ");
        String anio = scanner.nextLine();

        // Llamar al método para subir el archivo
        subirArchivo(rutaArchivo, codigoCurso, correoUsuario, anio);
    }

    public void subirArchivo(String rutaArchivo, String codigoCurso, String correoUsuario, String anio) {
        File archivo = new File(rutaArchivo); // Crear un objeto File a partir de la ruta proporcionada
    
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
                    Files.copy(archivo.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);
    
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
        File archivoCSV = new File(CARPETA_ARCHIVOS_CSV, ARCHIVO_PERFILES);

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[2].equals(correoUsuario)) { 
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
        Path rutaCSV = Paths.get(CARPETA_ARCHIVOS_CSV);

        // Crear la carpeta "archivos_csv" si no existe
        try {
            Files.createDirectories(rutaCSV);
        } catch (IOException e) {
            System.out.println("Error al crear la carpeta de CSV: " + e.getMessage());
        }

        // Definir la ruta completa del archivo CSV
        File archivoCSV = new File(CARPETA_ARCHIVOS_CSV, ARCHIVO_APUNTES);

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

    // Método para listar los archivos en la carpeta de una carrera, año y curso específico
    public List<String> listarArchivos(String carreraAbreviada, int anio, String codigoCurso) {
        // Construir el nombre de la carpeta usando el formato: ICCTI-año-código
        String nombreCarpeta = carreraAbreviada + "-" + anio + "-" + codigoCurso;
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

    //  Método para revisar archivos
    public void revisarArchivo(String carreraAbreviada, int anio, String codigoCurso) {
        List<String> archivos = listarArchivos(carreraAbreviada, anio, codigoCurso);

        if (archivos.isEmpty()) {
            System.out.println("No hay archivos para revisar.");
            return; // Salir del método si no hay archivos
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
            return; // Salir del método si la selección es inválida
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
            return; // Salir del método si la decisión es inválida
        }

        actualizarEstadoCSV(archivoSeleccionado, nuevoEstado);
    } 

    // Método para actualizar el estado del csv para indicar si fue aprobado o denegado
    private void actualizarEstadoCSV(String nombreArchivo, String nuevoEstado) {
        String carpetaCSV = CARPETA_ARCHIVOS_CSV;
        File archivoCSV = new File(carpetaCSV, ARCHIVO_APUNTES);
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

    public void descargarArchivo(String correoUsuario) {
        int anio = 0;
        boolean anioValido = false;
    
        // Solicitar el año
        while (!anioValido) {
            System.out.print("Ingrese el año al que pertenece el curso: ");
            String anioInput = scanner.nextLine();
    
            try {
                anio = Integer.parseInt(anioInput);
                anioValido = true; // El año es válido
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Ingrese un año válido.");
            }
        }
    
        // Solicitar el código del curso
        System.out.print("Ingrese el código del curso: ");
        String codigoCurso = scanner.nextLine();
    
        // Obtener la carrera desde el CSV de usuarios
        String carreraAbreviada = obtenerCarreraDesdeCSV(correoUsuario);
    
        if (carreraAbreviada == null) {
            System.out.println("No se pudo encontrar la carrera asociada a este usuario.");
            return;
        }
    
        // Crear el nombre de la carpeta
        String nombreCarpeta = carreraAbreviada + "-" + anio + "-" + codigoCurso;
        Path rutaCarpeta = Paths.get(carpetaBase, nombreCarpeta);

    
        // Verificar si la carpeta existe
        if (Files.exists(rutaCarpeta) && Files.isDirectory(rutaCarpeta)) {
            System.out.println("Carpeta encontrada: " + rutaCarpeta.toString());
    
            // Listar archivos aprobados
            List<String> archivos = listarArchivos(correoUsuario, anio, codigoCurso); 
    
            if (archivos.isEmpty()) {
                System.out.println("No hay archivos aprobados para descargar.");
                return;
            }
    
            System.out.println("Archivos disponibles para descargar:");
            for (int i = 0; i < archivos.size(); i++) {
                System.out.println((i + 1) + ". " + archivos.get(i));
            }
    
            System.out.print("Seleccione el número del archivo que desea descargar: ");
            int seleccion = scanner.nextInt();
            scanner.nextLine(); 
    
            // Validar selección
            if (seleccion < 1 || seleccion > archivos.size()) {
                System.out.println("Selección inválida.");
                return;
            }
    
            String archivoSeleccionado = archivos.get(seleccion - 1);
            Path archivoRuta = rutaCarpeta.resolve(archivoSeleccionado);
    
            // Copiar archivo a la carpeta de descargas del sistema
            Path carpetaDescargas = Paths.get(System.getProperty("user.home"), "Downloads");
            Path destino = carpetaDescargas.resolve(archivoSeleccionado);
    
            try {
                Files.copy(archivoRuta, destino, StandardCopyOption.REPLACE_EXISTING);
                // Cambiar la fecha de modificación al momento actual
                Files.setLastModifiedTime(destino, FileTime.fromMillis(System.currentTimeMillis()));
        
                System.out.println("Archivo descargado correctamente y guardado en: " + destino.toString());
            } catch (IOException e) {
                System.out.println("Error al descargar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("No se encontró la carpeta: " + rutaCarpeta.toString());
        }
    }
    
    public void cerrarScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
