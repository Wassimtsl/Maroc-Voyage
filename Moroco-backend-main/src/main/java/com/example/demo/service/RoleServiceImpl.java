package com.example.demo.service;

import com.example.demo.dto.RoleDto;
import com.example.demo.entities.Role;
import com.example.demo.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Role convertDtoToEntity(RoleDto dto) {
        return modelMapper.map(dto, Role.class);
    }

    @Override
    public RoleDto convertEntityToDto(Role entity) {
        return modelMapper.map(entity, RoleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Role id " + id + " introuvable"));
        return convertEntityToDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto save(RoleDto dto) {
        Role entity = convertDtoToEntity(dto);
        Role saved = roleRepository.save(entity);
        return convertEntityToDto(saved);
    }

    @Override
    public RoleDto update(Long id, RoleDto dto) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Role id " + id + " introuvable"));

        // grâce au config, id est ignoré + libelleRole va dans nom
        modelMapper.map(dto, existing);

        Role updated = roleRepository.save(existing);
        return convertEntityToDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new NoSuchElementException("Role id " + id + " introuvable");
        }
        roleRepository.deleteById(id);
    }
}
