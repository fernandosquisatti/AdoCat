package com.adocat.repository;
import com.adocat.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByCpfHashAndTipo(String cpfHash, String tipo);
}
