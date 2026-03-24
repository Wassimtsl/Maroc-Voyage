package com.example.demo.service;

import com.example.demo.dto.TarifDto;
import com.example.demo.entities.Tarif;
import com.example.demo.repository.TarifRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class TarifServiceImpl implements TarifService  {

    private final TarifRepository tarifRepository;
    private final ModelMapper modelMapper;

    public TarifServiceImpl(TarifRepository tarifRepository, ModelMapper modelMapper) {
        this.tarifRepository = tarifRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Tarif convertDtoToEntity(TarifDto dto) {
        return modelMapper.map(dto, Tarif.class);
    }

    @Override
    public TarifDto convertEntityToDto(Tarif entity) {
        return modelMapper.map(entity, TarifDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public TarifDto findById(Long id) {
        Tarif entity = tarifRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarif id " + id + " introuvable"));
        return convertEntityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarifDto> findAll() {
        return tarifRepository.findAll()
                .stream()
                .map(tarif -> convertEntityToDto(tarif))
                .collect(Collectors.toList());
    }

    @Override
    public TarifDto save(TarifDto dto) {
        Tarif toSave = convertDtoToEntity(dto);
        Tarif saved = tarifRepository.save(toSave);
        return convertEntityToDto(saved);
    }

    @Override
    public TarifDto update(Long id, TarifDto dto) {
        Tarif existing = tarifRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tarif id " + id + " introuvable"));

        // On garde l'id et on mappe le reste
        Long keepId = existing.getId();
        modelMapper.map(dto, existing);
        existing.setId(keepId);

        Tarif updated = tarifRepository.save(existing);
        return convertEntityToDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!tarifRepository.existsById(id)) {
            throw new NoSuchElementException("Tarif id " + id + " introuvable");
        }
        tarifRepository.deleteById(id);
    }

}

