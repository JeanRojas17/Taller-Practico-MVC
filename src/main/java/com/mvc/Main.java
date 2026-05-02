package com.mvc;

import com.mvc.controlador.ControladorEstudiante;
import com.mvc.controlador.ControladorDocente;
import com.mvc.dao.DocenteDao;
import com.mvc.dao.EstudianteDao;
import com.mvc.servicios.DocenteService;
import com.mvc.servicios.EstudianteService;
import com.mvc.vista.VistaDocenteSwing;
import com.mvc.vista.VistaEstudianteSwing;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private JPanel panelContenido;
    private CardLayout cardLayout;

    private JButton btnActivo;

    public static final String CARD_ESTUDIANTES = "Estudiantes";
    public static final String CARD_DOCENTES = "Docentes";
    public static final String CARD_MATERIAS = "Materias";
    public static final String CARD_GRUPOS = "Grupos";
    public static final String CARD_INSCRIPCIONES = "Inscripciones";

    public Main() {
        super("Sistema Académico UNIAJC");
        initComponents();
    }

    private void initComponents() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        JPanel sidebar = buildSidebar();

        registrarPaneles();

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        cardLayout.show(panelContenido, CARD_ESTUDIANTES);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        sidebar.add(buildSidebarHeader(), BorderLayout.NORTH);
        sidebar.add(buildSidebarNav(), BorderLayout.CENTER);
        sidebar.add(buildSidebarFooter(), BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel buildSidebarHeader() {
        JLabel lblTitulo = new JLabel(
                "<html><center>Sistema Académico<br>UNIAJC</center></html>",
                SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 16, 10));
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);

        JSeparator separador = new JSeparator();
        separador.setForeground(Color.LIGHT_GRAY);

        JPanel header = new JPanel(new BorderLayout());
        header.add(panelTitulo, BorderLayout.CENTER);
        header.add(separador,   BorderLayout.SOUTH);

        return header;
    }

    private JPanel buildSidebarNav() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        String[] entidades = {
            CARD_ESTUDIANTES,
            CARD_DOCENTES,
            CARD_MATERIAS,
            CARD_GRUPOS,
            CARD_INSCRIPCIONES
        };

        for(String entidad : entidades) {
            JButton btn = buildBotonNav(entidad);
            nav.add(btn);

            if(entidad.equals(CARD_ESTUDIANTES)) {
                marcarActivo(btn);
            }
        }

        return nav;
    }

    private JPanel buildSidebarFooter() {
        JLabel lblVersion = new JLabel("v1.0.0", SwingConstants.CENTER);
        lblVersion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblVersion.setForeground(Color.GRAY);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 14, 0));
        footer.add(lblVersion, BorderLayout.CENTER);

        return footer;
    }

    private JButton buildBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));

        btn.addActionListener(e -> {
            marcarActivo(btn);
            cardLayout.show(panelContenido, texto);
        });

        return btn;
    }

    private void marcarActivo(JButton btn) {
        if(btnActivo != null) {
            btnActivo.setBackground(UIManager.getColor("Panel.background"));
            btnActivo.setOpaque(false);
            btnActivo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        }

        btnActivo = btn;
        btnActivo.setOpaque(true);
        btnActivo.setBackground(new Color(220, 220, 235));
        btnActivo.setFont(new Font("SansSerif", Font.BOLD, 13));
    }

    private void registrarPaneles() {
        EstudianteDao estudianteDao = new EstudianteDao();
        EstudianteService estudianteService = new EstudianteService(estudianteDao);
        VistaEstudianteSwing vistaEstudiante = new VistaEstudianteSwing();
        new ControladorEstudiante(vistaEstudiante, estudianteService);

        DocenteDao docenteDao = new DocenteDao();
        DocenteService docenteService = new DocenteService(docenteDao);
        VistaDocenteSwing vistaDocente = new VistaDocenteSwing();
        new ControladorDocente(vistaDocente, docenteService);

        panelContenido.add(vistaEstudiante, CARD_ESTUDIANTES);
        panelContenido.add(vistaDocente, CARD_DOCENTES);

        panelContenido.add(buildPlaceholder(CARD_MATERIAS), CARD_MATERIAS);
        panelContenido.add(buildPlaceholder(CARD_GRUPOS), CARD_GRUPOS);
        panelContenido.add(buildPlaceholder(CARD_INSCRIPCIONES), CARD_INSCRIPCIONES);
    }

    private JPanel buildPlaceholder(String nombre) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Sección \"" + nombre + "\" — próximamente", SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 16));
        lbl.setForeground(Color.GRAY);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new Main().setVisible(true);
        });
    }
}