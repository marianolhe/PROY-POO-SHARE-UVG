import java.util.Scanner;

public class MainPDF {
    public static void main(String[] args) {
        // Carpeta base donde se guardarán los archivos PDF organizados
        String carpetaBase = "C:\\Users\\bianc\\OneDrive\\Documentos\\GitHub\\PROY-POO-SHARE-UVG\\APUNTES";

        // Crear una instancia de GestionPDF
        GestionPDF gestionPDF = new GestionPDF(carpetaBase);

        // Crear un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        // Pedir al usuario los datos del archivo a subir
        System.out.print("Ingrese la ruta completa del archivo PDF que desea subir: ");
        String rutaArchivo = scanner.nextLine();

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

