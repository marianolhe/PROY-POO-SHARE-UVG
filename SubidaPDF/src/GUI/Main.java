package GUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Crear e inicializar la ventana de la aplicación
        SwingUtilities.invokeLater(() -> {
            Desplay app = new Desplay();
            app.setVisible(true);
        });
    }
}

