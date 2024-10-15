import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class main {

    private static final String CSV_FILE = "usuarios.csv";
    private static final String[] CARRERAS = {"Computación"};

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        boolean continuar = true;
        while (continuar) {
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine(); 

            if (opcion == 1) {
                registrarUsuario(scanner);
            } else if (opcion == 2) {
                iniciarSesion(scanner);
            } else if (opcion == 3) {
                continuar = false;  
                System.out.println("Saliendo del sistema...");
            } else {
                System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

   // private static void mostrar

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

    private static void manejarRol(PersonaPlantilla persona) { 
        switch (persona.getRol()) {
            case "Usuario":
                ((Usuario) persona).subirDocumento(); // Simulación de subir un documento
                break;
            case "Revisor":
                ((Revisor) persona).aprobarDocumento(); // Simulación de aprobar un documento
                break;
            case "Administrador":
                ((Administrador) persona).gestionarBecas(); // Simulación de gestión de becas
                break;
            default: 
                System.out.println("Rol no válido.");
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
}
