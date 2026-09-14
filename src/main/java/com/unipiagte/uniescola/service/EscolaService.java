package com.unipiagte.uniescola.service;


import com.unipiagte.uniescola.dto.EscolaDTO;
import com.unipiagte.uniescola.entity.Escola;
import com.unipiagte.uniescola.exception.BusinessException;
import com.unipiagte.uniescola.exception.ResourceNotFoundException;
import com.unipiagte.uniescola.repository.EscolaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EscolaService {
    
    private final EscolaRepository repository;
    
    public EscolaService(EscolaRepository repository) {
        this.repository = repository;
    }
    
    public List<Escola> listarTodos() {
        return repository.findAll();
    }
    
    public Escola buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escola não encontrada com ID: " + id));
    }
    
    public List<Escola> buscarPorNome(String nome) {
        return repository.findByNomeContaining(nome);
    }
    
    @Transactional
    public Escola salvar(EscolaDTO dto) {
        if (dto.cnpj() != null && repository.existsByCnpj(dto.cnpj())) {
            throw new BusinessException("Já existe uma escola com este CNPJ");
        }
        
        Escola escola = new Escola();
        escola.setNome(dto.nome());
        escola.setCnpj(dto.cnpj());
        escola.setEndereco(dto.endereco());
        return repository.save(escola);
    }
    
    @Transactional
    public Escola atualizar(Long id, EscolaDTO dto) {
        Escola escola = buscarPorId(id);
        
        if (dto.cnpj() != null && !dto.cnpj().equals(escola.getCnpj()) 
            && repository.existsByCnpj(dto.cnpj())) {
            throw new BusinessException("CNPJ já cadastrado em outra escola");
        }
        
        escola.setNome(dto.nome());
        escola.setCnpj(dto.cnpj());
        escola.setEndereco(dto.endereco());
        return repository.save(escola);
    }
    
    @Transactional
    public void deletar(Long id) {
        Escola escola = buscarPorId(id);
        repository.delete(escola);
    }
}