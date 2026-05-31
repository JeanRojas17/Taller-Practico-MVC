package com.mvc.controllers;

import com.mvc.models.Estudiante;
import com.mvc.services.EstudianteService;
import com.mvc.views.VistaEstudianteSwing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ControladorEstudiante")
class ControladorEstudianteTest {

    @Mock private VistaEstudianteSwing vista;
    @Mock private EstudianteService service;

    private Runnable onRegistrar;
    private Runnable onActualizar;
    private Runnable onEliminar;
    private Runnable onRefrescar;

    @BeforeEach
    void setUp() {
        // Capturamos los Runnables que el controlador registra en la vista
        doAnswer(inv -> { onRegistrar  = inv.getArgument(0); return null; }).when(vista).setOnRegistrar(any());
        doAnswer(inv -> { onActualizar = inv.getArgument(0); return null; }).when(vista).setOnActualizar(any());
        doAnswer(inv -> { onEliminar   = inv.getArgument(0); return null; }).when(vista).setOnEliminar(any());
        doAnswer(inv -> { onRefrescar  = inv.getArgument(0); return null; }).when(vista).setOnRefrescar(any());

        when(service.mostrarTodosLosEstudiantes()).thenReturn(List.of());

        new ControladorEstudiante(vista, service);
    }

    // ── Registrar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registrar con campos válidos llama al servicio y refresca la tabla")
    void registrar_camposValidos_llamaServicioYRefresca() {
        when(vista.getNombre()).thenReturn("Jean");
        when(vista.getApellido()).thenReturn("Rojas");
        when(vista.getEmail()).thenReturn("jean@email.com");

        onRegistrar.run();

        verify(service).registrarEstudiante(any(Estudiante.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
        verify(service, atLeast(2)).mostrarTodosLosEstudiantes(); // inicial + tras registrar
    }

    @Test
    @DisplayName("Registrar con nombre vacío muestra error y no llama al servicio")
    void registrar_nombreVacio_muestraErrorSinLlamarServicio() {
        when(vista.getNombre()).thenReturn("");
        when(vista.getApellido()).thenReturn("Rojas");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).registrarEstudiante(any());
    }

    @Test
    @DisplayName("Registrar con apellido vacío muestra error y no llama al servicio")
    void registrar_apellidoVacio_muestraErrorSinLlamarServicio() {
        when(vista.getNombre()).thenReturn("Jean");
        when(vista.getApellido()).thenReturn("");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).registrarEstudiante(any());
    }

    @Test
    @DisplayName("Registrar cuando el servicio lanza excepción muestra el error en la vista")
    void registrar_servicioLanzaExcepcion_muestraError() {
        when(vista.getNombre()).thenReturn("Jean");
        when(vista.getApellido()).thenReturn("Rojas");
        when(vista.getEmail()).thenReturn("no-es-email");
        doThrow(new IllegalArgumentException("Email inválido")).when(service).registrarEstudiante(any());

        onRegistrar.run();

        verify(vista).mostrarError("Email inválido");
    }

    // ── Actualizar ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Actualizar sin fila seleccionada muestra error y no llama al servicio")
    void actualizar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onActualizar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).actualizarEstudiante(any());
    }

    @Test
    @DisplayName("Actualizar con fila seleccionada y datos válidos llama al servicio")
    void actualizar_conSeleccionYDatosValidos_llamaServicio() {
        when(vista.getIdSeleccionado()).thenReturn(1);
        when(vista.getNombre()).thenReturn("Jean");
        when(vista.getApellido()).thenReturn("Rojas");
        when(vista.getEmail()).thenReturn("jean@email.com");

        onActualizar.run();

        verify(service).actualizarEstudiante(any(Estudiante.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Actualizar con nombre vacío muestra error y no llama al servicio")
    void actualizar_nombreVacio_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(1);
        when(vista.getNombre()).thenReturn("");
        when(vista.getApellido()).thenReturn("Rojas");

        onActualizar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).actualizarEstudiante(any());
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Eliminar sin fila seleccionada muestra error y no llama al servicio")
    void eliminar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onEliminar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).eliminarEstudiante(anyInt());
    }

    // ── Refrescar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Refrescar carga los datos del servicio en la vista")
    void refrescar_cargaDatosEnVista() {
        List<Estudiante> lista = List.of(new Estudiante(1, "Jean", "Rojas", "jean@email.com"));
        when(service.mostrarTodosLosEstudiantes()).thenReturn(lista);

        onRefrescar.run();

        verify(vista).cargarEstudiantes(lista);
    }

    // ── Notificar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registrar exitoso invoca el callback notificar")
    void registrar_exitoso_invocaNotificar() {
        Runnable notificar = mock(Runnable.class);
        when(service.mostrarTodosLosEstudiantes()).thenReturn(List.of());

        // Capturamos nuevamente para el controlador con notificar
        doAnswer(inv -> { onRegistrar = inv.getArgument(0); return null; }).when(vista).setOnRegistrar(any());
        doAnswer(inv -> { return null; }).when(vista).setOnActualizar(any());
        doAnswer(inv -> { return null; }).when(vista).setOnEliminar(any());
        doAnswer(inv -> { return null; }).when(vista).setOnRefrescar(any());

        new ControladorEstudiante(vista, service, notificar);

        when(vista.getNombre()).thenReturn("Jean");
        when(vista.getApellido()).thenReturn("Rojas");
        when(vista.getEmail()).thenReturn("jean@email.com");

        onRegistrar.run();

        verify(notificar).run();
    }
}