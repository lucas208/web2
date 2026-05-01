package com.projeto.web2.repository;

import java.util.Optional;

import com.projeto.web2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
}
