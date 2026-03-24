package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.LangueDto;
import com.example.demo.entities.Langue;


public interface LangueService {

    Langue convertDtoToEntity(LangueDto langueDto);
    LangueDto convertEntityToDto(Langue langue);

    LangueDto findById(Long id);
    List<LangueDto> findAll();
    LangueDto save(LangueDto langueDto);
    LangueDto update(Long id, LangueDto langueDto); 
    void deleteById(Long id);
}
