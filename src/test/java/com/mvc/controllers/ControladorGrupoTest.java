package com.mvc.controllers;

import com.mvc.models.Docente;
import com.mvc.models.Grupo;
import com.mvc.models.Materia;
import com.mvc.services.DocenteService;
import com.mvc.services.GrupoService;
import com.mvc.services.MateriaService;
import com.mvc.views.VistaGrupoSwing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ControladorGrupo")
class ControladorGrupoTest {

    @Mock private VistaGrupoSwing vista;
    @Mock private GrupoService grupoService;
    @Mock private MateriaService materiaService;
    @Mock private DocenteService docenteService;

    private Runnable onRegistrar;
    private Runnable onActualizar;
    private Runnable onEliminar;
    private Runnable onRefrescar;

    private final Materia materia = new Materia(1, "Programacion II", 3);
    private final Docente docente = new Docente(1, "Ana Torres", "Software");

    @BeforeEach
    void setUp() {
        doAnswer(inv -> { onRegistrar  = inv.getArgument(0); return null; }).when(vista).setOnRegistrar(any());
        doAnswer(inv -> { onActualizar = inv.getArgument(0); return null; }).when(vista).setOnActualizar(any());
        doAnswer(inv -> { onEliminar   = inv.getArgument(0); return null; }).when(vista).setOnEliminar(any());
        doAnswer(inv -> { onRefrescar  = inv.getArgument(0); return null; }).when(vista).setOnRefrescar(any());

        when(grupoService.mostrarTodosLosGrupos()).thenReturn(List.of());

        new ControladorGrupo(vista, grupoService, materiaService, docenteService);
    }

    @Test
    @DisplayName("Registrar con campos válidos llama al servicio y refresca la tabla")
    void registrar_camposValidos_llamaServicioYRefresca() {
        when(vista.getIdMateriaTexto()).thenReturn("1");
        when(vista.getIdDocenteTexto()).thenReturn("1");
        when(vista.getAula()).thenReturn("Aula 101");
        when(vista.getHorario()).thenReturn("Lunes 8am");
        when(materiaService.obtenerMateriaPorId(1)).thenReturn(materia);
        when(docenteService.obtenerDocentePorId(1)).thenReturn(docente);

        onRegistrar.run();

        verify(grupoService).registrarGrupo(any(Grupo.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Registrar con campos vacíos muestra error y no llama al servicio")
    void registrar_camposVacios_muestraError() {
        when(vista.getIdMateriaTexto()).thenReturn("");
        when(vista.getIdDocenteTexto()).thenReturn("");
        when(vista.getAula()).thenReturn("");
        when(vista.getHorario()).thenReturn("");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(grupoService, never()).registrarGrupo(any());
    }

    @Test
    @DisplayName("Registrar con ID materia no numérico muestra error y no llama al servicio")
    void registrar_idMateriaNoNumerico_muestraError() {
        when(vista.getIdMateriaTexto()).thenReturn("abc");
        when(vista.getIdDocenteTexto()).thenReturn("1");
        when(vista.getAula()).thenReturn("Aula 101");
        when(vista.getHorario()).thenReturn("Lunes 8am");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(grupoService, never()).registrarGrupo(any());
    }

    @Test
    @DisplayName("Registrar con materia inexistente muestra error y no llama al servicio")
    void registrar_materiaInexistente_muestraError() {
        when(vista.getIdMateriaTexto()).thenReturn("99");
        when(vista.getIdDocenteTexto()).thenReturn("1");
        when(vista.getAula()).thenReturn("Aula 101");
        when(vista.getHorario()).thenReturn("Lunes 8am");
        when(materiaService.obtenerMateriaPorId(99)).thenReturn(null);

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(grupoService, never()).registrarGrupo(any());
    }

    @Test
    @DisplayName("Registrar con docente inexistente muestra error y no llama al servicio")
    void registrar_docenteInexistente_muestraError() {
        when(vista.getIdMateriaTexto()).thenReturn("1");
        when(vista.getIdDocenteTexto()).thenReturn("99");
        when(vista.getAula()).thenReturn("Aula 101");
        when(vista.getHorario()).thenReturn("Lunes 8am");
        when(materiaService.obtenerMateriaPorId(1)).thenReturn(materia);
        when(docenteService.obtenerDocentePorId(99)).thenReturn(null);

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(grupoService, never()).registrarGrupo(any());
    }

    @Test
    @DisplayName("Actualizar sin fila seleccionada muestra error y no llama al servicio")
    void actualizar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onActualizar.run();

        verify(vista).mostrarError(any());
        verify(grupoService, never()).actualizarGrupo(any());
    }

    @Test
    @DisplayName("Actualizar con fila seleccionada y datos válidos llama al servicio")
    void actualizar_conSeleccionYDatosValidos_llamaServicio() {
        when(vista.getIdSeleccionado()).thenReturn(1);
        when(vista.getIdMateriaTexto()).thenReturn("1");
        when(vista.getIdDocenteTexto()).thenReturn("1");
        when(vista.getAula()).thenReturn("Aula 202");
        when(vista.getHorario()).thenReturn("Martes 10am");
        when(materiaService.obtenerMateriaPorId(1)).thenReturn(materia);
        when(docenteService.obtenerDocentePorId(1)).thenReturn(docente);

        onActualizar.run();

        verify(grupoService).actualizarGrupo(any(Grupo.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Eliminar sin fila seleccionada muestra error y no llama al servicio")
    void eliminar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onEliminar.run();

        verify(vista).mostrarError(any());
        verify(grupoService, never()).eliminarGrupo(anyInt());
    }

    @Test
    @DisplayName("Refrescar carga los datos del servicio en la vista")
    void refrescar_cargaDatosEnVista() {
        List<Grupo> lista = List.of(new Grupo(1, materia, docente, "Aula 101", "Lunes 8am"));
        when(grupoService.mostrarTodosLosGrupos()).thenReturn(lista);

        onRefrescar.run();

        verify(vista).cargarGrupos(lista);
    }
}