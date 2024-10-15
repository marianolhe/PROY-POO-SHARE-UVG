//ejemplos de menus según rol

public class GestionLogin {
    // Métodos para el menú de usuario
    
    public void mostrarMenuUsuario() {
        System.out.println("Menú de usuario:");
        System.out.println("1. Ver información personal");
        System.out.println("2. Editar información personal");
        System.out.println("3. Salir");
    }

    public void verInformacionPersonal() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Apellido: " + apellido);
        System.out.println("Correo: " + correo);
        System.out.println("Carrera: " + carrera);
    }
    public void editarInformacionPersonal() {
        // Implementar la lógica para editar la información personal
    }

    // Métodos para el menú de administrador
    public void mostrarMenuAdministrador() {
        System.out.println("Menú de administrador:");
        System.out.println("1. Ver lista de usuarios");
        System.out.println("2. Agregar usuario");
        System.out.println("3. Eliminar usuario");
        System.out.println("4. Salir");
    }

    public void verListaUsuarios() {
        // Implementar la lógica para ver la lista de usuarios
    }

    public void agregarUsuario() {
        // Implementar la lógica para agregar un usuario
    }
}
