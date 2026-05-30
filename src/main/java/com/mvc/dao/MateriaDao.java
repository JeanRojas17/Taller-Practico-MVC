package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgreSQLDatabase;
import com.mvc.exception.PersistenciaException;
import com.mvc.models.Materia;

public class MateriaDao {

    public void guardarMateria(Materia materia) {
        String sql = "INSERT INTO \"practica-mvc\".materia (nombre_materia, creditos) VALUES (?, ?);";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombreMateria());
            pstmt.setInt(2, materia.getCreditos());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al guardar la materia.", error);
        }
    }

    public List<Materia> obtenerTodasLasMaterias() {
        List<Materia> materias = new ArrayList<>();

        String sql = "SELECT id_materia, nombre_materia, creditos FROM \"practica-mvc\".materia ORDER BY id_materia;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                materias.add(mapear(rs));
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener las materias.", error);
        }

        return materias;
    }

    public Materia obtenerMateriaPorId(int id) {
        String sql = "SELECT id_materia, nombre_materia, creditos FROM \"practica-mvc\".materia WHERE id_materia = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            
            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return mapear(rs);
                }
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener la materia con ID " +id+ ".", error);
        }

        return null;
    }

    public void actualizarMateria(Materia materia) {
        String sql = "UPDATE \"practica-mvc\".materia SET nombre_materia = ?, creditos = ? WHERE id_materia = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombreMateria());
            pstmt.setInt(2, materia.getCreditos());
            pstmt.setInt(3, materia.getId());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al actualizar la materia con ID " +materia.getId()+ ".", error);
        }
    }

    public void eliminarMateria(int id) {
        String sql = "DELETE FROM \"practica-mvc\".materia WHERE id_materia = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al eliminar la materia con ID " +id+ ".", error);
        }
    }

    private Materia mapear(ResultSet rs) throws SQLException {
        Materia materia = new Materia();

        materia.setId(rs.getInt("id_materia"));
        materia.setNombreMateria(rs.getString("nombre_materia"));
        materia.setCreditos(rs.getInt("creditos"));

        return materia;
    }
}