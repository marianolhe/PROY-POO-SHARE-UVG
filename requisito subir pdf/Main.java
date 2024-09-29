import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pedir la ruta del archivo PDF 
        System.out.print("Ingrese la ruta completa del archivo PDF que desea subir: ");
        String rutaArchivo = scanner.nextLine();

        // Carpeta de destino donde se guardarán los archivos 
        // (aun se debe estandarizar para que al copiar el proyecto en local en otra computadora se pueda guardar correctamente)
        String carpetaDestino = "C:\\Users\\bianc\\OneDrive\\Documentos\\GitHub\\PROY-POO-SHARE-UVG\\requisito subir pdf";

        // Crear una instancia de FileUploader
        subirPDF uploader = new subirPDF(carpetaDestino);

        // Llamar al método para subir y guardar el archivo en la carpeta
        uploader.subirArchivo(rutaArchivo);
        
        scanner.close();
    }
}