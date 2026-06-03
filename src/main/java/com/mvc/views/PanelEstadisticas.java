package com.mvc.views;

import com.mvc.models.Auditoria;
import com.mvc.services.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanelEstadisticas extends JPanel {

    private static final Color FONDO = new Color(242, 245, 249);
    private static final Color COLOR_AZUL = new Color(41, 128, 185);
    private static final Color COLOR_VERDE = new Color(39, 174, 96);
    private static final Color COLOR_NARANJA = new Color(230, 126, 34);
    private static final Color COLOR_MORADO = new Color(142, 68, 173);
    private static final Color COLOR_ROJO = new Color(192, 57, 43);

    private String username;

    private JLabel lblValorEstudiantes;
    private JLabel lblValorDocentes;
    private JLabel lblValorMaterias;
    private JLabel lblValorGrupos;
    private JLabel lblValorPromedio;
    private DefaultTableModel modeloAuditoria;
    private JLabel lblSubtitulo;

    public PanelEstadisticas(String username) {
        this.username = username;
        setBackground(FONDO);
        setLayout(new BorderLayout(0, 0));
        construir();
    }

    public void refrescar() {
        actualizarTarjetas();
        actualizarTablaAuditoria();
    }

    public void actualizarUsername(String nuevoUsername) {
        this.username = nuevoUsername;
        if(lblSubtitulo != null) {
            String saludo = nuevoUsername != null
                ? "Bienvenido, " +nuevoUsername+ ". Aquí tienes un resumen del sistema."
                : "Resumen general del sistema.";
            lblSubtitulo.setText(saludo);
        }
    }

    private void construir() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(236, 240, 241));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            new EmptyBorder(18, 22, 18, 22)
        ));

        JLabel titulo = new JLabel("Panel de Estadísticas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(31, 58, 147));

        String saludo = username != null
            ? "Bienvenido, " +username+ ". Aquí tienes un resumen del sistema."
            : "Resumen general del sistema.";
        lblSubtitulo = new JLabel(saludo);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(96, 125, 139));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(new Color(236, 240, 241));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(lblSubtitulo);
        header.add(textos, BorderLayout.WEST);

        JPanel cuerpo = new JPanel();
        cuerpo.setBackground(FONDO);
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBorder(new EmptyBorder(20, 24, 20, 24));

        cuerpo.add(buildFilaTarjetas());
        cuerpo.add(Box.createVerticalStrut(24));
        cuerpo.add(buildSeccionAuditoria());

        JScrollPane scroll = new JScrollPane(cuerpo);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildFilaTarjetas() {
        JPanel fila = new JPanel(new GridLayout(1, 5, 14, 0));
        fila.setBackground(FONDO);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel[] tarjetas = buildTarjetas();
        for (JPanel t : tarjetas) fila.add(t);

        actualizarTarjetas();

        return fila;
    }

    private JPanel[] buildTarjetas() {
        lblValorEstudiantes = crearLblValor();
        lblValorDocentes = crearLblValor();
        lblValorMaterias = crearLblValor();
        lblValorGrupos = crearLblValor();
        lblValorPromedio = crearLblValor();

        return new JPanel[] {
            buildTarjeta("Estudiantes", COLOR_AZUL, lblValorEstudiantes),
            buildTarjeta("Docentes", COLOR_VERDE, lblValorDocentes),
            buildTarjeta("Materias", COLOR_NARANJA, lblValorMaterias),
            buildTarjeta("Grupos", COLOR_MORADO, lblValorGrupos),
            buildTarjeta("Promedio notas", COLOR_ROJO, lblValorPromedio)
        };
    }

    private JLabel crearLblValor() {
        JLabel lbl = new JLabel("...");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lbl.setForeground(new Color(30, 40, 60));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel buildTarjeta(String etiqueta, Color color, JLabel lblValor) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblIcono = new JLabel(etiqueta);
        lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblIcono.setForeground(color);
        lblIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblIcono);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblValor);
        return tarjeta;
    }

    private void actualizarTarjetas() {
        int totalEstudiantes = contar("estudiante");
        int totalDocentes = contar("docente");
        int totalMaterias = contar("materia");
        int totalGrupos = contar("grupo");
        double promedio = obtenerPromedioNotas();

        SwingUtilities.invokeLater(() -> {
            lblValorEstudiantes.setText(String.valueOf(totalEstudiantes));
            lblValorDocentes.setText(String.valueOf(totalDocentes));
            lblValorMaterias.setText(String.valueOf(totalMaterias));
            lblValorGrupos.setText(String.valueOf(totalGrupos));
            lblValorPromedio.setText(promedio > 0 ? String.format("%.2f", promedio) : "N/A");
        });
    }

    private JPanel buildSeccionAuditoria() {
        JPanel seccion = new JPanel(new BorderLayout(0, 8));
        seccion.setBackground(FONDO);
        seccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lblTitulo = new JLabel("Historial de actividad reciente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(31, 58, 147));

        String[] columnas = {"Fecha y hora", "Usuario", "Acción", "Entidad", "Descripción"};
        modeloAuditoria = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloAuditoria);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(140);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(320);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220)));
        scroll.setPreferredSize(new Dimension(0, 280));

        seccion.add(lblTitulo, BorderLayout.NORTH);
        seccion.add(scroll, BorderLayout.CENTER);

        actualizarTablaAuditoria();

        return seccion;
    }

    private void actualizarTablaAuditoria() {
        List<Auditoria> registros = AuditoriaService.getInstance().listarTodas();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        SwingUtilities.invokeLater(() -> {
            modeloAuditoria.setRowCount(0);
            registros.stream().limit(50).forEach(a -> modeloAuditoria.addRow(new Object[]{
                a.getFechaHora() != null ? a.getFechaHora().format(fmt) : "-",
                a.getUsuario(),
                a.getAccion(),
                a.getEntidad(),
                a.getDescripcion()
            }));
        });
    }

    private int contar(String tabla) {
        String sql = "SELECT COUNT(*) FROM \"practica-mvc\"." +tabla+ ";";

        try(var conn = com.mvc.config.ConexionPostgreSQLDatabase.getConnection();
             var st = conn.prepareStatement(sql);
             var rs = st.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private double obtenerPromedioNotas() {
        String sql = "SELECT AVG(nota_final) FROM \"practica-mvc\".inscripcion_curso WHERE nota_final IS NOT NULL;";

        try(var conn = com.mvc.config.ConexionPostgreSQLDatabase.getConnection();
             var st   = conn.prepareStatement(sql);
             var rs   = st.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}