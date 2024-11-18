import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestionHorasBecaGUI {

    // Clase interna para representar a un Estudiante
    public static class Estudiante {
        private String nombre;
        private String id;
        private int horasBeca;

        public Estudiante(String nombre, String id, int horasBeca) {
            this.nombre = nombre;
            this.id = id;
            this.horasBeca = horasBeca;
        }

        public String getNombre() {
            return nombre;
        }

        public String getId() {
            return id;
        }

        public int getHorasBeca() {
            return horasBeca;
        }

        public void setHorasBeca(int horasBeca) {
            this.horasBeca = horasBeca;
        }

        public void agregarHorasBeca(int horas) {
            if (horas >= 0) {
                this.horasBeca += horas;
            }
        }

        @Override
        public String toString() {
            return nombre + "," + id + "," + horasBeca;
        }
    }

    public static class GestionEstudiantes {
        private List<Estudiante> estudiantes;
        private final String archivoCSV = "archivos_csv/estudiantes.csv";

        public GestionEstudiantes() {
            this.estudiantes = new ArrayList<>();
            cargarEstudiantes();
        }

        public void cargarEstudiantes() {
            try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(",");
                    String nombre = datos[0];
                    String id = datos[1];
                    int horasBeca = Integer.parseInt(datos[2]);
                    estudiantes.add(new Estudiante(nombre, id, horasBeca));
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void guardarEstudiantes() {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivoCSV))) {
                for (Estudiante estudiante : estudiantes) {
                    pw.println(estudiante.toString());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar en el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public Estudiante buscarEstudiantePorID(String id) {
            for (Estudiante estudiante : estudiantes) {
                if (estudiante.getId().equals(id)) {
                    return estudiante;
                }
            }
            return null;
        }

        public void agregarHorasEstudiante(String id, int horas) {
            Estudiante estudiante = buscarEstudiantePorID(id);
            if (estudiante != null) {
                estudiante.agregarHorasBeca(horas);
                guardarEstudiantes();
                JOptionPane.showMessageDialog(null, "Horas agregadas correctamente para " + estudiante.getNombre());
            } else {
                JOptionPane.showMessageDialog(null, "Estudiante no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        GestionEstudiantes gestion = new GestionEstudiantes();

        // Crear la ventana principal
        JFrame frame = new JFrame("Gestión de Horas de Beca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        // Componentes
        JLabel lblId = new JLabel("ID del estudiante:");
        JTextField txtId = new JTextField();
        JLabel lblHoras = new JLabel("Horas a añadir:");
        JTextField txtHoras = new JTextField();
        JButton btnAgregarHoras = new JButton("Agregar Horas");

        // Acción al presionar el botón
        btnAgregarHoras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText();
                String horasStr = txtHoras.getText();

                try {
                    int horas = Integer.parseInt(horasStr);
                    gestion.agregarHorasEstudiante(id, horas);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingrese un número válido para las horas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Añadir componentes al panel
        panel.add(lblId);
        panel.add(txtId);
        panel.add(lblHoras);
        panel.add(txtHoras);
        panel.add(btnAgregarHoras);

        // Configurar y mostrar la ventana
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}

