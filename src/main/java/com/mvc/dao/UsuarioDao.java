package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mvc.config.ConexionPostgresDatabase;
import com.mvc.models.Usuario;

public class UsuarioDao {

    public Usuario buscarPorCredenciales(String username, String password) {
        String sql = "SELECT id_usuario, username, password, rol FROM \"practica-mvc\".usuario WHERE username = ? AND password = ?;";

        try(Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("rol")
                );
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return null;
    }
}