package com.unipiagte.uniescola.controller;

import com.unipiagte.uniescola.dto.EscolaDTO;
import com.unipiagte.uniescola.entity.Escola;
import com.unipiagte.uniescola.service.EscolaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/escolas")
public class EscolaController {
    
    private final EscolaService service;
    
    public EscolaController(EscolaService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<Escola>> listar(@RequestParam(required = false) String nome) {
        List<Escola> escolas = (nome != null) ? service.buscarPorNome(nome) : service.listarTodos();
        return ResponseEntity.ok(escolas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Escola> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
    
    @PostMapping
    public ResponseEntity<Escola> salvar(@Valid @RequestBody EscolaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Escola> atualizar(@PathVariable Long id, @Valid @RequestBody EscolaDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}