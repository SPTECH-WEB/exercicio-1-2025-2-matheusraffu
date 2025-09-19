package school.sptech.prova_ac1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    List <Usuario> usuarios = new ArrayList<>();


    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(usuarios); // 200
    }


    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        for (Usuario u : usuarios) {
            if (usuario.getCpf().equals(u.getCpf()) || usuario.getEmail().equals(u.getEmail())) {
                return ResponseEntity.status(409).build();
            }
        }

        usuarios.add(usuario);
        return ResponseEntity.status(201).body(usuario); // 201 Created
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        for (Usuario u : usuarios) {
            if (id.equals(u.getId())) {
                return ResponseEntity.ok(u);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(Integer id) {
        for (Usuario u : usuarios) {
            if (id.equals(u.getId())) {
               usuarios.remove(u);
            }
        }
        return ResponseEntity.internalServerError().build();
    }


    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam LocalDate nascimento) {
        List<Usuario> filtrados = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u.getDataNascimento().equals(u.getDataNascimento())) {
                filtrados.add(u);
            }
        }
        if (filtrados.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(filtrados); // 200
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (id.equals(usuarios.get(i).getId())) {

                for (Usuario u : usuarios) {
                    if (!u.getId().equals(id) &&
                            (u.getCpf().equals(usuario.getCpf()) || u.getEmail().equals(usuario.getEmail()))) {
                        return ResponseEntity.status(409).build();
                    }
                }

                return ResponseEntity.ok(usuario); // 200
            }
        }
        return ResponseEntity.notFound().build(); // 404
    }}
