package com.mvc.controllers;

import com.mvc.models.Docente;
import com.mvc.models.Estudiante;
import com.mvc.models.Grupo;
import com.mvc.models.InscripcionCurso;
import com.mvc.models.Materia;
import com.mvc.services.EstudianteService;
import com.mvc.services.GrupoService;
import com.mvc.services.InscripcionCursoService;
import com.mvc.views.VistaInscripcionCursoSwing;

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
@DisplayName("ControladorInscripcionCurso")
class ControladorInscripcionCursoTest {

    @Mock private VistaInscripcionCursoSwing vista;
    @Mock private InscripcionCursoService inscripcionService;
    @Mock private EstudianteService estudianteService;
    @Mock private GrupoService grupoService;

    private Runnable onRegistrar;
    private Runnable onActualizar;
    private Runnable onEliminar;
    private Runnable onRefrescar;
    private Runnable onNotasEstudiante;
    private Runnable onNotasGrupo;

    private final Estudiante estudiante = new Estudiante(1, "Jean", "Rojas", "jean@email.com");
    private final Grupo grupo = new Grupo(1,
        new Materia(1, "Bases de Datos", 3),
        new Docente(1, "Ana Torres", "Software"),
        "Aula 101", "Lunes 8am"
    );

    @BeforeEach
    void setUp() {
        doAnswer(inv -> { onRegistrar       = inv.getArgument(0); return null; }).when(vista).setOnRegistrar(any());
        doAnswer(inv -> { onActualizar      = inv.getArgument(0); return null; }).when(vista).setOnActualizar(any());
        doAnswer(inv -> { onEliminar        = inv.getArgument(0); return null; }).when(vista).setOnEliminar(any());
        doAnswer(inv -> { onRefrescar       = inv.getArgument(0); return null; }).when(vista).setOnRefrescar(any());
        doAnswer(inv -> { onNotasEstudiante = inv.getArgument(0); return null; }).when(vista).setOnNotasEstudiante(any());
        doAnswer(inv -> { onNotasGrupo      = inv.getArgument(0); return null; }).when(vista).setOnNotasGrupo(any());

        when(inscripcionService.mostrarTodasLasInscripciones()).thenReturn(List.of());

        new ControladorInscripcionCurso(vista, inscripcionService, estudianteService, grupoService);
    }

    @Test
    @DisplayName("Registrar con campos válidos y sin nota llama al servicio")
    void registrar_sinNota_llamaServicio() {
        when(vista.getIdEstudianteTexto()).thenReturn("1");
        when(vista.getIdGrupoTexto()).thenReturn("1");
        when(vista.getNotaFinalTexto()).thenReturn("");
        when(vista.getEstado()).thenReturn("Inscrito");
        when(estudianteService.obtenerEstudiantePorId(1)).thenReturn(estudiante);
        when(grupoService.obtenerGrupoPorId(1)).thenReturn(grupo);

        onRegistrar.run();

        verify(inscripcionService).registrarInscripcion(any(InscripcionCurso.class));
        verify(vista).mostrarMensaje(any());
        verify(vista).limpiarCampos();
    }

    @Test
    @DisplayName("Registrar con nota válida calcula estado automáticamente")
    void registrar_conNota_calculaEstadoAutomatico() {
        when(vista.getIdEstudianteTexto()).thenReturn("1");
        when(vista.getIdGrupoTexto()).thenReturn("1");
        when(vista.getNotaFinalTexto()).thenReturn("4.5");
        when(vista.getEstado()).thenReturn("Inscrito");
        when(estudianteService.obtenerEstudiantePorId(1)).thenReturn(estudiante);
        when(grupoService.obtenerGrupoPorId(1)).thenReturn(grupo);

        onRegistrar.run();

        // El controlador debería haber calculado "Aprobado" y guardado
        verify(inscripcionService).registrarInscripcion(argThat(i ->
            "Aprobado".equals(i.getEstado())
        ));
    }

