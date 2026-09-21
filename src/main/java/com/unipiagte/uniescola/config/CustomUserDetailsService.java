// package com.unipiagte.uniescola.config;



// import com.unipiagte.uniescola.entity.Usuario;
// import com.unipiagte.uniescola.repository.UsuarioRepository;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
// import java.util.stream.Collectors;

// @Service
// public class CustomUserDetailsService implements UserDetailsService {
    
//     private final UsuarioRepository repository;
    
//     public CustomUserDetailsService(UsuarioRepository repository) {
//         this.repository = repository;
//     }
    
//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         Usuario usuario = repository.findByUsername(username)
//                 .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        
//         if (!usuario.isAtivo()) {
//             throw new UsernameNotFoundException("Usuário inativo: " + username);
//         }
        
//         return User.builder()
//                 .username(usuario.getUsername())
//                 .password(usuario.getPassword())
//                 .authorities(
//                     usuario.getPerfis().stream()
//                         .map(SimpleGrantedAuthority::new)
//                         .collect(Collectors.toList())
//                 )
//                 .build();
//     }
// }



package com.unipiagte.uniescola.config;

import com.unipiagte.uniescola.entity.Usuario;
import com.unipiagte.uniescola.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UsuarioRepository repository;
    
    public CustomUserDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        
        if (!usuario.isAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + username);
        }
        
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(
                    usuario.getPerfis().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                )
                .build();
    }
}