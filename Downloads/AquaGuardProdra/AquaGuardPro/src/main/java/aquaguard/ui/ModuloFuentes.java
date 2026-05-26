package aquaguard.ui;

import aquaguard.data.ArbolAVLFuentes;
import aquaguard.model.FuenteAgua;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Módulo 1: Gestión de Fuentes de Agua
 * Responsable: Joaquin Bedon
 * Estructura de datos: Árbol AVL
 */
public class ModuloFuentes extends JPanel {

    // --- Componentes de la interfaz ---
    public JTextField txtId;
    public JTextField txtNombre;
    public JComboBox<FuenteAgua.TipoFuente> cmbTipo;
    public JTextField txtUbicacion;
    public JSpinner spnCapacidad;
    public JSpinner spnNivel;
    public JComboBox<FuenteAgua.EstadoCalidad> cmbEstado;
    public JTable tabla;
    public DefaultTableModel modeloTabla;
    public JLabel lblDisponibilidad;
    public JLabel lblTotal;

    // --- Estructura de datos: Árbol AVL ---
    private ArbolAVLFuentes arbolFuentes;

    public ModuloFuentes() {
        arbolFuentes = new ArbolAVLFuentes();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inicializarUI();
        cargarDatosEjemplo();
        actualizarTabla();
    }

