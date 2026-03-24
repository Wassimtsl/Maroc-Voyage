package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.TarifDto;
import com.example.demo.entities.Tarif;

public interface TarifService {
    
    Tarif convertDtoToEntity(TarifDto dto);
    TarifDto convertEntityToDto(Tarif tarif);

  
    TarifDto findById(Long id);
    List<TarifDto> findAll();
    TarifDto save(TarifDto dto);
    TarifDto update(Long id, TarifDto dto);
    void deleteById(Long id);
}

