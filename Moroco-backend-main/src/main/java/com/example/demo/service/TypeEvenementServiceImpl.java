package com.example.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.dto.TypeEvenementDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entities.TypeEvenement;
import com.example.demo.repository.LangueRepository;
import com.example.demo.repository.TypeEvenementRepository;
	

@Service
public class TypeEvenementServiceImpl implements TypeEvenementService  {
@Autowired
  private  LangueRepository languerepo;

@Autowired
ModelMapper modelMapper;
@Autowired
private TypeEvenementRepository typeEvenementRepo;

@Override
public TypeEvenement convertDtoToEntity(TypeEvenementDto TypeEvenementDto) {
	// TODO Auto-generated method stub
	return modelMapper.map(TypeEvenementDto, TypeEvenement.class);
}

@Override
public TypeEvenementDto convertEntityToDto(TypeEvenement typeEvenement) {
	// TODO Auto-generated method stub
	return modelMapper.map(typeEvenement, TypeEvenementDto.class);
}

@Override
public TypeEvenementDto findById(Long id) {
	// TODO Auto-generated method stub
	return  this.convertEntityToDto(typeEvenementRepo.getReferenceById(id));
}

@Override
public TypeEvenementDto save(TypeEvenementDto typeEvenementDto) {
	// TODO Auto-generated method stub
	return this.convertEntityToDto(typeEvenementRepo.save( this.convertDtoToEntity(typeEvenementDto)));	
}

@Override
public TypeEvenementDto update(TypeEvenementDto typeEvenementDto) {
	// TODO Auto-generated method stub
	return this.convertEntityToDto(typeEvenementRepo.save( this.convertDtoToEntity(typeEvenementDto)));	
}

@Override
public void deleteById(Long id) {
	// TODO Auto-generated method stub
 typeEvenementRepo.deleteById(id);
	
}

@Override
public List<TypeEvenementDto> findAll() {
	// TODO Auto-generated method stub
	return typeEvenementRepo.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList()) ;
}

}