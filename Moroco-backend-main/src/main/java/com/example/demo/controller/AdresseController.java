package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AdresseDto;
import com.example.demo.service.AdresseService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/adresses")
public class AdresseController {

    private final AdresseService service;

    public AdresseController(AdresseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AdresseDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdresseDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AdresseDto> create(@Valid @RequestBody AdresseDto dto) {
        AdresseDto created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdresseDto> update(@PathVariable Long id,
                                             @Valid @RequestBody AdresseDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}