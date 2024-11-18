import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GestionPDF {
    private String carpetaBase;
    private static final String ARCHIVO_PERFILES = "perfiles.csv"; // Nombre del archivo CSV de usuarios
    private static final String CARPETA_ARCHIVOS_CSV = "archivos_csv"; // Carpeta donde se guardará el CSV de apuntes
    private static final String ARCHIVO_APUNTES = "Apuntes.csv"; // Nombre del archivo CSV de apuntes

    public GestionPDF(String carpetaBase) {
        this.carpetaBase = Paths.get("APUNTES").toString();
    }

    public void seleccionarYSubirArchivo(String correoUsuario) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Apunte");
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        fileChooser.setCurrentDirectory(new File(desktopPath));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

        int resultado = fileChooser.showOpenDialog(null);
        if (resultado != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún archivo.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rutaArchivo = fileChooser.getSelectedFile().getAbsolutePath();
        String codigoCurso = JOptionPane.showInputDialog("Ingrese el código del curso (ej. CC2019):");
        if (codigoCurso == null || codigoCurso.isBlank()) return;

        String anio = JOptionPane.showInputDialog("Ingrese el año del curso (ej. 3):");
        if (anio == null || anio.isBlank()) return;

        subirArchivo(rutaArchivo, codigoCurso, correoUsuario, anio);
    }

    public void subirArchivo(String rutaArchivo, String codigoCurso, String correoUsuario, String anio) {
        File archivo = new File(rutaArchivo);
        if (archivo.exists() && archivo.isFile() && rutaArchivo.endsWith(".pdf")) {
            try {
                String carreraAbreviada = obtenerCarreraDesdeCSV(correoUsuario);
                if (carreraAbreviada == null) {
                    JOptionPane.showMessageDialog(null, "No se encontró la carrera asociada a este usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String carne = obtenerCarneDesdeCorreo(correoUsuario);
                if (carne == null) {
                    JOptionPane.showMessageDialog(null, "No se pudo obtener el carné del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String nombreCarpeta = carreraAbreviada + "-" + anio + "-" + codigoCurso;
                Path rutaDestino = Paths.get(carpetaBase, nombreCarpeta);

                if (Files.exists(rutaDestino) && Files.isDirectory(rutaDestino)) {
                    String nuevoNombreArchivo = archivo.getName().replace(".pdf", "_" + carne + ".pdf");
                    Path archivoDestino = rutaDestino.resolve(nuevoNombreArchivo);

                    if (Files.exists(archivoDestino)) {
                        throw new IOException("El archivo " + nuevoNombreArchivo + " ya existe en la carpeta de destino.");
                    }

                    Files.copy(archivo.toPath(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);
                    String codigoBuscado = nombreCarpeta.split("-")[2];
                    String mensajeExito = buscarCodigo(codigoBuscado);

                    if (mensajeExito != null) {
                        JOptionPane.showMessageDialog(null, "¡Archivo subido correctamente al curso " + mensajeExito + "!");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró un curso correspondiente al código ingresado.");
                    }

                    guardarDatosCSV(nuevoNombreArchivo, rutaArchivo, codigoCurso, carreraAbreviada, "No revisado");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe el curso con el código ingresado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Asegúrate de que el archivo sea un archivo PDF válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerCarneDesdeCorreo(String correo) {
        String carne = null;
        String[] partes = correo.split("@");
        if (partes.length > 0) {
            String carneDesdeCorreo = partes[0].substring(3);
            if (carneDesdeCorreo.length() >= 5) {
                carne = carneDesdeCorreo;
            }
        }
        return carne;
    }

    private String buscarCodigo(String codigoBuscado) {
        String rutaCSV = Paths.get("archivos_csv", "Codigo Cursos.csv").toString(); 
        String resultado = null;
    
        File archivoCSV = new File(rutaCSV);
        if (!archivoCSV.exists()) {
            System.out.println("Archivo no encontrado en: " + archivoCSV.getAbsolutePath());
            return null;
        }
    
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 3 && datos[1].trim().equals(codigoBuscado)) {
                    resultado = datos[2].trim();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    
        return resultado;
    }
    

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
            JOptionPane.showMessageDialog(null, "Error al leer el archivo CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void guardarDatosCSV(String nombreArchivo, String rutaArchivo, String codigoCurso, String carreraAbreviada, String estado) {
        Path rutaCSV = Paths.get(CARPETA_ARCHIVOS_CSV);
        try {
            Files.createDirectories(rutaCSV);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al crear la carpeta CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        File archivoCSV = new File(CARPETA_ARCHIVOS_CSV, ARCHIVO_APUNTES);
        try (FileWriter writer = new FileWriter(archivoCSV, true)) {
            if (!archivoCSV.exists() || archivoCSV.length() == 0) {
                writer.write("Nombre del archivo,Ruta del archivo,Código del curso,Carrera abreviada,Estado\n");
            }
            writer.write(nombreArchivo + "," + rutaArchivo + "," + codigoCurso + "," + carreraAbreviada + "," + estado + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al escribir en el archivo CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    // Método para descargar archivo con JList para seleccionar archivo
    public void descargarArchivo(String correoUsuario) {
        int anio = 0;
        boolean anioValido = false;

        // Solicitar el año
        while (!anioValido) {
            String anioInput = JOptionPane.showInputDialog(null, "Ingrese el año al que pertenece el curso (ej. 1):");
            if (anioInput == null) return; // Si el usuario cancela

            try {
                anio = Integer.parseInt(anioInput);
                anioValido = true; // El año es válido
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ERROR: Ingrese un año válido.");
            }
        }

        // Solicitar el código del curso
        String codigoCurso = JOptionPane.showInputDialog(null, "Ingrese el código del curso (ej. CC2005):");
        if (codigoCurso == null) return; // Si el usuario cancela

        // Obtener la carrera desde el CSV de usuarios
        String carreraAbreviada = obtenerCarreraDesdeCSV(correoUsuario);
        if (carreraAbreviada == null) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar la carrera asociada a este usuario (._.)");
            return;
        }

        // Crear el nombre de la carpeta concatenando
        String nombreCarpeta = carreraAbreviada + "-" + anio + "-" + codigoCurso;
        Path rutaCarpeta = Paths.get(carpetaBase, nombreCarpeta);

        // Verificar si la carpeta existe
        if (Files.exists(rutaCarpeta) && Files.isDirectory(rutaCarpeta)) {

            // Listar archivos aprobados
            List<String> archivos = listarArchivos(carreraAbreviada, anio, codigoCurso);

            if (archivos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay archivos aprobados para descargar en este curso (._.).");
                return;
            }

            // Crear el JList con los archivos
            JList<String> archivoList = new JList<>(archivos.toArray(new String[0]));
            archivoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            archivoList.setVisibleRowCount(5); // Mostrar hasta 5 archivos a la vez
            JScrollPane scrollPane = new JScrollPane(archivoList);

            // Crear un cuadro de diálogo con el JList
            int option = JOptionPane.showConfirmDialog(null, scrollPane, "Seleccione el archivo a descargar",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.CANCEL_OPTION) {
                return; // Si el usuario cancela, no hace nada
            }

            String archivoSeleccionado = archivoList.getSelectedValue();

            if (archivoSeleccionado == null) {
                JOptionPane.showMessageDialog(null, "No se ha seleccionado un archivo.");
                return;
            }

            // Ruta del archivo seleccionado
            Path archivoRuta = rutaCarpeta.resolve(archivoSeleccionado);

            // Copiar archivo a la carpeta de descargas del sistema
            Path carpetaDescargas = Paths.get(System.getProperty("user.home"), "Downloads");
            Path destino = carpetaDescargas.resolve(archivoSeleccionado);

            try {
                Files.copy(archivoRuta, destino, StandardCopyOption.REPLACE_EXISTING);
                // Cambiar la fecha de modificación al momento actual
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
