package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgreSQLDatabase;
import com.mvc.models.Estudiante;

public class EstudianteDao {

    public void guardarEstudiante(Estudiante estudiante) {
        String sql = "INSERT INTO \"practica-mvc\".estudiante (nombre, apellido, email) VALUES (?, ?, ?);";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getCorreo());
            
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<Estudiante>();
        
        String sql = "SELECT id_estudiante, nombre, apellido, email FROM \"practica-mvc\".estudiante ORDER BY id_estudiante;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                Estudiante estudiante = new Estudiante();

                estudiante.setId(rs.getInt("id_estudiante"));
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellido(rs.getString("apellido"));
                estudiante.setCorreo(rs.getString("email"));

                estudiantes.add(estudiante);
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return estudiantes;
    }

    public Estudiante obtenerEstudiantePorId(int id) {
        Estudiante estudiante = null;
        
        String sql = "SELECT id_estudiante, nombre, apellido, email FROM \"practica-mvc\".estudiante WHERE id_estudiante = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                estudiante = new Estudiante();

                estudiante.setId(rs.getInt("id_estudiante"));
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellido(rs.getString("apellido"));
                estudiante.setCorreo(rs.getString("email"));
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return estudiante;
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        String sql = "UPDATE \"practica-mvc\".estudiante SET nombre = ?, apellido = ?, email = ? WHERE id_estudiante = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getCorreo());
            pstmt.setInt(4, estudiante.getId());
            
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }   
    }
    
    public void eliminarEstudiante(int id) {
        String sql = "DELETE FROM \"practica-mvc\".estudiante WHERE id_estudiante = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }   
    }
}