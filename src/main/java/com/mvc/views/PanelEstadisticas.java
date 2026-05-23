package com.mvc.views;

import com.mvc.dao.*;
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

    private final String username;

    public PanelEstadisticas(String username) {
        this.username = username;
        setBackground(FONDO);
        setLayout(new BorderLayout(0, 0));
        construir();
    }

    private void construir() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(236, 240, 241));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            new EmptyBorder(18, 22, 18, 22)
        ));

        JLabel titulo = new JLabel("Panel de estadísticas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(31, 58, 147));

        String saludo = username != null
            ? "Bienvenido, " +username+ ". Aquí tienes un resumen del sistema."
            : "Resumen general del sistema.";
        JLabel subtitulo = new JLabel(saludo);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(96, 125, 139));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(new Color(236, 240, 241));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(subtitulo);
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
        int totalEstudiantes = contar("estudiante");
        int totalDocentes = contar("docente");
        int totalMaterias = contar("materia");
        int totalGrupos = contar("grupo");
        double promedioNotas = obtenerPromedioNotas();

        JPanel fila = new JPanel(new GridLayout(1, 5, 14, 0));
        fila.setBackground(FONDO);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);

        fila.add(buildTarjeta("Estudiantes", String.valueOf(totalEstudiantes), "👨‍🎓", COLOR_AZUL));
        fila.add(buildTarjeta("Docentes", String.valueOf(totalDocentes), "👨‍🏫", COLOR_VERDE));
        fila.add(buildTarjeta("Materias", String.valueOf(totalMaterias), "📚", COLOR_NARANJA));
        fila.add(buildTarjeta("Grupos", String.valueOf(totalGrupos), "🏫", COLOR_MORADO));
        fila.add(buildTarjeta("Promedio notas", promedioNotas > 0 ? String.format("%.2f", promedioNotas) : "N/A", "📊", COLOR_ROJO));

        return fila;
    }

    private JPanel buildTarjeta(String etiqueta, String valor, String icono, Color color) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblIcono = new JLabel(icono+ "  " +etiqueta);
        lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblIcono.setForeground(color);
        lblIcono.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblValor.setForeground(new Color(30, 40, 60));
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblIcono);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblValor);

        return tarjeta;
    }

    private JPanel buildSeccionAuditoria() {
        JPanel seccion = new JPanel(new BorderLayout(0, 8));
        seccion.setBackground(FONDO);
        seccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lblTitulo = new JLabel("📋 Historial de actividad reciente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(new Color(31, 58, 147));

        String[] columnas = {"Fecha y hora", "Usuario", "Acción", "Entidad", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        List<Auditoria> registros = AuditoriaService.getInstance().listarTodas();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        registros.stream().limit(50).forEach(a -> modelo.addRow(new Object[]{
            a.getFechaHora() != null ? a.getFechaHora().format(fmt) : "-",
            a.getUsuario(),
            a.getAccion(),
            a.getEntidad(),
            a.getDescripcion()
        }));

        JTable tabla = new JTable(modelo);
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

        return seccion;
    }

    private int contar(String tabla) {
        String sql = "SELECT COUNT(*) FROM \"practica-mvc\"." +tabla+ ";";

        try(var conn = com.mvc.config.ConexionPostgresDatabase.getConnection();
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

        try(var conn = com.mvc.config.ConexionPostgresDatabase.getConnection();
             var st   = conn.prepareStatement(sql);
             var rs   = st.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}