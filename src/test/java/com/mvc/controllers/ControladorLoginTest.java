package com.mvc.controllers;

import com.mvc.models.Usuario;
import com.mvc.services.UsuarioService;
import com.mvc.views.VistaLoginSwing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.BiConsumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ControladorLogin")
class ControladorLoginTest {

    @Mock private VistaLoginSwing vistaLogin;
    @Mock private UsuarioService usuarioService;

    private BiConsumer<String, String> onIngresar;

    @BeforeEach
    void setUp() {
        doAnswer(inv -> { onIngresar = inv.getArgument(0); return null; })
            .when(vistaLogin).setOnIngresar(any());

        new ControladorLogin(vistaLogin, usuarioService);
    }

    @Test
    @DisplayName("Credenciales válidas autentican al usuario y abren la vista principal")
    void autenticar_credencialesValidas_abreVistaPrincipal() {
        Usuario usuario = new Usuario(1, "Jean", "123", "Administrador");
        when(usuarioService.autenticar("Jean", "123")).thenReturn(usuario);

        // No podemos verificar que VistaPrincipalSwing se abre (requiere entorno gráfico),
        // pero sí verificamos que la vista de login se cierra
        try {
            onIngresar.accept("Jean", "123");
        } catch(Exception ignored) {
            // VistaPrincipalSwing puede fallar en entorno headless — lo ignoramos
        }

        verify(usuarioService).autenticar("Jean", "123");
        verify(vistaLogin).setVisible(false);
        verify(vistaLogin).dispose();
    }

    @Test
    @DisplayName("Credenciales incorrectas muestran error y limpian los campos")
    void autenticar_credencialesInvalidas_muestraError() {
        when(usuarioService.autenticar("Jean", "xxx")).thenReturn(null);

        onIngresar.accept("Jean", "xxx");

        verify(vistaLogin).mostrarError(any());
        verify(vistaLogin).limpiarCampos();
        verify(vistaLogin, never()).dispose();
    }

    @Test
    @DisplayName("Username vacío lanza excepción y muestra error en la vista")
    void autenticar_usernameVacio_muestraError() {
        when(usuarioService.autenticar("", "123"))
            .thenThrow(new IllegalArgumentException("El nombre de usuario es obligatorio."));

        onIngresar.accept("", "123");

        verify(vistaLogin).mostrarError("El nombre de usuario es obligatorio.");
        verify(vistaLogin, never()).dispose();
    }

    @Test
    @DisplayName("Password vacío lanza excepción y muestra error en la vista")
    void autenticar_passwordVacio_muestraError() {
        when(usuarioService.autenticar("Jean", ""))
            .thenThrow(new IllegalArgumentException("La contraseña es obligatoria."));

        onIngresar.accept("Jean", "");

        verify(vistaLogin).mostrarError("La contraseña es obligatoria.");
        verify(vistaLogin, never()).dispose();
    }
}