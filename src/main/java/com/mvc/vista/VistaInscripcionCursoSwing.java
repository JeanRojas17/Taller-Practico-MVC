package com.mvc.vista;

import com.mvc.modelo.InscripcionCurso;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.util.List;

public class VistaInscripcionCursoSwing extends JPanel {

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtBuscar;

    private JTextField txtIdEstudiante;
    private JTextField txtIdGrupo;
    private JTextField txtNotaFinal;
    private JComboBox<String> cmbEstado;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JButton btnNotasEstudiante;
    private JButton btnNotasGrupo;
    private JButton btnEliminarEstudianteGrupo;

    private Runnable onRegistrar;
    private Runnable onActualizar;
    private Runnable onEliminar;
    private Runnable onRefrescar;
    private Runnable onNotasEstudiante;
    private Runnable onNotasGrupo;
    private Runnable onEliminarEstudianteGrupo;

    private static final String[] COLUMNAS = {
        "ID", "ID Estudiante", "Estudiante", "ID Grupo", "Materia", "Docente", "Aula", "Horario", "Nota Final", "Estado"
    };

    private static final String PLACEHOLDER_BUSCAR = "🔍 Buscar inscripción...";
    private static final String SIN_NOTA = "Sin nota";

    public VistaInscripcionCursoSwing() {
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
        tabla.getColumnModel().getColumn(1).setPreferredWidth(95);
        tabla.getColumnModel().getColumn(1).setMaxWidth(115);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(75);
        tabla.getColumnModel().getColumn(3).setMaxWidth(95);

        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        txtBuscar = new JTextField(18);
        txtBuscar.setForeground(Color.GRAY);
        txtBuscar.setText(PLACEHOLDER_BUSCAR);

        txtIdEstudiante = new JTextField(8);
        txtIdGrupo = new JTextField(8);
        txtNotaFinal = new JTextField(8);
        cmbEstado = new JComboBox<>(new String[]{"Inscrito", "En curso", "Retirado"});

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnRefrescar = new JButton("Refrescar");
        btnNotasEstudiante = new JButton("Notas estudiante");
        btnNotasGrupo = new JButton("Notas grupo");
        btnEliminarEstudianteGrupo = new JButton("Quitar estudiante");

        add(buildPanelEncabezado(), BorderLayout.NORTH);
        add(buildPanelTabla(), BorderLayout.CENTER);
        add(buildPanelAcciones(), BorderLayout.SOUTH);

        initEventos();
    }

    private JPanel buildPanelEncabezado() {
        JLabel lblTitulo = new JLabel("Gestión de Inscripciones");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel lblSubtitulo = new JLabel("Administra estudiantes inscritos en grupos, notas y estados");
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
        formulario.add(new JLabel("ID Estudiante:"));
        formulario.add(txtIdEstudiante);
        formulario.add(new JLabel("ID Grupo:"));
        formulario.add(txtIdGrupo);
        formulario.add(new JLabel("Nota Final:"));
        formulario.add(txtNotaFinal);
        formulario.add(new JLabel("Estado:"));
        formulario.add(cmbEstado);

        JPanel botonesIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botonesIzq.add(btnRegistrar);
        botonesIzq.add(btnActualizar);
        botonesIzq.add(btnEliminar);
        botonesIzq.add(btnNotasEstudiante);
        botonesIzq.add(btnNotasGrupo);
        botonesIzq.add(btnEliminarEstudianteGrupo);

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
            @Override public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override public void removeUpdate(DocumentEvent e) {
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
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" +texto));
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

        btnNotasEstudiante.addActionListener(e -> {
            if(onNotasEstudiante != null) onNotasEstudiante.run();
        });

        btnNotasGrupo.addActionListener(e -> {
            if(onNotasGrupo != null) onNotasGrupo.run();
        });

        btnEliminarEstudianteGrupo.addActionListener(e -> {
            if(onEliminarEstudianteGrupo != null) onEliminarEstudianteGrupo.run();
        });
    }

    private void precargarCamposDesdeSeleccion() {
        int filaVista = tabla.getSelectedRow();
        if(filaVista < 0) return;
        int fila = tabla.convertRowIndexToModel(filaVista);

        txtIdEstudiante.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtIdGrupo.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));

        Object nota = modeloTabla.getValueAt(fila, 8);
        txtNotaFinal.setText(SIN_NOTA.equals(nota) ? "" : String.valueOf(nota));
        cmbEstado.setSelectedItem((String) modeloTabla.getValueAt(fila, 9));
    }

    public void cargarInscripciones(List<InscripcionCurso> inscripciones) {
        modeloTabla.setRowCount(0);

        for(InscripcionCurso i : inscripciones) {
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
    }

    public int getIdSeleccionado() {
        int filaVista = tabla.getSelectedRow();
        if(filaVista < 0) return -1;
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

    public int[] solicitarDatosParaEliminarEstudianteDeGrupo() {
        JTextField txtEstudiante = new JTextField(10);
        JTextField txtGrupo = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.add(new JLabel("ID Estudiante:"));
        panel.add(txtEstudiante);
        panel.add(new JLabel("ID Grupo:"));
        panel.add(txtGrupo);

        int opcion = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Quitar estudiante de grupo",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if(opcion != JOptionPane.OK_OPTION) return null;

        try {
            return new int[]{
                Integer.parseInt(txtEstudiante.getText().trim()),
                Integer.parseInt(txtGrupo.getText().trim())
            };
        } catch(NumberFormatException ex) {
            mostrarError("Los IDs deben ser números enteros.");
            return null;
        }
    }

    private Integer solicitarEntero(String mensaje) {
        String valor = JOptionPane.showInputDialog(this, mensaje);
        if(valor == null) return null;

        try {
            return Integer.parseInt(valor.trim());
        } catch(NumberFormatException ex) {
            mostrarError("El valor ingresado debe ser un número entero.");
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

    public void setOnNotasEstudiante(Runnable r) {
        this.onNotasEstudiante = r;
    }

    public void setOnNotasGrupo(Runnable r) {
        this.onNotasGrupo = r;
    }

    public void setOnEliminarEstudianteGrupo(Runnable r) {
        this.onEliminarEstudianteGrupo = r;
    }
}