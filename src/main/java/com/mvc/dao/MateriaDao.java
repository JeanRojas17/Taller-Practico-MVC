package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgresDatabase;
import com.mvc.models.Materia;

public class MateriaDao {

    // INSERT
    public void guardarMateria(Materia materia) {
        String sql = "INSERT INTO \"practica-mvc\".materia (nombre_materia, creditos) VALUES (?, ?);";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombreMateria());
            pstmt.setInt(2, materia.getCreditos());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // SELECT ALL
    public List<Materia> obtenerTodasLasMaterias() {
        List<Materia> materias = new ArrayList<>();

        String sql = "SELECT id_materia, nombre_materia, creditos FROM \"practica-mvc\".materia ORDER BY id_materia;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                Materia materia = new Materia();

                materia.setId(rs.getInt("id_materia"));
                materia.setNombreMateria(rs.getString("nombre_materia"));
                materia.setCreditos(rs.getInt("creditos"));

                materias.add(materia);
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return materias;
    }

    // SELECT BY ID
    public Materia obtenerMateriaPorId(int id) {
        Materia materia = null;

        String sql = "SELECT id_materia, nombre_materia, creditos FROM \"practica-mvc\".materia WHERE id_materia = ?;";

        try (Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                materia = new Materia();

                materia.setId(rs.getInt("id_materia"));
                materia.setNombreMateria(rs.getString("nombre_materia"));
                materia.setCreditos(rs.getInt("creditos"));
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return materia;
    }

    // UPDATE
    public void actualizarMateria(Materia materia) {
        String sql = "UPDATE \"practica-mvc\".materia SET nombre_materia = ?, creditos = ? WHERE id_materia = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getNombreMateria());
            pstmt.setInt(2, materia.getCreditos());
            pstmt.setInt(3, materia.getId());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // DELETE
    public void eliminarMateria(int id) {
        String sql = "DELETE FROM \"practica-mvc\".materia WHERE id_materia = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }
}