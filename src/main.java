import java.io.*;
import java.util.Scanner;

public class Main {
    // Define el archivo CSV donde se guardarán los usuarios
    private static final String CSV_FILE = "src/usuarios.csv"; // Asegúrate de que este archivo exista en la ruta correcta
    private static final String[] CARRERAS = {"ICCTI"}; 

    public static void main (String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n+ =============================================== +");
            System.out.println("                      MENÚ                       ");
            System.out.println("+ =============================================== +");
            System.out.printf("| %-5s | %-40s |\n", "1", "Registrarse");
            System.out.printf("| %-5s | %-40s |\n", "2", "Iniciar sesión");
            System.out.printf("| %-5s | %-40s |\n", "3", "Salir");
            System.out.println("+ =============================================== +");

            int opcion = entrada.nextInt();
            entrada.nextLine(); 

            if (opcion == 1) {
                registrarUsuario(entrada);
            } else if (opcion == 2) {
                iniciarSesion(entrada);
            } else if (opcion == 3) {
                continuar = false;  
                System.out.println("Saliendo del sistema...");
            } else {
                System.out.println("Opción no válida.");
            }
        }
        entrada.close();
    }

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
        scanner.nextLine(); // Limpiar el buffer

        if (carreraIndex < 0 || carreraIndex >= CARRERAS.length) {
            System.out.println("Opción inválida.");
            return;
        }
        String carrera = CARRERAS[carreraIndex];

        // Selección de rol
        System.out.println("Seleccione el rol:");
        System.out.println("1. Usuario");
        System.out.println("2. Revisor");
        int rolSeleccionado = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        PersonaPlantilla persona; // Declaración
        if (rolSeleccionado == 1) {
            persona = new Usuario(nombre, apellido, correo, contrasena, carrera);
        } else if (rolSeleccionado == 2) {
            persona = new Revisor(nombre, apellido, correo, contrasena, carrera);
        } else {
            System.out.println("Rol inválido.");
            return; // Asegúrate de salir aquí si el rol no es válido
        }

        guardarUsuario(persona); // Método para guardar el usuario
        System.out.println("Registro exitoso.");
    }

    private static void iniciarSesion(Scanner scanner) throws IOException {
        System.out.println("Ingrese su correo:");
        String correo = scanner.nextLine();

        System.out.println("Ingrese su contraseña:");
        String contrasena = scanner.nextLine();

        PersonaPlantilla persona = buscarUsuario(correo, contrasena);

        if (persona != null) {
            System.out.println("Inicio de sesión exitoso.");
            GestionLogin.mostrarMenuPorRol(persona); // Maneja el rol del usuario
        } else {
            System.out.println("Correo o contraseña incorrectos.");
        }
    }

    private static void guardarUsuario(PersonaPlantilla persona) throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
            writer.write(persona.getNombre() + "," + persona.getApellido() + "," +
                         persona.getCorreo() + "," + persona.getContrasena() + "," +
                         persona.getCarrera() + "," + persona.getRol() + "\n");
        }
    }

    private static PersonaPlantilla buscarUsuario(String correo, String contrasena) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[2].equals(correo) && datos[3].equals(contrasena)) {
                    String nombre = datos[0];
                    String apellido = datos[1];
                    String carrera = datos[4];
                    String rol = datos[5];

                    // Verificación del rol y creación de la instancia adecuada
                    if (rol.equals("Usuario")) {
                        return new Usuario(nombre, apellido, correo, contrasena, carrera);
                    } else if (rol.equals("Revisor")) {
                        return new Revisor(nombre, apellido, correo, contrasena, carrera);
                    } else if (rol.equals("Administrador")) {
                        return new Administrador(nombre, apellido, correo, contrasena, carrera);
                    }
                }
            }
        }
        return null; // Si no se encuentra el usuario
    }
}
