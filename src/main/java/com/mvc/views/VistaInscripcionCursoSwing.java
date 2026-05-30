package com.mvc.views;

import com.mvc.config.ConfiguracionApp;
import com.mvc.models.InscripcionCurso;

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
import java.util.ArrayList;
import java.util.List;

public class VistaInscripcionCursoSwing extends JPanel {

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private java.util.List<InscripcionCurso> todosDatos = new ArrayList<>();
    private int paginaActual = 0;
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JLabel lblPagina;

    private JTextField txtBuscar;

    private JTextField txtIdEstudiante;
    private JTextField txtIdGrupo;
    private JTextField txtNotaFinal;
    private JComboBox<String> cmbEstado;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnRefrescar;
    private JButton btnNotasEstudiante;
    private JButton btnNotasGrupo;
    private JButton btnExportarPDF;
    private JButton btnExportarExcel;

    private Runnable onRegistrar;
    private Runnable onActualizar;
    private Runnable onEliminar;
    private Runnable onRefrescar;
    private Runnable onNotasEstudiante;
    private Runnable onNotasGrupo;

    private static final String[] COLUMNAS = {
            "ID", "ID Estudiante", "Estudiante", "ID Grupo", "Materia", "Docente", "Aula", "Horario", "Nota Final", "Estado"
    };
    private static final String[] ESTADOS = {"Inscrito", "En curso", "Retirado"};
    private static final String PLACEHOLDER_BUSCAR = "🔍 Buscar inscripcion...";
    private static final String SIN_NOTA = "Sin nota";

    public VistaInscripcionCursoSwing() {
        initComponents();
        ConfiguracionApp.getInstance().addListenerPaginacion(this::mostrarPaginaActual);
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

                return Object.class;
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
        tabla.getColumnModel().getColumn(1).setPreferredWidth(105);
        tabla.getColumnModel().getColumn(1).setMaxWidth(125);
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

        txtIdEstudiante = new JTextField(8);
        txtIdGrupo = new JTextField(8);
        txtNotaFinal = new JTextField(8);
        cmbEstado = new JComboBox<>(ESTADOS);

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnRefrescar = new JButton("Refrescar");
        btnNotasEstudiante = new JButton("Notas estudiante");
        btnNotasGrupo = new JButton("Notas grupo");
        btnExportarPDF = new JButton("Exportar PDF");
        btnExportarExcel = new JButton("Exportar Excel");

        btnAnterior = new JButton("◀  Anterior");
        btnSiguiente = new JButton("Siguiente  ▶");
        lblPagina = new JLabel("", SwingConstants.CENTER);

        estilizarBotones();

        btnAnterior.setBackground(new Color(52, 73, 94));
        btnAnterior.setForeground(Color.WHITE);
        btnAnterior.setFocusPainted(false);
        btnAnterior.setBorderPainted(false);
        btnAnterior.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAnterior.setPreferredSize(new Dimension(110, 28));
        btnAnterior.setFont(new Font("SansSerif", Font.BOLD, 11));

        btnSiguiente.setBackground(new Color(52, 73, 94));
        btnSiguiente.setForeground(Color.WHITE);
        btnSiguiente.setFocusPainted(false);
        btnSiguiente.setBorderPainted(false);
        btnSiguiente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSiguiente.setPreferredSize(new Dimension(110, 28));
        btnSiguiente.setFont(new Font("SansSerif", Font.BOLD, 11));

        lblPagina.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPagina.setForeground(new Color(60, 60, 60));
        lblPagina.setPreferredSize(new Dimension(140, 28));

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
        btnNotasEstudiante.setBackground(new Color(0, 150, 136));
        btnNotasEstudiante.setForeground(Color.WHITE);
        btnNotasGrupo.setBackground(new Color(0, 150, 136));
        btnNotasGrupo.setForeground(Color.WHITE);

        btnExportarPDF.setBackground(new Color(142, 68, 173));
        btnExportarPDF.setForeground(Color.WHITE);
        btnExportarExcel.setBackground(new Color(39, 174, 96));
        btnExportarExcel.setForeground(Color.WHITE);

        for(JButton btn : new JButton[]{
                btnRegistrar, btnActualizar, btnEliminar, btnLimpiar, btnRefrescar,
                btnNotasEstudiante, btnNotasGrupo, btnExportarPDF, btnExportarExcel
        }) {
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(130, 32));
            btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        }

