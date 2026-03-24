package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ReservationDto;
import com.example.demo.service.ReservationService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> all() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationDto> create(@Valid @RequestBody ReservationDto dto) {
        ReservationDto created = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> update(@PathVariable Long id,
                                                 @Valid @RequestBody ReservationDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ========== Endpoints spécifiques ==========

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDto>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/evenement/{eventId}")
    public ResponseEntity<List<ReservationDto>> byEvenement(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.findByEvenementId(eventId));
    }

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<ReservationDto> confirmer(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmer(id));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<ReservationDto> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(service.annuler(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ReservationDto> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.annuler(id));
    }

}