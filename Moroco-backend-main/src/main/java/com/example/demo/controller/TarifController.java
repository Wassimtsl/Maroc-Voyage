package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.TarifDto;
import com.example.demo.service.TarifService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tarifs")
public class TarifController {

    private final TarifService service;

    public TarifController(TarifService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TarifDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarifDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TarifDto> create(@Valid @RequestBody TarifDto dto) {
        TarifDto created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifDto> update(@PathVariable Long id,
                                           @Valid @RequestBody TarifDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}