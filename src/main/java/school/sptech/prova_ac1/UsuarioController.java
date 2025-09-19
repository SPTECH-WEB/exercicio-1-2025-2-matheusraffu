package school.sptech.prova_ac1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByCpfOrEmail(usuario.getCpf(), usuario.getEmail())) {
            return ResponseEntity.status(409).build();
        }
        Usuario salvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        var usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario uExistente = usuarioOptional.get();

        boolean conflito = usuarioRepository.findAll().stream()
                .anyMatch(u -> !u.getId().equals(id) &&
                        (u.getCpf().equals(usuario.getCpf()) ||
                                u.getEmail().equals(usuario.getEmail())));

        if (conflito) {
            return ResponseEntity.status(409).build();
        }

        uExistente.setNome(usuario.getNome());
        uExistente.setCpf(usuario.getCpf());
        uExistente.setEmail(usuario.getEmail());
        uExistente.setSenha(usuario.getSenha());
        uExistente.setDataNascimento(usuario.getDataNascimento());

        usuarioRepository.save(uExistente);

        return ResponseEntity.ok(uExistente);
    }


    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(
            @RequestParam(value = "nascimento", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate nascimento) {

        if (nascimento == null) {
            return ResponseEntity.badRequest().body(List.of());
        }

        List<Usuario> filtrados = usuarioRepository.findByDataNascimento(nascimento);

        if (filtrados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(filtrados);
    }



}
