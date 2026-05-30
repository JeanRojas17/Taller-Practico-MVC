package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgreSQLDatabase;
import com.mvc.exception.PersistenciaException;
import com.mvc.models.*;

public class InscripcionCursoDao {

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

    public void guardarInscripcion(InscripcionCurso inscripcion) {
        String sql = "INSERT INTO \"practica-mvc\".inscripcion_curso (id_estudiante, id_grupo, nota_final, estado) VALUES (?, ?, ?, ?);";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcion.getEstudiante().getId());
            pstmt.setInt(2, inscripcion.getGrupo().getId());

            if(inscripcion.getNotaFinal() != null) {
                pstmt.setBigDecimal(3, inscripcion.getNotaFinal());
            } else {
                pstmt.setNull(3, java.sql.Types.NUMERIC);
            }

            pstmt.setString(4, inscripcion.getEstado());
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al guardar la inscripción.", error);
        }
    }

    public List<InscripcionCurso> obtenerTodasLasInscripciones() {
        List<InscripcionCurso> inscripciones = new ArrayList<>();

        String sql = SQL_SELECT + "ORDER BY ic.id_inscripcion;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                inscripciones.add(mapearInscripcion(rs));
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener las inscripciones.", error);
        }

        return inscripciones;
    }

    public InscripcionCurso obtenerInscripcionPorId(int id) {
        String sql = SQL_SELECT + "WHERE ic.id_inscripcion = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return mapearInscripcion(rs);
                }
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener la inscripción con ID " +id+ ".", error);
        }

        return null;
    }

    public void actualizarInscripcion(InscripcionCurso inscripcion) {
        String sql = "UPDATE \"practica-mvc\".inscripcion_curso SET id_estudiante = ?, id_grupo = ?, nota_final = ?, estado = ? WHERE id_inscripcion = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcion.getEstudiante().getId());
            pstmt.setInt(2, inscripcion.getGrupo().getId());

            if(inscripcion.getNotaFinal() != null) {
                pstmt.setBigDecimal(3, inscripcion.getNotaFinal());
            } else {
                pstmt.setNull(3, java.sql.Types.NUMERIC);
            }

            pstmt.setString(4, inscripcion.getEstado());
            pstmt.setInt(5, inscripcion.getId());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al actualizar la inscripción con ID " +inscripcion.getId()+ ".", error);
        }
    }

    public void eliminarInscripcion(int id) {
        String sql = "DELETE FROM \"practica-mvc\".inscripcion_curso WHERE id_inscripcion = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al eliminar la inscripción con ID " +id+ ".", error);
        }
    }

    public List<InscripcionCurso> obtenerInscripcionesPorEstudiante(int idEstudiante) {
        List<InscripcionCurso> inscripciones = new ArrayList<>();

        String sql = SQL_SELECT + "WHERE ic.id_estudiante = ? ORDER BY ic.id_inscripcion;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEstudiante);

            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    inscripciones.add(mapearInscripcion(rs));
                }
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener inscripciones del estudiante con ID " +idEstudiante+ ".", error);
        }

        return inscripciones;
    }

    public List<InscripcionCurso> obtenerInscripcionesPorGrupo(int idGrupo) {
        List<InscripcionCurso> inscripciones = new ArrayList<>();

        String sql = SQL_SELECT + "WHERE ic.id_grupo = ? ORDER BY ic.id_inscripcion;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idGrupo);
            
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    inscripciones.add(mapearInscripcion(rs));
                }
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener inscripciones del grupo con ID " +idGrupo+ ".", error);
        }

        return inscripciones;
    }

    private InscripcionCurso mapearInscripcion(ResultSet rs) throws SQLException {

        Estudiante estudiante = new Estudiante();
        estudiante.setId(rs.getInt("id_estudiante"));
        estudiante.setNombre(rs.getString("nombre_estudiante"));
        estudiante.setApellido(rs.getString("apellido_estudiante"));
        estudiante.setEmail(rs.getString("email_estudiante"));

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
        inscripcion.setNotaFinal(rs.getBigDecimal("nota_final"));
        inscripcion.setEstado(rs.getString("estado"));

        return inscripcion;
    }
}