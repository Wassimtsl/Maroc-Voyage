package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.util.stream.Collectors;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import com.example.demo.repository.AdresseRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;




@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
	private UserRepository userRepo;
  @Autowired
	private RoleRepository roleRepository;
	@Autowired
	ModelMapper modelMapper;
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private AdresseRepository adresseRepo;
  @Override
  public User convertDtoToEntity(UserDto utilisateurDto) {
	// TODO Auto-generated method stub
	return modelMapper.map(utilisateurDto, User.class);
  }
  @Override
  public UserDto convertEntityToDto(User utilisateur) {
	// TODO Auto-generated method stub
	return modelMapper.map(utilisateur, UserDto.class) ;
  }
  @Override
  public UserDto findById(Long id) {
	// TODO Auto-generated method stub
	return   this.convertEntityToDto(userRepo.getReferenceById(id));
  }
  @Override
  public List<UserDto> findAll() {
	// TODO Auto-generated method stub
	return userRepo.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList()) ;
  }
  @Override
  public UserDto save(UserDto utilisateur) { User user = this.convertDtoToEntity(utilisateur);
    if (user.getPassword() != null && !user.getPassword().isBlank()) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
    User saved = userRepo.save(user);
    return this.convertEntityToDto(saved);
  }
  @Override
  public UserDto update(UserDto Utilisateur) {
	// TODO Auto-generated method stub
	return null;
  }
  @Override
  public void deleteById(Long id) {
	// TODO Auto-generated method stub
	  userRepo.deleteById(id);
	
  }
  @Override
  public UserDto findUserByAdresseEmail(String email) {
	// TODO Auto-generated method stub
	return this.convertEntityToDto( userRepo.findByEmail(email));
  }
  /*@Override
  public UserDto addRoleToUser(String email, String rolename) {
	// TODO Auto-generated method stub
	  User usr = userRepo.findByEmail(email);
		Role r = roleRepository.findByLibelleRole(rolename);
		usr.getRoles().add(r);
		return usr;
	}*/
  @Override
  public UserDto findByEmail(String email) {
	User user = userRepo.findByEmail(email);
	if (user == null) {
		throw new java.util.NoSuchElementException("Utilisateur avec email " + email + " introuvable");
	}
	return this.convertEntityToDto(user);
  }
  @Override
  public List<UserDto> findByRole(String roleLibelle) {
	return userRepo.findAll().stream()
		.filter(u -> u.getRole() != null && roleLibelle.equals(u.getRole().getNom()))
		.map(this::convertEntityToDto)
		.collect(Collectors.toList());
  }
  @Override
  public List<UserDto> findGuidesDisponibles() {
	return findByRole("GUIDE");
  }
  @Override
  public UserDto update(Long id, UserDto dto) {
	User existing = userRepo.findById(id)
		.orElseThrow(() -> new java.util.NoSuchElementException("User id " + id + " introuvable"));
	if (dto.getNom() != null) existing.setNom(dto.getNom());
	if (dto.getPrenom() != null) existing.setPrenom(dto.getPrenom());
	if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
	if (dto.getNumTel() != null) existing.setNumTel(dto.getNumTel());
	if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
		existing.setPassword(passwordEncoder.encode(dto.getPassword()));
	}
	User saved = userRepo.save(existing);
	return this.convertEntityToDto(saved);
  }
  @Override
  public UserDto updateDisponibilite(Long id, Boolean disponibilite) {
	User user = userRepo.findById(id)
		.orElseThrow(() -> new java.util.NoSuchElementException("User id " + id + " introuvable"));
	User saved = userRepo.save(user);
	return this.convertEntityToDto(saved);
  }
  @Override
  public UserDto updateTarifGuide(Long id, Double tarif) {
	User user = userRepo.findById(id)
		.orElseThrow(() -> new java.util.NoSuchElementException("User id " + id + " introuvable"));
	User saved = userRepo.save(user);
	return this.convertEntityToDto(saved);
  }
  @Override
  public boolean existsByEmail(String email) {
	return userRepo.findByEmail(email) != null;
  }
  }
  

