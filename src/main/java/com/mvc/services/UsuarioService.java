package com.mvc.services;

import com.mvc.dao.UsuarioDao;
import com.mvc.models.Usuario;

public class UsuarioService {

    private final UsuarioDao usuarioDao;

    public UsuarioService(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Usuario autenticar(String username, String password) {
        if(username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }

        if(password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        return usuarioDao.buscarPorCredenciales(username, password);
    }
}