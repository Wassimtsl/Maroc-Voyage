package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.GuideDto;
import com.example.demo.dto.TypeEvenementDto;
import com.example.demo.service.GuideService;
import com.example.demo.service.TypeEvenementService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/guides")
public class GuideController {

    private final GuideService service;

    public GuideController(GuideService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<GuideDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<GuideDto> create(@Valid @RequestBody GuideDto dto) {
    	GuideDto created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuideDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody GuideDto dto) {
        return ResponseEntity.ok(service.update(dto));  // ✅ id utilisé !
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}