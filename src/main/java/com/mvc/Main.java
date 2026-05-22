package com.mvc;

import com.mvc.controllers.ControladorLogin;
import com.mvc.dao.UsuarioDao;
import com.mvc.services.UsuarioService;
import com.mvc.views.VistaLoginSwing;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            UsuarioDao usuarioDao = new UsuarioDao();
            UsuarioService usuarioService = new UsuarioService(usuarioDao);
            VistaLoginSwing vistaLogin = new VistaLoginSwing();
            new ControladorLogin(vistaLogin, usuarioService);
            vistaLogin.setVisible(true);
        });
    }
}