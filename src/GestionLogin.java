import java.io.*;
import java.util.Scanner;

public class GestionLogin {
    private static final String CSV_FILE = "archivos_csv/perfiles.csv";
    private static final String[] CARRERAS = {"Ingeniería en Ciencias de la Computación y Tecnologías de la Información"};

    private static Scanner scanner = new Scanner(System.in);

    public void registrarUsuario(Scanner scanner) throws IOException {
        System.out.println("Para crear su perfil, necesitamos algunos datos ^_^");

        System.out.print("Ingrese su nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese su apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Ingrese su correo electrónico institucional: ");
        String correo = scanner.nextLine();

        boolean contrasenaValida = false;
        String contrasena = "";

        while (!contrasenaValida) {
            System.out.print("Ingrese su contraseña (debe tener entre 4 y 8 dígitos): ");
            contrasena = scanner.nextLine();

            if (contrasena.length() >= 4 && contrasena.length() <= 8 && contrasena.matches("\\d+")) {
                contrasenaValida = true;
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

        String inicialesCarrera = obtenerIniciales(carrera);

        System.out.println("De los siguientes roles:");
        System.out.println("1. Usuario");
        System.out.println("2. Revisor");
        System.out.print("Ingrese el número del rol a elegir: ");
        int rolSeleccionado = scanner.nextInt();
        scanner.nextLine();

        PersonaPlantilla persona;
        if (rolSeleccionado == 1) {
            persona = new Usuario(nombre, apellido, correo, contrasena, inicialesCarrera);
        } else if (rolSeleccionado == 2) {
            persona = new Revisor(nombre, apellido, correo, contrasena, inicialesCarrera);
        } else {
            System.out.println("ERROR: Rol inválido (._.) ");
            return;
        }
        guardarUsuario(persona);
        System.out.println("¡Registro exitoso ^._.^!");
    }

    public void iniciarSesion(Scanner scanner) throws IOException {
        System.out.print("Ingrese su correo electrónico institucional: ");
        String correo = scanner.nextLine();

        System.out.print("Ingrese su contraseña: ");
        String contrasena = scanner.nextLine();

        PersonaPlantilla persona = buscarUsuario(correo, contrasena);

        if (persona != null) {
            System.out.println("¡Inicio de sesión exitoso ^._.^!");
            mostrarMenuPorRol(persona);

        } else {
            System.out.println("ERROR: Correo o contraseña incorrectos (._.) ");
        }
    }

    private static void guardarUsuario(PersonaPlantilla persona) {
        File archivo = new File(CSV_FILE);
        
        try {
            // Si el archivo no existe, crear la estructura de directorios y el archivo
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs(); // Crea la carpeta archivos_csv si no existe
                archivo.createNewFile();
                System.out.println("Archivo creado en: " + archivo.getAbsolutePath());
            }
            
            // Escribir los datos del usuario en el archivo
            try (FileWriter writer = new FileWriter(archivo, true)) {
                writer.write(persona.getNombre() + "," + persona.getApellido() + "," +
                                persona.getCorreo() + "," + persona.getContrasena() + "," +
                                persona.getCarrera() + "," + persona.getRol() + "\n");
                System.out.println("Usuario guardado en el archivo correctamente.");
            }
            
        } catch (IOException e) {
            System.out.println("Error al intentar guardar el usuario: " + e.getMessage());
        }
    }
    

    private PersonaPlantilla buscarUsuario(String correo, String contrasena) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[2].equals(correo) && datos[3].equals(contrasena)) {
                    String nombre = datos[0];
                    String apellido = datos[1];
                    String carrera = datos[4];
                    String rol = datos[5];

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
        return null;
    }

    private String obtenerIniciales(String carrera) {
        String[] palabras = carrera.split(" ");
        StringBuilder iniciales = new StringBuilder();
        for (String palabra : palabras) {
            char primeraLetra = palabra.charAt(0);
            if (Character.isUpperCase(primeraLetra)) {
                iniciales.append(primeraLetra);
            }
        }
        return iniciales.toString();
    }


    public static void mostrarMenuPorRol(PersonaPlantilla persona) {
        // Crear una instancia de GestionPDF con la ruta base de APUNTES
        GestionPDF gestionPDF = new GestionPDF("APUNTES");
        boolean continuar = true;

        while (continuar) {
            switch (persona.getRol()) {
                case "Usuario":
                    System.out.println("\n+ =============================================== +");
                    System.out.println("                      MENÚ ESTUDIANTE               ");
                    System.out.println("+ =============================================== +");
                    System.out.printf("| %-5s | %-40s |\n", "1", "Subir Apunte");
                    System.out.printf("| %-5s | %-40s |\n", "2", "Descargar Apunte");
                    System.out.printf("| %-5s | %-40s |\n", "3", "Cerrar Sesión");
                    System.out.println("+ =============================================== +");
                    System.out.print("Ingrese el N° de la opción a elegir ^o^: ");

                    int opcionUsuario = scanner.nextInt();
                    scanner.nextLine(); 

                    // Obtener el correo del usuario desde el objeto `persona` 
                    String correoUsuario = persona.getCorreo();

                    switch (opcionUsuario) {
                        case 1:
                            // Llamar al método para subir el archivo
                            gestionPDF.seleccionarYSubirArchivo(correoUsuario);
                            break;

                        case 2:
                            gestionPDF.descargarArchivo(correoUsuario);
                            break;
                        case 3:
                            continuar = false;
                            System.out.println("Cerrando sesión (^-^)/...");
                            break;
                        default:
                            System.out.println("Opción no válida (._.).");
                            break;
                    }
                    break;

                case "Revisor":
                    System.out.println("\n+ =============================================== +");
                    System.out.println("                      MENÚ REVISOR               ");
                    System.out.println("+ =============================================== +");
                    System.out.printf("| %-5s | %-40s |\n", "1", "Revisar apuntes");
                    System.out.printf("| %-5s | %-40s |\n", "2", "Cerrar Sesión");
                    System.out.println("+ =============================================== +");
                    System.out.print("Ingrese el N° de la opción a elegir ^o^: ");

                    int opcionRevisor = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    switch (opcionRevisor) {
                        case 1:
                            System.out.println("Marcando documento como revisado");
                            break;
                        case 2:
                            continuar = false;
                            System.out.println("Cerrando sesión (^-^)/ ...");
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            break;
                    }
                    break;

                case "Administrador":
                    System.out.println("\n+ =============================================== +");
                    System.out.println("                  MENÚ ADMINISTRADOR              ");
                    System.out.println("+ =============================================== +");
                    System.out.printf("| %-5s | %-40s |\n", "1", "Aceptar Revisor");
                    System.out.printf("| %-5s | %-40s |\n", "2", "Gestionar Horas Beca");
                    System.out.printf("| %-5s | %-40s |\n", "3", "Cerrar Sesión");
                    System.out.println("+ =============================================== +");
                    System.out.print("Ingrese el N° de la opción a elegir ^o^: ");

                    int opcionAdmin = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    switch (opcionAdmin) {
                        case 1:
                            System.out.println("Aceptando revisores");
                            break;
                        case 2:
                            System.out.println("Gestionando horas de beca");
                            break;
                        case 3:
                            continuar = false;
                            System.out.println("Saliendo del menú de administrador...");
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            break;
                    }
                    break;

                default:
                    System.out.println("Rol no válido.");
                    continuar = false;
                    break;
            }
        }
    }
}
