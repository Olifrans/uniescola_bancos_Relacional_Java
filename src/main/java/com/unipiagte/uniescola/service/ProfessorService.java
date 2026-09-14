package com.unipiagte.uniescola.service;

import com.unipiagte.uniescola.dto.ProfessorDTO;
import com.unipiagte.uniescola.entity.Escola;
import com.unipiagte.uniescola.entity.Professor;
import com.unipiagte.uniescola.exception.BusinessException;
import com.unipiagte.uniescola.exception.ResourceNotFoundException;
import com.unipiagte.uniescola.repository.EscolaRepository;
import com.unipiagte.uniescola.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProfessorService {
    
    private final ProfessorRepository repository;
    private final EscolaRepository escolaRepository;
    
    public ProfessorService(ProfessorRepository repository, EscolaRepository escolaRepository) {
        this.repository = repository;
        this.escolaRepository = escolaRepository;
    }
    
    public List<Professor> listarTodos() {
        return repository.findAll();
    }
    
    public Professor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor não encontrado com ID: " + id));
    }
    
    public List<Professor> buscarPorEscola(Long escolaId) {
        return repository.findByEscolaId(escolaId);
    }
    
    public List<Professor> buscarPorEspecialidade(String especialidade) {
        return repository.findByEspecialidadeIgnoreCase(especialidade);
    }
    
    @Transactional
    public Professor salvar(ProfessorDTO dto) {
        if (dto.email() != null && repository.existsByEmail(dto.email())) {
            throw new BusinessException("Já existe um professor com este email");
        }
        
        Escola escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada"));
        
        Professor professor = new Professor();
        professor.setNome(dto.nome());
        professor.setEmail(dto.email());
        professor.setEspecialidade(dto.especialidade());
        professor.setSalario(dto.salario());
        professor.setDataContratacao(dto.dataContratacao());
        professor.setEscola(escola);
        
        return repository.save(professor);
    }
    
    @Transactional
    public Professor atualizar(Long id, ProfessorDTO dto) {
        Professor professor = buscarPorId(id);
        
        if (dto.email() != null && !dto.email().equals(professor.getEmail()) 
            && repository.existsByEmail(dto.email())) {
            throw new BusinessException("Email já cadastrado");
        }
        
        Escola escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada"));
        
        professor.setNome(dto.nome());
        professor.setEmail(dto.email());
        professor.setEspecialidade(dto.especialidade());
        professor.setSalario(dto.salario());
        professor.setDataContratacao(dto.dataContratacao());
        professor.setEscola(escola);
        
        return repository.save(professor);
    }
    
    @Transactional
    public void deletar(Long id) {
        Professor professor = buscarPorId(id);
        repository.delete(professor);
    }
}