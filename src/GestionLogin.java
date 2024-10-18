import java.util.Scanner;

public class GestionLogin {
    private static Scanner scanner = new Scanner(System.in);

    public static void mostrarMenuPorRol(PersonaPlantilla persona) {
        boolean continuar = true;

        while (continuar) {
            switch (persona.getRol()) {
                case "Usuario":
                    System.out.println("\n+ =============================================== +");
                    System.out.println("                      MENÚ USUARIO               ");
                    System.out.println("+ =============================================== +");
                    System.out.printf("| %-5s | %-40s |\n", "1", "Subir Documento");
                    System.out.printf("| %-5s | %-40s |\n", "2", "Descargar Documento");
                    System.out.printf("| %-5s | %-40s |\n", "3", "Regresar al menú principal");
                    System.out.println("+ =============================================== +");
                    System.out.print("Ingrese el N° de la opción a elegir ^o^: ");

                    int opcionUsuario = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    switch (opcionUsuario) {
                        case 1:
                            System.out.println("Subiendo los documentos");
                            break;
                        case 2:
                            System.out.println("Descargando los documentos");
                            break;
                        case 3:
                            continuar = false;
                            System.out.println("Saliendo del menú de usuario...");
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            break;
                    }
                    break;

                case "Revisor":
                    System.out.println("Menú Revisor:");
                    System.out.println("1. Descargar Documentos Pendientes");
                    System.out.println("2. Marcar Documento como Revisado");
                    System.out.println("3. Salir");

                    int opcionRevisor = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    switch (opcionRevisor) {
                        case 1:
                            System.out.println("Descargando documentos pendientes");
                            break;
                        case 2:
                            System.out.println("Marcando documento como revisado");
                            break;
                        case 3:
                            continuar = false;
                            System.out.println("Saliendo del menú de revisor...");
                            break;
                        default:
                            System.out.println("Opción no válida.");
                            break;
                    }
                    break;

                case "Administrador":
                    System.out.println("Menú Administrador:");
                    System.out.println("1. Aceptar Revisor");
                    System.out.println("2. Gestionar Horas de Beca");
                    System.out.println("3. Salir");

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
