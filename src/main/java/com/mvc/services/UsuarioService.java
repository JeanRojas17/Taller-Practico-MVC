package com.mvc.services;

import com.mvc.dao.UsuarioDao;
import com.mvc.models.Usuario;

import java.util.List;

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

    public void cambiarUsername(int idUsuario, String usernameActual, String nuevoUsername) {
        if(nuevoUsername == null || nuevoUsername.isBlank()) {
            throw new IllegalArgumentException("El nuevo nombre de usuario no puede estar vacío.");
        }

        if(nuevoUsername.equals(usernameActual)) {
            throw new IllegalArgumentException("El nuevo nombre de usuario es igual al actual.");
        }

        if(usuarioDao.existeUsername(nuevoUsername)) {
            throw new IllegalArgumentException("Ese nombre de usuario ya está en uso.");
        }

        usuarioDao.actualizarUsername(idUsuario, nuevoUsername);
    }

    public void cambiarPassword(int idUsuario, String passwordActual, String passwordNueva, String passwordConfirm) {
        if(passwordNueva == null || passwordNueva.isBlank()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía.");
        }
        
        if(!passwordNueva.equals(passwordConfirm)) {
            throw new IllegalArgumentException("La confirmación no coincide con la nueva contraseña.");
        }

        Usuario usuario = usuarioDao.buscarPorId(idUsuario);

        if(usuario == null || !usuario.getPassword().equals(passwordActual)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        usuarioDao.actualizarPassword(idUsuario, passwordNueva);
    }

    public List<Usuario> listarTodos() {
        return usuarioDao.listarTodos();
    }

    public void crearUsuario(String username, String password, String rol) {
        if(username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }

        if(password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        
        if(!rol.equals("Administrador") && !rol.equals("Usuario")) {
            throw new IllegalArgumentException("El rol debe ser Administrador o Usuario.");
        }
        
        if(usuarioDao.existeUsername(username)) {
            throw new IllegalArgumentException("Ese nombre de usuario ya está en uso.");
        }

        usuarioDao.crear(new Usuario(null, username, password, rol));
    }

    public void actualizarUsuario(Usuario usuario) {
        if(usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        
        if(usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        
        usuarioDao.actualizar(usuario);
    }

    public void eliminarUsuario(int idUsuario, int idUsuarioActual) {
        if(idUsuario == idUsuarioActual) {
            throw new IllegalArgumentException("No puedes eliminar tu propia cuenta.");
        }
        
        usuarioDao.eliminar(idUsuario);
    }
}