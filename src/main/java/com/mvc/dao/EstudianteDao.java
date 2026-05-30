package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgreSQLDatabase;
import com.mvc.exception.PersistenciaException;
import com.mvc.models.Estudiante;

public class EstudianteDao {

    public void guardarEstudiante(Estudiante estudiante) {
        String sql = "INSERT INTO \"practica-mvc\".estudiante (nombre, apellido, email) VALUES (?, ?, ?);";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getEmail());
            
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al guardar el estudiante.", error);
        }
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();
        
        String sql = "SELECT id_estudiante, nombre, apellido, email FROM \"practica-mvc\".estudiante ORDER BY id_estudiante;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                estudiantes.add(mapear(rs));
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener los estudiantes.", error);
        }

        return estudiantes;
    }

    public Estudiante obtenerEstudiantePorId(int id) {
        String sql = "SELECT id_estudiante, nombre, apellido, email FROM \"practica-mvc\".estudiante WHERE id_estudiante = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return mapear(rs);
                }
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al obtener el estudiante con ID " +id+ ".", error);
        }

        return null;
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        String sql = "UPDATE \"practica-mvc\".estudiante SET nombre = ?, apellido = ?, email = ? WHERE id_estudiante = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getEmail());
            pstmt.setInt(4, estudiante.getId());
            
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al actualizar el estudiante con ID " +estudiante.getId()+ ".", error);
        }   
    }
    
    public void eliminarEstudiante(int id) {
        String sql = "DELETE FROM \"practica-mvc\".estudiante WHERE id_estudiante = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al eliminar el estudiante con ID " +id+ ".", error);
        }   
    }

    private Estudiante mapear(ResultSet rs) throws SQLException {
        Estudiante estudiante = new Estudiante();

        estudiante.setId(rs.getInt("id_estudiante"));
        estudiante.setNombre(rs.getString("nombre"));
        estudiante.setApellido(rs.getString("apellido"));
        estudiante.setEmail(rs.getString("email"));
        
        return estudiante;
    }
}