package com.mvc.views;

import com.mvc.models.Grupo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.util.Comparator;
import java.util.List;

public class VistaGrupoSwing extends JPanel {

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtBuscar;

    private JTextField txtIdMateria;
    private JTextField txtIdDocente;
    private JTextField txtAula;
    private JTextField txtHorario;

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

    private static final String[] COLUMNAS = {
            "ID", "ID Materia", "Materia", "ID Docente", "Docente", "Aula", "Horario"
    };
    private static final String PLACEHOLDER_BUSCAR = "🔍 Buscar grupo...";

    public VistaGrupoSwing() {
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
                if(columnIndex == 0 || columnIndex == 1 || columnIndex == 3) {
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
        tabla.getColumnModel().getColumn(1).setPreferredWidth(85);
        tabla.getColumnModel().getColumn(1).setMaxWidth(105);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(85);
        tabla.getColumnModel().getColumn(3).setMaxWidth(105);

        DefaultTableCellRenderer rendererIzq = new DefaultTableCellRenderer();
        rendererIzq.setHorizontalAlignment(SwingConstants.LEFT);
        tabla.getColumnModel().getColumn(0).setCellRenderer(rendererIzq);
        tabla.getColumnModel().getColumn(1).setCellRenderer(rendererIzq);
        tabla.getColumnModel().getColumn(3).setCellRenderer(rendererIzq);

        sorter = new TableRowSorter<>(modeloTabla);
        sorter.setComparator(0, Comparator.comparingInt(Integer::intValue));
        sorter.setComparator(1, Comparator.comparingInt(Integer::intValue));
        sorter.setComparator(3, Comparator.comparingInt(Integer::intValue));
        tabla.setRowSorter(sorter);

        txtBuscar = new JTextField(18);
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText(PLACEHOLDER_BUSCAR);

        txtIdMateria = new JTextField(8);
        txtIdDocente = new JTextField(8);
        txtAula = new JTextField(12);
        txtHorario = new JTextField(16);

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
        JLabel lblTitulo = new JLabel("Gestion de Grupos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 70, 130));

        JLabel lblSubtitulo = new JLabel("Administra el registro, actualizacion y eliminacion de grupos");
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
            com.mvc.services.ExportadorService.exportarPDF(tabla, "Grupos"));
        btnExportarExcel.addActionListener(e ->
            com.mvc.services.ExportadorService.exportarExcel(tabla, "Grupos"));
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
        JTextField idMateria = new JTextField(20);
        JTextField idDocente = new JTextField(20);
        JTextField aula = new JTextField(20);
        JTextField horario = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("ID Materia:"));
        panel.add(idMateria);
        panel.add(new JLabel("ID Docente:"));
        panel.add(idDocente);
        panel.add(new JLabel("Aula:"));
        panel.add(aula);
        panel.add(new JLabel("Horario:"));
        panel.add(horario);

        int opcion = JOptionPane.showOptionDialog(
                this,
                panel,
                "Registrar grupo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Guardar", "Cancelar"},
                "Guardar"
        );

        if(opcion == JOptionPane.YES_OPTION) {
            txtIdMateria.setText(idMateria.getText().trim());
            txtIdDocente.setText(idDocente.getText().trim());
            txtAula.setText(aula.getText().trim());
            txtHorario.setText(horario.getText().trim());

            if(onRegistrar != null) {
                onRegistrar.run();
            }
        }
    }

    private void mostrarDialogoActualizar() {
        int idSeleccionado = getIdSeleccionado();

        if(idSeleccionado < 0) {
            mostrarError("Selecciona un grupo para modificar.");
            return;
        }

        int filaVista = tabla.getSelectedRow();
        int fila = tabla.convertRowIndexToModel(filaVista);

        String idMateria = String.valueOf(modeloTabla.getValueAt(fila, 1));
        String idDocente = String.valueOf(modeloTabla.getValueAt(fila, 3));
        String aula = String.valueOf(modeloTabla.getValueAt(fila, 5));
        String horario = String.valueOf(modeloTabla.getValueAt(fila, 6));

        JTextField idMateriaField = new JTextField(idMateria, 20);
        JTextField idDocenteField = new JTextField(idDocente, 20);
        JTextField aulaField = new JTextField(aula, 20);
        JTextField horarioField = new JTextField(horario, 20);

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("ID Materia:"));
        panel.add(idMateriaField);
        panel.add(new JLabel("ID Docente:"));
        panel.add(idDocenteField);
        panel.add(new JLabel("Aula:"));
        panel.add(aulaField);
        panel.add(new JLabel("Horario:"));
        panel.add(horarioField);

        int opcion = JOptionPane.showOptionDialog(
                this,
                panel,
                "Modificar grupo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Guardar", "Cancelar"},
                "Guardar"
        );

        if(opcion == JOptionPane.YES_OPTION) {
            txtIdMateria.setText(idMateriaField.getText().trim());
            txtIdDocente.setText(idDocenteField.getText().trim());
            txtAula.setText(aulaField.getText().trim());
            txtHorario.setText(horarioField.getText().trim());

            if(onActualizar != null) {
                onActualizar.run();
            }
        }
    }

    private void precargarCamposDesdeSeleccion() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) return;
        int fila = tabla.convertRowIndexToModel(filaVista);

        txtIdMateria.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtIdDocente.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
        txtAula.setText((String) modeloTabla.getValueAt(fila, 5));
        txtHorario.setText((String) modeloTabla.getValueAt(fila, 6));
    }

    public void cargarGrupos(List<Grupo> grupos) {
        modeloTabla.setRowCount(0);

        for(Grupo g : grupos) {
            modeloTabla.addRow(new Object[]{
                    g.getId(),
                    g.getMateria().getId(),
                    g.getMateria().getNombreMateria(),
                    g.getDocente().getId(),
                    g.getDocente().getNombre(),
                    g.getAula(),
                    g.getHorario()
            });
        }
    }

    public int getIdSeleccionado() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) return -1;
        int fila = tabla.convertRowIndexToModel(filaVista);
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public String getIdMateriaTexto() {
        return txtIdMateria.getText().trim();
    }

    public String getIdDocenteTexto() {
        return txtIdDocente.getText().trim();
    }

    public String getAula() {
        return txtAula.getText().trim();
    }

    public String getHorario() {
        return txtHorario.getText().trim();
    }

    public void limpiarCampos() {
        txtIdMateria.setText("");
        txtIdDocente.setText("");
        txtAula.setText("");
        txtHorario.setText("");
        tabla.clearSelection();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Academico UNIAJC", JOptionPane.INFORMATION_MESSAGE);
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