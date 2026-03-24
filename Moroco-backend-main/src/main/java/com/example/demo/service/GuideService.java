package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.GuideDto;
import com.example.demo.entities.Guide;

import jakarta.validation.Valid;

public interface GuideService {
	GuideDto findById(Long id);
	GuideDto convertEntityToDto(Guide entity);
	Guide convertDtoToEntity(GuideDto dto);
	List<GuideDto> findAll();
	GuideDto save(@Valid GuideDto dto);
	GuideDto update(@Valid GuideDto dto);
	void deleteById(Long id);

}
