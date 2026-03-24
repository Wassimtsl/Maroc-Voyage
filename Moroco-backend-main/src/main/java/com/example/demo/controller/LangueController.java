package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LangueDto;
import com.example.demo.service.LangueService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/langues")
public class LangueController {

    private final LangueService service;

    public LangueController(LangueService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LangueDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LangueDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<LangueDto> create(@Valid @RequestBody LangueDto dto) {
        LangueDto created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LangueDto> update(@PathVariable Long id,
                                            @Valid @RequestBody LangueDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}