package service;

import dao.UsuarioDAO;
import entities.Usuario;
import enums.Rol;
import exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void crearUsuario(String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }

        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacio.");
        }

        if (mail == null || mail.isBlank()) {
            throw new IllegalArgumentException("El mail no puede estar vacio.");
        }

        if (usuarioDAO.findByMail(mail).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese mail.");
        }

        Usuario usuario = new Usuario(nombre, apellido, mail, celular, contrasena, rol);
        usuarioDAO.save(usuario);
        System.out.println("Usuario creado con ID: " + usuario.getId());
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioDAO.findById(id);
    }

    public void editarUsuario(Long id, String nombre, String apellido, String mail, String celular, String contrasena, Rol rol) {

        Optional<Usuario> optional = usuarioDAO.findById(id);

        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No se encontro usuario con ID: " + id);
        }

        Usuario usuario = optional.get();

        if (nombre != null && !nombre.isBlank()) {
            usuario.setNombre(nombre);
        }

        if (apellido != null && !apellido.isBlank()) {
            usuario.setApellido(apellido);
        }

        if (mail != null && !mail.isBlank()) {

            Optional<Usuario> existente = usuarioDAO.findByMail(mail);

            if (existente.isPresent() && !existente.get().getId().equals(id)) {

                throw new IllegalArgumentException("Ya existe un usuario con ese mail.");
            }
            usuario.setMail(mail);
        }

        if (celular != null && !celular.isBlank()) {
            usuario.setCelular(celular);
        }

        if (contrasena != null && !contrasena.isBlank()) {
            usuario.setContrasena(contrasena);
        }

        if (rol != null) {
            usuario.setRol(rol);
        }

        usuarioDAO.update(usuario);
        System.out.println("Usuario actualizado correctamente.");
    }

    public void eliminarUsuario(Long id) {

        Optional<Usuario> optional = usuarioDAO.findById(id);

        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No se encontro usuario con ID: " + id);
        }

        usuarioDAO.delete(id);
        System.out.println("Usuario eliminado correctamente.");
    }
}