        btnNotasEstudiante.setPreferredSize(new Dimension(145, 32));
        btnNotasGrupo.setPreferredSize(new Dimension(120, 32));
    }

    private JPanel buildPanelEncabezado() {
        JLabel lblTitulo = new JLabel("Gestion de Inscripciones");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 70, 130));

        JLabel lblSubtitulo = new JLabel("Administra estudiantes inscritos en grupos, notas y estados");
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

        JPanel accionesCrud = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        accionesCrud.setBackground(Color.WHITE);
        accionesCrud.add(btnRegistrar);
        accionesCrud.add(btnActualizar);
        accionesCrud.add(btnEliminar);
        accionesCrud.add(btnLimpiar);

        JPanel accionesConsulta = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        accionesConsulta.setBackground(Color.WHITE);
        accionesConsulta.add(btnNotasEstudiante);
        accionesConsulta.add(btnNotasGrupo);

        JPanel accionesIzq = new JPanel();
        accionesIzq.setBackground(Color.WHITE);
        accionesIzq.setLayout(new BoxLayout(accionesIzq, BoxLayout.Y_AXIS));
        accionesIzq.add(accionesCrud);
        accionesIzq.add(Box.createVerticalStrut(8));
        accionesIzq.add(accionesConsulta);

        JPanel botonesDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        botonesDer.setBackground(Color.WHITE);
        botonesDer.add(btnExportarPDF);
        botonesDer.add(btnExportarExcel);
        botonesDer.add(btnRefrescar);

        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(accionesIzq, BorderLayout.WEST);
        panelBotones.add(botonesDer, BorderLayout.EAST);

        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        panelPaginacion.setBackground(Color.WHITE);
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPagina);
        panelPaginacion.add(btnSiguiente);

        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(200, 200, 200));

        panel.add(separador, BorderLayout.NORTH);
        panel.add(panelPaginacion, BorderLayout.CENTER);
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
            com.mvc.services.ExportadorService.exportarPDF(tabla, "Inscripciones"));
        btnExportarExcel.addActionListener(e ->
            com.mvc.services.ExportadorService.exportarExcel(tabla, "Inscripciones"));

        btnNotasEstudiante.addActionListener(e -> {
            if(onNotasEstudiante != null) {
                onNotasEstudiante.run();
            }
        });

        btnNotasGrupo.addActionListener(e -> {
            if(onNotasGrupo != null) {
                onNotasGrupo.run();
            }
        });

        btnAnterior.addActionListener(e -> {
            if(paginaActual > 0) {
                paginaActual--;
                mostrarPaginaActual();
            }
        });

        btnSiguiente.addActionListener(e -> {
            if(paginaActual < calcularTotalPaginas() - 1) {
                paginaActual++;
                mostrarPaginaActual();
            }
        });
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
        JTextField idEstudiante = new JTextField(20);
        JTextField idGrupo = new JTextField(20);
        JTextField notaFinal = new JTextField(20);
        JComboBox<String> estado = new JComboBox<>(ESTADOS);

        JPanel panel = buildPanelFormulario(idEstudiante, idGrupo, notaFinal, estado);

        int opcion = JOptionPane.showOptionDialog(
                this,
                panel,
                "Registrar inscripcion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Guardar", "Cancelar"},
                "Guardar"
        );

        if(opcion == JOptionPane.YES_OPTION) {
            txtIdEstudiante.setText(idEstudiante.getText().trim());
            txtIdGrupo.setText(idGrupo.getText().trim());
            txtNotaFinal.setText(notaFinal.getText().trim());
            cmbEstado.setSelectedItem(estado.getSelectedItem());

            if(onRegistrar != null) {
                onRegistrar.run();
            }
        }
    }

    private void mostrarDialogoActualizar() {
        int idSeleccionado = getIdSeleccionado();

        if(idSeleccionado < 0) {
            mostrarError("Selecciona una inscripcion para modificar.");
            return;
        }

        int filaVista = tabla.getSelectedRow();
        int fila = tabla.convertRowIndexToModel(filaVista);

        String idEstudiante = String.valueOf(modeloTabla.getValueAt(fila, 1));
        String idGrupo = String.valueOf(modeloTabla.getValueAt(fila, 3));
        Object nota = modeloTabla.getValueAt(fila, 8);
        String notaFinal = SIN_NOTA.equals(nota) ? "" : String.valueOf(nota);
        String estadoActual = String.valueOf(modeloTabla.getValueAt(fila, 9));

        JTextField idEstudianteField = new JTextField(idEstudiante, 20);
        JTextField idGrupoField = new JTextField(idGrupo, 20);
        JTextField notaFinalField = new JTextField(notaFinal, 20);
        JComboBox<String> estadoField = new JComboBox<>(ESTADOS);
        estadoField.setSelectedItem(estadoActual);

        JPanel panel = buildPanelFormulario(idEstudianteField, idGrupoField, notaFinalField, estadoField);

        int opcion = JOptionPane.showOptionDialog(
                this,
                panel,
                "Modificar inscripcion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Guardar", "Cancelar"},
                "Guardar"
        );

        if(opcion == JOptionPane.YES_OPTION) {
            txtIdEstudiante.setText(idEstudianteField.getText().trim());
            txtIdGrupo.setText(idGrupoField.getText().trim());
            txtNotaFinal.setText(notaFinalField.getText().trim());
            cmbEstado.setSelectedItem(estadoField.getSelectedItem());
            
            if(onActualizar != null) {
                onActualizar.run();
            }
        }
    }

    private JPanel buildPanelFormulario(
            JTextField idEstudiante,
            JTextField idGrupo,
            JTextField notaFinal,
            JComboBox<String> estado
    ) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("ID Estudiante:"));
        panel.add(idEstudiante);
        panel.add(new JLabel("ID Grupo:"));
        panel.add(idGrupo);
        panel.add(new JLabel("Nota Final:"));
        panel.add(notaFinal);
        panel.add(new JLabel("Estado:"));
        panel.add(estado);
        return panel;
    }

    private void precargarCamposDesdeSeleccion() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) return;
        int fila = tabla.convertRowIndexToModel(filaVista);

        txtIdEstudiante.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtIdGrupo.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));

        Object nota = modeloTabla.getValueAt(fila, 8);
        txtNotaFinal.setText(SIN_NOTA.equals(nota) ? "" : String.valueOf(nota));
        cmbEstado.setSelectedItem((String) modeloTabla.getValueAt(fila, 9));
    }

    private int calcularTotalPaginas() {
        int porPagina = ConfiguracionApp.getInstance().getRegistrosPorPagina();
        if (todosDatos.isEmpty()) return 1;
        return (int) Math.ceil((double) todosDatos.size() / porPagina);
    }

    private void mostrarPaginaActual() {
        int porPagina = ConfiguracionApp.getInstance().getRegistrosPorPagina();
        int totalPaginas = calcularTotalPaginas();

        if (paginaActual >= totalPaginas) paginaActual = totalPaginas - 1;

        int desde = paginaActual * porPagina;
        int hasta = Math.min(desde + porPagina, todosDatos.size());
        modeloTabla.setRowCount(0);

        for(int idx = desde; idx < hasta; idx++) {
            InscripcionCurso i = todosDatos.get(idx);
            modeloTabla.addRow(new Object[]{
                    i.getId(),
                    i.getEstudiante().getId(),
                    i.getEstudiante().getNombre()+ " " +i.getEstudiante().getApellido(),
                    i.getGrupo().getId(),
                    i.getGrupo().getMateria().getNombreMateria(),
                    i.getGrupo().getDocente().getNombre(),
                    i.getGrupo().getAula(),
                    i.getGrupo().getHorario(),
                    i.getNotaFinal() != null ? i.getNotaFinal() : SIN_NOTA,
                    i.getEstado()
            });
        }

        lblPagina.setText("Página " +(paginaActual + 1)+ " de " +totalPaginas);
        btnAnterior.setEnabled(paginaActual > 0);
        btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
    }

    public void cargarInscripciones(List<InscripcionCurso> datos) {
        this.todosDatos = new ArrayList<>(datos);
        this.paginaActual = 0;
        mostrarPaginaActual();
    }

    public int getIdSeleccionado() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) return -1;
        int fila = tabla.convertRowIndexToModel(filaVista);
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public String getIdEstudianteTexto() {
        return txtIdEstudiante.getText().trim();
    }

    public String getIdGrupoTexto() {
        return txtIdGrupo.getText().trim();
    }

    public String getNotaFinalTexto() {
        return txtNotaFinal.getText().trim();
    }

    public String getEstado() {
        return (String) cmbEstado.getSelectedItem();
    }

    public Integer solicitarIdEstudianteParaConsulta() {
        return solicitarEntero("Ingrese el ID del estudiante:");
    }

    public Integer solicitarIdGrupoParaConsulta() {
        return solicitarEntero("Ingrese el ID del grupo:");
    }

    private Integer solicitarEntero(String mensaje) {
        String valor = JOptionPane.showInputDialog(this, mensaje);
        if (valor == null) return null;

        try {
            return Integer.parseInt(valor.trim());
        } catch(NumberFormatException ex) {
            mostrarError("El valor ingresado debe ser un numero entero.");
            return null;
        }
    }

    public void limpiarCampos() {
        txtIdEstudiante.setText("");
        txtIdGrupo.setText("");
        txtNotaFinal.setText("");
        cmbEstado.setSelectedIndex(0);
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

    public void setOnNotasEstudiante(Runnable r) {
        this.onNotasEstudiante = r;
    }

    public void setOnNotasGrupo(Runnable r) {
        this.onNotasGrupo = r;
    }
}