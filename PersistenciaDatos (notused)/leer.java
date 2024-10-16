package PersistenciaDatos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class leer {

    // Método genérico para leer un archivo CSV y devolver una lista de objetos
    public static <T> List<T> leerCSV(String archivo, CSVParser<T> parser) {
        List<T> lista = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                T obj = parser.fromCSV(linea); // Utiliza el parser para convertir la línea a un objeto
                if (obj != null) {
                    lista.add(obj); // Añadir a la lista si no es null
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return lista;
    }

    // Interfaz para convertir líneas CSV a objetos
    public interface CSVParser<T> {
        T fromCSV(String linea);
    }
}
