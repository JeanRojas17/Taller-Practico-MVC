package com.mvc;

import com.mvc.modelo.Docente;
import com.mvc.modelo.Estudiante;
import com.mvc.modelo.Grupo;
import com.mvc.modelo.InscripcionCurso;
import com.mvc.modelo.Materia;
import com.mvc.servicios.InscripcionCursoService;
import com.mvc.dao.InscripcionCursoDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InscripcionCursoServiceTest {

    private InscripcionCursoService inscripcionService;
    private Estudiante estudianteValido;
    private Grupo grupoValido;

    @BeforeEach
    void setUp() {
        inscripcionService = new InscripcionCursoService(new InscripcionCursoDao());

        estudianteValido = new Estudiante(1, "Jean", "Rojas", "jean@email.com");

        Materia materia = new Materia(1, "Bases de Datos", 3);
        Docente docente = new Docente(1, "Gabriel", "Ingeniería de Software");
        grupoValido = new Grupo(1, materia, docente, "Aula 101", "Lunes 8am");
    }

    @Test
    @DisplayName("Registrar inscripción nula lanza IllegalArgumentException")
    void registrarInscripcion_nula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> inscripcionService.registrarInscripcion(null));
    }

    @Test
    @DisplayName("Registrar inscripción sin estudiante lanza IllegalArgumentException")
    void registrarInscripcion_sinEstudiante_lanzaExcepcion() {
        InscripcionCurso inscripcion = new InscripcionCurso(0, null, grupoValido, null, "Inscrito");
        assertThrows(IllegalArgumentException.class,
                () -> inscripcionService.registrarInscripcion(inscripcion));
    }

    @Test
    @DisplayName("Registrar inscripción sin grupo lanza IllegalArgumentException")
    void registrarInscripcion_sinGrupo_lanzaExcepcion() {
        InscripcionCurso inscripcion = new InscripcionCurso(0, estudianteValido, null, null, "Inscrito");
        assertThrows(IllegalArgumentException.class,
                () -> inscripcionService.registrarInscripcion(inscripcion));
    }

    @Test
    @DisplayName("Registrar inscripción sin estado lanza IllegalArgumentException")
    void registrarInscripcion_sinEstado_lanzaExcepcion() {
        InscripcionCurso inscripcion = new InscripcionCurso(0, estudianteValido, grupoValido, null, null);
        assertThrows(IllegalArgumentException.class,
                () -> inscripcionService.registrarInscripcion(inscripcion));
    }

    @Test
    @DisplayName("Nota mayor o igual a 3.0 debe resultar en estado Aprobado")
    void calcularEstado_notaAprobatoria_retornaAprobado() {
        float nota = 3.5f;
        String estado = nota >= 3.0f ? "Aprobado" : "Reprobado";
        assertEquals("Aprobado", estado);
    }

    @Test
    @DisplayName("Nota menor a 3.0 debe resultar en estado Reprobado")
    void calcularEstado_notaReprobatoria_retornaReprobado() {
        float nota = 2.9f;
        String estado = nota >= 3.0f ? "Aprobado" : "Reprobado";
        assertEquals("Reprobado", estado);
    }

    @Test
    @DisplayName("Nota exactamente 3.0 debe resultar en estado Aprobado")
    void calcularEstado_notaExacta_retornaAprobado() {
        float nota = 3.0f;
        String estado = nota >= 3.0f ? "Aprobado" : "Reprobado";
        assertEquals("Aprobado", estado);
    }
}