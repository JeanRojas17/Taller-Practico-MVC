package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgresDatabase;
import com.mvc.modelo.Docente;
import com.mvc.modelo.Grupo;
import com.mvc.modelo.Materia;

public class GrupoDao {

    // Método auxiliar para construir un Grupo completo desde un ResultSet
    private Grupo mapearGrupo(ResultSet rs) throws SQLException {
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

        return grupo;
    }

    // INSERT
    // Solo guardamos los IDs de materia y docente como claves foráneas
    public void guardarGrupo(Grupo grupo) {
        String sql = "INSERT INTO \"practica-mvc\".grupo (id_materia, id_docente, aula, horario) VALUES (?, ?, ?, ?);";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grupo.getMateria().getId());
            pstmt.setInt(2, grupo.getDocente().getId());
            pstmt.setString(3, grupo.getAula());
            pstmt.setString(4, grupo.getHorario());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // SELECT ALL con JOIN para traer nombre de materia y docente
    public List<Grupo> obtenerTodosLosGrupos() {
        List<Grupo> grupos = new ArrayList<>();

        String sql = """
                SELECT
                    g.id_grupo,
                    g.aula,
                    g.horario,
                    m.id_materia,
                    m.nombre_materia,
                    m.creditos,
                    d.id_docente,
                    d.nombre  AS nombre_docente,
                    d.especialidad
                FROM "practica-mvc".grupo g
                JOIN "practica-mvc".materia  m ON g.id_materia = m.id_materia
                JOIN "practica-mvc".docente  d ON g.id_docente = d.id_docente
                ORDER BY g.id_grupo;
                """;

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                grupos.add(mapearGrupo(rs));
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return grupos;
    }

    // SELECT BY ID con JOIN
    public Grupo obtenerGrupoPorId(int id) {
        Grupo grupo = null;

        String sql = """
                SELECT
                    g.id_grupo,
                    g.aula,
                    g.horario,
                    m.id_materia,
                    m.nombre_materia,
                    m.creditos,
                    d.id_docente,
                    d.nombre  AS nombre_docente,
                    d.especialidad
                FROM "practica-mvc".grupo g
                JOIN "practica-mvc".materia  m ON g.id_materia = m.id_materia
                JOIN "practica-mvc".docente  d ON g.id_docente = d.id_docente
                WHERE g.id_grupo = ?;
                """;

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                grupo = mapearGrupo(rs);
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return grupo;
    }

    // UPDATE
    public void actualizarGrupo(Grupo grupo) {
        String sql = "UPDATE \"practica-mvc\".grupo SET id_materia = ?, id_docente = ?, aula = ?, horario = ? WHERE id_grupo = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grupo.getMateria().getId());
            pstmt.setInt(2, grupo.getDocente().getId());
            pstmt.setString(3, grupo.getAula());
            pstmt.setString(4, grupo.getHorario());
            pstmt.setInt(5, grupo.getId());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // DELETE
    public void eliminarGrupo(int id) {
        String sql = "DELETE FROM \"practica-mvc\".grupo WHERE id_grupo = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }
}