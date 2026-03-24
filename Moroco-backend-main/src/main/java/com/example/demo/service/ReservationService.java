package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.ReservationDto;
import com.example.demo.entities.Reservation;

public interface ReservationService {
    
    ReservationDto findById(Long id);
    List<ReservationDto> findAll();
    List<ReservationDto> findByUserId(Long userId);
    List<ReservationDto> findByEvenementId(Long evenementId);
    List<ReservationDto> findByStatut(String codeStatut);
    
    ReservationDto save(ReservationDto dto);
    ReservationDto confirmer(Long id);
    ReservationDto annuler(Long id);
    
    void deleteById(Long id);
	ReservationDto update(Long id, ReservationDto dto);
	Reservation convertDtoToEntity(ReservationDto dto);
	ReservationDto convertEntityToDto(Reservation entity);
}

