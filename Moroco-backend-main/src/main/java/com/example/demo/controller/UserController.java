package com.example.demo.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    // ✅ Injection par constructeur
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        UserDto created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                          @Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(service.update(id, dto));  // ✅ id utilisé !
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();  // ✅ 204 No Content
    }

    // ========== Endpoints spécifiques aux GUIDES ==========

    @GetMapping("/guides")
    public ResponseEntity<List<UserDto>> allGuides() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/guides/disponibles")
    public ResponseEntity<List<UserDto>> guidesDisponibles() {
        return ResponseEntity.ok(service.findGuidesDisponibles());
    }

    @PatchMapping("/{id}/disponibilite")
    public ResponseEntity<UserDto> updateDisponibilite(
            @PathVariable Long id,
            @RequestParam Boolean disponibilite) {
        return ResponseEntity.ok(service.updateDisponibilite(id, disponibilite));
    }

    @PatchMapping("/{id}/tarif")
    public ResponseEntity<UserDto> updateTarif(
            @PathVariable Long id,
            @RequestParam Double tarif) {
        return ResponseEntity.ok(service.updateTarifGuide(id, tarif));
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        String email = authentication.getName();
        UserDto user = service.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}