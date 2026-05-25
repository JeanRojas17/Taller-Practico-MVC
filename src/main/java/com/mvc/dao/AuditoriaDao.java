package com.mvc.dao;

import com.mvc.config.ConexionPostgreSQLDatabase;
import com.mvc.models.Auditoria;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.List;

public class AuditoriaDao {

    public void registrar(Auditoria auditoria) {
        String sql = "INSERT INTO \"practica-mvc\".auditoria (usuario, accion, entidad, descripcion) VALUES (?, ?, ?, ?);";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, auditoria.getUsuario());
            pstmt.setString(2, auditoria.getAccion());
            pstmt.setString(3, auditoria.getEntidad());
            pstmt.setString(4, auditoria.getDescripcion());
            
            pstmt.executeUpdate();

        } catch(SQLException error) {
            error.printStackTrace();
        }
    }

    public List<Auditoria> listarTodas() {
        List<Auditoria> lista = new ArrayList<>();
        String sql = "SELECT id_auditoria, usuario, accion, entidad, descripcion, fecha_hora FROM \"practica-mvc\".auditoria ORDER BY fecha_hora DESC;";

        try(Connection conn = ConexionPostgreSQLDatabase.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()) {
                Auditoria a = new Auditoria();

                a.setId(rs.getInt("id_auditoria"));
                a.setUsuario(rs.getString("usuario"));
                a.setAccion(rs.getString("accion"));
                a.setEntidad(rs.getString("entidad"));
                a.setDescripcion(rs.getString("descripcion"));
                Timestamp ts = rs.getTimestamp("fecha_hora");

                if(ts != null) {
                    Instant instant = ts.toInstant();
                    LocalDateTime local = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    a.setFechaHora(local);
                } else {
                    a.setFechaHora(null);
                }

                lista.add(a);
            }

        } catch(SQLException error) {
            error.printStackTrace();
        }

        return lista;
    }
}