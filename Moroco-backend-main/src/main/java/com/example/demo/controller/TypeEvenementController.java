package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.TypeEvenementDto;
import com.example.demo.service.TypeEvenementService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/types-evenements")
public class TypeEvenementController {

    private final TypeEvenementService service;

    public TypeEvenementController(TypeEvenementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TypeEvenementDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeEvenementDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TypeEvenementDto> create(@Valid @RequestBody TypeEvenementDto dto) {
        TypeEvenementDto created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeEvenementDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody TypeEvenementDto dto) {
        return ResponseEntity.ok(service.update(dto));  // ✅ id utilisé !
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}