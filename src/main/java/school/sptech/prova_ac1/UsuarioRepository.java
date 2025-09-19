package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    default boolean existsByCpfOrEmail(String cpf, String email) {
        return existsByCpf(cpf) || existsByEmail(email);
    }

    List<Usuario> findByDataNascimento(LocalDate dataNascimento);
}