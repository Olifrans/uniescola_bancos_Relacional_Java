// package com.unipiagte.uniescola.controller;

// import com.unipiagte.uniescola.dto.AlunoDTO;
// import com.unipiagte.uniescola.entity.Aluno;
// import com.unipiagte.uniescola.service.AlunoService;
// import jakarta.validation.Valid;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import java.util.List;

// @RestController
// @RequestMapping("/api/alunos")
// public class AlunoController {
    
//     private final AlunoService service;
    
//     public AlunoController(AlunoService service) {
//         this.service = service;
//     }
    
//     @GetMapping
//     public ResponseEntity<List<Aluno>> listar(@RequestParam(required = false) Long escolaId) {
//         List<Aluno> alunos = (escolaId != null) ? service.buscarPorEscola(escolaId) : service.listarTodos();
//         return ResponseEntity.ok(alunos);
//     }
    
//     @GetMapping("/{id}")
//     public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
//         return ResponseEntity.ok(service.buscarPorId(id));
//     }
    
//     @PostMapping
//     public ResponseEntity<Aluno> salvar(@Valid @RequestBody AlunoDTO dto) {
//         return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
//     }
    
//     @PutMapping("/{id}")
//     public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @Valid @RequestBody AlunoDTO dto) {
//         return ResponseEntity.ok(service.atualizar(id, dto));
//     }
    
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deletar(@PathVariable Long id) {
//         service.deletar(id);
//         return ResponseEntity.noContent().build();
//     }
// }




package com.unipiagte.uniescola.controller;

import com.unipiagte.uniescola.dto.AlunoDTO;
import com.unipiagte.uniescola.dto.AlunoResponseDTO;
import com.unipiagte.uniescola.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listar(@RequestParam(required = false) Long escolaId) {
        List<AlunoResponseDTO> alunos = (escolaId != null) 
            ? service.buscarPorEscola(escolaId) 
            : service.listarTodos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> salvar(@Valid @RequestBody AlunoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}