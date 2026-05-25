package com.mvc.views;

import com.mvc.models.Estudiante;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.util.Comparator;
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
    private JButton btnLimpiar;
    private JButton btnExportarPDF;
    private JButton btnExportarExcel;
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
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 0) {
                    return Integer.class;
                }

                return String.class;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setFillsViewportHeight(true);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(0).setMaxWidth(80);
        
        DefaultTableCellRenderer rendererIzq = new DefaultTableCellRenderer();
        rendererIzq.setHorizontalAlignment(SwingConstants.LEFT);
        tabla.getColumnModel().getColumn(0).setCellRenderer(rendererIzq);

        sorter = new TableRowSorter<>(modeloTabla);
        sorter.setComparator(0, Comparator.comparingInt(Integer::intValue));
        tabla.setRowSorter(sorter);

        txtBuscar = new JTextField(18);
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText(PLACEHOLDER_BUSCAR);

        txtNombre = new JTextField(14);
        txtApellido = new JTextField(14);
        txtCorreo = new JTextField(16);

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnExportarPDF = new JButton("Exportar PDF");
        btnExportarExcel = new JButton("Exportar Excel");
        btnRefrescar = new JButton("Refrescar");

        estilizarBotones();

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 16));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));

        panelPrincipal.add(buildPanelEncabezado(), BorderLayout.NORTH);
        panelPrincipal.add(buildPanelTabla(), BorderLayout.CENTER);
        panelPrincipal.add(buildPanelAcciones(), BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);

        initEventos();
    }

    private void estilizarBotones() {
        btnRegistrar.setBackground(new Color(76, 175, 80));
        btnRegistrar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(33, 150, 243));
        btnActualizar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(244, 67, 54));
        btnEliminar.setForeground(Color.WHITE);
        btnRefrescar.setBackground(new Color(96, 125, 139));
        btnRefrescar.setForeground(Color.WHITE);
        btnLimpiar.setBackground(new Color(158, 158, 158));
        btnLimpiar.setForeground(Color.WHITE);

        btnExportarPDF.setBackground(new Color(142, 68, 173));
        btnExportarPDF.setForeground(Color.WHITE);
        btnExportarExcel.setBackground(new Color(39, 174, 96));
        btnExportarExcel.setForeground(Color.WHITE);

        for(JButton btn : new JButton[]{btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar, btnExportarPDF, btnExportarExcel}) {
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(130, 32));
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        }
    }

    private JPanel buildPanelEncabezado() {
        JLabel lblTitulo = new JLabel("Gestión de Estudiantes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 70, 130));

        JLabel lblSubtitulo = new JLabel("Administra el registro, actualización y eliminación de estudiantes");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(100, 110, 125));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setBackground(Color.WHITE);
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.add(lblTitulo);
        tituloPanel.add(Box.createVerticalStrut(4));
        tituloPanel.add(lblSubtitulo);

        JPanel buscador = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buscador.setBackground(Color.WHITE);
        buscador.add(txtBuscar);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.add(tituloPanel, BorderLayout.WEST);
        encabezado.add(buscador, BorderLayout.EAST);
        encabezado.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        return encabezado;
    }

    private JScrollPane buildPanelTabla() {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        return scroll;
    }

    private JPanel buildPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(Color.WHITE);

        JPanel botonesIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botonesIzq.setBackground(Color.WHITE);
        botonesIzq.add(btnRegistrar);
        botonesIzq.add(btnActualizar);
        botonesIzq.add(btnEliminar);
        botonesIzq.add(btnLimpiar);

        JPanel botonesDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botonesDer.setBackground(Color.WHITE);
        botonesDer.add(btnExportarPDF);
        botonesDer.add(btnExportarExcel);
        botonesDer.add(btnRefrescar);

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(botonesIzq, BorderLayout.WEST);
        panelBotones.add(botonesDer, BorderLayout.EAST);

        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(200, 200, 200));

        panel.add(separador, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);

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
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();

                if(texto.isEmpty() || texto.equals(PLACEHOLDER_BUSCAR)) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" +texto));
                }
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                precargarCamposDesdeSeleccion();
            }
        });

        btnRegistrar.addActionListener(e -> mostrarDialogoRegistro());

        btnActualizar.addActionListener(e -> mostrarDialogoActualizar());

        btnEliminar.addActionListener(e -> {
            if(onEliminar != null) {
                onEliminar.run();
            }
        });

        btnRefrescar.addActionListener(e -> {
            if(onRefrescar != null) {
                onRefrescar.run();
            }

            limpiarBusqueda();
        });

        btnLimpiar.addActionListener(e -> limpiarBusqueda());

        btnExportarPDF.addActionListener(e ->
            com.mvc.services.ExportadorService.exportarPDF(tabla, "Estudiantes"));
        btnExportarExcel.addActionListener(e ->
            com.mvc.services.ExportadorService.exportarExcel(tabla, "Estudiantes"));
    }

    public void ocultarBotonEliminar() {
        btnEliminar.setVisible(false);
    }

    private void limpiarBusqueda() {
        sorter.setRowFilter(null);
        sorter.setSortKeys(null);
        tabla.clearSelection();
        txtBuscar.setText(PLACEHOLDER_BUSCAR);
        txtBuscar.setForeground(Color.GRAY);
    }

    private void mostrarDialogoRegistro() {
        JTextField nombre = new JTextField(20);
        JTextField apellido = new JTextField(20);
        JTextField correo = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(apellido);
        panel.add(new JLabel("Correo:"));
        panel.add(correo);

        int opcion = JOptionPane.showOptionDialog(
                this,
                panel,
                "Registrar estudiante",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Guardar", "Cancelar"},
                "Guardar"
        );

        if(opcion == JOptionPane.YES_OPTION) {
            txtNombre.setText(nombre.getText().trim());
            txtApellido.setText(apellido.getText().trim());
            txtCorreo.setText(correo.getText().trim());

            if(onRegistrar != null) {
                onRegistrar.run();
            }
        }
    }

    private void mostrarDialogoActualizar() {
        int idSeleccionado = getIdSeleccionado();

        if(idSeleccionado < 0) {
            mostrarError("Selecciona un estudiante para modificar.");
            return;
        }

        int filaVista = tabla.getSelectedRow();
        int fila = tabla.convertRowIndexToModel(filaVista);

        String nombre = String.valueOf(modeloTabla.getValueAt(fila, 1));
        String apellido = String.valueOf(modeloTabla.getValueAt(fila, 2));
        String correo = String.valueOf(modeloTabla.getValueAt(fila, 3));

        JTextField nombreField = new JTextField(nombre, 20);
        JTextField apellidoField = new JTextField(apellido, 20);
        JTextField correoField = new JTextField(correo, 20);

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Apellido:"));
        panel.add(apellidoField);
        panel.add(new JLabel("Correo:"));
        panel.add(correoField);

        int opcion = JOptionPane.showOptionDialog(
                this,
                panel,
                "Modificar estudiante",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Guardar", "Cancelar"},
                "Guardar"
        );

        if(opcion == JOptionPane.YES_OPTION) {
            txtNombre.setText(nombreField.getText().trim());
            txtApellido.setText(apellidoField.getText().trim());
            txtCorreo.setText(correoField.getText().trim());

            if(onActualizar != null) {
                onActualizar.run();
            }
        }
    }

    private void precargarCamposDesdeSeleccion() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) return;
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
        if (filaVista < 0) return -1;
        int fila = tabla.convertRowIndexToModel(filaVista);
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getApellido() {
        return txtApellido.getText().trim();
    }

    public String getCorreo() {
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