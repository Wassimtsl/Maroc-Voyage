package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.TypeEvenementDto;
import com.example.demo.entities.TypeEvenement;


public interface TypeEvenementService {

    TypeEvenement convertDtoToEntity(TypeEvenementDto dto);
    TypeEvenementDto convertEntityToDto(TypeEvenement entity);

    TypeEvenementDto findById(Long id);
    List<TypeEvenementDto> findAll();
    TypeEvenementDto save(TypeEvenementDto dto);
    TypeEvenementDto update(TypeEvenementDto dto); 
    void deleteById(Long id);
}
