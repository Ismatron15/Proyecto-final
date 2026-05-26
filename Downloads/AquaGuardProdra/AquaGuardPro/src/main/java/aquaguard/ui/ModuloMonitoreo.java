package aquaguard.ui;

import aquaguard.data.ColaAlertas;
import aquaguard.model.Medicion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Módulo 2: Monitoreo y Alertas de Calidad del Agua
 * Responsable: Ismael Benavides
 * Estructura de datos: Cola (FIFO) de Alertas + Lista de mediciones
 */
public class ModuloMonitoreo extends JPanel {

    // --- Componentes ---
    public JTextField txtIdMedicion;
    public JTextField txtIdFuente;
    public JSpinner spnPH;
    public JSpinner spnTurbidez;
    public JSpinner spnTemperatura;
    public JCheckBox chkColiformes;
    public JTextField txtFecha;
    public JTable tablaHistorico;
    public DefaultTableModel modeloHistorico;
    public JList<String> listAlertas;
    public DefaultListModel<String> modeloAlertas;
    public JLabel lblEstadoAlerta;
    public JLabel lblCantAlertas;

    // --- Estructuras de datos ---
    private List<Medicion> historialMediciones; // lista simple para el historial
    private ColaAlertas colaAlertas;             // cola FIFO para alertas

    public ModuloMonitoreo() {
        historialMediciones = new ArrayList<>();
        colaAlertas = new ColaAlertas();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inicializarUI();
        cargarDatosEjemplo();
        actualizarVistas();
    }

