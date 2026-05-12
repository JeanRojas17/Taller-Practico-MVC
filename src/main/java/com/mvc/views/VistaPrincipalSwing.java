package com.mvc.views;

import com.mvc.controllers.*;
import com.mvc.dao.*;
import com.mvc.services.*;

import javax.swing.*;
import java.awt.*;

public class VistaPrincipalSwing extends JFrame {

    private static final String CARD_INICIO = "Inicio";
    private static final String CARD_ESTUDIANTES = "Estudiantes";
    private static final String CARD_DOCENTES = "Docentes";
    private static final String CARD_MATERIAS = "Materias";
    private static final String CARD_GRUPOS = "Grupos";
    private static final String CARD_INSCRIPCIONES = "Inscripciones";

    private CardLayout cardLayout;
    private JPanel panelContenido;

    public VistaPrincipalSwing() {
        super("Sistema Académico UNIAJC");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 700);
        setMinimumSize(new Dimension(940, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setJMenuBar(buildMenuBar());
        panelContenido = new JPanel(cardLayout = new CardLayout());
        panelContenido.setBackground(new Color(242, 245, 249));

        registrarPaneles();

        add(buildHeader(), BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);

        cardLayout.show(panelContenido, CARD_INICIO);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(41, 128, 185));
        menuBar.setOpaque(true);
        menuBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(Color.WHITE);
        menuArchivo.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JMenuItem itemInicio = buildMenuItem("Inicio", CARD_INICIO);
        JMenuItem itemConfiguracion = new JMenuItem("Configuración");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemConfiguracion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemConfiguracion.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad de configuración en desarrollo.",
                "Configuración",
                JOptionPane.INFORMATION_MESSAGE));

        itemSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemInicio);
        menuArchivo.add(itemConfiguracion);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        JMenu menuGestion = new JMenu("Gestión");
        menuGestion.setForeground(Color.WHITE);
        menuGestion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menuGestion.add(buildMenuItem("Estudiantes", CARD_ESTUDIANTES));
        menuGestion.add(buildMenuItem("Docentes", CARD_DOCENTES));
        menuGestion.add(buildMenuItem("Materias", CARD_MATERIAS));
        menuGestion.add(buildMenuItem("Grupos", CARD_GRUPOS));
        menuGestion.add(buildMenuItem("Inscripciones", CARD_INSCRIPCIONES));

        menuBar.add(menuArchivo);
        menuBar.add(menuGestion);
        menuBar.add(Box.createHorizontalGlue());

        return menuBar;
    }

    private JMenuItem buildMenuItem(String text, String cardName) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.addActionListener(e -> cardLayout.show(panelContenido, cardName));
        return item;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(236, 240, 241));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)));

        JLabel title = new JLabel("Panel Principal");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(31, 58, 147));

        JLabel subtitle = new JLabel("Bienvenido al Sistema Académico de UNIAJC. Selecciona una opción en el menú para comenzar.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(96, 125, 139));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(236, 240, 241));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(6));
        textPanel.add(subtitle);

        header.add(textPanel, BorderLayout.WEST);

        return header;
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(242, 245, 249));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Bienvenido al Sistema Académico UNIAJC");
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(new Color(31, 58, 147));

        JLabel subtitle = new JLabel("Utiliza el menú para gestionar estudiantes, docentes, materias, grupos e inscripciones.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(96, 125, 139));

        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(Box.createVerticalStrut(12), gbc);
        gbc.gridy++;
        panel.add(subtitle, gbc);

        return panel;
    }

    private void registrarPaneles() {
        panelContenido.add(crearPanelInicio(), CARD_INICIO);

        EstudianteDao estudianteDao = new EstudianteDao();
        EstudianteService estudianteService = new EstudianteService(estudianteDao);
        VistaEstudianteSwing vistaEstudiante = new VistaEstudianteSwing();
        new ControladorEstudiante(vistaEstudiante, estudianteService);

        DocenteDao docenteDao = new DocenteDao();
        DocenteService docenteService = new DocenteService(docenteDao);
        VistaDocenteSwing vistaDocente = new VistaDocenteSwing();
        new ControladorDocente(vistaDocente, docenteService);

        MateriaDao materiaDao = new MateriaDao();
        MateriaService materiaService = new MateriaService(materiaDao);
        VistaMateriaSwing vistaMateria = new VistaMateriaSwing();
        new ControladorMateria(vistaMateria, materiaService);

        GrupoDao grupoDao = new GrupoDao();
        GrupoService grupoService = new GrupoService(grupoDao);
        VistaGrupoSwing vistaGrupo = new VistaGrupoSwing();
        new ControladorGrupo(vistaGrupo, grupoService, materiaService, docenteService);

        InscripcionCursoDao inscripcionCursoDao = new InscripcionCursoDao();
        InscripcionCursoService inscripcionCursoService = new InscripcionCursoService(inscripcionCursoDao);
        VistaInscripcionCursoSwing vistaInscripcionCurso = new VistaInscripcionCursoSwing();
        new ControladorInscripcionCurso(vistaInscripcionCurso, inscripcionCursoService, estudianteService, grupoService);

        panelContenido.add(vistaEstudiante, CARD_ESTUDIANTES);
        panelContenido.add(vistaDocente, CARD_DOCENTES);
        panelContenido.add(vistaMateria, CARD_MATERIAS);
        panelContenido.add(vistaGrupo, CARD_GRUPOS);
        panelContenido.add(vistaInscripcionCurso, CARD_INSCRIPCIONES);
    }
}