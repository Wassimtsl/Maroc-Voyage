package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;

public interface UserService {
    
    UserDto findById(Long id);
    UserDto findByEmail(String email);
    List<UserDto> findAll();
    List<UserDto> findByRole(String roleLibelle);
    List<UserDto> findGuidesDisponibles();
    
    UserDto save(UserDto dto);
    UserDto update(Long id, UserDto dto);
    
    // Méthodes spécifiques aux guides
    UserDto updateDisponibilite(Long id, Boolean disponibilite);
    UserDto updateTarifGuide(Long id, Double tarif);
    
    void deleteById(Long id);
    boolean existsByEmail(String email);
	User convertDtoToEntity(UserDto utilisateurDto);
	UserDto convertEntityToDto(User utilisateur);
	UserDto update(UserDto Utilisateur);
	UserDto findUserByAdresseEmail(String email);
}