    private void inicializarUI() {
        // ===== Panel superior: formulario de medicion =====
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva Medicion de Calidad"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Medicion:"), gbc);
        gbc.gridx = 1; txtIdMedicion = new JTextField(10);
        panelFormulario.add(txtIdMedicion, gbc);

        gbc.gridx = 2; panelFormulario.add(new JLabel("ID Fuente:"), gbc);
        gbc.gridx = 3; txtIdFuente = new JTextField(10);
        panelFormulario.add(txtIdFuente, gbc);

        gbc.gridx = 4; panelFormulario.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 5; txtFecha = new JTextField(LocalDate.now().toString(), 10);
        panelFormulario.add(txtFecha, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("pH (6.5 - 8.5 OMS):"), gbc);
        gbc.gridx = 1;
        spnPH = new JSpinner(new SpinnerNumberModel(7.0, 0.0, 14.0, 0.1));
        panelFormulario.add(spnPH, gbc);

        gbc.gridx = 2; panelFormulario.add(new JLabel("Turbidez NTU (< 5):"), gbc);
        gbc.gridx = 3;
        spnTurbidez = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 100.0, 0.5));
        panelFormulario.add(spnTurbidez, gbc);

        gbc.gridx = 4; panelFormulario.add(new JLabel("Temperatura C:"), gbc);
        gbc.gridx = 5;
        spnTemperatura = new JSpinner(new SpinnerNumberModel(18.0, 0.0, 50.0, 0.5));
        panelFormulario.add(spnTemperatura, gbc);

        // Fila 3
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Coliformes presentes:"), gbc);
        gbc.gridx = 1;
        chkColiformes = new JCheckBox("Si (contaminado)");
        panelFormulario.add(chkColiformes, gbc);

        gbc.gridx = 2;
        lblEstadoAlerta = new JLabel("Estado: ---");
        lblEstadoAlerta.setFont(lblEstadoAlerta.getFont().deriveFont(Font.BOLD, 13f));
        panelFormulario.add(lblEstadoAlerta, gbc);

        // Botones
        gbc.gridx = 4; gbc.gridy = 2;
        JButton btnRegistrar = new JButton("Registrar Medicion");
        btnRegistrar.setBackground(new Color(39, 174, 96));
        btnRegistrar.setForeground(Color.WHITE);
        panelFormulario.add(btnRegistrar, gbc);

        gbc.gridx = 5;
        JButton btnAtenderAlerta = new JButton("Atender Alerta");
        btnAtenderAlerta.setBackground(new Color(231, 76, 60));
        btnAtenderAlerta.setForeground(Color.WHITE);
        panelFormulario.add(btnAtenderAlerta, gbc);

        // ===== Panel central: tabla historial =====
        JPanel panelHistorial = new JPanel(new BorderLayout(5, 5));
        panelHistorial.setBorder(BorderFactory.createTitledBorder("Historico de Mediciones"));

        String[] cols = {"ID", "Fuente", "pH", "Turbidez", "Temp °C", "Coliformes", "Fecha", "Nivel Alerta"};
        modeloHistorico = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaHistorico = new JTable(modeloHistorico);
        tablaHistorico.setRowHeight(22);
        panelHistorial.add(new JScrollPane(tablaHistorico), BorderLayout.CENTER);

        // ===== Panel derecho: Cola de alertas =====
        JPanel panelCola = new JPanel(new BorderLayout(5, 5));
        panelCola.setBorder(BorderFactory.createTitledBorder("Cola de Alertas (FIFO)"));
        panelCola.setPreferredSize(new Dimension(310, 0));

        modeloAlertas = new DefaultListModel<>();
        listAlertas = new JList<>(modeloAlertas);
        listAlertas.setFont(new Font("Monospaced", Font.PLAIN, 11));
        panelCola.add(new JScrollPane(listAlertas), BorderLayout.CENTER);

        lblCantAlertas = new JLabel("Alertas en cola: 0");
        lblCantAlertas.setFont(lblCantAlertas.getFont().deriveFont(Font.BOLD));
        panelCola.add(lblCantAlertas, BorderLayout.SOUTH);

        // ===== Layout principal =====
        add(panelFormulario, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelHistorial, panelCola);
        split.setResizeWeight(0.7);
        add(split, BorderLayout.CENTER);

        // ===== Listeners =====
        btnRegistrar.addActionListener(e -> registrarMedicion());
        btnAtenderAlerta.addActionListener(e -> atenderAlerta());

        // Previsualizacion en tiempo real del estado
        spnPH.addChangeListener(e -> previsualizarAlerta());
        spnTurbidez.addChangeListener(e -> previsualizarAlerta());
        chkColiformes.addActionListener(e -> previsualizarAlerta());
    }

    private void registrarMedicion() {
        if (txtIdMedicion.getText().trim().isEmpty() || txtIdFuente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID de medicion e ID de fuente son obligatorios.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Medicion m = new Medicion(
                txtIdMedicion.getText().trim(),
                txtIdFuente.getText().trim(),
                (double) spnPH.getValue(),
                (double) spnTurbidez.getValue(),
                (double) spnTemperatura.getValue(),
                chkColiformes.isSelected(),
                txtFecha.getText().trim()
        );

        historialMediciones.add(m);

        boolean encolada = colaAlertas.encolar(m);
        actualizarVistas();

        String msg = "Medicion registrada.\n" + m.getMensajeEstado();
        if (encolada) msg += "\n\nAlerta agregada a la cola de atencion.";
        JOptionPane.showMessageDialog(this, msg, "Resultado", JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
    }

    private void atenderAlerta() {
        if (colaAlertas.estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay alertas pendientes en la cola.", "Cola vacia", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Medicion atendida = colaAlertas.desencolar();
        actualizarVistas();
        JOptionPane.showMessageDialog(this,
                "Alerta atendida:\n" + atendida.toString() + "\nFuente: " + atendida.getIdFuente(),
                "Alerta atendida (desencolada)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void previsualizarAlerta() {
        Medicion temp = new Medicion("tmp", "tmp",
                (double) spnPH.getValue(),
                (double) spnTurbidez.getValue(),
                (double) spnTemperatura.getValue(),
                chkColiformes.isSelected(), "");
        String msg = temp.getMensajeEstado();
        lblEstadoAlerta.setText("Estado: " + temp.calcularNivelAlerta());
        switch (temp.calcularNivelAlerta()) {
            case ROJA:    lblEstadoAlerta.setForeground(new Color(192, 57, 43)); break;
            case NARANJA: lblEstadoAlerta.setForeground(new Color(211, 84, 0)); break;
            case AMARILLA:lblEstadoAlerta.setForeground(new Color(183, 149, 11)); break;
            default:      lblEstadoAlerta.setForeground(new Color(39, 174, 96)); break;
        }
    }

    private void actualizarVistas() {
        // Actualizar tabla historial
        modeloHistorico.setRowCount(0);
        for (Medicion m : historialMediciones) {
            modeloHistorico.addRow(new Object[]{
                    m.getIdMedicion(), m.getIdFuente(),
                    String.format("%.1f", m.getpH()),
                    String.format("%.1f", m.getTurbidez()),
                    String.format("%.1f", m.getTemperatura()),
                    m.isColiformes() ? "SI" : "No",
                    m.getFecha(),
                    m.calcularNivelAlerta()
            });
        }

        // Actualizar lista de alertas en cola
        modeloAlertas.clear();
        for (String alerta : colaAlertas.listarAlertas()) {
            modeloAlertas.addElement(alerta);
        }
        lblCantAlertas.setText("Alertas en cola (FIFO): " + colaAlertas.getTamanio());
    }

    private void limpiarFormulario() {
        txtIdMedicion.setText("");
        txtIdFuente.setText("");
        spnPH.setValue(7.0);
        spnTurbidez.setValue(1.0);
        spnTemperatura.setValue(18.0);
        chkColiformes.setSelected(false);
        txtFecha.setText(LocalDate.now().toString());
        lblEstadoAlerta.setText("Estado: ---");
        lblEstadoAlerta.setForeground(Color.BLACK);
    }

    private void cargarDatosEjemplo() {
        Medicion m1 = new Medicion("M001", "F001", 7.2, 2.5, 18.0, false, "2025-04-10");
        Medicion m2 = new Medicion("M002", "F002", 6.1, 8.3, 20.5, false, "2025-04-11");
        Medicion m3 = new Medicion("M003", "F001", 5.8, 12.0, 21.0, true,  "2025-04-12");
        Medicion m4 = new Medicion("M004", "F003", 7.5, 3.0, 17.5, false, "2025-04-12");

        historialMediciones.add(m1);
        historialMediciones.add(m2);
        historialMediciones.add(m3);
        historialMediciones.add(m4);

        colaAlertas.encolar(m1);
        colaAlertas.encolar(m2);
        colaAlertas.encolar(m3);
        colaAlertas.encolar(m4);
    }
}
