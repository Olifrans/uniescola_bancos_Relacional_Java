// package com.unipiagte.uniescola.controller;


// import com.unipiagte.uniescola.config.JwtUtil;
// import com.unipiagte.uniescola.dto.LoginRequestDTO;
// import com.unipiagte.uniescola.dto.LoginResponseDTO;
// import com.unipiagte.uniescola.entity.Usuario;
// import com.unipiagte.uniescola.exception.BusinessException;
// import com.unipiagte.uniescola.repository.UsuarioRepository;
// import jakarta.validation.Valid;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {
    
//     private final UsuarioRepository usuarioRepository;
//     private final PasswordEncoder passwordEncoder;
//     private final JwtUtil jwtUtil;
    
//     public AuthController(UsuarioRepository usuarioRepository, 
//                          PasswordEncoder passwordEncoder, 
//                          JwtUtil jwtUtil) {
//         this.usuarioRepository = usuarioRepository;
//         this.passwordEncoder = passwordEncoder;
//         this.jwtUtil = jwtUtil;
//     }
    
//     @PostMapping("/login")
//     public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
//         Usuario usuario = usuarioRepository.findByUsername(request.username())
//                 .orElseThrow(() -> new BusinessException("Usuário ou senha inválidos"));
        
//         if (!usuario.isAtivo()) {
//             throw new BusinessException("Usuário inativo");
//         }
        
//         if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
//             throw new BusinessException("Usuário ou senha inválidos");
//         }
        
//         String token = jwtUtil.generateToken(usuario.getUsername());
        
//         return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getUsername(), usuario.getNome()));
//     }
    
//     @PostMapping("/register")
//     public ResponseEntity<String> register(@Valid @RequestBody LoginRequestDTO request) {
//         if (usuarioRepository.existsByUsername(request.username())) {
//             throw new BusinessException("Usuário já existe");
//         }
        
//         Usuario usuario = new Usuario();
//         usuario.setUsername(request.username());
//         usuario.setPassword(passwordEncoder.encode(request.password()));
//         usuario.setNome(request.username());
//         usuario.getPerfis().add("ROLE_USER");
        
//         usuarioRepository.save(usuario);
        
//         return ResponseEntity.ok("Usuário criado com sucesso");
//     }
// }




package com.unipiagte.uniescola.controller;

import com.unipiagte.uniescola.config.JwtUtil;
import com.unipiagte.uniescola.dto.LoginRequestDTO;
import com.unipiagte.uniescola.dto.LoginResponseDTO;
import com.unipiagte.uniescola.entity.Usuario;
import com.unipiagte.uniescola.exception.BusinessException;
import com.unipiagte.uniescola.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthController(UsuarioRepository usuarioRepository, 
                         PasswordEncoder passwordEncoder, 
                         JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException("Usuário ou senha inválidos"));
        
        if (!usuario.isAtivo()) {
            throw new BusinessException("Usuário inativo");
        }
        
        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new BusinessException("Usuário ou senha inválidos");
        }
        
        String token = jwtUtil.generateToken(usuario.getUsername());
        
        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getUsername(), usuario.getNome()));
    }
}