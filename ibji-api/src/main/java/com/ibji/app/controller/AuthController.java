package com.ibji.app.controller;

import com.ibji.app.security.JwtTokenProvider;
import com.ibji.app.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken(authentication);
        
        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", Map.of(
                "id", userDetails.getMembro().getId(),
                "nome", userDetails.getMembro().getNome(),
                "email", userDetails.getMembro().getEmail(),
                "nivelAcesso", userDetails.getMembro().getNivelAcesso().name()
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", userDetails.getMembro().getId());
            response.put("nome", userDetails.getMembro().getNome());
            response.put("email", userDetails.getMembro().getEmail());
            response.put("nivelAcesso", userDetails.getMembro().getNivelAcesso().name());
            
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
