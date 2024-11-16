import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class MainPDF {
    public static void main(String[] args) {
        // Carpeta base donde se guardarán los archivos PDF 
        String carpetaBase = "APUNTES";  // Ruta relativa dentro del repositorio

        // Crear una instancia de GestionPDF
        GestionPDF gestionPDF = new GestionPDF(carpetaBase);

        // Crear un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        // Usar JFileChooser para seleccionar el archivo PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Prueba el archivo PDF");
        
        // Filtrar solo archivos PDF
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));
        
        int userSelection = fileChooser.showOpenDialog(null);
        String rutaArchivo = "";

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            rutaArchivo = fileToOpen.getAbsolutePath();
            System.out.println("Archivo seleccionado: " + rutaArchivo);
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }

        // Pedir la carrera abreviada
        System.out.print("Ingrese el año: ");
        int anio = scanner.nextInt();

        // Pedir el código del curso
        System.out.print("Ingrese el código del curso (por ejemplo, CC2005): ");
        String codigoCurso = scanner.nextLine();

        // Pedir la carrera abreviada
        System.out.print("Ingrese la carrera abreviada (por ejemplo, ICC): ");
        String carreraAbreviada = scanner.nextLine();

        // Llamar al método para subir el archivo y crear la estructura de carpetas
        gestionPDF.subirArchivo(rutaArchivo, codigoCurso, carreraAbreviada, carreraAbreviada);

        // Llamar al método para revisar archivo
        gestionPDF.revisarArchivo(carreraAbreviada, anio, codigoCurso);
        scanner.close();

    }
}
