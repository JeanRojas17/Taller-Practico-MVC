package com.mvc.dao;

// Importaciones necesarias para la conexión a la base de datos y consulta de datos a la tabla de estudiantes
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Importaciones necesarias para manejar listas de estudiantes
import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgresDatabase;
import com.mvc.modelo.Estudiante;

public class EstudianteDao {

    // guardarEstudiante(Estudiante estudiante)
    // INSERT INTO "practica-mvc".estudiantes (name, lastname, email) VALUES ('Gabriel', 'Perez', 'pepito@email.com');
    public void guardarEstudiante(Estudiante estudiante) {
        String sql = "INSERT INTO \"practica-mvc\".estudiantes (name, lastname, email) VALUES (?, ?, ?);";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getCorreo());
            
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // obtenerTodosLosEstudiantes()
    // SELECT id, name, lastname, email FROM "practica-mvc".estudiantes;
    public List<Estudiante> obtenerTodosLosEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<Estudiante>();
        
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes ORDER BY id;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                Estudiante estudiante = new Estudiante();

                estudiante.setId(rs.getInt("id"));
                estudiante.setNombre(rs.getString("name"));
                estudiante.setApellido(rs.getString("lastname"));
                estudiante.setCorreo(rs.getString("email"));

                estudiantes.add(estudiante);
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return estudiantes;
    }

    // obtenerEstudiantePorId(int id)
    // SELECT id, name, lastname, email FROM "practica-mvc".estudiantes WHERE id = 2;
    public Estudiante obtenerEstudiantePorId(int id) {
        Estudiante estudiante = null;
        
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes WHERE id = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                estudiante = new Estudiante();

                estudiante.setId(rs.getInt("id"));
                estudiante.setNombre(rs.getString("name"));
                estudiante.setApellido(rs.getString("lastname"));
                estudiante.setCorreo(rs.getString("email"));
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return estudiante;
    }

    // actualizarEstudiante(Estudiante estudiante)
    // UPDATE "practica-mvc".estudiantes
    // SET email = 'emailactualizado@example.com', lastname = 'nuevoapellido'
    // WHERE id = 2;
    public void actualizarEstudiante(Estudiante estudiante) {
        String sql = "UPDATE \"practica-mvc\".estudiantes SET name = ?, lastname = ?, email = ? WHERE id = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getApellido());
            pstmt.setString(3, estudiante.getCorreo());
            pstmt.setInt(4, estudiante.getId());
            
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }   
    }
    
    // eliminarEstudiante(int id)
    // DELETE FROM "practica-mvc".estudiantes WHERE id = 1;
    public void eliminarEstudiante(int id) {
        String sql = "DELETE FROM \"practica-mvc\".estudiantes WHERE id = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }   
    }
}