import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;


public class Main {
    // Define el archivo CSV donde se guardarán los usuarios
    private static final String CSV_FILE = "archivos_csv/usuarios.csv"; // Ruta a la carpeta 'archivos_csv'
    private static final String[] CARRERAS = {"Ingeniería en Ciencias de la Computación y Tecnologías de la Información"}; 

    public static void main(String[] args) {
        GestionLogin gestionLogin = new GestionLogin();
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
            System.out.print("Ingrese el N° de la opción a elegir ^o^ : ");

            int opcion = entrada.nextInt();
            entrada.nextLine();

            try {
                if (opcion == 1) {
                    gestionLogin.registrarUsuario(entrada);
                } else if (opcion == 2) {
                    gestionLogin.iniciarSesion(entrada);
                } else if (opcion == 3) {
                    continuar = false;
                    System.out.println("Saliendo del sistema (^-^)/ ...");
                } else {
                    System.out.println("ERROR: Opción no válida (._.) ");
                }
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }
        entrada.close();
    }

    private static void registrarUsuario(Scanner scanner) throws IOException {
        System.out.println("Para crear su perfil, necesitamos algunos datos ^_^");
        
        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();

        System.out.print ("Ingrese su apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Ingrese su correo electrónico institucional: ");
        String correo = scanner.nextLine();
 
        boolean contrasenaValida = false;
        String contrasena = "";

        while (!contrasenaValida) {
            System.out.print("Ingrese su contraseña (debe tener entre 4 y 8 dígitos): ");
            contrasena = scanner.nextLine();

            // Validar que la contraseña tenga entre 4 y 8 dígitos
            if (contrasena.length() >= 4 && contrasena.length() <= 8 && contrasena.matches("\\d+")) {
                contrasenaValida = true; // Contraseña válida
            } else {
                System.out.println("ERROR: La contraseña debe contener solo dígitos y tener entre 4 y 8 caracteres.");
            }
        }

        System.out.print("De las siguientes carreras: \n");
        for (int i = 0; i < CARRERAS.length; i++) {
            System.out.println((i + 1) + ". " + CARRERAS[i]);
        }

        System.out.print("Ingrese el número de la carrera a la que pertenece: ");
        int carreraIndex = scanner.nextInt() - 1;
        scanner.nextLine(); 

        if (carreraIndex < 0 || carreraIndex >= CARRERAS.length) {
            System.out.println("ERROR: Opción inválida (._.)");
            return;
        }
        String carrera = CARRERAS[carreraIndex];

        // Obtener las iniciales de la carrera
        String inicialesCarrera = obtenerIniciales(carrera);

        // Selección de rol
        System.out.println("De los siguientes roles:");
        System.out.println("1. Usuario");
        System.out.println("2. Revisor");
        System.out.print("Ingrese el número del rol a elegir: ");
        int rolSeleccionado = scanner.nextInt();
        scanner.nextLine(); 

        PersonaPlantilla persona; // Declaración
        if (rolSeleccionado == 1) {
            persona = new Usuario(nombre, apellido, correo, contrasena, inicialesCarrera);
        } else if (rolSeleccionado == 2) {
            persona = new Revisor(nombre, apellido, correo, contrasena, inicialesCarrera);
        } else {
            System.out.println("ERROR: Rol inválido (._.) ");
            return; 
        }
        guardarUsuario(persona); // Método para guardar el usuario
        System.out.println("¡Registro exitoso ^._.^!");
    }

    private static void iniciarSesion(Scanner scanner) throws IOException {
        System.out.print("Ingrese su correo electrónico institucional: ");
        String correo = scanner.nextLine();

        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        PersonaPlantilla persona = buscarUsuario(correo, contrasena);

        if (persona != null) {
            System.out.println("¡Inicio de sesión exitoso ^._.^!");
            GestionLogin.mostrarMenuPorRol(persona); // Maneja el rol del usuario
        } else {
            System.out.println("ERROR: Correo o contraseña incorrectos (._.) ");
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

    // Método para obtener las iniciales en mayúsculas de la carrera
    private static String obtenerIniciales(String carrera) {
        String[] palabras = carrera.split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (String palabra : palabras) {
            // Obtiene la primera letra de la palabra
            char primeraLetra = palabra.charAt(0);
            // Solo añadir la letra si es mayúscula
            if (Character.isUpperCase(primeraLetra)) {
                iniciales.append(primeraLetra); // Obtener la primera letra de cada palabra
            }
        }
        return iniciales.toString(); // Retorna solo las letras mayúsculas
    }
}