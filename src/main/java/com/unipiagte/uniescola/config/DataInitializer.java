package com.unipiagte.uniescola.config;


import com.unipiagte.uniescola.entity.Usuario;
import com.unipiagte.uniescola.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNome("Administrador");
            admin.setEmail("admin@uniescola.com");
            admin.setPerfis(Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
            usuarioRepository.save(admin);
            System.out.println("✅ Usuário admin criado: admin / admin123");
        }
    }
}