    private void inicializarUI() {
        // ===== Panel de formulario (lado izquierdo) =====
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Fuente"));
        panelFormulario.setPreferredSize(new Dimension(280, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID Fuente:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(12);
        panelFormulario.add(txtId, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(12);
        panelFormulario.add(txtNombre, gbc);

        // Tipo
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cmbTipo = new JComboBox<>(FuenteAgua.TipoFuente.values());
        panelFormulario.add(cmbTipo, gbc);

        // Ubicación
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Ubicacion:"), gbc);
        gbc.gridx = 1;
        txtUbicacion = new JTextField(12);
        panelFormulario.add(txtUbicacion, gbc);

        // Capacidad (Spinner)
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Capacidad (L):"), gbc);
        gbc.gridx = 1;
        spnCapacidad = new JSpinner(new SpinnerNumberModel(1000, 100, 100000, 100));
        panelFormulario.add(spnCapacidad, gbc);

        // Nivel actual (Spinner)
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(new JLabel("Nivel (%):"), gbc);
        gbc.gridx = 1;
        spnNivel = new JSpinner(new SpinnerNumberModel(100, 0, 100, 5));
        panelFormulario.add(spnNivel, gbc);

        // Estado calidad
        gbc.gridx = 0; gbc.gridy = 6;
        panelFormulario.add(new JLabel("Estado Calidad:"), gbc);
        gbc.gridx = 1;
        cmbEstado = new JComboBox<>(FuenteAgua.EstadoCalidad.values());
        panelFormulario.add(cmbEstado, gbc);

        // Label disponibilidad calculada
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        lblDisponibilidad = new JLabel("Disponibilidad: -");
        lblDisponibilidad.setFont(lblDisponibilidad.getFont().deriveFont(Font.BOLD));
        panelFormulario.add(lblDisponibilidad, gbc);

        // Botones CRUD
        gbc.gridy = 8; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnAgregar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Dar de baja");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.setBackground(new Color(52, 152, 219));
        btnAgregar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(241, 196, 15));
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones, gbc);

        // ===== Panel de tabla (derecha) =====
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder("Inventario de Fuentes (Arbol AVL)"));

        String[] columnas = {"ID", "Nombre", "Tipo", "Ubicacion", "Capacidad (L)", "Nivel %", "Estado", "Operativa"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccionEnFormulario();
        });

        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);

        lblTotal = new JLabel("Total fuentes: 0");
        panelTabla.add(lblTotal, BorderLayout.SOUTH);

        // ===== Layout principal =====
        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        // ===== Listeners de botones =====
        btnAgregar.addActionListener(e -> registrarFuente());
        btnActualizar.addActionListener(e -> actualizarFuente());
        btnEliminar.addActionListener(e -> eliminarFuente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Actualizar disponibilidad al cambiar spinner
        spnCapacidad.addChangeListener(e -> actualizarDisponibilidadLabel());
        spnNivel.addChangeListener(e -> actualizarDisponibilidadLabel());
    }

    // ===== Acciones CRUD =====

    private void registrarFuente() {
        if (!validarCampos()) return;

        String id = txtId.getText().trim();
        if (arbolFuentes.buscar(id) != null) {
            JOptionPane.showMessageDialog(this, "Ya existe una fuente con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FuenteAgua fuente = new FuenteAgua(
                id,
                txtNombre.getText().trim(),
                (FuenteAgua.TipoFuente) cmbTipo.getSelectedItem(),
                txtUbicacion.getText().trim(),
                (double) (Integer) spnCapacidad.getValue()
        );
        fuente.setNivelActual((double) (Integer) spnNivel.getValue());
        fuente.setEstadoCalidad((FuenteAgua.EstadoCalidad) cmbEstado.getSelectedItem());

        arbolFuentes.insertar(fuente);
        actualizarTabla();
        limpiarFormulario();
        JOptionPane.showMessageDialog(this, "Fuente registrada correctamente en el Arbol AVL.", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarFuente() {
        String id = txtId.getText().trim();
        FuenteAgua fuente = arbolFuentes.buscar(id);
        if (fuente == null) {
            JOptionPane.showMessageDialog(this, "No se encontro la fuente con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        fuente.setNombre(txtNombre.getText().trim());
        fuente.setTipo((FuenteAgua.TipoFuente) cmbTipo.getSelectedItem());
        fuente.setUbicacion(txtUbicacion.getText().trim());
        fuente.setCapacidadLitros((double) (Integer) spnCapacidad.getValue());
        fuente.setNivelActual((double) (Integer) spnNivel.getValue());
        fuente.setEstadoCalidad((FuenteAgua.EstadoCalidad) cmbEstado.getSelectedItem());

        actualizarTabla();
        JOptionPane.showMessageDialog(this, "Fuente actualizada correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarFuente() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de la fuente a dar de baja.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Dar de baja la fuente " + id + "? (Baja logica, historial conservado)",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            arbolFuentes.eliminar(id);
            actualizarTabla();
            limpiarFormulario();
        }
    }

    // ===== Helpers =====

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<FuenteAgua> fuentes = arbolFuentes.listarTodas();
        for (FuenteAgua f : fuentes) {
            modeloTabla.addRow(new Object[]{
                    f.getIdFuente(),
                    f.getNombre(),
                    f.getTipo(),
                    f.getUbicacion(),
                    f.getCapacidadLitros(),
                    f.getNivelActual() + "%",
                    f.getEstadoCalidad(),
                    f.isActiva() ? "Si" : "No (baja)"
            });
        }
        lblTotal.setText("Total fuentes en AVL: " + fuentes.size()
                + "  |  Activas: " + arbolFuentes.listarSoloActivas().size());
    }

    private void cargarSeleccionEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        String id = (String) modeloTabla.getValueAt(fila, 0);
        FuenteAgua f = arbolFuentes.buscar(id);
        if (f == null) return;

        txtId.setText(f.getIdFuente());
        txtNombre.setText(f.getNombre());
        cmbTipo.setSelectedItem(f.getTipo());
        txtUbicacion.setText(f.getUbicacion());
        spnCapacidad.setValue((int) f.getCapacidadLitros());
        spnNivel.setValue((int) f.getNivelActual());
        cmbEstado.setSelectedItem(f.getEstadoCalidad());
        actualizarDisponibilidadLabel();
    }

    private void actualizarDisponibilidadLabel() {
        double cap = (double) (Integer) spnCapacidad.getValue();
        double nivel = (double) (Integer) spnNivel.getValue();
        double litros = (nivel / 100.0) * cap;
        lblDisponibilidad.setText(String.format("Disponible: %.0f L", litros));
    }

    private boolean validarCampos() {
        if (txtId.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID y Nombre son obligatorios.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtUbicacion.setText("");
        cmbTipo.setSelectedIndex(0);
        spnCapacidad.setValue(1000);
        spnNivel.setValue(100);
        cmbEstado.setSelectedIndex(0);
        lblDisponibilidad.setText("Disponibilidad: -");
        tabla.clearSelection();
    }

    private void cargarDatosEjemplo() {
        FuenteAgua f1 = new FuenteAgua("F001", "Pozo Norte", FuenteAgua.TipoFuente.POZO, "Sector Norte - GPS: -0.22, -78.51", 5000);
        f1.setNivelActual(75);
        f1.setEstadoCalidad(FuenteAgua.EstadoCalidad.BUENA);

        FuenteAgua f2 = new FuenteAgua("F002", "Rio Machangara", FuenteAgua.TipoFuente.RIO, "Zona Sur - GPS: -0.25, -78.53", 20000);
        f2.setNivelActual(60);
        f2.setEstadoCalidad(FuenteAgua.EstadoCalidad.REGULAR);

        FuenteAgua f3 = new FuenteAgua("F003", "Reservorio Central", FuenteAgua.TipoFuente.RESERVORIO, "Centro Comunitario", 10000);
        f3.setNivelActual(40);
        f3.setEstadoCalidad(FuenteAgua.EstadoCalidad.BUENA);

        arbolFuentes.insertar(f1);
        arbolFuentes.insertar(f2);
        arbolFuentes.insertar(f3);
    }
}
