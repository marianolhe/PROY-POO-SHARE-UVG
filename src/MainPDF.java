import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class MainPDF {
    public static void main(String[] args) {
        // Carpeta base donde se guardarán los archivos PDF (dentro del repositorio)
        String carpetaBase = "APUNTES";  // Ruta relativa dentro del repositorio

        // Crear una instancia de GestionPDF
        GestionPDF gestionPDF = new GestionPDF(carpetaBase);

        // Crear un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        // Usar JFileChooser para seleccionar el archivo PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

        int userSelection = fileChooser.showOpenDialog(null);
        File archivoSeleccionado = null;

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            System.out.println("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath());
        } else {
            System.out.println("No se seleccionó ningún archivo.");
            scanner.close();
            return;  // Termina el programa si no se selecciona un archivo
        }

        // Obtener la ruta del archivo seleccionado
        String rutaArchivo = archivoSeleccionado.getAbsolutePath();

        // Pedir el código del curso
        System.out.print("Ingrese el código del curso (por ejemplo, CC2005): ");
        String codigoCurso = scanner.nextLine();

        // Pedir la carrera abreviada
        System.out.print("Ingrese la carrera abreviada (por ejemplo, ICC): ");
        String carreraAbreviada = scanner.nextLine();

        // Llamar al método para subir el archivo y crear la estructura de carpetas
        gestionPDF.subirArchivo(rutaArchivo, codigoCurso, carreraAbreviada);

        // Cerrar el Scanner
        scanner.close();
    }
}