    @Test
    @DisplayName("Registrar con nota reprobatoria calcula estado Reprobado")
    void registrar_notaReprobatoria_calculaEstadoReprobado() {
        when(vista.getIdEstudianteTexto()).thenReturn("1");
        when(vista.getIdGrupoTexto()).thenReturn("1");
        when(vista.getNotaFinalTexto()).thenReturn("2.5");
        when(vista.getEstado()).thenReturn("Inscrito");
        when(estudianteService.obtenerEstudiantePorId(1)).thenReturn(estudiante);
        when(grupoService.obtenerGrupoPorId(1)).thenReturn(grupo);

        onRegistrar.run();

        verify(inscripcionService).registrarInscripcion(argThat(i ->
            "Reprobado".equals(i.getEstado())
        ));
    }

    @Test
    @DisplayName("Registrar con campos vacíos muestra error y no llama al servicio")
    void registrar_camposVacios_muestraError() {
        when(vista.getIdEstudianteTexto()).thenReturn("");
        when(vista.getIdGrupoTexto()).thenReturn("");
        when(vista.getEstado()).thenReturn("");

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(inscripcionService, never()).registrarInscripcion(any());
    }

    @Test
    @DisplayName("Registrar con estudiante inexistente muestra error y no llama al servicio")
    void registrar_estudianteInexistente_muestraError() {
        when(vista.getIdEstudianteTexto()).thenReturn("99");
        when(vista.getIdGrupoTexto()).thenReturn("1");
        when(vista.getNotaFinalTexto()).thenReturn("");
        when(vista.getEstado()).thenReturn("Inscrito");
        when(estudianteService.obtenerEstudiantePorId(99)).thenReturn(null);

        onRegistrar.run();

        verify(vista).mostrarError(any());
        verify(inscripcionService, never()).registrarInscripcion(any());
    }

    @Test
    @DisplayName("Actualizar sin fila seleccionada muestra error y no llama al servicio")
    void actualizar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onActualizar.run();

        verify(vista).mostrarError(any());
        verify(inscripcionService, never()).actualizarInscripcion(any());
    }

    @Test
    @DisplayName("Eliminar sin fila seleccionada muestra error y no llama al servicio")
    void eliminar_sinSeleccion_muestraError() {
        when(vista.getIdSeleccionado()).thenReturn(-1);

        onEliminar.run();

        verify(vista).mostrarError(any());
        verify(inscripcionService, never()).eliminarInscripcion(anyInt());
    }

    @Test
    @DisplayName("Refrescar carga las inscripciones del servicio en la vista")
    void refrescar_cargaDatosEnVista() {
        List<InscripcionCurso> lista = List.of(
            new InscripcionCurso(1, estudiante, grupo, null, "Inscrito")
        );
        when(inscripcionService.mostrarTodasLasInscripciones()).thenReturn(lista);

        onRefrescar.run();

        verify(vista).cargarInscripciones(lista);
    }

    @Test
    @DisplayName("Consultar notas por estudiante inexistente muestra error y no carga")
    void notasEstudiante_estudianteInexistente_muestraError() {
        when(vista.solicitarIdEstudianteParaConsulta()).thenReturn(99);
        when(estudianteService.obtenerEstudiantePorId(99)).thenReturn(null);

        onNotasEstudiante.run();

        verify(vista).mostrarError(any());
        verify(inscripcionService, never()).obtenerInscripcionesPorEstudiante(anyInt());
    }

    @Test
    @DisplayName("Consultar notas por grupo inexistente muestra error y no carga")
    void notasGrupo_grupoInexistente_muestraError() {
        when(vista.solicitarIdGrupoParaConsulta()).thenReturn(99);
        when(grupoService.obtenerGrupoPorId(99)).thenReturn(null);

        onNotasGrupo.run();

        verify(vista).mostrarError(any());
        verify(inscripcionService, never()).obtenerInscripcionesPorGrupo(anyInt());
    }
}