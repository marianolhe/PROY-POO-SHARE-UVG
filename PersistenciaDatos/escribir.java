import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class escribir {

    // Método genérico para escribir cualquier lista de objetos que tengan un método toCSV() en un archivo CSV
    public static <T> void escribirCSV(String archivo, List<T> lista, CSVFormatter<T> formatter) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            for (T item : lista) {
                writer.write(formatter.toCSV(item)); // Utiliza el formateador para convertir el objeto a CSV
                writer.newLine(); // Añadir nueva línea después de cada objeto
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo CSV: " + e.getMessage());
        }
    }

    // Interface para convertir objetos a formato CSV
    public interface CSVFormatter<T> {
        String toCSV(T obj);
    }
}

