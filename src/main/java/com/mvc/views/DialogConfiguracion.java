package com.mvc.views;

import com.mvc.config.ConfiguracionApp;
import com.mvc.models.Usuario;
import com.mvc.services.UsuarioService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

public class DialogConfiguracion extends JDialog {

    private final Usuario usuarioActual;
    private final UsuarioService usuarioService;
    private final java.util.function.Consumer<String> onUsernameActualizado;

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_FONDO = new Color(242, 245, 249);
    private static final Color COLOR_BORDE = new Color(210, 215, 220);

    public DialogConfiguracion(JFrame parent, Usuario usuarioActual, UsuarioService usuarioService) {
        this(parent, usuarioActual, usuarioService, null);
    }

    public DialogConfiguracion(JFrame parent, Usuario usuarioActual, UsuarioService usuarioService, java.util.function.Consumer<String> onUsernameActualizado) {
        super(parent, "Configuración", true);
        this.usuarioActual = usuarioActual;
        this.usuarioService = usuarioService;
        this.onUsernameActualizado = onUsernameActualizado;
        initComponents();
    }

    private void initComponents() {
        setSize(560, 520);
        setResizable(false);
        setLocationRelativeTo(getOwner());
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(COLOR_FONDO);

        tabs.addTab("Mi perfil", buildTabPerfil());
        tabs.addTab("Preferencias", buildTabPreferencias());

        if (usuarioActual != null && usuarioActual.esAdministrador()) {
            tabs.addTab("Gestión de usuarios", buildTabGestionUsuarios());
        }

        add(tabs, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildTabPerfil() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel panelUsername = buildSection("Cambiar nombre de usuario");

        JTextField txtNuevoUsername = buildField();
        JButton btnCambiarUsername = buildBoton("Guardar", COLOR_PRIMARIO);

        panelUsername.add(buildLabel("Nuevo nombre de usuario"));
        panelUsername.add(Box.createVerticalStrut(4));
        panelUsername.add(txtNuevoUsername);
        panelUsername.add(Box.createVerticalStrut(10));
        panelUsername.add(wrapCenter(btnCambiarUsername));

        btnCambiarUsername.addActionListener(e -> {
            String nuevo = txtNuevoUsername.getText().trim();

            try {
                usuarioService.cambiarUsername(usuarioActual.getId(), usuarioActual.getUsername(), nuevo);
                usuarioActual.setUsername(nuevo);
                txtNuevoUsername.setText("");
                if (onUsernameActualizado != null) onUsernameActualizado.accept(nuevo);
                JOptionPane.showMessageDialog(this,
                    "Nombre de usuario actualizado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelPassword = buildSection("Cambiar contraseña");

        JPasswordField txtActual = buildPasswordField();
        JPasswordField txtNueva = buildPasswordField();
        JPasswordField txtConfirm = buildPasswordField();
        JButton btnCambiarPassword = buildBoton("Guardar contraseña", COLOR_PRIMARIO);
        btnCambiarPassword.setPreferredSize(new Dimension(170, 32));

        panelPassword.add(buildLabel("Contraseña actual"));
        panelPassword.add(Box.createVerticalStrut(4));
        panelPassword.add(txtActual);
        panelPassword.add(Box.createVerticalStrut(10));
        panelPassword.add(buildLabel("Nueva contraseña"));
        panelPassword.add(Box.createVerticalStrut(4));
        panelPassword.add(txtNueva);
        panelPassword.add(Box.createVerticalStrut(10));
        panelPassword.add(buildLabel("Confirmar nueva contraseña"));
        panelPassword.add(Box.createVerticalStrut(4));
        panelPassword.add(txtConfirm);
        panelPassword.add(Box.createVerticalStrut(10));
        panelPassword.add(wrapCenter(btnCambiarPassword));

        btnCambiarPassword.addActionListener(e -> {
            String actual = new String(txtActual.getPassword());
            String nueva = new String(txtNueva.getPassword());
            String confirm = new String(txtConfirm.getPassword());
            
            try {
                usuarioService.cambiarPassword(usuarioActual.getId(), actual, nueva, confirm);
                txtActual.setText("");
                txtNueva.setText("");
                txtConfirm.setText("");
                JOptionPane.showMessageDialog(this,
                    "Contraseña actualizada correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(panelUsername);
        panel.add(Box.createVerticalStrut(14));
        panel.add(panelPassword);
        return panel;
    }

    private JPanel buildTabPreferencias() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        // Sección: Comportamiento al eliminar
        JPanel seccionEliminar = buildSection("Comportamiento al eliminar");

        JCheckBox chkConfirmar = new JCheckBox("Solicitar confirmación antes de eliminar un registro");
        chkConfirmar.setSelected(ConfiguracionApp.getInstance().isConfirmarEliminacion());
        chkConfirmar.setBackground(COLOR_FONDO);
        chkConfirmar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkConfirmar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescEliminar = new JLabel(
            "<html><i style='color:#888'>Cuando está activo, se pedirá confirmación antes de cada eliminación.</i></html>"
        );
        lblDescEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescEliminar.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescEliminar.setBorder(BorderFactory.createEmptyBorder(4, 22, 0, 0));

        chkConfirmar.addActionListener(e ->
            ConfiguracionApp.getInstance().setConfirmarEliminacion(chkConfirmar.isSelected())
        );

        seccionEliminar.add(chkConfirmar);
        seccionEliminar.add(lblDescEliminar);

        // Sección: Paginación de tablas
        JPanel seccionPaginacion = buildSection("Paginación de tablas");

        JLabel lblRegistros = new JLabel("Registros por página:");
        lblRegistros.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblRegistros.setAlignmentX(Component.LEFT_ALIGNMENT);

        Integer[] opciones = {5, 10, 15, 20, 25};
        JComboBox<Integer> cmbRegistros = new JComboBox<>(opciones);
        cmbRegistros.setSelectedItem(ConfiguracionApp.getInstance().getRegistrosPorPagina());
        cmbRegistros.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbRegistros.setMaximumSize(new Dimension(100, 32));
        cmbRegistros.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescPaginacion = new JLabel(
            "<html><i style='color:#888'>Define cuántos registros se muestran por página en cada gestión.</i></html>"
        );
        lblDescPaginacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescPaginacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescPaginacion.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        cmbRegistros.addActionListener(e -> {
            int seleccion = (int) cmbRegistros.getSelectedItem();
            ConfiguracionApp.getInstance().setRegistrosPorPagina(seleccion);
        });

        seccionPaginacion.add(lblRegistros);
        seccionPaginacion.add(Box.createVerticalStrut(6));
        seccionPaginacion.add(cmbRegistros);
        seccionPaginacion.add(Box.createVerticalStrut(6));
        seccionPaginacion.add(lblDescPaginacion);

        panel.add(seccionEliminar);
        panel.add(Box.createVerticalStrut(14));
        panel.add(seccionPaginacion);
        return panel;
    }

    private JPanel buildTabGestionUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        String[] columnas = {"ID", "Usuario", "Contraseña", "Rol"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setRowHeight(24);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));

        cargarTablaUsuarios(modelo);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(COLOR_FONDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        JTextField txtUsername = buildField();
        JTextField txtPassword = buildField();
        String[] roles = {"Usuario", "Administrador"};
        JComboBox<String> cmbRol = new JComboBox<>(roles);
        cmbRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        g.gridx = 0;
        g.gridy = 0;
        form.add(new JLabel("Usuario:"), g);

        g.gridx = 1;
        form.add(txtUsername, g);

        g.gridx = 0;
        g.gridy = 1;
        form.add(new JLabel("Contraseña:"), g);
        
        g.gridx = 1;
        form.add(txtPassword, g);
        
        g.gridx = 0;
        g.gridy = 2;
        form.add(new JLabel("Rol:"), g);

        g.gridx = 1;
        form.add(cmbRol, g);

        JButton btnCrear = buildBoton("Crear", new Color(39, 174, 96));
        JButton btnActualizar = buildBoton("Actualizar", COLOR_PRIMARIO);
        JButton btnEliminar = buildBoton("Eliminar", new Color(192, 57, 43));
        JButton btnLimpiar = buildBoton("Limpiar", new Color(149, 165, 166));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        botones.setBackground(COLOR_FONDO);
        botones.add(btnCrear);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        tabla.getSelectionModel().addListSelectionListener(ev -> {
            if (ev.getValueIsAdjusting()) return;
            int fila = tabla.getSelectedRow();
            if (fila < 0) return;
            txtUsername.setText((String) modelo.getValueAt(fila, 1));
            txtPassword.setText((String) modelo.getValueAt(fila, 2));
            cmbRol.setSelectedItem(modelo.getValueAt(fila, 3));
        });

        btnCrear.addActionListener(e -> {
            try {
                usuarioService.crearUsuario(txtUsername.getText().trim(), txtPassword.getText().trim(), (String) cmbRol.getSelectedItem());
                cargarTablaUsuarios(modelo);
                limpiarForm(txtUsername, txtPassword, cmbRol, tabla);
                JOptionPane.showMessageDialog(this, "Usuario creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) modelo.getValueAt(fila, 0);

            try {
                usuarioService.actualizarUsuario(new Usuario(id, txtUsername.getText().trim(), txtPassword.getText().trim(), (String) cmbRol.getSelectedItem()));
                cargarTablaUsuarios(modelo);
                limpiarForm(txtUsername, txtPassword, cmbRol, tabla);
                JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) modelo.getValueAt(fila, 0);
            int conf = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar este usuario?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf != JOptionPane.YES_OPTION) return;

            try {
                usuarioService.eliminarUsuario(id, usuarioActual.getId());
                cargarTablaUsuarios(modelo);
                limpiarForm(txtUsername, txtPassword, cmbRol, tabla);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch(IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> limpiarForm(txtUsername, txtPassword, cmbRol, tabla));

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(form, BorderLayout.SOUTH);

        JPanel sur = new JPanel(new BorderLayout());
        sur.setBackground(COLOR_FONDO);
        sur.add(form, BorderLayout.CENTER);
        sur.add(botones, BorderLayout.SOUTH);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarTablaUsuarios(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        List<Usuario> usuarios = usuarioService.listarTodos();

        for(Usuario u : usuarios) {
            modelo.addRow(new Object[]{u.getId(), u.getUsername(), u.getPassword(), u.getRol()});
        }
    }

    private void limpiarForm(JTextField txtU, JTextField txtP, JComboBox<String> cmb, JTable tabla) {
        txtU.setText("");
        txtP.setText("");
        cmb.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(230, 234, 238));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDE));
        JButton btnCerrar = buildBoton("Cerrar", new Color(149, 165, 166));
        btnCerrar.addActionListener(e -> dispose());
        footer.add(btnCerrar);
        return footer;
    }

    private JPanel buildSection(String titulo) {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(COLOR_FONDO);
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            titulo,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            COLOR_PRIMARIO
        );

        seccion.setBorder(BorderFactory.createCompoundBorder(
            border,
            BorderFactory.createEmptyBorder(8, 10, 10, 10)
        ));

        seccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        seccion.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        return seccion;
    }

    private JTextField buildField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            BorderFactory.createEmptyBorder(3, 7, 3, 7)
        ));

        return f;
    }

    private JPasswordField buildPasswordField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            BorderFactory.createEmptyBorder(3, 7, 3, 7)
        ));

        return f;
    }

    private JLabel buildLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        return lbl;
    }

    private JButton buildBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 32));

        return btn;
    }

    private JPanel wrapCenter(JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(COLOR_FONDO);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(comp);
        
        return p;
    }
}