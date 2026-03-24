package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.AdresseDto;
import com.example.demo.entities.Adresse;


public interface AdresseService {

  
    Adresse convertDtoToEntity(AdresseDto dto);
    AdresseDto convertEntityToDto(Adresse entity);

    
    AdresseDto findById(Long id);
    List<AdresseDto> findAll();
    AdresseDto save(AdresseDto dto);
    AdresseDto update(Long id, AdresseDto dto);
    void deleteById(Long id);
}
