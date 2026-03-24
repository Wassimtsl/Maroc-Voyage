package com.example.demo.service;

import com.example.demo.dto.AdresseDto;
import com.example.demo.entities.Adresse;
import com.example.demo.repository.AdresseRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdresseServiceImpl implements AdresseService  {

    private final AdresseRepository adresseRepository;
    private final ModelMapper modelMapper;

    public AdresseServiceImpl(AdresseRepository adresseRepository, ModelMapper modelMapper) {
        this.adresseRepository = adresseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Adresse convertDtoToEntity(AdresseDto dto) {
        return modelMapper.map(dto, Adresse.class);
    }

    @Override
    public AdresseDto convertEntityToDto(Adresse entity) {
        return modelMapper.map(entity, AdresseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AdresseDto findById(Long id) {
        Adresse entity = adresseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Adresse id " + id + " introuvable"));
        return convertEntityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdresseDto> findAll() {
        return adresseRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList()); 
    }

    @Override
    public AdresseDto save(AdresseDto dto) {
        Adresse toSave = convertDtoToEntity(dto);
        Adresse saved = adresseRepository.save(toSave);
        return convertEntityToDto(saved);
    }

    @Override
    public AdresseDto update(Long id, AdresseDto dto) {
        Adresse existing = adresseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Adresse id " + id + " introuvable"));

        Long keepId = existing.getId();   
        modelMapper.map(dto, existing);
        existing.setId(keepId);

        Adresse updated = adresseRepository.save(existing);
        return convertEntityToDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!adresseRepository.existsById(id)) {
            throw new NoSuchElementException("Adresse id " + id + " introuvable");
        }
        adresseRepository.deleteById(id);
    }
}

