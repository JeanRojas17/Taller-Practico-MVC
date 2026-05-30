package com.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.mvc.config.ConexionPostgreSQLDatabase;
import com.mvc.exception.PersistenciaException;
import com.mvc.models.Usuario;

public class UsuarioDao {

    public Usuario buscarPorCredenciales(String username, String password) {
        String sql = "SELECT id_usuario, username, password, rol FROM \"practica-mvc\".usuario WHERE username = ? AND password = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return mapear(rs);
                }
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al autenticar el usuario.", error);
        }

        return null;
    }

    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT id_usuario, username, password, rol FROM \"practica-mvc\".usuario WHERE id_usuario = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try(ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) {
                    return mapear(rs);
                }
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al buscar el usuario con ID " +idUsuario+ ".", error);
        }

        return null;
    }

    public boolean actualizarUsername(int idUsuario, String nuevoUsername) {
        String sql = "UPDATE \"practica-mvc\".usuario SET username = ? WHERE id_usuario = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoUsername);
            pstmt.setInt(2, idUsuario);

            return pstmt.executeUpdate() > 0;

        } catch(SQLException error) {
            throw new PersistenciaException("Error al actualizar el username del usuario con ID " +idUsuario+ ".", error);
        }
    }

    public boolean actualizarPassword(int idUsuario, String nuevaPassword) {
        String sql = "UPDATE \"practica-mvc\".usuario SET password = ? WHERE id_usuario = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaPassword);
            pstmt.setInt(2, idUsuario);

            return pstmt.executeUpdate() > 0;

        } catch(SQLException error) {
            throw new PersistenciaException("Error al actualizar la contraseña del usuario con ID " +idUsuario+ ".", error);
        }
    }

    public boolean existeUsername(String username) {
        String sql = "SELECT 1 FROM \"practica-mvc\".usuario WHERE username = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try(ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al verificar la existencia del username.", error);
        }
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT id_usuario, username, password, rol FROM \"practica-mvc\".usuario ORDER BY id_usuario;";
        List<Usuario> lista = new ArrayList<>();

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                lista.add(mapear(rs));
            }

        } catch(SQLException error) {
            throw new PersistenciaException("Error al listar los usuarios.", error);
        }

        return lista;
    }

    public void crear(Usuario usuario) {
        String sql = "INSERT INTO \"practica-mvc\".usuario (username, password, rol) VALUES (?, ?, ?);";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getRol());
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al crear el usuario.", error);
        }
    }

    public void actualizar(Usuario usuario) {
        String sql = "UPDATE \"practica-mvc\".usuario SET username = ?, password = ?, rol = ? WHERE id_usuario = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getRol());
            pstmt.setInt(4, usuario.getId());
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al actualizar el usuario con ID " +usuario.getId()+ ".", error);
        }
    }

    public void eliminar(int idUsuario) {
        String sql = "DELETE FROM \"practica-mvc\".usuario WHERE id_usuario = ?;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();

        } catch(SQLException error) {
            throw new PersistenciaException("Error al eliminar el usuario con ID " +idUsuario+ ".", error);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("rol")
        );
    }
}