package com.mvc.vista;

import com.mvc.modelo.Estudiante;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.util.List;

public class VistaEstudianteSwing extends JPanel {

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtBuscar;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtCorreo;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnRefrescar;

    private Runnable onRegistrar;
    private Runnable onActualizar;
    private Runnable onEliminar;
    private Runnable onRefrescar;

    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "Correo"};

    private static final String PLACEHOLDER_BUSCAR = "🔍 Buscar estudiante...";

    public VistaEstudianteSwing() {
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(0).setMaxWidth(70);

        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        txtBuscar = new JTextField(18);
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText(PLACEHOLDER_BUSCAR);

        txtNombre  = new JTextField(14);
        txtApellido = new JTextField(14);
        txtCorreo = new JTextField(16);

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("↻ Refrescar");

        add(buildPanelEncabezado(), BorderLayout.NORTH);
        add(buildPanelTabla(), BorderLayout.CENTER);
        add(buildPanelAcciones(), BorderLayout.SOUTH);

        initEventos();
    }

    private JPanel buildPanelEncabezado() {
        JLabel lblTitulo = new JLabel("Gestión de Estudiantes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel lblSubtitulo = new JLabel("Administra el registro, actualización y eliminación de estudiantes");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(Color.GRAY);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.add(lblTitulo);
        panelTitulo.add(Box.createVerticalStrut(3));
        panelTitulo.add(lblSubtitulo);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBuscar.add(txtBuscar);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(panelTitulo, BorderLayout.WEST);
        encabezado.add(panelBuscar, BorderLayout.EAST);
        encabezado.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        return encabezado;
    }

    private JScrollPane buildPanelTabla() {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return scroll;
    }

    private JPanel buildPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(txtApellido);
        formulario.add(new JLabel("Correo:"));
        formulario.add(txtCorreo);

        JPanel botonesIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botonesIzq.add(btnRegistrar);
        botonesIzq.add(btnActualizar);
        botonesIzq.add(btnEliminar);

        JPanel botonesDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        botonesDer.add(btnRefrescar);

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.add(botonesIzq, BorderLayout.WEST);
        panelBotones.add(botonesDer, BorderLayout.EAST);

        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(200, 200, 200));

        panel.add(separador, BorderLayout.NORTH);
        panel.add(formulario, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void initEventos() {

        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(txtBuscar.getText().equals(PLACEHOLDER_BUSCAR)) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtBuscar.getText().isEmpty()) {
                    txtBuscar.setForeground(Color.GRAY);
                    txtBuscar.setText(PLACEHOLDER_BUSCAR);
                }
            }
        });

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  {
                filtrar();
            }

            @Override public void removeUpdate(DocumentEvent e)  {
                filtrar();
            }

            @Override public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();

                if(texto.isEmpty() || texto.equals(PLACEHOLDER_BUSCAR)) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) precargarCamposDesdeSeleccion();
        });

        btnRegistrar.addActionListener(e -> {
            if(onRegistrar != null) onRegistrar.run();
        });

        btnActualizar.addActionListener(e -> {
            if(onActualizar != null) onActualizar.run();
        });

        btnEliminar.addActionListener(e -> {
            if(onEliminar != null) onEliminar.run();
        });

        btnRefrescar.addActionListener(e -> {
            if(onRefrescar != null) onRefrescar.run();
        });
    }

    private void precargarCamposDesdeSeleccion() {
        int filaVista = tabla.getSelectedRow();
        if(filaVista < 0) return;
        int fila = tabla.convertRowIndexToModel(filaVista);

        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        txtApellido.setText((String) modeloTabla.getValueAt(fila, 2));
        txtCorreo.setText((String) modeloTabla.getValueAt(fila, 3));
    }

    public void cargarEstudiantes(List<Estudiante> estudiantes) {
        modeloTabla.setRowCount(0);

        for(Estudiante e : estudiantes) {
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getNombre(),
                e.getApellido(),
                e.getCorreo()
            });
        }
    }

    public int getIdSeleccionado() {
        int filaVista = tabla.getSelectedRow();
        if(filaVista < 0) return -1;
        int fila = tabla.convertRowIndexToModel(filaVista);
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getApellido() {
        return txtApellido.getText().trim();
    }

    public String getCorreo()   {
        return txtCorreo.getText().trim();
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        tabla.clearSelection();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Académico UNIAJC", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setOnRegistrar(Runnable r) {
        this.onRegistrar = r;
    }

    public void setOnActualizar(Runnable r) {
        this.onActualizar = r;
    }

    public void setOnEliminar(Runnable r) {
        this.onEliminar = r;
    }

    public void setOnRefrescar(Runnable r) {
        this.onRefrescar = r;
    }
}