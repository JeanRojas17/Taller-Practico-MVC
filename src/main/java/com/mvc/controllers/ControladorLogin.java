package com.mvc.controllers;

import com.mvc.models.Usuario;
import com.mvc.services.AuditoriaService;
import com.mvc.services.UsuarioService;
import com.mvc.views.VistaLoginSwing;
import com.mvc.views.VistaPrincipalSwing;

public class ControladorLogin {

    private final VistaLoginSwing vistaLogin;
    private final UsuarioService usuarioService;

    public ControladorLogin(VistaLoginSwing vistaLogin, UsuarioService usuarioService) {
        this.vistaLogin = vistaLogin;
        this.usuarioService = usuarioService;

        vistaLogin.setOnIngresar(this::autenticar);
    }

    private void autenticar(String username, String password) {
        try {
            Usuario usuario = usuarioService.autenticar(username, password);

            if(usuario == null) {
                vistaLogin.mostrarError("Usuario o contraseña incorrectos. Intenta de nuevo.");
                vistaLogin.limpiarCampos();
                return;
            }

            AuditoriaService.getInstance().setUsuarioActivo(usuario.getUsername());
            AuditoriaService.getInstance().registrar("CREAR", "Sesión", "Inicio de sesión como " +usuario.getRol());

            vistaLogin.setVisible(false);
            vistaLogin.dispose();

            VistaPrincipalSwing vistaPrincipal = new VistaPrincipalSwing(usuario);
            vistaPrincipal.setVisible(true);

        } catch(IllegalArgumentException ex) {
            vistaLogin.mostrarError(ex.getMessage());
        } catch(Exception ex) {
            vistaLogin.mostrarError("Error inesperado al iniciar sesión: " +ex.getMessage());
        }
    }
}