// package com.unipiagte.uniescola.controller;

// import com.unipiagte.uniescola.dto.ProfessorDTO;
// import com.unipiagte.uniescola.entity.Professor;
// import com.unipiagte.uniescola.service.ProfessorService;
// import jakarta.validation.Valid;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import java.util.List;

// @RestController
// @RequestMapping("/api/professores")
// public class ProfessorController {
    
//     private final ProfessorService service;
    
//     public ProfessorController(ProfessorService service) {
//         this.service = service;
//     }
    
//     @GetMapping
//     public ResponseEntity<List<Professor>> listar(
//             @RequestParam(required = false) Long escolaId,
//             @RequestParam(required = false) String especialidade) {
        
//         List<Professor> professores;
//         if (escolaId != null) professores = service.buscarPorEscola(escolaId);
//         else if (especialidade != null) professores = service.buscarPorEspecialidade(especialidade);
//         else professores = service.listarTodos();
        
//         return ResponseEntity.ok(professores);
//     }
    
//     @GetMapping("/{id}")
//     public ResponseEntity<Professor> buscarPorId(@PathVariable Long id) {
//         return ResponseEntity.ok(service.buscarPorId(id));
//     }
    
//     @PostMapping
//     public ResponseEntity<Professor> salvar(@Valid @RequestBody ProfessorDTO dto) {
//         return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
//     }
    
//     @PutMapping("/{id}")
//     public ResponseEntity<Professor> atualizar(@PathVariable Long id, @Valid @RequestBody ProfessorDTO dto) {
//         return ResponseEntity.ok(service.atualizar(id, dto));
//     }
    
//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deletar(@PathVariable Long id) {
//         service.deletar(id);
//         return ResponseEntity.noContent().build();
//     }
// }



package com.unipiagte.uniescola.controller;

import com.unipiagte.uniescola.dto.ProfessorDTO;
import com.unipiagte.uniescola.dto.ProfessorResponseDTO;
import com.unipiagte.uniescola.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {
    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listar(
            @RequestParam(required = false) Long escolaId,
            @RequestParam(required = false) String especialidade) {
        
        List<ProfessorResponseDTO> professores;
        if (escolaId != null) professores = service.buscarPorEscola(escolaId);
        else if (especialidade != null) professores = service.buscarPorEspecialidade(especialidade);
        else professores = service.listarTodos();
        
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> salvar(@Valid @RequestBody ProfessorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProfessorDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}