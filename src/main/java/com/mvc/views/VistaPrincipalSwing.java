package com.mvc.views;

import com.mvc.controllers.*;
import com.mvc.dao.*;
import com.mvc.models.Usuario;
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
    private JPanel headerPanel;

    private final JPanel panelInicioPrueba;
    private final JPanel panelEstudiantesPrueba;
    private final JPanel panelDocentesPrueba;
    private final JPanel panelMateriasPrueba;
    private final JPanel panelGruposPrueba;
    private final JPanel panelInscripcionesPrueba;

    private final Usuario usuarioActual;

    public VistaPrincipalSwing() {
        this(null, null, null, null, null, null);
    }

    public VistaPrincipalSwing(Usuario usuario) {
        super("Sistema Académico UNIAJC");
        this.usuarioActual = usuario;
        this.panelInicioPrueba = null;
        this.panelEstudiantesPrueba = null;
        this.panelDocentesPrueba = null;
        this.panelMateriasPrueba = null;
        this.panelGruposPrueba = null;
        this.panelInscripcionesPrueba = null;
        initComponents();
    }

    VistaPrincipalSwing(JPanel panelInicio, JPanel panelEstudiantes, JPanel panelDocentes,
                        JPanel panelMaterias, JPanel panelGrupos, JPanel panelInscripciones) {
        super("Sistema Académico UNIAJC");
        this.usuarioActual = null;
        this.panelInicioPrueba = panelInicio;
        this.panelEstudiantesPrueba = panelEstudiantes;
        this.panelDocentesPrueba = panelDocentes;
        this.panelMateriasPrueba = panelMaterias;
        this.panelGruposPrueba = panelGrupos;
        this.panelInscripcionesPrueba = panelInscripciones;
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

        headerPanel = buildHeader();
        add(headerPanel, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);

        showCard(CARD_INICIO);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(41, 128, 185));
        menuBar.setOpaque(true);
        menuBar.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JMenu menuArchivo = createTopMenu("Archivo");
        JMenuItem itemInicio = buildMenuItem("Inicio", CARD_INICIO);
        JMenuItem itemConfiguracion = createMenuItem("Configuración");
        JMenuItem itemCerrarSesion  = createMenuItem("Cerrar sesión");
        JMenuItem itemSalir = createMenuItem("Salir");

        itemConfiguracion.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad de configuración en desarrollo.",
                "Configuración",
                JOptionPane.INFORMATION_MESSAGE));
        itemCerrarSesion.addActionListener(e -> cerrarSesion());
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemInicio);
        menuArchivo.add(itemConfiguracion);
        menuArchivo.add(itemCerrarSesion);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        menuBar.add(menuArchivo);
        menuBar.add(Box.createHorizontalStrut(3));
        menuBar.add(createTopMenu("Estudiante", "Gestión de Estudiantes", CARD_ESTUDIANTES));
        menuBar.add(Box.createHorizontalStrut(3));
        menuBar.add(createTopMenu("Docente", "Gestión de Docentes", CARD_DOCENTES));
        menuBar.add(Box.createHorizontalStrut(3));
        menuBar.add(createTopMenu("Materia", "Gestión de Materias", CARD_MATERIAS));
        menuBar.add(Box.createHorizontalStrut(3));
        menuBar.add(createTopMenu("Grupo", "Gestión de Grupos", CARD_GRUPOS));
        menuBar.add(Box.createHorizontalStrut(3));
        menuBar.add(createTopMenu("Inscripción", "Gestión de Inscripciones", CARD_INSCRIPCIONES));

        if(usuarioActual != null) {
            menuBar.add(Box.createHorizontalGlue());
            JLabel lblRol = new JLabel(usuarioActual.getUsername()+ "  ·  " +usuarioActual.getRol()+ "  ");
            lblRol.setForeground(Color.WHITE);
            lblRol.setFont(new Font("Segoe UI", Font.BOLD, 12));
            menuBar.add(lblRol);
        }

        return menuBar;
    }

    private JMenu createTopMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setBackground(new Color(41, 128, 185));
        menu.setOpaque(true);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menu.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return menu;
    }

    private JMenu createTopMenu(String text, String itemText, String cardName) {
        JMenu menu = createTopMenu(text);
        JMenuItem item = buildMenuItem(itemText, cardName);
        menu.add(item);
        return menu;
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.setBackground(Color.WHITE);
        item.setForeground(Color.BLACK);
        return item;
    }

    private JMenuItem buildMenuItem(String text, String cardName) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.addActionListener(e -> showCard(cardName));
        return item;
    }

    private void showCard(String cardName) {
        cardLayout.show(panelContenido, cardName);
        headerPanel.setVisible(CARD_INICIO.equals(cardName));
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

        JLabel subtitle = new JLabel(usuarioActual != null
            ? "Bienvenido, " +usuarioActual.getUsername()+ " (" +usuarioActual.getRol()+ "). Selecciona una opción en el menú para comenzar."
            : "Bienvenido al Sistema Académico de UNIAJC. Selecciona una opción en el menú para comenzar.");
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
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Bienvenido al Sistema Académico UNIAJC");
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(new Color(31, 58, 147));

        JLabel subtitle = new JLabel("Utiliza el menú para gestionar estudiantes, docentes, materias, grupos e inscripciones.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(96, 125, 139));

        JLabel versionLabel = new JLabel("Versión 1.0");
        versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        versionLabel.setForeground(new Color(129, 140, 150));

        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(subtitle, gbc);
        gbc.gridy++;
        panel.add(versionLabel, gbc);

        return panel;
    }

    private void registrarPaneles() {
        if(panelInicioPrueba != null) {
            panelContenido.add(panelInicioPrueba, CARD_INICIO);
            panelContenido.add(panelEstudiantesPrueba, CARD_ESTUDIANTES);
            panelContenido.add(panelDocentesPrueba, CARD_DOCENTES);
            panelContenido.add(panelMateriasPrueba, CARD_MATERIAS);
            panelContenido.add(panelGruposPrueba, CARD_GRUPOS);
            panelContenido.add(panelInscripcionesPrueba, CARD_INSCRIPCIONES);
            return;
        }

        boolean esAdmin = usuarioActual == null || usuarioActual.esAdministrador();

        panelContenido.add(crearPanelInicio(), CARD_INICIO);

        EstudianteDao estudianteDao = new EstudianteDao();
        EstudianteService estudianteService = new EstudianteService(estudianteDao);
        VistaEstudianteSwing vistaEstudiante = new VistaEstudianteSwing();
        new ControladorEstudiante(vistaEstudiante, estudianteService);
        if (!esAdmin) vistaEstudiante.ocultarBotonEliminar();

        DocenteDao docenteDao = new DocenteDao();
        DocenteService docenteService = new DocenteService(docenteDao);
        VistaDocenteSwing vistaDocente = new VistaDocenteSwing();
        new ControladorDocente(vistaDocente, docenteService);
        if (!esAdmin) vistaDocente.ocultarBotonEliminar();

        MateriaDao materiaDao = new MateriaDao();
        MateriaService materiaService = new MateriaService(materiaDao);
        VistaMateriaSwing vistaMateria = new VistaMateriaSwing();
        new ControladorMateria(vistaMateria, materiaService);
        if (!esAdmin) vistaMateria.ocultarBotonEliminar();

        GrupoDao grupoDao = new GrupoDao();
        GrupoService grupoService = new GrupoService(grupoDao);
        VistaGrupoSwing vistaGrupo = new VistaGrupoSwing();
        new ControladorGrupo(vistaGrupo, grupoService, materiaService, docenteService);
        if (!esAdmin) vistaGrupo.ocultarBotonEliminar();

        InscripcionCursoDao inscripcionCursoDao = new InscripcionCursoDao();
        InscripcionCursoService inscripcionCursoService = new InscripcionCursoService(inscripcionCursoDao);
        VistaInscripcionCursoSwing vistaInscripcionCurso = new VistaInscripcionCursoSwing();
        new ControladorInscripcionCurso(vistaInscripcionCurso, inscripcionCursoService, estudianteService, grupoService);
        if (!esAdmin) vistaInscripcionCurso.ocultarBotonEliminar();

        panelContenido.add(vistaEstudiante, CARD_ESTUDIANTES);
        panelContenido.add(vistaDocente, CARD_DOCENTES);
        panelContenido.add(vistaMateria, CARD_MATERIAS);
        panelContenido.add(vistaGrupo, CARD_GRUPOS);
        panelContenido.add(vistaInscripcionCurso, CARD_INSCRIPCIONES);
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas cerrar sesión?",
            "Cerrar sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (opcion != JOptionPane.YES_OPTION) return;

        dispose();

        SwingUtilities.invokeLater(() -> {
            com.mvc.dao.UsuarioDao usuarioDao = new com.mvc.dao.UsuarioDao();
            com.mvc.services.UsuarioService usuarioService = new com.mvc.services.UsuarioService(usuarioDao);
            VistaLoginSwing vistaLogin = new VistaLoginSwing();
            new com.mvc.controllers.ControladorLogin(vistaLogin, usuarioService);
            vistaLogin.setVisible(true);
        });
    }
}