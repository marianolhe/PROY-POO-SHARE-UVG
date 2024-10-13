import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class subriPDF {
    // Carpeta donde se guardaran los archivos
    private String carpetaDestinada;

    public void subirPDF(String carpetaDestinada) {
        this.carpetaDestinada = carpetaDestinada;
    }

    // Método para subir y guardar archivo PDF en carpeta predestinada
    public  void subirArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);

        // Verificar que el archivo exista y sea un archivo PDF
        if (archivo.exists() && archivo.isFile() && rutaArchivo.endsWith(".pdf")) {
            try {
                // Definir la carpeta de destino
                Path  rutaDestino = Paths.get(carpetaDestinada + File.separator + archivo.getName());

                // Guardar el archivo en la carpeta de destino
                Files.copy(archivo.toPath(), rutaDestino);

                System.out.println("Archivo subido y copiado a la carpeta: " + carpetaDestinada);
            } catch (IOException e) {
                System.out.println("Error al subir el archivo: " + e.getMessage());
            }
        } else {
            System.out.println("El archivo no existe o no es un archivo PDF.");

      
        }
        }
        }
    }
}