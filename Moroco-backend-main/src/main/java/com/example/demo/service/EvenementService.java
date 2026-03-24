package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.EvenementDto;
import com.example.demo.entities.Evenement;
import com.example.demo.entities.TypeEvenement;
import com.example.demo.entities.User;
import java.time.LocalDateTime;



public interface EvenementService {

    // conversions
    Evenement convertDtoToEntity(EvenementDto dto);
    EvenementDto convertEntityToDto(Evenement entity);

    // CRUD (retourne des DTO)
    EvenementDto findById(Long id);
    List<EvenementDto> findAll();
    EvenementDto save(EvenementDto dto);
    EvenementDto update(Long id, EvenementDto dto);
    void deleteById(Long id);
	List<EvenementDto> findByTypeEvenementId(Long Id);
	List<EvenementDto> findByUserId(Long Id);
	//Date debut 
	List<EvenementDto> findByDateDebutBetween(LocalDateTime debut, LocalDateTime fin);

}
