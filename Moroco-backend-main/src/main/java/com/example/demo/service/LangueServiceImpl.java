package com.example.demo.service;

import com.example.demo.dto.LangueDto;
import com.example.demo.entities.Langue;
import com.example.demo.repository.LangueRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LangueServiceImpl implements LangueService  {

    private final LangueRepository repo;
    private final ModelMapper modelMapper;

    public LangueServiceImpl(LangueRepository repo, ModelMapper modelMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
    }

    @Override
    public Langue convertDtoToEntity(LangueDto dto) {
        return modelMapper.map(dto, Langue.class);
    }

    @Override
    public LangueDto convertEntityToDto(Langue entity) {
        return modelMapper.map(entity, LangueDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public LangueDto findById(Long id) {
        Langue entity = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Langue id " + id + " introuvable"));
        return convertEntityToDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LangueDto> findAll() {
        return repo.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList()); 
    }

    @Override
    public LangueDto save(LangueDto dto) {
        Langue entity = convertDtoToEntity(dto);
        Langue saved = repo.save(entity);
        return convertEntityToDto(saved);
    }

    @Override
    public LangueDto update(Long id, LangueDto dto) { // <--- pas "_langueDto"
        Langue existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Langue id " + id + " introuvable"));

        Long keepId = existing.getId();
        modelMapper.map(dto, existing);
        existing.setId(keepId);

        Langue updated = repo.save(existing);
        return convertEntityToDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Langue id " + id + " introuvable");
        }
        repo.deleteById(id);
    }
}
