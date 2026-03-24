package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.SecParams;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecParams secParams;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          SecParams secParams,
                          PasswordEncoder passwordEncoder,
                          UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.secParams = secParams;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        // 1) Récupérer l'utilisateur par email
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Utilisateur introuvable");
        }

        // 2) Vérifier le mot de passe avec BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Mot de passe incorrect");
        }

        // 3) Récupérer le rôle
        Role role = user.getRole();
        String roleName = role != null ? role.getNom() : "USER";
        String[] roles = { roleName };

        // 4) Générer le JWT
        String jwt = JWT.create()
                .withSubject(user.getEmail())
                .withArrayClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()
                        + secParams.getExpTime() * 1000))
                .sign(Algorithm.HMAC256(secParams.getSecret()));

        // 5) Retourner token + user
        UserDto userDto = userService.findByEmail(user.getEmail());
        return ResponseEntity.ok(new AuthResponseDto(jwt, userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email déjà utilisé");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setNom(userDto.getNom());
        user.setPrenom(userDto.getPrenom());
        if (userDto.getNumTel() != null) {
            user.setNumTel(userDto.getNumTel());
        }

        // Rôle TOURISTE par défaut (id=3)
        Role touristeRole = roleRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Rôle TOURISTE introuvable"));
        user.setRole(touristeRole);

        userRepository.save(user);

        // Générer un token pour l'auto-login
        String[] roles = { touristeRole.getNom() };
        String jwt = JWT.create()
                .withSubject(user.getEmail())
                .withArrayClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()
                        + secParams.getExpTime() * 1000))
                .sign(Algorithm.HMAC256(secParams.getSecret()));

        UserDto savedUserDto = userService.findByEmail(user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponseDto(jwt, savedUserDto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        String email = authentication.getName();
        UserDto userDto = userService.findByEmail(email);
        return ResponseEntity.ok(userDto);
    }
}