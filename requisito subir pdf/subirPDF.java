import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class subirPDF {

    // Carpeta predeterminada donde se guardarán los archivos
    private String carpetaDestino;

    public subirPDF(String carpetaDestino) {
        this.carpetaDestino = carpetaDestino;
    }

    // Método para subir y guardar el archivo a la carpeta predestinada
    public void subirArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);

        // Verificar que el archivo exista y sea un archivo PDF
        if (archivo.exists() && archivo.isFile() && rutaArchivo.endsWith(".pdf")) {
            try {
                // Definir la carpeta de destino para el PDF
                Path rutaDestino = Paths.get(carpetaDestino, archivo.getName());
                
                // Guardar el PDF en la carpeta de destino
                Files.copy(archivo.toPath(), rutaDestino);

                System.out.println("Archivo subido correctamente a la carpeta :D");
            } catch (IOException e) {
                System.out.println("Error al copiar el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("ERROR: asegurate que el archivo exista y sea un archivo PDF.");
        }
    }
}

