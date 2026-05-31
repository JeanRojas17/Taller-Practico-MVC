package com.mvc.controllers;

import com.mvc.models.Materia;
import com.mvc.services.MateriaService;
import com.mvc.views.VistaMateriaSwing;

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
@DisplayName("ControladorMateria")
class ControladorMateriaTest {

    @Mock private VistaMateriaSwing vista;
    @Mock private MateriaService service;

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

        when(service.mostrarTodasLasMaterias()).thenReturn(List.of());

        new ControladorMateria(vista, service);
    }

    @Test
    @DisplayName("Registrar con campos válidos llama al servicio y refresca la tabla")
    void registrar_camposValidos_llamaServicioYRefresca() {
        when(vista.getNombreMateria()).thenReturn("Bases de Datos");
        when(vista.getCreditosTexto()).thenReturn("3");

        onRegistrar.run();

        verify(service).registrarMateria(any(Materia.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Registrar con nombre vacío muestra error y no llama al servicio")
    void registrar_nombreVacio_muestraError() {
        when(vista.getNombreMateria()).thenReturn("");
        when(vista.getCreditosTexto()).thenReturn("3");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).registrarMateria(any());
    }

    @Test
    @DisplayName("Registrar con créditos no numéricos muestra error y no llama al servicio")
    void registrar_creditosNoNumericos_muestraError() {
        when(vista.getNombreMateria()).thenReturn("Bases de Datos");
        when(vista.getCreditosTexto()).thenReturn("abc");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).registrarMateria(any());
    }

    @Test
    @DisplayName("Registrar con créditos vacíos muestra error y no llama al servicio")
    void registrar_creditosVacios_muestraError() {
        when(vista.getNombreMateria()).thenReturn("Bases de Datos");
        when(vista.getCreditosTexto()).thenReturn("");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).registrarMateria(any());
    }

    @Test
    @DisplayName("Actualizar sin fila seleccionada muestra error y no llama al servicio")
    void actualizar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onActualizar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).actualizarMateria(any());
    }

    @Test
    @DisplayName("Actualizar con fila seleccionada y datos válidos llama al servicio")
    void actualizar_conSeleccionYDatosValidos_llamaServicio() {
        when(vista.getIdSeleccionado()).thenReturn(1);
        when(vista.getNombreMateria()).thenReturn("Bases de Datos");
        when(vista.getCreditosTexto()).thenReturn("3");

        onActualizar.run();

        verify(service).actualizarMateria(any(Materia.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Eliminar sin fila seleccionada muestra error y no llama al servicio")
    void eliminar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onEliminar.run();

        verify(vista).mostrarError(any());
        verify(service, never()).eliminarMateria(anyInt());
    }

    @Test
    @DisplayName("Refrescar carga los datos del servicio en la vista")
    void refrescar_cargaDatosEnVista() {
        List<Materia> lista = List.of(new Materia(1, "Bases de Datos", 3));
        when(service.mostrarTodasLasMaterias()).thenReturn(lista);

        onRefrescar.run();

        verify(vista).cargarMaterias(lista);
    }
}