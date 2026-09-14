package com.unipiagte.uniescola.service;


import com.unipiagte.uniescola.config.UniescolaProperties;
import com.unipiagte.uniescola.dto.AlunoDTO;
import com.unipiagte.uniescola.entity.Aluno;
import com.unipiagte.uniescola.entity.Escola;
import com.unipiagte.uniescola.exception.BusinessException;
import com.unipiagte.uniescola.exception.ResourceNotFoundException;
import com.unipiagte.uniescola.repository.AlunoRepository;
import com.unipiagte.uniescola.repository.EscolaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AlunoService {
    
    private final AlunoRepository repository;
    private final EscolaRepository escolaRepository;
    private final UniescolaProperties properties;
    
    public AlunoService(AlunoRepository repository, 
                       EscolaRepository escolaRepository,
                       UniescolaProperties properties) {
        this.repository = repository;
        this.escolaRepository = escolaRepository;
        this.properties = properties;
    }
    
    public List<Aluno> listarTodos() {
        return repository.findAll();
    }
    
    public Aluno buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
    }
    
    public List<Aluno> buscarPorEscola(Long escolaId) {
        return repository.findByEscolaId(escolaId);
    }
    
    @Transactional
    public Aluno salvar(AlunoDTO dto) {
        Escola escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada"));
        
        // Validação usando Configuration Properties
        long totalAlunos = repository.countByEscolaId(dto.escolaId());
        if (totalAlunos >= properties.getMaxAlunosTurma()) {
            throw new BusinessException("Limite de " + properties.getMaxAlunosTurma() 
                + " alunos atingido para esta escola");
        }
        
        if (dto.matricula() != null && repository.existsByMatricula(dto.matricula())) {
            throw new BusinessException("Matrícula já cadastrada");
        }
        
        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome());
        aluno.setMatricula(dto.matricula());
        aluno.setDataNascimento(dto.dataNascimento());
        aluno.setEmail(dto.email());
        aluno.setEscola(escola);
        
        return repository.save(aluno);
    }
    
    @Transactional
    public Aluno atualizar(Long id, AlunoDTO dto) {
        Aluno aluno = buscarPorId(id);
        
        if (dto.matricula() != null && !dto.matricula().equals(aluno.getMatricula()) 
            && repository.existsByMatricula(dto.matricula())) {
            throw new BusinessException("Matrícula já cadastrada");
        }
        
        Escola escola = escolaRepository.findById(dto.escolaId())
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada"));
        
        aluno.setNome(dto.nome());
        aluno.setMatricula(dto.matricula());
        aluno.setDataNascimento(dto.dataNascimento());
        aluno.setEmail(dto.email());
        aluno.setEscola(escola);
        
        return repository.save(aluno);
    }
    
    @Transactional
    public void deletar(Long id) {
        Aluno aluno = buscarPorId(id);
        repository.delete(aluno);
    }
}