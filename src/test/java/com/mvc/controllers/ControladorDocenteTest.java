package com.mvc.controllers;

import com.mvc.models.Docente;
import com.mvc.services.DocenteService;
import com.mvc.views.VistaDocenteSwing;

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
@DisplayName("ControladorDocente")
class ControladorDocenteTest {

    @Mock private VistaDocenteSwing vista;
    @Mock private DocenteService service;

    private Runnable onRegistrar;
    private Runnable onActualizar;
    private Runnable onEliminar;
    private Runnable onRefrescar;

    @BeforeEach
    void setUp() {
        doAnswer(inv -> { onRegistrar  = inv.getArgument(0); return null; }).when(vista).setOnRegistrar(any());
        doAnswer(inv -> { onActualizar = inv.getArgument(0); return null; }).when(vista).setOnActualizar(any());
        doAnswer(inv -> { onEliminar   = inv.getArgument(0); return null; }).when(vista).setOnEliminar(any());
        doAnswer(inv -> { onRefrescar  = inv.getArgument(0); return null; }).when(vista).setOnRefrescar(any());

        when(service.mostrarTodosLosDocentes()).thenReturn(List.of());

        new ControladorDocente(vista, service);
    }

    @Test
    @DisplayName("Registrar con campos válidos llama al servicio y refresca la tabla")
    void registrar_camposValidos_llamaServicioYRefresca() {
        when(vista.getNombre()).thenReturn("Ana Torres");
        when(vista.getEspecialidad()).thenReturn("Bases de Datos");

        onRegistrar.run();

        verify(service).registrarDocente(any(Docente.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Registrar con nombre vacío muestra error y no llama al servicio")
    void registrar_nombreVacio_muestraError() {
        when(vista.getNombre()).thenReturn("");
        when(vista.getEspecialidad()).thenReturn("Bases de Datos");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).registrarDocente(any());
    }

    @Test
    @DisplayName("Registrar con especialidad vacía muestra error y no llama al servicio")
    void registrar_especialidadVacia_muestraError() {
        when(vista.getNombre()).thenReturn("Ana Torres");
        when(vista.getEspecialidad()).thenReturn("");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).registrarDocente(any());
    }

    @Test
    @DisplayName("Actualizar sin fila seleccionada muestra error y no llama al servicio")
    void actualizar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onActualizar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).actualizarDocente(any());
    }

    @Test
    @DisplayName("Actualizar con fila seleccionada y datos válidos llama al servicio")
    void actualizar_conSeleccionYDatosValidos_llamaServicio() {
        when(vista.getIdSeleccionado()).thenReturn(1);
        when(vista.getNombre()).thenReturn("Ana Torres");
        when(vista.getEspecialidad()).thenReturn("Software");

        onActualizar.run();

        verify(service).actualizarDocente(any(Docente.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Eliminar sin fila seleccionada muestra error y no llama al servicio")
    void eliminar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onEliminar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).eliminarDocente(anyInt());
    }

    @Test
    @DisplayName("Refrescar carga los datos del servicio en la vista")
    void refrescar_cargaDatosEnVista() {
        List<Docente> lista = List.of(new Docente(1, "Ana Torres", "Bases de Datos"));
        when(service.mostrarTodosLosDocentes()).thenReturn(lista);

        onRefrescar.run();

        verify(vista).cargarDocentes(lista);
    }
}