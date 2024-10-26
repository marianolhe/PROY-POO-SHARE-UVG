import java.util.Scanner;

public class Main {
    private static GestionLogin gestionLogin = new GestionLogin();

    public static void main(String[] args) {
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
}