import java.util.Scanner;

public class GestionLogin {
    private static Scanner scanner = new Scanner(System.in);

    public static void mostrarMenuPorRol(PersonaPlantilla persona) {
        // Crear una instancia de GestionPDF con la ruta base de APUNTES
        GestionPDF gestionPDF = new GestionPDF("APUNTES");
        boolean continuar = true;

        while (continuar) {
            switch (persona.getRol()) {
                case "Usuario":
                    System.out.println("\n+ =============================================== +");
                    System.out.println("                      MENÚ USUARIO               ");
                    System.out.println("+ =============================================== +");
                    System.out.printf("| %-5s | %-40s |\n", "1", "Subir Documento");
                    System.out.printf("| %-5s | %-40s |\n", "2", "Descargar Documento");
                    System.out.printf("| %-5s | %-40s |\n", "3", "Cerrar sesión");
                    System.out.println("+ =============================================== +");
                    System.out.print("Ingrese el N° de la opción a elegir ^o^: ");

                    int opcionUsuario = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    switch (opcionUsuario) {
                        case 1:
                            // Solicitar la ruta del archivo al usuario
                            System.out.print("Ingrese la ruta completa del archivo PDF: ");
                            String rutaArchivo = scanner.nextLine();

                            // Solicitar el código del curso
                            System.out.print("Ingrese el código del curso: ");
                            String codigoCurso = scanner.nextLine();

                            // Solicitar el año
                            System.out.print("Ingrese el año (en números): ");
                            String anio = scanner.nextLine();

                            // Obtener el correo del usuario desde el objeto `persona`
                            String correoUsuario = persona.getCorreo();

                            // Llamar al método para subir el archivo, ahora con el formato correcto
                            gestionPDF.subirArchivo(rutaArchivo, codigoCurso, correoUsuario, anio);
                            break;
                        case 2:
                            System.out.println("Descargando los documentos");
                            break;
                        case 3:
                            continuar = false;
                            System.out.println("Cerrando sesión (^-^)/...");
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            break;
                    }
                    break;

                case "Revisor":
                    System.out.println("\n+ =============================================== +");
                    System.out.println("                      MENÚ REVISOR               ");
                    System.out.println("+ =============================================== +");
                    System.out.printf("| %-5s | %-40s |\n", "1", "Revisar apuntes");
                    System.out.printf("| %-5s | %-40s |\n", "2", "Cerrar sesión");
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
                    System.out.printf("| %-5s | %-40s |\n", "2", "Gestionar Horas de Beca");
                    System.out.printf("| %-5s | %-40s |\n", "3", "Salir");
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
