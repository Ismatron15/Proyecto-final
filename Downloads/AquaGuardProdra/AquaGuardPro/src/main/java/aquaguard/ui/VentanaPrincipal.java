package aquaguard.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de AquaGuard
 * Equipo: HydroTech Solutions
 * Integrantes: Joaquin Bedon, Ismael Benavides, Juan Diego Coronel
 */
public class VentanaPrincipal extends JFrame {

    public JTabbedPane tabbedPane;
    public JLabel lblTitulo;
    public JLabel lblStatus;
    public JPanel panelRaiz;

    public VentanaPrincipal() {
        setTitle("AquaGuard - Gestión del Estrés Hídrico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(850, 560));
        setLocationRelativeTo(null);
        inicializarUI();
    }

    private void inicializarUI() {
        panelRaiz = new JPanel(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(23, 82, 120));
        header.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        lblTitulo = new JLabel("💧 AquaGuard  |  HydroTech Solutions");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.CENTER);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabbedPane.addTab("Módulo 1: Fuentes", new ModuloFuentes());
        tabbedPane.addTab("Módulo 2: Monitoreo", new ModuloMonitoreo());
        tabbedPane.addTab("Módulo 3: Distribución", new ModuloDistribucion());

        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 3));
        statusBar.setBackground(new Color(236, 240, 241));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        lblStatus = new JLabel("Estructuras: Árbol AVL  |  Cola FIFO  |  Lista Enlazada");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(100, 100, 100));
        statusBar.add(lblStatus);

        panelRaiz.add(header, BorderLayout.NORTH);
        panelRaiz.add(tabbedPane, BorderLayout.CENTER);
        panelRaiz.add(statusBar, BorderLayout.SOUTH);

        setContentPane(panelRaiz);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
