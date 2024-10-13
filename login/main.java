import java.io.*;
import java.util.Scanner;

public class Main {
    private static final String[] CARRERAS = {"Computación"};
    private static final String CSV_FILE = "usuarios.csv";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        int opcion = scanner.nextInt();
        scanner.nextLine();  // Limpiar el buffer

        if (opcion == 1) {
            registrarUsuario(scanner);
        } else if (opcion == 2) {
            iniciarSesion(scanner);
        }
    }

    // Método para registrar un nuevo usuario
    private static void registrarUsuario(Scanner scanner) throws IOException {
        System.out.println("Ingrese su nombre:");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese su apellido:");
        String apellido = scanner.nextLine();

        System.out.println("Ingrese su correo:");
        String correo = scanner.nextLine();

        System.out.println("Ingrese su contraseña:");
        String contrasena = scanner.nextLine();

        System.out.println("Elija su carrera:");
        for (int i = 0; i < CARRERAS.length; i++) {
            System.out.println((i + 1) + ". " + CARRERAS[i]);
        }
        int carreraIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // Limpiar el buffer

        if (carreraIndex < 0 || carreraIndex >= CARRERAS.length) {
            System.out.println("Opción inválida.");
            return;
        }
        String carrera = CARRERAS[carreraIndex];

        // Guardar los datos en el CSV
        guardarUsuario(new Usuario(nombre, apellido, correo, contrasena, carrera));
        System.out.println("Registro exitoso.");
    }

    // Método para iniciar sesión
    private static void iniciarSesion(Scanner scanner) throws IOException {
        System.out.println("Ingrese su correo:");
        String correo = scanner.nextLine();

        System.out.println("Ingrese su contraseña:");
        String contrasena = scanner.nextLine();

        PersonaPlantilla usuario = buscarUsuario(correo, contrasena);

        if (usuario != null) {
            String tipoMenu = "";

            if (usuario instanceof Administrador) {
                tipoMenu = ((Administrador) usuario).getTipoMenu();
            } else if (usuario instanceof Revisor) {
                tipoMenu = ((Revisor) usuario).getTipoMenu();
            } else if (usuario instanceof Usuario) {
                tipoMenu = ((Usuario) usuario).getTipoMenu();
            }

            System.out.println("Bienvenido al menú de " + tipoMenu);
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    // Método para guardar el usuario en un archivo CSV
    private static void guardarUsuario(PersonaPlantilla persona) throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
            writer.write(persona.toCSV() + "\n");
        }
    }

    // Método para buscar el usuario en el archivo CSV
    private static PersonaPlantilla buscarUsuario(String correo, String contrasena) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[2].equals(correo) && datos[3].equals(contrasena)) {
                    if (correo.contains("admin")) {
                        return new Administrador(datos[0], datos[1], datos[2], datos[3], datos[4]);
                    } else if (correo.contains("revisor")) {
                        return new Revisor(datos[0], datos[1], datos[2], datos[3], datos[4]);
                    } else {
                        return new Usuario(datos[0], datos[1], datos[2], datos[3], datos[4]);
                    }
                }
            }
        }
        return null;
    }
}
