package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.RoleDto;
import com.example.demo.entities.Role;

public interface RoleService {

	Role convertDtoToEntity(RoleDto dto);
    RoleDto convertEntityToDto(Role role);
    RoleDto findById(Long id);
    List<RoleDto> findAll();
    RoleDto save(RoleDto dto);
    RoleDto update(Long id, RoleDto dto);
    void deleteById(Long id);
}

