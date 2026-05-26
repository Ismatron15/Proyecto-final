package aquaguard.ui;

import aquaguard.data.ListaFamilias;
import aquaguard.model.Familia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Módulo 3: Gestión de Distribución y Demanda Comunitaria
 * Responsable: Juan Diego Coronel
 * Estructura de datos: Lista Enlazada de Familias
 */
public class ModuloDistribucion extends JPanel {

    // --- Componentes ---
    public JTextField txtIdFamilia;
    public JTextField txtJefe;
    public JTextField txtSector;
    public JSpinner spnIntegrantes;
    public JComboBox<Familia.TipoUso> cmbTipoUso;
    public JCheckBox chkVulnerable;
    public JTable tablaFamilias;
    public DefaultTableModel modeloTabla;
    public JSpinner spnLitrosDisponibles;
    public JTextArea txtResultadoDistribucion;
    public JLabel lblTotal;

    // --- Estructura de datos: Lista enlazada ---
    private ListaFamilias listaFamilias;

    public ModuloDistribucion() {
        listaFamilias = new ListaFamilias();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inicializarUI();
        cargarDatosEjemplo();
        actualizarTabla();
    }

    private void inicializarUI() {
        // ===== Panel izquierdo: formulario familia =====
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Registro de Familia Beneficiaria"));
        panelForm.setPreferredSize(new Dimension(270, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("ID Familia:"), gbc);
        gbc.gridx = 1; txtIdFamilia = new JTextField(10);
        panelForm.add(txtIdFamilia, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Jefe de familia:"), gbc);
        gbc.gridx = 1; txtJefe = new JTextField(10);
        panelForm.add(txtJefe, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Sector:"), gbc);
        gbc.gridx = 1; txtSector = new JTextField(10);
        panelForm.add(txtSector, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("N° integrantes:"), gbc);
        gbc.gridx = 1;
        spnIntegrantes = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
        panelForm.add(spnIntegrantes, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Tipo de uso:"), gbc);
        gbc.gridx = 1;
        cmbTipoUso = new JComboBox<>(Familia.TipoUso.values());
        panelForm.add(cmbTipoUso, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        chkVulnerable = new JCheckBox("Familia vulnerable (adultos mayores / ninos / enfermos)");
        panelForm.add(chkVulnerable, gbc);

        // Botones CRUD
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnAgregar   = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar  = new JButton("Eliminar");
        JButton btnLimpiar   = new JButton("Limpiar");

        btnAgregar.setBackground(new Color(52, 152, 219));
        btnAgregar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(241, 196, 15));
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelForm.add(panelBotones, gbc);

        // ===== Panel central: tabla de familias =====
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder("Familias Registradas (Lista Enlazada)"));

        String[] cols = {"ID", "Jefe", "Sector", "Personas", "Uso", "Vulnerable", "Cuota Asign. (L)"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaFamilias = new JTable(modeloTabla);
        tablaFamilias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFamilias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });
        panelTabla.add(new JScrollPane(tablaFamilias), BorderLayout.CENTER);
        lblTotal = new JLabel("Total familias: 0");
        panelTabla.add(lblTotal, BorderLayout.SOUTH);

        // ===== Panel inferior: distribución de agua =====
        JPanel panelDistribucion = new JPanel(new BorderLayout(8, 8));
        panelDistribucion.setBorder(BorderFactory.createTitledBorder("Algoritmo de Distribucion Equitativa"));
        panelDistribucion.setPreferredSize(new Dimension(0, 200));

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelControles.add(new JLabel("Litros disponibles hoy:"));
        spnLitrosDisponibles = new JSpinner(new SpinnerNumberModel(1000, 100, 50000, 100));
        panelControles.add(spnLitrosDisponibles);

        JButton btnDistribuir = new JButton("Calcular Distribucion");
        btnDistribuir.setBackground(new Color(46, 204, 113));
        btnDistribuir.setForeground(Color.WHITE);
        btnDistribuir.setFont(btnDistribuir.getFont().deriveFont(Font.BOLD));
        panelControles.add(btnDistribuir);

        panelDistribucion.add(panelControles, BorderLayout.NORTH);

        txtResultadoDistribucion = new JTextArea(6, 40);
        txtResultadoDistribucion.setEditable(false);
        txtResultadoDistribucion.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultadoDistribucion.setText("Presione 'Calcular Distribucion' para ejecutar el algoritmo de la lista enlazada.");
        panelDistribucion.add(new JScrollPane(txtResultadoDistribucion), BorderLayout.CENTER);

        // ===== Layout principal =====
        add(panelForm, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
        add(panelDistribucion, BorderLayout.SOUTH);

        // ===== Listeners =====
        btnAgregar.addActionListener(e -> registrarFamilia());
        btnActualizar.addActionListener(e -> actualizarFamilia());
        btnEliminar.addActionListener(e -> eliminarFamilia());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnDistribuir.addActionListener(e -> ejecutarDistribucion());
    }

    // ===== CRUD =====

    private void registrarFamilia() {
        if (!validarCampos()) return;
        String id = txtIdFamilia.getText().trim();
        if (listaFamilias.buscar(id) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe una familia con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Familia f = new Familia(
                id,
                txtJefe.getText().trim(),
                txtSector.getText().trim(),
                (int) spnIntegrantes.getValue(),
                (Familia.TipoUso) cmbTipoUso.getSelectedItem(),
                chkVulnerable.isSelected()
        );
        listaFamilias.agregar(f);
        actualizarTabla();
        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Familia agregada a la lista enlazada.", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarFamilia() {
        String id = txtIdFamilia.getText().trim();
        Familia f = listaFamilias.buscar(id);
        if (f == null) {
            JOptionPane.showMessageDialog(this, "No se encontro familia con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        f.setJefeFamilia(txtJefe.getText().trim());
        f.setSector(txtSector.getText().trim());
        f.setIntegrantes((int) spnIntegrantes.getValue());
        f.setTipoUso((Familia.TipoUso) cmbTipoUso.getSelectedItem());
        f.setEsVulnerable(chkVulnerable.isSelected());
        actualizarTabla();
        JOptionPane.showMessageDialog(this, "Familia actualizada.", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarFamilia() {
        String id = txtIdFamilia.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de la familia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la familia " + id + " de la lista?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean eliminado = listaFamilias.eliminar(id);
            if (eliminado) {
                actualizarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Familia no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ejecutarDistribucion() {
        if (listaFamilias.estaVacia()) {
            JOptionPane.showMessageDialog(this, "No hay familias registradas.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double litros = (double) (int) (Integer) spnLitrosDisponibles.getValue();
        String resultado = listaFamilias.distribuirAgua(litros);
        txtResultadoDistribucion.setText(resultado);
        actualizarTabla(); // reflejar cuotas asignadas
    }

    // ===== Helpers =====

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Familia f : listaFamilias.listarTodas()) {
            modeloTabla.addRow(new Object[]{
                    f.getIdFamilia(), f.getJefeFamilia(), f.getSector(),
                    f.getIntegrantes(), f.getTipoUso(),
                    f.isEsVulnerable() ? "SI" : "No",
                    String.format("%.1f", f.getCuotaAsignada())
            });
        }
        lblTotal.setText("Familias en lista enlazada: " + listaFamilias.getTamanio());
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tablaFamilias.getSelectedRow();
        if (fila < 0) return;
        String id = (String) modeloTabla.getValueAt(fila, 0);
        Familia f = listaFamilias.buscar(id);
        if (f == null) return;
        txtIdFamilia.setText(f.getIdFamilia());
        txtJefe.setText(f.getJefeFamilia());
        txtSector.setText(f.getSector());
        spnIntegrantes.setValue(f.getIntegrantes());
        cmbTipoUso.setSelectedItem(f.getTipoUso());
        chkVulnerable.setSelected(f.isEsVulnerable());
    }

    private boolean validarCampos() {
        if (txtIdFamilia.getText().trim().isEmpty() || txtJefe.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID y Jefe de familia son obligatorios.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtIdFamilia.setText("");
        txtJefe.setText("");
        txtSector.setText("");
        spnIntegrantes.setValue(4);
        cmbTipoUso.setSelectedIndex(0);
        chkVulnerable.setSelected(false);
        tablaFamilias.clearSelection();
    }

    private void cargarDatosEjemplo() {
        listaFamilias.agregar(new Familia("FAM001", "Rosa Guaman", "Norte", 5, Familia.TipoUso.DOMESTICO, true));
        listaFamilias.agregar(new Familia("FAM002", "Pedro Taco", "Sur",  3, Familia.TipoUso.AGRICOLA,  false));
        listaFamilias.agregar(new Familia("FAM003", "Ana Teran",  "Centro",6, Familia.TipoUso.DOMESTICO, false));
        listaFamilias.agregar(new Familia("FAM004", "Luis Cando", "Este", 2, Familia.TipoUso.GANADERO,  true));
    }
}
