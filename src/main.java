import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
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

            try {
                int opcion = entrada.nextInt(); // Intentar leer un número entero
                entrada.nextLine(); 

                if (opcion == 1) {
                    gestionLogin.registrarUsuario(entrada);
                } else if (opcion == 2) {
                    gestionLogin.iniciarSesion(entrada);
                } else if (opcion == 3) {
                    continuar = false;
                    System.out.println("Saliendo del sistema (^-^)/ ...");
                } else {
                    System.out.println("Opción no válida (._.)");
                }
            } catch (InputMismatchException e) {
                // Manejar la entrada si no es un número
                System.out.println("Opción no válida (._.)");
                entrada.nextLine(); 
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }
        entrada.close();
    }
}