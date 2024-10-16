import java.io.*;
import java.nio.file.*;

// Esta clase tiene su propio Main y se ejecuta fuera del progrma solo para crear automaticamente las carpetas donde se guardaran lso apuntes. 
public class CrearCarpetas {

    private static final String PREFIJO_CARPETA = "ICCTI-"; // Prefijo predeterminado para todas las carpetas
    private static final String CARPETA_APUNTES = "APUNTES"; // Carpeta base donde se crearán las carpetas
    private static final String ARCHIVO_CSV = "Datos CSV/Codigo Cursos.csv"; // Ruta del archivo CSV

    public static void main(String[] args) {
        // Leer el archivo CSV y crear las carpetas
        leerYCrearCarpetas();
    }

    // Método que lee el archivo CSV y crea las carpetas
    public static void leerYCrearCarpetas() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_CSV))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                // Saltar la primera línea 
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                // Dividir la línea en columnas 
                String[] columnas = linea.split(",");

                if (columnas.length >= 2) {
                    String anio = columnas[0].trim(); // Primera columna es el año
                    String codigoCurso = columnas[1].trim(); // Segunda columna es el código del curso

                    // Crear el nombre de la carpeta siguiendo el formato: ICCTI-<año>-<código del curso>
                    String nombreCarpeta = PREFIJO_CARPETA + anio + "-" + codigoCurso;

                    // Definir la ruta de la carpeta dentro de la carpeta "APUNTES"
                    Path rutaCarpeta = Paths.get(CARPETA_APUNTES, nombreCarpeta);

                    // Verificar si la carpeta ya existe
                    if (Files.exists(rutaCarpeta)) {
                        System.out.println("La carpeta '" + nombreCarpeta + "' ya existe. No se creará de nuevo.");
                    } else {
                        // Crear la carpeta
                        Files.createDirectories(rutaCarpeta);
                        System.out.println("Carpeta creada: " + rutaCarpeta.toString());
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV o crear las carpetas: " + e.getMessage());
        }
    }
}
