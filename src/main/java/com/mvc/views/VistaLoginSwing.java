package com.mvc.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.function.BiConsumer;

public class VistaLoginSwing extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnCancelar;

    private BiConsumer<String, String> onIngresar;

    private static final String PLACEHOLDER_USUARIO = "Ingresa tu usuario";
    private static final String PLACEHOLDER_PASSWORD = "Ingresa tu contraseña";

    private static final Color COLOR_FONDO = new Color(242, 245, 249);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_TITULO = new Color(31, 58, 147);
    private static final Color COLOR_SUBTITULO = new Color(96, 125, 139);
    private static final Color COLOR_BTN_INGRESO = new Color(41, 128, 185);
    private static final Color COLOR_BTN_CANCEL = new Color(158, 158, 158);
    private static final Color COLOR_BORDE = new Color(210, 215, 220);

    public VistaLoginSwing() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema Académico UNIAJC — Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(440, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());

        add(buildCard(), new GridBagConstraints());
    }

    private JPanel buildCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1), BorderFactory.createEmptyBorder(36, 40, 32, 40)));
        card.setMaximumSize(new Dimension(360, 420));

        JLabel lblIcono = new JLabel("🎓", SwingConstants.CENTER);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 48));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_TITULO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Sistema Académico UNIAJC", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(COLOR_SUBTITULO);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(COLOR_BORDE);

        JPanel camposPanel = new JPanel();
        camposPanel.setLayout(new BoxLayout(camposPanel, BoxLayout.Y_AXIS));
        camposPanel.setBackground(COLOR_TARJETA);
        camposPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        camposPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtUsuario.setForeground(Color.GRAY);
        txtUsuario.setText(PLACEHOLDER_USUARIO);
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_BORDE), BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        applyPlaceholder(txtUsuario, PLACEHOLDER_USUARIO);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setForeground(Color.GRAY);
        txtPassword.setEchoChar((char) 0);
        txtPassword.setText(PLACEHOLDER_PASSWORD);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(COLOR_BORDE), BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        applyPlaceholderPassword(txtPassword, PLACEHOLDER_PASSWORD);

        JLabel lblUsuario = buildLabel("Usuario");
        JLabel lblPassword = buildLabel("Contraseña");

        camposPanel.add(lblUsuario);
        camposPanel.add(Box.createVerticalStrut(4));
        camposPanel.add(txtUsuario);
        camposPanel.add(Box.createVerticalStrut(14));
        camposPanel.add(lblPassword);
        camposPanel.add(Box.createVerticalStrut(4));
        camposPanel.add(txtPassword);

        btnIngresar = buildBoton("Ingresar", COLOR_BTN_INGRESO);
        btnCancelar = buildBoton("Cancelar", COLOR_BTN_CANCEL);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botonesPanel.setBackground(COLOR_TARJETA);
        botonesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonesPanel.add(btnIngresar);
        botonesPanel.add(btnCancelar);

        card.add(lblIcono);
        card.add(Box.createVerticalStrut(10));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSubtitulo);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));
        card.add(camposPanel);
        card.add(Box.createVerticalStrut(40));
        card.add(botonesPanel);

        btnIngresar.addActionListener(e -> disparaIngreso());
        btnCancelar.addActionListener(e -> System.exit(0));

        txtUsuario.addActionListener(e -> disparaIngreso());
        txtPassword.addActionListener(e -> disparaIngreso());

        return card;
    }

    private JLabel buildLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 80, 100));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton buildBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }

    private void applyPlaceholder(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isBlank()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void applyPlaceholderPassword(JPasswordField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void disparaIngreso() {
        String usuario = txtUsuario.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();

        if (usuario.equals(PLACEHOLDER_USUARIO)) usuario = "";
        if (password.equals(PLACEHOLDER_PASSWORD)) password = "";

        if (onIngresar != null) {
            onIngresar.accept(usuario, password);
        }
    }

    public void setOnIngresar(BiConsumer<String, String> handler) {
        this.onIngresar = handler;
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de autenticación", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarCampos() {
        txtUsuario.setForeground(Color.GRAY);
        txtUsuario.setText(PLACEHOLDER_USUARIO);
        txtPassword.setForeground(Color.GRAY);
        txtPassword.setEchoChar((char) 0);
        txtPassword.setText(PLACEHOLDER_PASSWORD);
    }
}