package com.example.lab6_20213852.Repository;

import com.example.lab6_20213852.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findUsuarioByCorreo(String correo);
}