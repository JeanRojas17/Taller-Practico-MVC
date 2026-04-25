package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgresDatabase;
import com.mvc.modelo.Docente;

public class DocenteDao {

    // INSERT
    public void guardarDocente(Docente docente) {
        String sql = "INSERT INTO \"practica-mvc\".docente (nombre, especialidad) VALUES (?, ?);";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getEspecialidad());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // SELECT ALL
    public List<Docente> obtenerTodosLosDocentes() {
        List<Docente> docentes = new ArrayList<>();

        String sql = "SELECT id_docente, nombre, especialidad FROM \"practica-mvc\".docente ORDER BY id_docente;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                Docente docente = new Docente();

                docente.setId(rs.getInt("id_docente"));
                docente.setNombre(rs.getString("nombre"));
                docente.setEspecialidad(rs.getString("especialidad"));

                docentes.add(docente);
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return docentes;
    }

    // SELECT BY ID
    public Docente obtenerDocentePorId(int id) {
        Docente docente = null;

        String sql = "SELECT id_docente, nombre, especialidad FROM \"practica-mvc\".docente WHERE id_docente = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                docente = new Docente();

                docente.setId(rs.getInt("id_docente"));
                docente.setNombre(rs.getString("nombre"));
                docente.setEspecialidad(rs.getString("especialidad"));
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return docente;
    }

    // UPDATE
    public void actualizarDocente(Docente docente) {
        String sql = "UPDATE \"practica-mvc\".docente SET nombre = ?, especialidad = ? WHERE id_docente = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getEspecialidad());
            pstmt.setInt(3, docente.getId());

            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    // DELETE
    public void eliminarDocente(int id) {
        String sql = "DELETE FROM \"practica-mvc\".docente WHERE id_docente = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }
}