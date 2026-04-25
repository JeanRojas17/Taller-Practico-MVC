package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgresDatabase;
import com.mvc.modelo.Docente;
import com.mvc.modelo.Estudiante;
import com.mvc.modelo.Grupo;
import com.mvc.modelo.InscripcionCurso;
import com.mvc.modelo.Materia;

public class InscripcionCursoDao {

    // Método auxiliar para construir una InscripcionCurso completa desde un ResultSet
    private InscripcionCurso mapearInscripcion(ResultSet rs) throws SQLException {

        Estudiante estudiante = new Estudiante();

        estudiante.setId(rs.getInt("id_estudiante"));
        estudiante.setNombre(rs.getString("nombre_estudiante"));
        estudiante.setApellido(rs.getString("apellido_estudiante"));
        estudiante.setCorreo(rs.getString("email_estudiante"));

        Materia materia = new Materia();

        materia.setId(rs.getInt("id_materia"));
        materia.setNombreMateria(rs.getString("nombre_materia"));
        materia.setCreditos(rs.getInt("creditos"));

        Docente docente = new Docente();

        docente.setId(rs.getInt("id_docente"));
        docente.setNombre(rs.getString("nombre_docente"));
        docente.setEspecialidad(rs.getString("especialidad"));

        Grupo grupo = new Grupo();

        grupo.setId(rs.getInt("id_grupo"));
        grupo.setMateria(materia);
        grupo.setDocente(docente);
        grupo.setAula(rs.getString("aula"));
        grupo.setHorario(rs.getString("horario"));

        InscripcionCurso inscripcion = new InscripcionCurso();

        inscripcion.setId(rs.getInt("id_inscripcion"));
        inscripcion.setEstudiante(estudiante);
        inscripcion.setGrupo(grupo);
        inscripcion.setNotaFinal(rs.getObject("nota_final") != null ? rs.getFloat("nota_final") : null);
        inscripcion.setEstado(rs.getString("estado"));

        return inscripcion;
    }

    private static final String SQL_SELECT = """
            SELECT
                ic.id_inscripcion,
                ic.nota_final,
                ic.estado,
                e.id_estudiante,
                e.nombre   AS nombre_estudiante,
                e.apellido AS apellido_estudiante,
                e.email    AS email_estudiante,
                g.id_grupo,
                g.aula,
                g.horario,
                m.id_materia,
                m.nombre_materia,
                m.creditos,
                d.id_docente,
                d.nombre   AS nombre_docente,
                d.especialidad
            FROM "practica-mvc".inscripcion_curso ic
            JOIN "practica-mvc".estudiante e ON ic.id_estudiante = e.id_estudiante
            JOIN "practica-mvc".grupo      g ON ic.id_grupo      = g.id_grupo
            JOIN "practica-mvc".materia    m ON g.id_materia     = m.id_materia
            JOIN "practica-mvc".docente    d ON g.id_docente     = d.id_docente
            """;

    // INSERT
    public void guardarInscripcion(InscripcionCurso inscripcion) {
        String sql = "INSERT INTO \"practica-mvc\".inscripcion_curso (id_estudiante, id_grupo, nota_final, estado) VALUES (?, ?, ?, ?);";

        try (Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcion.getEstudiante().getId());
            pstmt.setInt(2, inscripcion.getGrupo().getId());

            if(inscripcion.getNotaFinal() != null) {
                pstmt.setFloat(3, inscripcion.getNotaFinal());
            } else {
                pstmt.setNull(3, java.sql.Types.FLOAT);
            }

            pstmt.setString(4, inscripcion.getEstado());
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // SELECT ALL
    public List<InscripcionCurso> obtenerTodasLasInscripciones() {
        List<InscripcionCurso> inscripciones = new ArrayList<>();

        String sql = SQL_SELECT + "ORDER BY ic.id_inscripcion;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                inscripciones.add(mapearInscripcion(rs));
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return inscripciones;
    }

    // SELECT BY ID
    public InscripcionCurso obtenerInscripcionPorId(int id) {
        InscripcionCurso inscripcion = null;

        String sql = SQL_SELECT + "WHERE ic.id_inscripcion = ?;";

        try (Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                inscripcion = mapearInscripcion(rs);
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return inscripcion;
    }

    // UPDATE
    public void actualizarInscripcion(InscripcionCurso inscripcion) {
        String sql = "UPDATE \"practica-mvc\".inscripcion_curso SET id_estudiante = ?, id_grupo = ?, nota_final = ?, estado = ? WHERE id_inscripcion = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcion.getEstudiante().getId());
            pstmt.setInt(2, inscripcion.getGrupo().getId());

            if(inscripcion.getNotaFinal() != null) {
                pstmt.setFloat(3, inscripcion.getNotaFinal());
            } else {
                pstmt.setNull(3, java.sql.Types.FLOAT);
            }

            pstmt.setString(4, inscripcion.getEstado());
            pstmt.setInt(5, inscripcion.getId());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // DELETE
    public void eliminarInscripcion(int id) {
        String sql = "DELETE FROM \"practica-mvc\".inscripcion_curso WHERE id_inscripcion = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }
}