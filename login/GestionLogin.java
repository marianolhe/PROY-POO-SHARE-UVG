import java.util.Scanner;

public class GestionLogin {
    private static Scanner scanner = new Scanner(System.in);

public static void mostrarMenuPorRol(PersonaPlantilla persona) {
    switch (persona.getRol()) {
        case "Usuario":
            System.out.println("Menú Usuario:");
            System.out.println("1. Subir Documento");
            System.out.println("2. Descargar Documento");
            System.out.println("3. Salir");

            int opcionUsuario = scanner.nextInt();
            boolean continuarU = true;
            scanner.nextLine(); // Limpiar el buffer

            if (opcionUsuario == 1) {
                System.out.println("Subiendo los documentos");
            } else if (opcionUsuario == 2) {
                System.out.println("Descargando los documentos");
            } else if (opcionUsuario == 3) {
                continuarU = false;
                System.out.println("Saliendo");
            } else {
                System.out.println("Opción no válida.");
            }
            break;

        case "Revisor":
            System.out.println("Menú Revisor:");
            System.out.println("1. Descargar Documentos Pendientes");
            System.out.println("2. Marcar Documento como Revisado");
            System.out.println("3. Salir");

            int opcionRevisor = scanner.nextInt();
            boolean continuarR = true; 
            scanner.nextLine(); // Limpiar el buffer

            if (opcionRevisor == 1) {
                System.out.println("Descargando Pendientes");
            } else if (opcionRevisor == 2) {
                System.out.println("Marcando como revisado");
            } else if (opcionRevisor == 3) {
                continuarR = false;
                System.out.println("Saliendo");
            } else {
                System.out.println("Opción no válida.");
            }
            break;

        case "Administrador":
            System.out.println("Menú Administrador:");
            System.out.println("1. Aceptar Revisor");
            System.out.println("2. Gestionar Horas de Beca");
            System.out.println("3. Salir");

            int opcionAdmin = scanner.nextInt();
            boolean continuarA = true;
            scanner.nextLine(); // Limpiar el buffer
            

            if (opcionAdmin == 1) {
                System.out.println("Aceptando Revisores");
            } else if (opcionAdmin == 2) {
                System.out.println("Gestionando Horas");
            } else if (opcionAdmin == 3) {
                continuarA = false;
                System.out.println("Saliendo");
            } else {
                System.out.println("Opción no válida.");
            }
            break;
        
        default:
            System.out.println("Rol no válido.");
        }
    }
}
