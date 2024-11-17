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

        System.out.print("Para subir el archivo al curso correspondiente, necesitamos algunos datos ^o^ ");
        // Solicitar el código del curso
        System.out.print("\nCódigo del curso (ej. CC2019): ");
        String codigoCurso = scanner.nextLine();

        // Solicitar el año del curso
        System.out.print("Año del curso  (ej. 3): ");
        String anio = scanner.nextLine();

        // Llamar al método para subir el archivo
        subirArchivo(rutaArchivo, codigoCurso, correoUsuario, anio);
    }

    public void subirArchivo(String rutaArchivo, String codigoCurso, String correoUsuario, String anio) {
        File archivo = new File(rutaArchivo); // Crear un objeto File a partir de la ruta proporcionada
    
        if (archivo.exists() && archivo.isFile() && rutaArchivo.endsWith(".pdf")) {
            try {
                // Obtener la carrera el estudiante a partir del correo con el que se inicio sesion
                String carreraAbreviada = obtenerCarreraDesdeCSV(correoUsuario);
    
                if (carreraAbreviada == null) {
                    System.out.println("No se pudo encontrar la carrera asociada a este usuario (._.).");
                    return;
                }
    
                // Obtener el carné del estudiante a partir del correo con el que se inicio sesion
                String carne = obtenerCarneDesdeCorreo(correoUsuario);
                if (carne == null) {
                    System.out.println("No se pudo obtener el carné del usuario (._.).");
                    return;
                }

                // Concatenar carrera abreviada, año y código del curso para encontrar la carpeta donde se cuardara el apunte
                String nombreCarpeta = carreraAbreviada + "-" + anio + "-" + codigoCurso;
                Path rutaDestino = Paths.get(carpetaBase, nombreCarpeta);
    
                // Verificar si la carpeta existe
                if (Files.exists(rutaDestino) && Files.isDirectory(rutaDestino)) {
                    // Crear un nuevo nombre de archivo con el carné
                    String nuevoNombreArchivo = archivo.getName().replace(".pdf", "_" + carne + ".pdf");
                    Path archivoDestino = rutaDestino.resolve(nuevoNombreArchivo); // Usar el nuevo nombre

                    // Verificar si el archivo de destino ya existe
                    if (Files.exists(archivoDestino)) {
                        throw new IOException("El archivo " + nuevoNombreArchivo + " ya existe en la carpeta de destino.");
                    }

                    // Guardar el PDF en la carpeta de destino
                    Files.copy(archivo.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);

                    // Obtener los últimos 6 dígitos del nombre de la carpeta
                    String[] partesNombreCarpeta = nombreCarpeta.split("-");
                    String codigoBuscado = partesNombreCarpeta[partesNombreCarpeta.length - 1];

                    // Leer el archivo Codigo Cursos.csv y buscar el código
                    String mensajeExito = buscarCodigo(codigoBuscado);

                    if (mensajeExito != null) {
                        System.out.println("¡Archivo subido correctamente al curso " + mensajeExito + "!");
                    } else {
                        System.out.println("No se encontró un curso correspondiente al código ingresado.");
                    }

                    // Guardar los datos en el archivo CSV
                    guardarDatosCSV(nuevoNombreArchivo, rutaArchivo, codigoCurso, carreraAbreviada, "No revisado");
                } else {
                    System.out.println("No existe el curso con el código ingresado.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Asegúrate de que el archivo exista y sea un archivo PDF (._.).");
        }
    }
    
    private String obtenerCarneDesdeCorreo(String correo) {
        // Obtener las primeras tres letras y extraer el carné
        String carne = null;
        String[] partes = correo.split("@");
        
        if (partes.length > 0) {
            // Asegurarse de que el carné tenga al menos 5 caracteres después de las primeras tres letras
            String carneDesdeCorreo = partes[0].substring(3); // Obtener los caracteres después de las primeras tres letras
            if (carneDesdeCorreo.length() >= 5) {
                carne = carneDesdeCorreo; // Guardar el carné
            }
        }
        return carne; // Retornar el carné o null si no se pudo obtener
    }

    private String buscarCodigo(String codigoBuscado) {
        String rutaCSV = "archivos_csv/Codigo Cursos.csv"; // Ruta al archivo CSV
        String resultado = null;
    
        try (BufferedReader br = new BufferedReader(new FileReader(rutaCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(","); // Suponiendo que el CSV está separado por comas
                if (datos.length >= 3) {
                    String codigo = datos[1].trim(); // Segunda columna
                    if (codigo.equals(codigoBuscado)) {
                        resultado = datos[2].trim(); // Tercera columna
                        break; // Salir del bucle si se encuentra el código
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    
        return resultado; // Devuelve el resultado o null si no se encontró
    }

    
    // Método para obtener la carrera desde el CSV de usuarios
    private String obtenerCarreraDesdeCSV(String correoUsuario) {
        File archivoCSV = new File(CARPETA_ARCHIVOS_CSV, ARCHIVO_PERFILES);

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[2].equals(correoUsuario)) { 
                    return datos[4]; 
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
            JOptionPane.showMessageDialog(null, "Error al listar archivos: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "No se encontró la carpeta: " + rutaCarpeta.toString());
    }

    return archivos;
}

// Método para revisar archivos
public void revisarArchivo(String carreraAbreviada, int anio, String codigoCurso) {
    List<String> archivos = listarArchivos(carreraAbreviada, anio, codigoCurso);

    if (archivos.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hay archivos para revisar.");
        return; // Salir del método si no hay archivos
    }

    String archivoSeleccionado = (String) JOptionPane.showInputDialog(null,
        "Seleccione el archivo que desea revisar", "Revisar archivo",
        JOptionPane.PLAIN_MESSAGE, null, archivos.toArray(), archivos.get(0));

    if (archivoSeleccionado == null) {
        return; // Salir del método si no se selecciona un archivo
    }

    String decision = JOptionPane.showInputDialog(null, "¿Desea aprobar o rechazar el archivo? (aprobar/rechazar):");
    String nuevoEstado = "";

    if (decision.equalsIgnoreCase("aprobar")) {
        nuevoEstado = "Aprobado";
    } else if (decision.equalsIgnoreCase("rechazar")) {
        nuevoEstado = "Rechazado";
    } else {
        JOptionPane.showMessageDialog(null, "Decisión inválida.");
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
        JOptionPane.showMessageDialog(null, "Error al leer el archivo CSV: " + e.getMessage());
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCSV))) {
        for (String linea : lineasActualizadas) {
            writer.write(linea);
            writer.newLine();
        }
        JOptionPane.showMessageDialog(null, "Estado actualizado a: " + nuevoEstado);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al escribir en el archivo CSV: " + e.getMessage());
    }
}

// Método para descargar archivo
public void descargarArchivo(String correoUsuario) {
    int anio = 0;
    boolean anioValido = false;

    // Solicitar el año
    while (!anioValido) {
        String anioInput = JOptionPane.showInputDialog(null, "Ingrese el año al que pertenece el curso: ");
        try {
            anio = Integer.parseInt(anioInput);
            anioValido = true; // El año es válido
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ERROR: Ingrese un año válido.");
        }
    }

    // Solicitar el código del curso
    String codigoCurso = JOptionPane.showInputDialog(null, "Ingrese el código del curso: ");
    String carreraAbreviada = obtenerCarreraDesdeCSV(correoUsuario);

    if (carreraAbreviada == null) {
        JOptionPane.showMessageDialog(null, "No se pudo encontrar la carrera asociada a este usuario.");
        return;
    }

    String nombreCarpeta = carreraAbreviada + "-" + anio + "-" + codigoCurso;
    Path rutaCarpeta = Paths.get(carpetaBase, nombreCarpeta);

    if (Files.exists(rutaCarpeta) && Files.isDirectory(rutaCarpeta)) {
        List<String> archivos = listarArchivos(carreraAbreviada, anio, codigoCurso);

        if (archivos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay archivos aprobados para descargar en este curso.");
            return;
        }

        String archivoSeleccionado = (String) JOptionPane.showInputDialog(null, 
            "Seleccione el archivo que desea descargar", "Descargar archivo",
            JOptionPane.PLAIN_MESSAGE, null, archivos.toArray(), archivos.get(0));

        if (archivoSeleccionado == null) {
            return; // Salir del método si no se selecciona un archivo
        }

        Path archivoRuta = rutaCarpeta.resolve(archivoSeleccionado);
        Path carpetaDescargas = Paths.get(System.getProperty("user.home"), "Downloads");
        Path destino = carpetaDescargas.resolve(archivoSeleccionado);

        try {
            Files.copy(archivoRuta, destino, StandardCopyOption.REPLACE_EXISTING);
            Files.setLastModifiedTime(destino, FileTime.fromMillis(System.currentTimeMillis()));
            JOptionPane.showMessageDialog(null, "¡Apunte descargado correctamente!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al descargar el archivo: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "No se encontró la carpeta: " + rutaCarpeta.toString());
    }
}
}
